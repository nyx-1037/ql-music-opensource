package com.nyx.musicPlatform.common.result;

/**
 * 封装API的错误码接口
 *
 * @author nyx
 * @since 2025-06
 */
public interface IResultCode {
    
    /**
     * 获取错误码
     */
    Integer getCode();
    
    /**
     * 获取错误信息
     */
    String getMessage();
}