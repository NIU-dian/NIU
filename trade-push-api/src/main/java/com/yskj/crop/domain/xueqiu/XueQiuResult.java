package com.yskj.crop.domain.xueqiu;

import com.yskj.crop.constant.ApiConstant;
import com.yskj.crop.response.tushare.TushareResult;

import java.util.Map;

/**
 * @Author kai.tang@yintech.cn
 * @Date 2019/5/13 10:02
 * @Version 1.0.0
 */
public class XueQiuResult<T> {

    private int error_code;

    private String error_description;

    private T data;

    public int getError_code() {
        return error_code;
    }

    public void setError_code(int error_code) {
        this.error_code = error_code;
    }

    public String getError_description() {
        return error_description;
    }

    public void setError_description(String error_description) {
        this.error_description = error_description;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }



    public static XueQiuResult getInstance(){
        return new XueQiuResult();
    }

    public XueQiuResult<T> error(){
        XueQiuResult result = new XueQiuResult();
        result.setError_code(Integer.valueOf(ApiConstant.Code.SYSTEM_ERROR.getCode()));
        result.setError_description(ApiConstant.Code.SYSTEM_ERROR.getMessage());
        return result;
    }

    public XueQiuResult<Map<String,Object>> success() {
        XueQiuResult result = new XueQiuResult();
        result.setError_code(Integer.valueOf(ApiConstant.Code.SUCCESS.getCode()));
        result.setError_description(ApiConstant.Code.SUCCESS.getMessage());
        return result;
    }
}
