package com.nyx.musicPlatform.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * 用户登录请求数据传输对象
 * 
 * @author nyx
 * @since 2025-06
 */
@Data
@Schema(description = "用户登录请求")
public class UserLoginDTO {

    @Schema(description = "用户名或邮箱", required = true, example = "admin")
    @NotBlank(message = "用户名或邮箱不能为空")
    private String usernameOrEmail;

    @Schema(description = "密码", required = true, example = "123456")
    @NotBlank(message = "密码不能为空")
    @Size(min = 6, max = 20, message = "密码长度必须在6-20个字符之间")
    private String password;

    @Schema(description = "记住我", example = "false")
    private Boolean rememberMe = false;
}