package com.baojikouyu.teach.shiro;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.apache.shiro.authc.AuthenticationToken;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class JWTToken implements AuthenticationToken, Serializable {


    // 密钥
    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }


    public JWTToken() {
    }

    public JWTToken(String token) {
        this.token = token;
    }

    @Override
    public Object getPrincipal() {
        return token;
    }

    @Override
    public Object getCredentials() {
        return token;
    }
}
