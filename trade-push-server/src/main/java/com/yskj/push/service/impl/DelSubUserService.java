package com.yskj.push.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yskj.push.framework.util.HttpRequestUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author kai.tang@yintech.cn
 * @Date 2019/6/3 15:57
 * @Version 1.0.0
 */
public class DelSubUserService {


    public static void main(String[] args) {

        String accounts  = "0000005711110,0000005710640,0000003680121,0000003680081,0000003620620,0000003620610,0000003620600,0000003620540,0000003680021,0000003670090,0000003670081,0000003670071,0000003670061,0000003650290,0000003650280,0000003670021,0000003610330,0000003610310,0000003610270,0000003460151,0000003460111,0000003460161,0000003430700,0000003430680,0000003460141,0000003430670,0000003460071,0000003460021,0000003440820,0000003440800,0000003420950,0000003450850,0000003450840,0000003911890,0000003911600,0000003940400,0000003550560,0000003911790,0000003911690,0000005710320,0000005710960,0000005710800,0000003440700,0000003620570,0000003450780,0000006321663,0000006321633,0000006328593,0000006328573,0000006329493,0000006350543,0000006350463,0000003320913,0000003340183,0000003310993,0000006325783,0000006321103,0000006327783,0000006326563,0000006323643,0000006323623,0000006324463,0000006329492,0000006350542,0000006350462,0000003320912,0000003340182,0000003310992,0000006325782,0000006321102,0000006327782,0000006326562,0000006323642,0000006323622,0000006324462,0000006321664,0000006321634,0000006328594,0000006328574,0000006329494,0000006350544,0000006350464,0000003320914,0000003340184,0000003310994,0000006325784,0000006321104,0000006327784,0000006326564,0000006323644,0000006323624,0000006324464,0000006321665,0000006321635,0000006328595,0000006328575,0000006329495,0000006350545,0000006350465,0000003320915,0000003340185,0000003310995,0000006325785,0000006321105,0000006327785,0000006326565,0000006323645,0000006323625,0000006324465";
        String[] accountArry = accounts.split(",");
        StringBuilder sb = new StringBuilder();
        for(String account : accountArry) {
            try {
                //查询用户信息
                String userInfoUrl = "https://user.hz5800.com/api/1/user/web/info/account";
                Map<String, Object> userInfoParam = new HashMap<>();
                userInfoParam.put("account", account);
                String userInfoResult = HttpRequestUtil.get(userInfoUrl, userInfoParam);
                JSONObject jsonResult = JSON.parseObject(userInfoResult);
                if (jsonResult.getInteger("code") != 1) {
                    System.out.println("未找到该账号信息");
                    continue;
                }
                JSONObject data = jsonResult.getJSONObject("data");
                String userName = data.getString("username");
                Integer accountType = 21;
                Integer serverId = 104;
                sb.append("http://mgr.hz5800.com/admin/subuser/delete?");
                sb.append("username=").append(userName).append("&");
                sb.append("account=").append(account).append("&");
                sb.append("accountType=").append(accountType).append("&");
                sb.append("serverId=").append(serverId).append("\r\n");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.out.println(sb.toString());


//
//
//        //删除用户信息
//        String url = "http://mgr.hz5800.com/admin/subuser/delete?username="+ userName+"&account="+ account +"&accountType=21&serverId=100";
//        Map<String, Object> params = new HashMap<>();
////        params.put("username", userName);
////        params.put("account", account);
////        params.put("accountType", accountType);
////        params.put("serverId", "104");
//        Map<String, String> heads = new HashMap<>();
//        //设置管理后台登录的cookie
//        heads.put("Cookie", "JSESSIONID=32E9D13AD3B42E64D519B5DF1C084D57");
//        heads.put("Origin", "http://mgr.hz5800.com");
//        heads.put("Referer", "http://mgr.hz5800.com/");
//        heads.put("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/74.0.3729.157 Safari/537.36");
//        heads.put("X-Requested-With", "XMLHttpRequest");
//        heads.put("DNT", "1");
//        String result = HttpRequestUtil.post(url, params, heads);
//        System.out.println(result);
    }






}
