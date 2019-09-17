package com.yskj.crop.constant;/**
 * Created by Administrator on 2016/10/27.
 */

/**
 * @author kai.tang
 * @version 1.0
 * @date 2016/10/27 13:00
 */
public class Result<T> extends BaseResultDomain {

    private T data;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public static Result getInstance(){
        return new Result();
    }

    public Result<T> success(){
        Result result = new Result();
        result.setCode(ApiConstant.Code.SUCCESS.getCode());
        result.setMessage(ApiConstant.Code.SUCCESS.getMessage());
        return result;
    }

    public Result<T> success(Object data){
        Result result = new Result();
        result.setCode(ApiConstant.Code.SUCCESS.getCode());
        result.setMessage(ApiConstant.Code.SUCCESS.getMessage());
        result.setData(data);
        return result;
    }

    public Result<T> error(){
        Result result = new Result();
        result.setCode(ApiConstant.Code.SYSTEM_ERROR.getCode());
        result.setMessage( ApiConstant.Code.SYSTEM_ERROR.getMessage());
        return result;
    }

    public Result<T> error(String code, String message){
        Result result = new Result();
        result.setCode(code);
        result.setMessage(message);
        return result;
    }


}
