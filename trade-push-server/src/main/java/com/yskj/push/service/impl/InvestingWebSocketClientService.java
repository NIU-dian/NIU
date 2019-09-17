package com.yskj.push.service.impl;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.yskj.push.framework.util.HtmlRequestUtil;
import com.yskj.push.listener.Jin10Listener;


/**
 * @Author kai.tang@yintech.cn
 * @Date 2019/8/28 18:07
 * @Version 1.0.0
 */

public class InvestingWebSocketClientService {


    private static final String JIN10_URL = "https://datacenter.jin10.com/market";

    public static void main(String[] args){
        try {
            WebClient webClient = HtmlRequestUtil.getWebClient(JIN10_URL);
            webClient.waitForBackgroundJavaScript(10 * 1000);
            HtmlPage page = webClient.getPage(JIN10_URL);
            System.out.println(page.asXml());
            webClient.getInternals().addListener(new Jin10Listener());
            System.out.println(" end ");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}

