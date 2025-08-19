package com.nyx.musicPlatform.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * 用户注册请求数据传输对象
 * 
 * @author nyx
 * @since 2025-06
 */
@Data
@Schema(description = "用户注册请求")
public class UserRegisterDTO {

    @Schema(description = "用户名", required = true, example = "testuser")
    @NotBlank(message = "用户名不能为空")
    @Size(min = 3, max = 20, message = "用户名长度必须在3-20个字符之间")
    private String username;

    @Schema(description = "密码", required = true, example = "123456")
    @NotBlank(message = "密码不能为空")
    @Size(min = 6, max = 20, message = "密码长度必须在6-20个字符之间")
    private String password;

    @Schema(description = "确认密码", required = true, example = "123456")
    private String confirmPassword;

    @Schema(description = "邮箱", required = true, example = "test@example.com")
    @NotBlank(message = "邮箱不能为空")
    @Email(message = "邮箱格式不正确")
    private String email;

    @Schema(description = "昵称", example = "测试用户")
    @Size(max = 50, message = "昵称长度不能超过50个字符")
    private String nickname;
}