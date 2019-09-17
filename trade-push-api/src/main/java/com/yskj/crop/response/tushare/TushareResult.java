/**
 * Copyright 2019 bejson.com
 */
package com.yskj.crop.response.tushare;

import com.yskj.crop.constant.ApiConstant;
import com.yskj.crop.constant.Result;

/**
 * Auto-generated: 2019-05-09 17:25:3
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class TushareResult<T> {

    private String request_id;
    private int code;
    private String msg;
    private T data;

    public void setRequest_id(String request_id) {
        this.request_id = request_id;
    }

    public String getRequest_id() {
        return request_id;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public void setData(T data) {
        this.data = data;
    }

    public T getData() {
        return data;
    }

    public static TushareResult getInstance(){
        return new TushareResult();
    }

    public TushareResult<T> error(){
        TushareResult result = new TushareResult();
        result.setCode(Integer.valueOf(ApiConstant.Code.SYSTEM_ERROR.getCode()));
        result.setMsg( ApiConstant.Code.SYSTEM_ERROR.getMessage());
        return result;
    }
}