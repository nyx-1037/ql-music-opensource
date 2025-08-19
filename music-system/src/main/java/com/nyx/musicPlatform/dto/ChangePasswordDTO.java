package com.nyx.musicPlatform.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * 修改密码请求数据传输对象
 * 
 * @author nyx
 * @since 2025-06
 */
@Data
@Schema(description = "修改密码请求")
public class ChangePasswordDTO {

    @Schema(description = "原密码", required = true, example = "oldpassword")
    @NotBlank(message = "原密码不能为空")
    private String oldPassword;

    @Schema(description = "新密码", required = true, example = "newpassword")
    @NotBlank(message = "新密码不能为空")
    @Size(min = 6, max = 20, message = "新密码长度必须在6-20个字符之间")
    private String newPassword;

    @Schema(description = "确认新密码", required = true, example = "newpassword")
    @NotBlank(message = "确认新密码不能为空")
    private String confirmPassword;
}