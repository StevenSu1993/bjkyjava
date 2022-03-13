package com.baojikouyu.teach.shiro;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.apache.shiro.authz.SimpleAuthorizationInfo;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CoustomSimpleAuthorizationInfo extends SimpleAuthorizationInfo {


}
