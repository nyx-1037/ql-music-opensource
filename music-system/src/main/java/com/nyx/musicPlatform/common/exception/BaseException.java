package com.nyx.musicPlatform.common.exception;

import com.nyx.musicPlatform.common.result.IResultCode;

/**
 * 自定义异常基类
 *
 * @author nyx
 * @since 2025-06
 */
public class BaseException extends RuntimeException {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 错误码
     */
    private final Integer code;
    
    /**
     * 错误信息
     */
    private final String message;
    
    public BaseException(IResultCode resultCode) {
        super(resultCode.getMessage());
        this.code = resultCode.getCode();
        this.message = resultCode.getMessage();
    }
    
    public BaseException(IResultCode resultCode, String message) {
        super(message);
        this.code = resultCode.getCode();
        this.message = message;
    }
    
    public BaseException(Integer code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }
    
    public BaseException(String message) {
        super(message);
        this.code = 500;
        this.message = message;
    }
    
    public BaseException(String message, Throwable cause) {
        super(message, cause);
        this.code = 500;
        this.message = message;
    }
    
    public BaseException(Integer code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
        this.message = message;
    }
    
    public Integer getCode() {
        return code;
    }
    
    @Override
    public String getMessage() {
        return message;
    }
}