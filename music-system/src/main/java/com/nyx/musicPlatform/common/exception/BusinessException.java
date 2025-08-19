package com.nyx.musicPlatform.common.exception;

import com.nyx.musicPlatform.common.result.IResultCode;
import com.nyx.musicPlatform.common.result.ResultCode;

/**
 * 业务异常类
 *
 * @author nyx
 * @since 2025-06
 */
public class BusinessException extends BaseException {
    
    private static final long serialVersionUID = 1L;
    
    public BusinessException(IResultCode resultCode) {
        super(resultCode);
    }
    
    public BusinessException(IResultCode resultCode, String message) {
        super(resultCode, message);
    }
    
    public BusinessException(String message) {
        super(ResultCode.FAILED.getCode(), message);
    }
    
    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public BusinessException(Integer code, String message) {
        super(code, message);
    }
}