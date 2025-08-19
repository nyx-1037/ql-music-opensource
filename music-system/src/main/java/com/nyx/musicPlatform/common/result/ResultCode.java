package com.nyx.musicPlatform.common.result;

/**
 * 枚举了一些常用API操作码
 *
 * @author nyx
 * @since 2025-06
 */
public enum ResultCode implements IResultCode {
    
    SUCCESS(200, "操作成功"),
    FAILED(500, "操作失败"),
    VALIDATE_FAILED(400, "参数检验失败"),
    PARAM_ERROR(400, "参数错误"),
    UNAUTHORIZED(401, "暂未登录或token已经过期"),
    FORBIDDEN(403, "没有相关权限"),
    NOT_FOUND(404, "资源不存在"),
    METHOD_NOT_ALLOWED(405, "请求方法不允许"),
    UNSUPPORTED_MEDIA_TYPE(415, "不支持的媒体类型"),
    TOO_MANY_REQUESTS(429, "请求过于频繁"),
    
    // 用户相关错误码 1000-1999
    USER_NOT_FOUND(1001, "用户不存在"),
    USER_ALREADY_EXISTS(1002, "用户已存在"),
    USERNAME_ALREADY_EXISTS(1003, "用户名已存在"),
    EMAIL_ALREADY_EXISTS(1004, "邮箱已存在"),
    PASSWORD_ERROR(1005, "密码错误"),
    USER_DISABLED(1006, "用户已被禁用"),
    USER_LOCKED(1007, "用户已被锁定"),
    OLD_PASSWORD_ERROR(1008, "原密码错误"),
    NEW_PASSWORD_SAME_AS_OLD(1009, "新密码不能与原密码相同"),
    USER_NOT_ACTIVE(1010, "用户未激活"),
    USER_UPDATE_FAILED(1011, "用户更新失败"),
    USER_PASSWORD_RESET_FAILED(1012, "密码重置失败"),
    USER_DELETE_FAILED(1013, "用户删除失败"),
    USER_PASSWORD_CHANGE_FAILED(1014, "密码修改失败"),
    USER_LOGIN_FAILED(1015, "用户登录失败"),
    USER_REGISTER_FAILED(1016, "用户注册失败"),
    
    // 音乐相关错误码 2000-2999
    MUSIC_NOT_FOUND(2001, "音乐不存在"),
    MUSIC_UPLOAD_FAILED(2002, "音乐上传失败"),
    MUSIC_DELETE_FAILED(2003, "音乐删除失败"),
    MUSIC_FILE_NOT_FOUND(2004, "音乐文件不存在"),
    MUSIC_FORMAT_NOT_SUPPORTED(2005, "不支持的音乐格式"),
    MUSIC_FILE_TOO_LARGE(2006, "音乐文件过大"),
    MUSIC_ALREADY_EXISTS(2007, "音乐已存在"),
    MUSIC_PERMISSION_DENIED(2008, "没有权限操作该音乐"),
    MUSIC_DISABLED(2009, "音乐已被禁用"),
    MUSIC_PROCESSING(2010, "音乐正在处理中"),
    MUSIC_UPDATE_FAILED(2011, "音乐更新失败"),
    
    // 歌单相关错误码 2500-2599
    PLAYLIST_NOT_FOUND(2501, "歌单不存在"),
    PLAYLIST_ALREADY_FAVORITED(2502, "歌单已收藏"),
    PLAYLIST_NOT_FAVORITED(2503, "歌单未收藏"),
    PLAYLIST_FAVORITE_FAILED(2504, "歌单收藏失败"),
    PLAYLIST_UNFAVORITE_FAILED(2505, "取消收藏歌单失败"),
    
    // 文件相关错误码 3000-3999
    FILE_UPLOAD_FAILED(3001, "文件上传失败"),
    FILE_DELETE_FAILED(3002, "文件删除失败"),
    FILE_NOT_FOUND(3003, "文件不存在"),
    FILE_FORMAT_ERROR(3004, "文件格式错误"),
    FILE_SIZE_EXCEEDED(3005, "文件大小超出限制"),
    FILE_NAME_INVALID(3006, "文件名无效"),
    FILE_READ_ERROR(3007, "文件读取失败"),
    FILE_WRITE_ERROR(3008, "文件写入失败"),
    
    // 认证相关错误码 4000-4999
    TOKEN_INVALID(4001, "Token无效"),
    TOKEN_EXPIRED(4002, "Token已过期"),
    TOKEN_MISSING(4003, "Token缺失"),
    REFRESH_TOKEN_INVALID(4004, "刷新Token无效"),
    REFRESH_TOKEN_EXPIRED(4005, "刷新Token已过期"),
    LOGIN_FAILED(4006, "登录失败"),
    LOGOUT_FAILED(4007, "登出失败"),
    
    // 系统相关错误码 5000-5999
    SYSTEM_ERROR(5001, "系统错误"),
    DATABASE_ERROR(5002, "数据库错误"),
    NETWORK_ERROR(5003, "网络错误"),
    SERVICE_UNAVAILABLE(5004, "服务不可用"),
    RATE_LIMIT_EXCEEDED(5005, "请求频率超出限制"),
    CONFIGURATION_ERROR(5006, "配置错误"),
    
    // 业务相关错误码 6000-6999
    OPERATION_NOT_ALLOWED(6001, "操作不被允许"),
    RESOURCE_LOCKED(6002, "资源被锁定"),
    DUPLICATE_OPERATION(6003, "重复操作"),
    OPERATION_TIMEOUT(6004, "操作超时"),
    QUOTA_EXCEEDED(6005, "配额已超出"),
    DEPENDENCY_ERROR(6006, "依赖错误");
    
    private final Integer code;
    private final String message;
    
    ResultCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
    
    @Override
    public Integer getCode() {
        return code;
    }
    
    @Override
    public String getMessage() {
        return message;
    }
    
    /**
     * 根据错误码获取枚举
     */
    public static ResultCode getByCode(Integer code) {
        for (ResultCode resultCode : values()) {
            if (resultCode.getCode().equals(code)) {
                return resultCode;
            }
        }
        return null;
    }
}