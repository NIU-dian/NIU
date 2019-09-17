package com.yskj.crop.constant;


public interface ApiConstant {


    /**
     * api请求根
     */
    String API_ROOT = "/api";
    /**
     * api版本号码
     */
    String API_V_1 = "/1";


    /**
     * 返回结果代码
     */
    enum Code{
        SUCCESS("0","操作成功"),
        SYSTEM_ERROR("10","服务器开小差了，请稍后重试！");


        private String code;
        private String message;

        public String getCode() {
            return code;
        }

        public String getMessage() {
            return message;
        }


        Code(String code, String message){
            this.code = code;
            this.message = message;
        }

        public static String getMessage(String code){
            for(Code c : Code.values()){
                if(c.code.equals(code)){
                    return c.message;
                }
            }
            return "unknown error";
        }
    }

}
