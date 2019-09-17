package com.yskj.push.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yskj.crop.constant.Result;
import com.yskj.push.framework.util.DateUtil;
import com.yskj.push.framework.util.HttpRequestUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

import static com.yskj.crop.constant.TradeConstant.DAILY;
import static com.yskj.crop.constant.TradeConstant.MARKET_API_URL;
import static com.yskj.crop.constant.TradeConstant.SINA_GC_URL;

/**
 * @Author kai.tang@yintech.cn
 * @Date 2019/5/14 14:10
 * @Version 1.0.0
 */
@Service("sinaService")
public class SinaService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SinaService.class);

    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;


    public static void main(String[] args) {

        String symbol = "GC";
        String date = "2019_5_14";
//        syncGold2Market(symbol, date);



    }

    public Result getGoldKLine(String symbol, String date ){
        String json =  getCOMEXGold(symbol, date);
        JSONArray jsonArray = JSON.parseArray(json);
        syncGold2Market(jsonArray, symbol);
        return Result.getInstance().success(jsonArray);
    }

    /**
     * 同步数据到行情服务
     * @param jsonArray
     * @param symbol
     */
    private void syncGold2Market(JSONArray jsonArray, String symbol) {
        threadPoolTaskExecutor.execute(new Runnable() {
            @Override
            public void run() {
                for( int i = 0; i < jsonArray.size(); i ++){
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String tradeDate = jsonObject.getString("date");
                    String open = jsonObject.getString("open");
                    String high = jsonObject.getString("high");
                    String low = jsonObject.getString("low");
                    String close = jsonObject.getString("close");
                    String volume = jsonObject.getString("volume");

                    Map<String, Object> param = new HashMap<>();
                    param.put("exchange_name", symbol);
                    param.put("trade_date", DateUtil.parseDate(tradeDate, "yyyy-MM-dd").getTime());
                    param.put("contract", "COMEX");
                    param.put("ktype", DAILY);
                    param.put("high_price", high);
                    param.put("open_price", open);
                    param.put("low_price", low);
                    param.put("close_price", close);
                    param.put("volumn", volume);
                    param.put("amount", "");
                    param.put("setment_price", "");
                    HttpRequestUtil.postWithJson(MARKET_API_URL, JSON.toJSONString(param));
                }
            }
        });

    }

    private  String getCOMEXGold(String symbol, String date) {
        StringBuilder gcUrl = new StringBuilder();
        gcUrl.append(SINA_GC_URL);
        gcUrl.append("/var%20_");
        gcUrl.append(symbol).append(date);
        gcUrl.append("=/GlobalFuturesService.getGlobalFuturesDailyKLine");
        gcUrl.append("?");
        gcUrl.append("symbol=").append(symbol);
        gcUrl.append("&_=").append(date);

        String url = gcUrl.toString();
        String result = HttpRequestUtil.get(url);
        String json = result.substring(result.indexOf("(") + 1, result.lastIndexOf(");"));
        return json;
    }


}
