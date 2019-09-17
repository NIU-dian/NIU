package com.yskj.push.framework.util;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.UnexpectedPage;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Map;

public class HtmlRequestUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(HtmlRequestUtil.class);


    /**
     * 使用浏览器发起html请求
     * @param url
     * @param params
     * @param webClient
     * @return
     * @throws java.io.IOException
     */
    public static String htmlRequest(String url, Map<String, Object> params, WebClient webClient) throws java.io.IOException {
        StringBuilder getURL = new StringBuilder();
        getURL.append(url);
        if (null != params && params.size() > 0) {
            getURL.append("?");
            for (Map.Entry<String, Object> param : params.entrySet()) {
                if (param.getValue() != null) {
                    getURL.append(param.getKey()).append("=").append(param.getValue().toString()).append("&");
                }
            }
            getURL.deleteCharAt(getURL.length() - 1);
        }
        UnexpectedPage jsonPage = webClient.getPage(getURL.toString());
        BufferedReader reader = new BufferedReader(new InputStreamReader(jsonPage.getInputStream(), "UTF-8"));
        String s; // 依次循环，至到读的值为空
        StringBuilder sb = new StringBuilder();
        while ((s = reader.readLine()) != null) {
            sb.append(s);
        }
        reader.close();

        return sb.toString();
    }




    /**
     * 初始化雪球请求信息
     *
     * @return
     * @throws java.io.IOException
     */
    public static WebClient getWebClient(String url) throws java.io.IOException {
        WebClient webClient = new WebClient(BrowserVersion.CHROME);
        webClient.getOptions().setJavaScriptEnabled(true);
        webClient.getOptions().setCssEnabled(false);
        webClient.getOptions().setThrowExceptionOnScriptError(false);
        webClient.getOptions().setTimeout(30 * 1000);
        webClient.getOptions().setActiveXNative(false);
        webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
        webClient.setAjaxController(new NicelyResynchronizingAjaxController());

        HtmlPage page = webClient.getPage(url);
        webClient.waitForBackgroundJavaScript(10 * 1000);
        LOGGER.info(page.asXml());
        return webClient;
    }


    public static void closeWebClient(WebClient webClient){
        try {
            webClient.close();
        } catch (Exception e) {
            LOGGER.error("关闭浏览器异常");
        }
    }


}
