package com.baojikouyu.teach.pojo;

import lombok.Data;
import javax.validation.constraints.NotBlank;

/**
 * 前端发过来的数据传输类，包含用户信息和验证码信息
 */

@Data
public class AuthUserDto {

    private Boolean rememberMe;
    @NotBlank
    private String username;

    @NotBlank
    private String password;

    private String code;

    private String uuid = "";

    public boolean isRememberMe() {
        return rememberMe;
    }

}
