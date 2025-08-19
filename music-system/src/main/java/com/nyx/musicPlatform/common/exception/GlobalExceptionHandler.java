package com.nyx.musicPlatform.common.exception;

import com.nyx.musicPlatform.common.result.Result;
import com.nyx.musicPlatform.common.result.ResultCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import java.sql.SQLException;
import java.util.Set;

/**
 * 全局异常处理器
 *
 * @author nyx
 * @since 2025-06
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * 处理业务异常
     */
    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(HttpStatus.OK)
    public Result<Object> handleBusinessException(BusinessException e, HttpServletRequest request) {
        log.warn("业务异常：{}, 请求路径：{}", e.getMessage(), request.getRequestURI());
        return new Result<>(e.getCode(), e.getMessage());
    }

    /**
     * 处理基础异常
     */
    @ExceptionHandler(BaseException.class)
    @ResponseStatus(HttpStatus.OK)
    public Result<Object> handleBaseException(BaseException e, HttpServletRequest request) {
        log.warn("基础异常：{}, 请求路径：{}", e.getMessage(), request.getRequestURI());
        return new Result<>(e.getCode(), e.getMessage());
    }

    /**
     * 处理参数校验异常（@Valid）
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<Object> handleMethodArgumentNotValidException(MethodArgumentNotValidException e, HttpServletRequest request) {
        log.warn("参数校验异常，请求路径：{}", request.getRequestURI());
        StringBuilder message = new StringBuilder("参数校验失败：");
        for (FieldError fieldError : e.getBindingResult().getFieldErrors()) {
            message.append(fieldError.getField()).append(" ").append(fieldError.getDefaultMessage()).append("; ");
        }
        return Result.validateFailed(message.toString());
    }

    /**
     * 处理参数绑定异常
     */
    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<Object> handleBindException(BindException e, HttpServletRequest request) {
        log.warn("参数绑定异常，请求路径：{}", request.getRequestURI());
        StringBuilder message = new StringBuilder("参数绑定失败：");
        for (FieldError fieldError : e.getBindingResult().getFieldErrors()) {
            message.append(fieldError.getField()).append(" ").append(fieldError.getDefaultMessage()).append("; ");
        }
        return Result.validateFailed(message.toString());
    }

    /**
     * 处理约束违反异常（@Validated）
     */
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<Object> handleConstraintViolationException(ConstraintViolationException e, HttpServletRequest request) {
        log.warn("约束违反异常，请求路径：{}", request.getRequestURI());
        StringBuilder message = new StringBuilder("参数校验失败：");
        Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
        for (ConstraintViolation<?> violation : violations) {
            message.append(violation.getPropertyPath()).append(" ").append(violation.getMessage()).append("; ");
        }
        return Result.validateFailed(message.toString());
    }

    /**
     * 处理缺少请求参数异常
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<Object> handleMissingServletRequestParameterException(MissingServletRequestParameterException e, HttpServletRequest request) {
        log.warn("缺少请求参数异常，请求路径：{}, 参数名：{}", request.getRequestURI(), e.getParameterName());
        return Result.validateFailed("缺少必需的请求参数：" + e.getParameterName());
    }

    /**
     * 处理方法参数类型不匹配异常
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<Object> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e, HttpServletRequest request) {
        log.warn("方法参数类型不匹配异常，请求路径：{}, 参数名：{}", request.getRequestURI(), e.getName());
        return Result.validateFailed("参数类型不匹配：" + e.getName());
    }

    /**
     * 处理HTTP消息不可读异常
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<Object> handleHttpMessageNotReadableException(HttpMessageNotReadableException e, HttpServletRequest request) {
        log.warn("HTTP消息不可读异常，请求路径：{}", request.getRequestURI());
        return Result.validateFailed("请求体格式错误或为空");
    }

    /**
     * 处理不支持的HTTP方法异常
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public Result<Object> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e, HttpServletRequest request) {
        log.warn("不支持的HTTP方法异常，请求路径：{}, 方法：{}", request.getRequestURI(), request.getMethod());
        return Result.failed(ResultCode.METHOD_NOT_ALLOWED, "不支持的请求方法：" + request.getMethod());
    }

    /**
     * 处理不支持的媒体类型异常
     */
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    @ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
    public Result<Object> handleHttpMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException e, HttpServletRequest request) {
        log.warn("不支持的媒体类型异常，请求路径：{}", request.getRequestURI());
        return Result.failed(ResultCode.UNSUPPORTED_MEDIA_TYPE, "不支持的媒体类型");
    }

    /**
     * 处理文件上传大小超出限制异常
     */
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<Object> handleMaxUploadSizeExceededException(MaxUploadSizeExceededException e, HttpServletRequest request) {
        log.warn("文件上传大小超出限制异常，请求路径：{}", request.getRequestURI());
        return Result.failed(ResultCode.FILE_SIZE_EXCEEDED, "文件大小超出限制");
    }

    /**
     * 处理404异常
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Result<Object> handleNoHandlerFoundException(NoHandlerFoundException e, HttpServletRequest request) {
        log.warn("404异常，请求路径：{}", request.getRequestURI());
        return Result.failed(ResultCode.NOT_FOUND, "请求的资源不存在");
    }

    /**
     * 处理认证异常
     */
    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Result<Object> handleAuthenticationException(AuthenticationException e, HttpServletRequest request) {
        log.warn("认证异常，请求路径：{}, 异常信息：{}", request.getRequestURI(), e.getMessage());
        if (e instanceof BadCredentialsException) {
            return Result.failed(ResultCode.PASSWORD_ERROR, "用户名或密码错误");
        }
        return Result.failed(ResultCode.UNAUTHORIZED, "认证失败");
    }

    /**
     * 处理授权异常
     */
    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public Result<Object> handleAccessDeniedException(AccessDeniedException e, HttpServletRequest request) {
        log.warn("授权异常，请求路径：{}, 异常信息：{}", request.getRequestURI(), e.getMessage());
        return Result.failed(ResultCode.FORBIDDEN, "没有访问权限");
    }

    /**
     * 处理文件访问拒绝异常
     */
    @ExceptionHandler(java.nio.file.AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public Result<Object> handleFileAccessDeniedException(java.nio.file.AccessDeniedException e, HttpServletRequest request) {
        log.warn("文件访问拒绝异常，请求路径：{}, 异常信息：{}", request.getRequestURI(), e.getMessage());
        return Result.failed(ResultCode.FORBIDDEN, "文件访问被拒绝");
    }

    /**
     * 处理SQL异常
     */
    @ExceptionHandler(SQLException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result<Object> handleSQLException(SQLException e, HttpServletRequest request) {
        log.error("SQL异常，请求路径：" + request.getRequestURI() + ", 异常信息：" + e.getMessage());
        return Result.failed(ResultCode.DATABASE_ERROR, "数据库操作失败");
    }

    /**
     * 处理空指针异常
     */
    @ExceptionHandler(NullPointerException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result<Object> handleNullPointerException(NullPointerException e, HttpServletRequest request) {
        log.error("空指针异常，请求路径：" + request.getRequestURI() + ", 异常信息：" + e.getMessage());
        return Result.failed(ResultCode.SYSTEM_ERROR, "系统内部错误");
    }

    /**
     * 处理非法参数异常
     */
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<Object> handleIllegalArgumentException(IllegalArgumentException e, HttpServletRequest request) {
        log.warn("非法参数异常，请求路径：{}, 异常信息：{}", request.getRequestURI(), e.getMessage());
        return Result.validateFailed("参数错误：" + e.getMessage());
    }

    /**
     * 处理非法状态异常
     */
    @ExceptionHandler(IllegalStateException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result<Object> handleIllegalStateException(IllegalStateException e, HttpServletRequest request) {
        log.error("非法状态异常，请求路径：" + request.getRequestURI() + ", 异常信息：" + e.getMessage());
        return Result.failed(ResultCode.SYSTEM_ERROR, "系统状态异常");
    }

    /**
     * 处理运行时异常
     */
    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result<Object> handleRuntimeException(RuntimeException e, HttpServletRequest request) {
        log.error("运行时异常，请求路径：" + request.getRequestURI() + ", 异常信息：" + e.getMessage());
        return Result.failed(ResultCode.SYSTEM_ERROR, "系统运行时错误");
    }

    /**
     * 处理其他异常
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result<Object> handleException(Exception e, HttpServletRequest request) {
        log.error("未知异常，请求路径：" + request.getRequestURI() + ", 异常信息：" + e.getMessage());
        return Result.failed(ResultCode.SYSTEM_ERROR, "系统异常，请联系管理员");
    }
}