package com.yskj.crop.constant;/**
 * Created by Administrator on 2016/10/31.
 */

import java.util.ArrayList;
import java.util.List;

/**
 * @author kai.tang
 * @version 1.0
 * @date 2016/10/31 16:31
 */
public class PageResult<T>  extends BaseResultDomain{

    private Integer currentPage;

    private Integer totalPage;

    private Integer totalCount;

    private List<T> data;

    public Integer getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }

    public Integer getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(Integer totalPage) {
        this.totalPage = totalPage;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public static PageResult getInstance(){
        return new PageResult();
    }

    public PageResult<T> success(){
        return success(new ArrayList<Object>(), 0, 0, 0);
    }

    public PageResult<T> success(List<Object> data){
        PageResult result = new PageResult();
        result.setCode(ApiConstant.Code.SUCCESS.getCode());
        result.setMessage(ApiConstant.Code.SUCCESS.getMessage());
        result.setData(data);
        return result;
    }

    public PageResult<T> success(List<Object> data, Integer currentPage, Integer totalCount, Integer totalPage){
        PageResult result = new PageResult();
        result.setCode(ApiConstant.Code.SUCCESS.getCode());
        result.setMessage(ApiConstant.Code.SUCCESS.getMessage());
        result.setData(data);
        result.setCurrentPage(currentPage);
        result.setTotalCount(totalCount);
        result.setTotalPage(totalPage);
        return result;
    }

    public PageResult<T> error(){
        PageResult result = new PageResult();
        result.setCode(ApiConstant.Code.SYSTEM_ERROR.getCode());
        result.setMessage( ApiConstant.Code.SYSTEM_ERROR.getMessage());
        return result;
    }

    public PageResult<T> error(Integer code, String message){
        PageResult result = new PageResult();
        result.setCode(code.toString());
        result.setMessage(message);
        return result;
    }

    public PageResult<T> error(String code, String message){
        PageResult result = new PageResult();
        result.setCode(code);
        result.setMessage(message);
        return result;
    }
}
