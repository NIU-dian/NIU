package com.yskj.push.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.gargoylesoftware.htmlunit.UnexpectedPage;
import com.gargoylesoftware.htmlunit.WebClient;
import com.yskj.crop.domain.xueqiu.*;
import com.yskj.crop.domain.xueqiu.newStock.NewStock;
import com.yskj.crop.domain.xueqiu.newStock.NewStockItem;
import com.yskj.push.framework.util.DateUtil;
import com.yskj.push.framework.util.HtmlRequestUtil;
import com.yskj.push.framework.util.HttpRequestUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.yskj.crop.constant.TradeConstant.*;

/**
 * @Author kai.tang@yintech.cn
 * @Date 2019/5/10 10:40
 * @Version 1.0.0
 */
@Service
public class XueqiuService {

    private static final Logger LOGGER = LoggerFactory.getLogger(XueqiuService.class);

    @Value("${market.url}")
    private String marketUrl;

    @Value("${min.url}")
    private String minUrl;

    @Value("${inst.url}")
    private String instUrl;

    @Value("${eod.url}")
    private String eodUrl;

    @Value("${new.stock.url}")
    private String newStockUrl;

    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    public static void main(String[] args) {

//        String symbol = "08089";
//        String begin = "1557538037050";
//        String period = "day";
//        String type = "before";
//        String count = "-142";
//        String indicator = "kline,pe,pb,ps,pcf,market_capital,agt,ggt,balance";
//
//        Map<String, Object> params = new HashMap<>();
//        params.put("symbol", symbol);
//        params.put("begin", begin);
//        params.put("period", period);
//        params.put("type", type);
//        params.put("count", count);
//        params.put("indicator", indicator);

        try {
//            String str = getXueQiuRequest(url, params);
//            System.out.println(str);
            XueQiuResult<StockKLineResult> stock = new XueqiuService().getStockKLine("SH600260", "20190513151900", "day", "SH");
            System.out.println(JSON.toJSON(stock));
            XueqiuService xueqiuService = new XueqiuService();
            xueqiuService.sync2TransZuoShou("BRK.A", "");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    /**
     * @param symbol 股票代码
     * @param begin  开始时间 yyyyMMddHHmmss
     * @param period k线类型 一分钟:1m   日:day
     * @param market 股票市场 SH,SZ,HKSE,AMEX,NYSE,NASDAQ
     * @return
     */
    public XueQiuResult<StockKLineResult> getStockKLine(String symbol, String begin, String period, String market) {
        Long beginTime = System.currentTimeMillis();
        if (StringUtils.isNoneEmpty(begin)) {
            beginTime = DateUtil.parseDate(begin, "yyyyMMddHHmm").getTime();
        }

        Map<String, Object> params = new HashMap<>();
        params.put("symbol", symbol);
        params.put("begin", beginTime);
        params.put("period", period);
        params.put("type", "before");
        params.put("count", "-142");
        params.put("indicator", "kline,pe,pb,ps,pcf,market_capital,agt,ggt,balance");
        try {
            String result = getXueQiuRequest(XUE_QIU_K_LINE_URL, params);
            XueQiuResult<StockKLineResult> stockKLineResult = JSON.parseObject(result, new TypeReference<XueQiuResult<StockKLineResult>>() {
            });

            syncData2Market(stockKLineResult, period, market);
            return stockKLineResult;
        } catch (Exception e) {
            LOGGER.error("getStockKLine.error:{}", e);
        }
        return XueQiuResult.getInstance().error();

    }


    public XueQiuResult<MinuteInfoResult> getMinuteInfo(String symbol, String period, String market) {

        Map<String, Object> params = new HashMap<>();
        params.put("symbol", symbol);
        params.put("period", period);
        try {
            String result = getXueQiuRequest(XUE_QIU_MINUTE_URL, params);
            System.out.println("result:" + result);
            XueQiuResult<MinuteInfoResult> minuteInfoResult = JSON.parseObject(result, new TypeReference<XueQiuResult<MinuteInfoResult>>() {
            });
            syncMinuteData2Market(minuteInfoResult, symbol, market);
            System.out.println(JSON.toJSONString(minuteInfoResult));
            return minuteInfoResult;
        } catch (Exception e) {
            LOGGER.error("getStockKLine.error:{}", e);
        }
        return XueQiuResult.getInstance().error();
    }


    /**
     * 获取美股上市预告
     * @return
     */
    public XueQiuResult<NewStock> getUSNewStockInfo() {

        Map<String, Object> params = new HashMap<>();
        params.put("page", 1);
        params.put("size", 90);
        params.put("order", "desc");
        params.put("order_by", "percent");
        params.put("market", "US");
        params.put("type", "unlisted");
        params.put("_", System.currentTimeMillis()/1000);
        try {
            WebClient webClient = HtmlRequestUtil.getWebClient(XUE_QIU_HQ_HTML_URL);
            String result = getXueQiuRequest(XUE_QIU_NEW_STOCK_INFO_URL, params, webClient);
            System.out.println("result:" + result);
            XueQiuResult<NewStock> newStockResult = JSON.parseObject(result, new TypeReference<XueQiuResult<NewStock>>() {
            });

            NewStock stockKLine = newStockResult.getData();
            List<NewStockItem> items = stockKLine.getItems();
            for (NewStockItem item : items) {
                Map<String, Object> detailParams = new HashMap<>();
                detailParams.put("symbol", item.getSymbol());
                detailParams.put("extend", "detail");
                String detailResult = getXueQiuRequest(XUE_QIU_NEW_STOCK_DETAIL_URL, detailParams, webClient);
                XueQiuResult<QuoteInfoResult> stockDetailResult = JSONObject.parseObject(detailResult, new TypeReference<XueQiuResult<QuoteInfoResult>>() {
                });
                item.setExchange(stockDetailResult.getData().getQuote().getExchange());

            }
            syncNewStockData2Market(newStockResult);
            System.out.println(JSON.toJSONString(newStockResult));
            HtmlRequestUtil.closeWebClient(webClient);
            return newStockResult;
        } catch (Exception e) {
            LOGGER.error("getStockKLine.error:{}", e);
        }
        return XueQiuResult.getInstance().error();
    }

    /**
     * 获取港股上市预告
     * @return
     */
    public XueQiuResult<NewStock> getHKNewStockInfo() {

        Map<String, Object> params = new HashMap<>();
        params.put("page", 1);
        params.put("size", 90);
        params.put("order", "desc");
        params.put("order_by", "percent");
        params.put("type", "unlisted");
        params.put("_", System.currentTimeMillis()/1000);
        try {
            WebClient webClient = HtmlRequestUtil.getWebClient(XUE_QIU_HQ_HTML_URL);
            String result = getXueQiuRequest(XUE_QIU_HK_NEW_STOCK_INFO_URL, params, webClient);
            System.out.println("result:" + result);
            XueQiuResult<NewStock> newStockResult = JSON.parseObject(result, new TypeReference<XueQiuResult<NewStock>>() {
            });

            NewStock stockKLine = newStockResult.getData();
            List<NewStockItem> items = stockKLine.getItems();
            for (NewStockItem item : items) {
                Map<String, Object> detailParams = new HashMap<>();
                detailParams.put("symbol", item.getSymbol());
                detailParams.put("extend", "detail");
                String detailResult = getXueQiuRequest(XUE_QIU_NEW_STOCK_DETAIL_URL, detailParams, webClient);
                XueQiuResult<QuoteInfoResult> stockDetailResult = JSONObject.parseObject(detailResult, new TypeReference<XueQiuResult<QuoteInfoResult>>() {
                });
                item.setExchange(stockDetailResult.getData().getQuote().getExchange());
            }
            syncNewStockData2Market(newStockResult);
            HtmlRequestUtil.closeWebClient(webClient);
            System.out.println(JSON.toJSONString(newStockResult));
            return newStockResult;
        } catch (Exception e) {
            LOGGER.error("getStockKLine.error:{}", e);
        }
        return XueQiuResult.getInstance().error();
    }

    /**
     * 获取上证 沪深股上市预告
     * @return
     */
    public XueQiuResult<NewStock> getSZSHNewStockInfo() {

        Map<String, Object> params = new HashMap<>();
        params.put("page", 1);
        params.put("size", 30);
        params.put("order", "asc");
        params.put("order_by", "onl_subbeg_date");
        params.put("type", "subscribe");
        params.put("_", System.currentTimeMillis()/1000);
        try {
            WebClient webClient = HtmlRequestUtil.getWebClient(XUE_QIU_HQ_HTML_URL);
            String result = getXueQiuRequest(XUE_QIU_SZSH_NEW_STOCK_INFO_URL, params, webClient);
            System.out.println("result:" + result);
            XueQiuResult<NewStock> newStockResult = JSON.parseObject(result, new TypeReference<XueQiuResult<NewStock>>() {
            });

            NewStock stockKLine = newStockResult.getData();
            List<NewStockItem> items = stockKLine.getItems();
            for (NewStockItem item : items) {
                Map<String, Object> detailParams = new HashMap<>();
                detailParams.put("symbol", item.getSymbol());
                detailParams.put("extend", "detail");
                String detailResult = getXueQiuRequest(XUE_QIU_NEW_STOCK_DETAIL_URL, detailParams, webClient);
                XueQiuResult<QuoteInfoResult> stockDetailResult = JSONObject.parseObject(detailResult, new TypeReference<XueQiuResult<QuoteInfoResult>>() {
                });
                item.setExchange(stockDetailResult.getData().getQuote().getExchange());
                item.setSymbol(item.getSymbol().substring(2));
                item.setList_date(String.valueOf(item.getOnl_distr_date()));
            }
            syncNewStockData2Market(newStockResult);
            HtmlRequestUtil.closeWebClient(webClient);
            System.out.println(JSON.toJSONString(newStockResult));
            return newStockResult;
        } catch (Exception e) {
            LOGGER.error("getStockKLine.error:{}", e);
        }
        return XueQiuResult.getInstance().error();
    }

    /**
     * 同步即将上市的股票信息到交易系统
     * @param newStockResult
     */
    private void syncNewStockData2Market(XueQiuResult<NewStock> newStockResult) {
        if (0 != newStockResult.getError_code()) {
            return;
        }

        NewStock stockKLine = newStockResult.getData();
        List<NewStockItem> items = stockKLine.getItems();
        for (NewStockItem item : items) {
            threadPoolTaskExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    String issprice_max = item.getIssprice_max();
                    String issprice_min = item.getIssprice_min();
                    String issue_price = item.getIssue_price();
                    String list_date = item.getList_date();
                    String name = item.getName();
                    String percent = item.getPercent();
                    String shares = item.getShares();
                    String symbol = item.getSymbol();
                    String updated_at = item.getUpdated_at();
                    String exchange = item.getExchange();

                    Map<String, Object> param = new HashMap<>();
                    param.put("symbol", symbol);
                    param.put("name", name);
                    param.put("shares", shares);
                    param.put("issprice_max", issprice_max);
                    param.put("issprice_min", issprice_min);
                    param.put("list_date", list_date);
                    param.put("exchange", exchange);
                    HttpRequestUtil.postWithJson(newStockUrl, com.alibaba.fastjson.JSON.toJSONString(param));
                }
            });

        }
    }

    /**
     * 同步分时数据到交易系统
     *
     * @param minuteInfoResult
     * @param symbol
     */
    private void syncMinuteData2Market(XueQiuResult<MinuteInfoResult> minuteInfoResult, String symbol, String market) {
        if (0 != minuteInfoResult.getError_code()) {
            return;
        }

        MinuteInfoResult stockKLine = minuteInfoResult.getData();
        List<Item> items = stockKLine.getItems();
        for (Item item : items) {
            threadPoolTaskExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    Integer amount = item.getAmount();
                    Double avg_price = item.getAvg_price();
                    Double current = item.getCurrent();
                    Long timestamp = item.getTimestamp();
                    Integer volume = item.getVolume();

                    Map<String, Object> param = new HashMap<>();
                    param.put("exchange_name", market);
                    param.put("contract", symbol);
                    param.put("avg_price", avg_price == null ? "" : avg_price.toString());
                    param.put("current", current == null ? "" : current.toString());
                    param.put("timestamp", timestamp == null ? "" : timestamp.toString());
                    param.put("volumn", volume == null ? "" : volume.toString());
                    param.put("amount", amount == null ? "" : amount.toString());
                    HttpRequestUtil.postWithJson(minUrl, com.alibaba.fastjson.JSON.toJSONString(param));
                }
            });

        }

    }

    private String getXueQiuRequest(String url, Map<String, Object> params) throws IOException {
        WebClient webClient = HtmlRequestUtil.getWebClient(XUE_QIU_HQ_HTML_URL);
        return getXueQiuRequest(url, params, webClient);
    }

    private String getXueQiuRequest(String url, Map<String, Object> params, WebClient webClient) throws IOException {
        return HtmlRequestUtil.htmlRequest(url, params, webClient);
    }




    private void syncData2Market(XueQiuResult<StockKLineResult> stockKLineResult, String period, String market) {
        threadPoolTaskExecutor.execute(new Runnable() {
            @Override
            public void run() {
                if (0 != stockKLineResult.getError_code()) {
                    return;
                }
                Integer ktype = DAILY;
                if ("1m".equalsIgnoreCase(period)) {
                    ktype = MINUTE;
                }

                StockKLineResult stockKLine = stockKLineResult.getData();
                String contract = stockKLine.getSymbol();
                String exchange_name = market;
                List<List<String>> items = stockKLine.getItem();
                for (List<String> rows : items) {

                    String tradeTime = rows.get(0);
                    String volumn = rows.get(1);
                    String openPrice = rows.get(2);
                    String highPrice = rows.get(3);
                    String lowPrice = rows.get(4);
                    String closePrice = rows.get(5);
                    String amount = rows.get(9);
                    String setmentPrice = "0";

                    Map<String, Object> param = new HashMap<>();
                    param.put("exchange_name", exchange_name);
                    param.put("trade_date", tradeTime);
                    param.put("contract", contract);
                    param.put("ktype", ktype);
                    param.put("high_price", highPrice);
                    param.put("open_price", openPrice);
                    param.put("low_price", lowPrice);
                    param.put("close_price", closePrice);
                    param.put("volumn", volumn);
                    param.put("amount", amount);
                    param.put("setment_price", setmentPrice);
                    HttpRequestUtil.postWithJson(MARKET_API_URL, com.alibaba.fastjson.JSON.toJSONString(param));
                }
            }
        });

    }


    /**
     * 获取昨收数据信息
     *
     * @return
     */
    public XueQiuResult<Map<String, Object>> getQuoteRequest() {
        XueQiuResult<Map<String, Object>> xueQiuResultMap = new XueQiuResult<>();
        try {
            String instResult = HttpRequestUtil.get(instUrl);
            JSONObject instJsonObject = JSON.parseObject(instResult);
            String code = instJsonObject.getString("code");
            if (!"0".equalsIgnoreCase(code)) {
                return XueQiuResult.getInstance().error();
            }
            String instStr = instJsonObject.getString("data");
            List<String> instList = com.alibaba.fastjson.JSON.parseArray(instStr, String.class);
            WebClient xueqiuWebClient = HtmlRequestUtil.getWebClient(XUE_QIU_HQ_HTML_URL);
            for (String instCode : instList) {
                Map<String, Object> params = new HashMap<>();
                params.put("symbol", instCode);
                params.put("extend", "detail");
                StringBuilder getURL = new StringBuilder();
                getURL.append(XUE_QIU_ZUO_SHOU_URL);
                if (null != params && params.size() > 0) {
                    getURL.append("?");
                    for (Map.Entry<String, Object> param : params.entrySet()) {
                        if (param.getValue() != null) {
                            getURL.append(param.getKey()).append("=").append(param.getValue().toString()).append("&");
                        }
                    }
                    getURL.deleteCharAt(getURL.length() - 1);
                }
                UnexpectedPage jsonPage = xueqiuWebClient.getPage(getURL.toString());
                BufferedReader reader = new BufferedReader(new InputStreamReader(jsonPage.getInputStream(), "UTF-8"));
                String s; // 依次循环，至到读的值为空
                StringBuilder sb = new StringBuilder();
                while ((s = reader.readLine()) != null) {
                    sb.append(s);
                }
                reader.close();


                String result = sb.toString();
                LOGGER.info("result:{}", result);
                sync2TransZuoShou(instCode, result);
            }
            xueqiuWebClient.close();
            return xueQiuResultMap;
        } catch (Exception e) {
            LOGGER.error("getStockKLine.error:{}", e);
        }
        return XueQiuResult.getInstance().error();
    }

    private void sync2TransZuoShou(String symbol, String result) {
        XueQiuResult<QuoteInfoResult> quoteInfoResult = JSON.parseObject(result, new TypeReference<XueQiuResult<QuoteInfoResult>>() {
        });
        if( quoteInfoResult == null || quoteInfoResult.getData() == null || quoteInfoResult.getData().getQuote() == null ){
            return;
        }
        Double high = quoteInfoResult.getData().getQuote().getHigh();
        Double lastClose = quoteInfoResult.getData().getQuote().getLast_close();
        Double current = quoteInfoResult.getData().getQuote().getCurrent();
        Double open = quoteInfoResult.getData().getQuote().getOpen();
        Double low = quoteInfoResult.getData().getQuote().getLow();
        Long time = quoteInfoResult.getData().getQuote().getTime();
        String amount = quoteInfoResult.getData().getQuote().getAmount() + "";
        String volume = quoteInfoResult.getData().getQuote().getVolume() + "";
        String high52w = quoteInfoResult.getData().getQuote().getHigh52w() + "";
        String low52w = quoteInfoResult.getData().getQuote().getLow52w() + "";
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("symbol", symbol);
        resultMap.put("high", high == null ? "":high.toString());
        resultMap.put("last_close", lastClose == null ? "" : lastClose.toString());
        resultMap.put("current", current == null ? "" : current.toString());
        resultMap.put("open", open == null ? "" : open.toString());
        resultMap.put("low", low == null ? "" : low.toString());
        resultMap.put("time", time == null ? "" : time.toString());
        resultMap.put("amount", amount);
        resultMap.put("volume", volume);
        resultMap.put("high52w", high52w);
        resultMap.put("low52w", low52w);
        threadPoolTaskExecutor.execute(new Runnable() {
            @Override
            public void run() {
                HttpRequestUtil.postWithJson(eodUrl, com.alibaba.fastjson.JSON.toJSONString(resultMap));
            }
        });
    }


}
