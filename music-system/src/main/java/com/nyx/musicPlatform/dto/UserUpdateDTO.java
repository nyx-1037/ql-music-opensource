package com.nyx.musicPlatform.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

/**
 * 用户更新请求数据传输对象
 * 
 * @author nyx
 * @since 2025-06
 */
@Data
@Schema(description = "用户更新请求")
public class UserUpdateDTO {

    @Schema(description = "邮箱", example = "newemail@example.com")
    @Email(message = "邮箱格式不正确")
    private String email;

    @Schema(description = "昵称", example = "新昵称")
    @Size(max = 50, message = "昵称长度不能超过50个字符")
    private String nickname;

    @Schema(description = "头像URL", example = "https://example.com/avatar.jpg")
    private String avatarUrl;
}