package com.nyx.musicPlatform.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 登录响应视图对象
 * 
 * @author nyx
 * @since 2025-06
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "登录响应")
public class LoginVO {

    @Schema(description = "访问令牌")
    private String accessToken;

    @Schema(description = "刷新令牌")
    private String refreshToken;

    @Schema(description = "令牌类型")
    private String tokenType = "Bearer";

    @Schema(description = "访问令牌过期时间（秒）")
    private Long expiresIn;

    @Schema(description = "用户信息")
    private UserVO userInfo;

    public LoginVO(String accessToken, String refreshToken, Long expiresIn, UserVO userInfo) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.expiresIn = expiresIn;
        this.userInfo = userInfo;
    }
}