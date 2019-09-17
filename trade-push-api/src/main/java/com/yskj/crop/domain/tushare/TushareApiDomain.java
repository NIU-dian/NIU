/**
 * Copyright 2019 bejson.com
 */
package com.yskj.crop.domain.tushare;

/**
 * Auto-generated: 2019-05-09 17:2:52
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class TushareApiDomain {

    /**
     * api名称
     */
    private String api_name;

    /**
     * 请求token
     */
    private String token;
    private Object params;

    private String fields = "";

    public void setApi_name(String api_name) {
        this.api_name = api_name;
    }

    public String getApi_name() {
        return api_name;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setParams(Object params) {
        this.params = params;
    }

    public Object getParams() {
        return params;
    }

    public void setFields(String fields) {
        this.fields = fields;
    }

    public String getFields() {
        return fields;
    }

}