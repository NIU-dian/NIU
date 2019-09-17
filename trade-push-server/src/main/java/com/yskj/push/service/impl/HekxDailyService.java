package com.yskj.push.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.yskj.crop.domain.hekx.daily.Content;
import com.yskj.crop.domain.hekx.daily.HekxDailyStat;
import com.yskj.crop.domain.hekx.daily.Table;
import com.yskj.crop.domain.hekx.daily.Tr;
import com.yskj.crop.domain.hekx.rel.HekxRelStat;
import com.yskj.push.framework.util.DateUtil;
import com.yskj.push.framework.util.HtmlRequestUtil;
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

import java.util.*;

@Service
public class HekxDailyService {

    private static final Logger LOGGER = LoggerFactory.getLogger(HekxDailyService.class);

    @Value("${stock.top.day.url}")
    private String stockTopDayUrl;

    @Value("${stock.capital.day.url}")
    private String stockCapitalDayUrl = "http://test-quotes.fdzq.com:8071/stockCapitalDay";

    private static final String HEKX_URL = "https://www.hkex.com.hk/chi/csm/DailyStat/data_tab_daily_%sc.js?_=%s";

    private static final String REL_TIME_HEKX_URL = "https://sc.hkex.com.hk/TuniS/www.hkex.com.hk/eng/csm/script/data_%s_Turnover_eng.js?_=%s";

    private static final String SSE_HU_HK_URL = "http://yunhq.sse.com.cn:32041/v1/hkp/status/amount_status";

    private static final String SSE_SZ_HK_URL = "http://www.szse.cn/api/market/sgt/dailyamount";

    private static final String SSE_NBSH_URL = "https://sc.hkex.com.hk/TuniS/www.hkex.com.hk/chi/csm/script/data_NBSH_QuotaUsage_chi.js";

    private static final String SSE_NBSZ_URL = "https://sc.hkex.com.hk/TuniS/www.hkex.com.hk/chi/csm/script/data_NBSZ_QuotaUsage_chi.js";

    /**
     * 沪股通
     */
    private static final String HKEX_HU_HK_URL = "https://www.hkex.com.hk/-/media/HKEX-Market/Mutual-Market/Stock-Connect/Eligible-Stocks/View-All-Eligible-Securities_xls/SSE_Securities_c.xls?la=zh-HK";

    /**
     * 港股通（沪）
     */
    private static final String SSE_HK_HU_URL = "http://www.sse.com.cn/services/hkexsc/disclo/eligible/";

    /**
     * 深股通
     */
    private static final String HKEX_SZ_HK_URL = "https://www.hkex.com.hk/-/media/HKEX-Market/Mutual-Market/Stock-Connect/Eligible-Stocks/View-All-Eligible-Securities_xls/SZSE_Securities_c.xls?la=zh-HK";

    /**
     * 港股通（深）
     */
    private static final String SZSE_HK_SZ_URL = "http://www.szse.cn/api/report/ShowReport?SHOWTYPE=xls&CATALOGID=SGT_GGTBDQD&TABKEY=tab1&random=0.8222886835328729";


    @Value("${stock.connect.url}")
    private String stockConnectUrl  = "http://test-quotes.fdzq.com:8071/stockConnect" ;


    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    public static void main(String[] args) {
        HekxDailyService hekxDailyService = new HekxDailyService();
//        hekxDailyService.pushHekxRel();
//        hekxDailyService.pushHekxDaily();
//        hekxDailyService.pushSseHU2HK();
//        hekxDailyService.pushSseSZ2HK();
//        hekxDailyService.pushNBSH();
//        hekxDailyService.pushNBSZ();
        hekxDailyService.hkexHuHkData();
        hekxDailyService.sseHkHu();
        hekxDailyService.hkexSzHk();
        hekxDailyService.szseHkSz();

    }

    /**
     * 推送实时的香港股票信息
     */
    public void pushHekxRel() {
        String[] markets = new String[]{"NBSH", "SBSH", "NBSZ", "SBSZ"};
        for (String market : markets) {
            Long currentTime = System.currentTimeMillis();
            String requestUrl = String.format(REL_TIME_HEKX_URL, market, currentTime.toString());
            String result = HttpRequestUtil.get(requestUrl);
            String resultJson = result.substring(result.indexOf("=") + 2, result.length() - 2);
            LOGGER.info("resultJson:{}", resultJson);
            Object obj = JSON.parse(resultJson);
            LOGGER.info("resultObj:{}", obj);
            HekxRelStat hekxRelStat = JSON.parseObject(resultJson, HekxRelStat.class);
            String tableHead = hekxRelStat.getTablehead().get(0);
            String type = getMarketType(tableHead);
            List<List<String>> item = hekxRelStat.getSection().get(0).getItem();
            List<String> subtitle = hekxRelStat.getSection().get(0).getSubtitle();
            //获取当前拉去的时间
            String tradeDate = subtitle.get(1);
            Date tradeDateTime = DateUtil.parseDate(tradeDate, "dd/MM/yyyy (HH:mm)");
            String buy = item.get(1).get(1);
            String sell = item.get(2).get(1);
            String buyMoney = buy.substring(3);
            String sellMoney = sell.substring(3);
            String moneyType = getMoneyType(tableHead);

            Map<String, String> params = new HashMap<>();
            params.put("TradeDay", tradeDateTime.getTime() / 1000 + "");
            params.put("Type", type);
            params.put("BuyAmount", buyMoney);
            params.put("SellAmount", sellMoney);
            params.put("Currency", moneyType);
            threadPoolTaskExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    HttpRequestUtil.postWithJson(stockCapitalDayUrl, JSON.toJSONString(params));
                }
            });
        }
    }

    /**
     * 推送每天的股票信息
     */
    public void pushHekxDaily() {
        String currentDate = DateUtil.getFormatDate(new Date(), "yyyyMMdd");
        Long currentTime = System.currentTimeMillis();
        String requestUrl = String.format(HEKX_URL, currentDate, currentTime.toString());
        String hekxData = HttpRequestUtil.get(requestUrl);
        String hekxJsonData = hekxData.substring(10);
        LOGGER.info("hekxJsonData:{}", hekxJsonData);
        List<HekxDailyStat> hekxDailyStatList = JSON.parseObject(hekxJsonData, new TypeReference<List<HekxDailyStat>>() {
        });
        for (HekxDailyStat hekxDailyStat : hekxDailyStatList) {
            LOGGER.info("market:{}", hekxDailyStat.getMarket());
            List<Content> contentList = hekxDailyStat.getContent();
            //只获取top10的数据信息
            Content content = contentList.get(1);
            Table table = content.getTable();
            List<Tr> trList = table.getTr();
            //内容
            for (Tr tr : trList) {
                List<List<String>> tdList = tr.getTd();
                for (List<String> stockList : tdList) {
                    String instCode = stockList.get(1);
                    String instName = stockList.get(2);
                    String buyAmount = stockList.get(3);
                    String sellAmount = stockList.get(4);

                    Long tradeDay = hekxDailyStat.getDate().getTime() / 1000;
                    String marketType = getMarketType(hekxDailyStat.getMarket());
                    Map<String, String> params = new HashMap<>();
                    params.put("InstCode", instCode);
                    params.put("InstName", instName);
                    params.put("BuyAmount", buyAmount);
                    params.put("SellAmount", sellAmount);
                    params.put("TradeDay", tradeDay.toString());
                    params.put("Type", marketType);
                    threadPoolTaskExecutor.execute(new Runnable() {
                        @Override
                        public void run() {
                            HttpRequestUtil.postWithJson(stockTopDayUrl, JSON.toJSONString(params));
                        }
                    });

                }
            }
        }

    }

    private static String getMoneyType(String tableHead) {
        String moneyType = null;
        if ("Shanghai > Hong Kong".equals(tableHead)) {
            moneyType = "HK";
        }
        if ("Shenzhen > Hong Kong".equals(tableHead)) {
            moneyType = "HK";
        }
        if ("Hong Kong > Shanghai".equals(tableHead)) {
            moneyType = "RMB";
        }
        if ("Hong Kong > Shenzhen".equals(tableHead)) {
            moneyType = "RMB";
        }
        return moneyType;
    }


    private static String getMarketType(String market) {
        String marketType = null;
        if ("SSE Southbound".equals(market) || "Shanghai > Hong Kong".equals(market)) {
            marketType = "1";
        }
        if ("SZSE Southbound".equals(market) || "Shenzhen > Hong Kong".equals(market)) {
            marketType = "2";
        }
        if ("SSE Northbound".equals(market) || "Hong Kong > Shanghai".equals(market)) {
            marketType = "3";
        }
        if ("SZSE Northbound".equals(market) || "Hong Kong > Shenzhen".equals(market)) {
            marketType = "4";
        }
        return marketType;
    }


    /**
     * 沪深到香港
     */
    public void pushSseHU2HK() {
        String resultJson = HttpRequestUtil.get(SSE_HU_HK_URL);
        JSONObject jsonObject = JSON.parseObject(resultJson);
        String date = jsonObject.getString("date");
        String type = "1";
        JSONArray jsonArray = jsonObject.getJSONArray("status");
        String totalAmount = jsonArray.get(3).toString();
        String balanceAmount = jsonArray.getString(4);
        Long dateTime = DateUtil.parseDate(date, "yyyyMMdd").getTime() / 1000;
        pushRelData(type, "", dateTime.toString(), totalAmount, balanceAmount, "RMB");

    }

    /**
     * 深证到香港
     */
    public void pushSseSZ2HK() {
        String resultJson = null;
        try {
            resultJson = HttpRequestUtil.get(SSE_SZ_HK_URL);
            JSONObject jsonObject = JSON.parseObject(resultJson);
            String tradeDate = jsonObject.getString("gxsj");
            String type = "2";
            String total = jsonObject.getString("mred");
            String balance = jsonObject.getString("edye");
            String percent = jsonObject.getString("edyeratio");

            Long dateTime = DateUtil.parseDate(tradeDate, "yyyy-MM-dd HH:mm").getTime() / 1000;
            Long totalAmount = Long.valueOf(total) * 100000000;
            Long balanceAmount = Long.valueOf(balance) * 1000000;
            pushRelData(type, percent, dateTime.toString(), totalAmount.toString(), balanceAmount.toString(), "RMB");
        } catch (Exception e) {
            LOGGER.error("pushSseSZ2HK error:{}", e);
        }

    }


    /**
     * 香港到沪深
     */
    public void pushNBSH() {
        String result = HttpRequestUtil.get(SSE_NBSH_URL);
        System.out.println(result);
        parseNBSHandNBSZData(result, "3");


    }

    private void parseNBSHandNBSZData(String result, String type) {
        String resultJson = result.substring(result.indexOf("=") + 2, result.length() - 2);
        HekxRelStat hekxRelStat = JSON.parseObject(resultJson, HekxRelStat.class);
        String tableHead = hekxRelStat.getTablehead().get(0);
        List<List<String>> item = hekxRelStat.getSection().get(0).getItem();
        List<String> subtitle = hekxRelStat.getSection().get(0).getSubtitle();
        //获取当前拉去的时间
        String tradeDate = subtitle.get(1);
        String total = item.get(0).get(1);
        String balance = item.get(1).get(1);
        String time = item.get(1).get(0);
        String percentStr = item.get(2).get(1);
        String totalAmount = total.substring(3);
        String balanceAmount = balance.substring(3);
        String percent = percentStr.replace("%", "");
        time = time.substring(5, time.length() - 1).trim();
        Date tradeDateTime = DateUtil.parseDate(tradeDate + " " + time, "dd/MM/yyyy HH:mm");

        String currency = "RMB";
        Long dateTime = tradeDateTime.getTime() / 1000;
        pushRelData(type, percent, dateTime.toString(), totalAmount, balanceAmount, currency);
    }

    /**
     * 香港到上证
     */
    public void pushNBSZ() {
        String result = HttpRequestUtil.get(SSE_NBSZ_URL);
        parseNBSHandNBSZData(result, "4");
    }

    private void pushRelData(String type, String percent, String dateTime, String totalAmount, String balanceAmount, String currency) {
        Map<String, Object> params = new HashMap<>();
        params.put("TradeDay", dateTime);
        params.put("Type", type);
        params.put("CapitalTotalAmount", totalAmount);
        params.put("RemainingAmount", balanceAmount);
        params.put("Percent", percent);
        params.put("Currency", currency);
        threadPoolTaskExecutor.execute(new Runnable() {
            @Override
            public void run() {
                String paramJson = JSON.toJSONString(params);
                LOGGER.info("pushJson:{}", paramJson);
                String result = HttpRequestUtil.postWithJson(stockCapitalDayUrl, paramJson);
                LOGGER.info("pushResult:{}", result);
            }
        });
    }


    /**
     * 沪股通
     *
     * @return
     */
    private void hkexHuHkData() {
        try {
            List<List<String[]>> tableList = HttpRequestUtil.getExceFileContent(HKEX_HU_HK_URL, "1.xls", "UTF-16");
            for (List<String[]> table : tableList) {
                for (int i = 5; i < table.size(); i++) {
                    String[] row = table.get(i);
                    LOGGER.info(JSON.toJSONString(row));
                    Map<String, String> params = new HashMap<>();
                    params.put("InstCode", row[1]);
                    params.put("InstName", row[3]);
                    params.put("Type", "3");
                    HttpRequestUtil.postWithJson(stockConnectUrl, JSON.toJSONString(params));
                }
            }
//            LOGGER.info("result:{}", result);
        } catch (Exception e) {
            LOGGER.error("沪股通 推送异常：{}", e);
        }
    }


    /**
     * 港股通（沪）
     *
     * @return
     */
    private void sseHkHu() {
        try {
            WebClient webClient = HtmlRequestUtil.getWebClient(SSE_HK_HU_URL);
//            webClient.
            HtmlPage page = webClient.getPage(SSE_HK_HU_URL);
            webClient.waitForBackgroundJavaScript(2 * 1000);
            Document doc = Jsoup.parse(page.asXml());
            Elements tableEles = doc.getElementsByClass("table search_gpmd searchT");
            Element tableEle = tableEles.get(0);
            Elements trEles = tableEle.getElementsByTag("tr");
            for (int i = 0; i < trEles.size(); i++) {
                Element tr = trEles.get(i);
                Elements tdEles = tr.getElementsByTag("td");
                if (tdEles.isEmpty()) {
                    continue;
                }
                LOGGER.info(tdEles.text());
                Map<String, String> params = new HashMap<>();
                params.put("InstCode", tdEles.get(0).text());
                params.put("InstName", tdEles.get(2).text());
                params.put("Type", "1");
                HttpRequestUtil.postWithJson(stockConnectUrl, JSON.toJSONString(params));
            }
//            LOGGER.info("result:{}", doc.html());
        } catch (Exception e) {
             LOGGER.error("港股通（沪）error:{}", e);
        }


    }


    /**
     * 深股通
     *
     * @return
     */
    private void hkexSzHk() {
        try {
            List<List<String[]>> tableList = HttpRequestUtil.getExceFileContent(HKEX_SZ_HK_URL, "1.xls", "UTF-16");
            for (List<String[]> table : tableList) {
                for (int i = 5; i < table.size(); i++) {
                    String[] row = table.get(i);
                    Map<String, String> params = new HashMap<>();
                    String instCode = getStockCode(row[1], 6);
                    LOGGER.info("code:{}", instCode);
                    params.put("InstCode", instCode);
                    params.put("InstName", row[3]);
                    params.put("Type", "4");
                    HttpRequestUtil.postWithJson(stockConnectUrl, JSON.toJSONString(params));
                }
            }
        } catch (Exception e) {
            LOGGER.error("深股通 推送异常：{}", e);
        }

    }

    private String getStockCode(String code, int len) {
        StringBuilder sb = new StringBuilder();
        for (int j = 0; j < (6 - code.length()); j++) {
            sb.append("0");
        }
        sb.append(code);
        return sb.toString();
    }


    /**
     * 港股通（深）
     *
     * @return
     */
    private void szseHkSz() {
        try {
            List<List<String[]>> tableList = HttpRequestUtil.getExceFileContent(SZSE_HK_SZ_URL, "1.xls", "UTF-16");
            for (List<String[]> table : tableList) {
                for (int i = 1; i < table.size(); i ++ ) {
                    String[] row = table.get(i);
                    LOGGER.info(JSON.toJSONString(row));
                    Map<String, String> params = new HashMap<>();
                    params.put("InstCode", row[0]);
                    params.put("InstName", row[1]);
                    params.put("Type", "2");
                    HttpRequestUtil.postWithJson(stockConnectUrl, JSON.toJSONString(params));
                }
            }
        } catch (Exception e) {
            LOGGER.error("港股通（深） 推送异常：{}", e);
        }

    }


    public void pushHK_SH_SZ() {
        threadPoolTaskExecutor.execute(new Runnable() {
            @Override
            public void run() {
                hkexHuHkData();
            }
        });
        threadPoolTaskExecutor.execute(new Runnable() {
            @Override
            public void run() {
                sseHkHu();
            }
        });
        threadPoolTaskExecutor.execute(new Runnable() {
            @Override
            public void run() {
                hkexSzHk();
            }
        });
        threadPoolTaskExecutor.execute(new Runnable() {
            @Override
            public void run() {
                szseHkSz();
            }
        });
}


}
