package com.yskj.push.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yskj.push.framework.util.HttpRequestUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class SseStockService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SseStockService.class);


    @Value("${stock.ah.rel.url}")
    private  String stockAhRelUrl ;

    private static final String SSE_URL = "http://www.sse.com.cn/assortment/stock/areatrade/ahassortment/";



    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;


    public static void main(String[] args) {

        SseStockService sseStockService = new SseStockService();
//        sseStockService.pushSseStock();
//        sseStockService.pushSseHU2HK();
//        sseStockService.pushSseSZ2HK();

    }

    public  void pushSseStock() {
        try {
            Document doc = Jsoup.connect(SSE_URL).get();
            Elements tableEles = doc.getElementsByClass("table");
            Element tableEle = tableEles.last();
            Elements trEles = tableEle.getElementsByTag("tr");
            for(int i = 0; i < trEles.size(); i ++){
                Element tr = trEles.get(i);
                Elements tdEles = tr.getElementsByTag("td");
                if( tdEles.isEmpty() ){
                    continue;
                }
                Map<String, String> params = new HashMap();
                params.put("AInstCode", tdEles.get(1).text());
                params.put("AInstName", tdEles.get(2).text());
                params.put("HInstCode", tdEles.get(3).text());

                threadPoolTaskExecutor.execute(new Runnable() {
                    @Override
                    public void run() {
                        HttpRequestUtil.postWithJson(stockAhRelUrl, JSON.toJSONString(params));
                    }
                });
            }
        } catch (Exception e) {
            LOGGER.error("获取对照表信息异常：{}", e);
        }
    }











}
