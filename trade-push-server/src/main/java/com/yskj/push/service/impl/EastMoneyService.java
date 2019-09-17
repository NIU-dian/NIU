package com.yskj.push.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.yskj.crop.constant.EastConstant;
import com.yskj.crop.domain.xueqiu.QuoteInfoResult;
import com.yskj.crop.domain.xueqiu.XueQiuResult;
import com.yskj.push.framework.util.DateUtil;
import com.yskj.push.framework.util.DoubleUtil;
import com.yskj.push.framework.util.HtmlRequestUtil;
import com.yskj.push.framework.util.HttpRequestUtil;
import org.apache.commons.lang3.StringUtils;
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

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class EastMoneyService {

    private static final String EAST_MONEY_STOCK_URL = "http://data.eastmoney.com/xg/xg/default.html";

    /**
     * 港股通（沪）
     */
    private static final String EAST_MONEY_HK2SH_URL = "http://quote.eastmoney.com/center/gridlist.html#hk_sh_stocks";

    /**
     * 港股通（深）
     */
    private static final String EAST_MONEY_HK2SZ_URL = "http://quote.eastmoney.com/center/gridlist.html#hk_sz_stocks";


    /**
     * AH股信息
     */
    private static final String EAST_MONEY_AH_STOCK_URL = "http://quote.eastmoney.com/center/gridlist.html#ah_comparison";


    /**
     * 沪港通
     */
    private static final String EAST_MONEY_SH2HK_URL = "http://quote.eastmoney.com/center/gridlist.html#sh_hk_board";


    /**
     * 深港通
     */
    private static final String EAST_MONEY_SZ2HK_URL = "http://quote.eastmoney.com/center/gridlist.html#sz_hk_board";


    private static final String EAST_MONEY_YESTERDAY_CLOSED = "http://push2.eastmoney.com/api/qt/stock/get?secid=105.%s&fields=f43,f169,f170,f46,f60,f84,f116,f44,f45,f171,f126,f47,f48,f168,f164,f49,f161,f55,f92,f59,f152,f167,f50,f86,f71,f172&type=CT&cmd=%s7&sty=FDPBPFB&st=z";


    private static final Logger LOGGER = LoggerFactory.getLogger(EastMoneyService.class);

    @Value("${new.stock.url}")
    private String newStockUrl;

    @Value("${stock.ah.rel.url}")
    private String stockAhRelUrl = "http://test-quotes.fdzq.com:8071/stockAhRel";

    @Value("${stock.connect.url}")
    private String stockConnectUrl = "http://test-quotes.fdzq.com:8071/stockConnect";

    @Value("${inst.url}")
    private String instUrl = "http://test-quotes.fdzq.com:8071/instList";

    @Value("${eod.url}")
    private String eodUrl = "http://test-quotes.fdzq.com:8071/eod";

    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    public static void main(String[] args) {
        EastMoneyService eastMoneyService = new EastMoneyService();
//        eastMoneyService.hk2shStock();
//        eastMoneyService.hk2szStock();
//        eastMoneyService.sh2hkStock();
//        eastMoneyService.sz2hkStock();
        eastMoneyService.ahStock();
//        eastMoneySpider();
//        eastMoneyService.eastMoneyYesterdayClosed();
    }

    /**
     * 推送东方财富的数据信息
     */
    public void pushEastMoneySpider() {
        try {
            WebClient webClient = HtmlRequestUtil.getWebClient(EAST_MONEY_STOCK_URL);
            HtmlPage page = webClient.getPage(EAST_MONEY_STOCK_URL);
            Document doc = Jsoup.parse(page.asXml());
            Elements tableEles = doc.getElementsByClass("tab1");
            Element tableEle = tableEles.get(1);
            Elements trEles = tableEle.getElementsByTag("tr");
            for (int i = 0; i < trEles.size(); i++) {
                Element tr = trEles.get(i);
                Elements tdEles = tr.getElementsByTag("td");
                if (tdEles.isEmpty()) {
                    continue;
                }

                String symbol = tdEles.get(0).text();
                String name = tdEles.get(1).text();
//                    String shares = tdEles.get().text();
                String issprice_max = tdEles.get(8).text();
                String issprice_min = tdEles.get(8).text();
                String year = DateUtil.getFormatDate(new Date(), "yyyy");
                String month = tdEles.get(14).text();
                String list_date = "";
                if (StringUtils.isNotEmpty(month)) {
                    Long ipoDate = DateUtil.parseDate(year + "-" + tdEles.get(14).text(), "yyyy-MM-dd").getTime();
                    list_date = ipoDate.toString();

                }

                String exchange = symbol.startsWith("6") ? "SH" : "SZ";
                Map<String, Object> param = new HashMap<>();
                param.put("symbol", symbol);
                param.put("name", name);
//                param.put("shares", shares);
                param.put("issprice_max", issprice_max);
                param.put("issprice_min", issprice_min);
                param.put("list_date", list_date);
                param.put("exchange", exchange);
                HttpRequestUtil.postWithJson(newStockUrl, com.alibaba.fastjson.JSON.toJSONString(param));
            }
            HtmlRequestUtil.closeWebClient(webClient);
//            System.out.println(doc.html());
        } catch (Exception e) {
            LOGGER.error("同步数据异常：{}", e);
        }
    }


    /**
     * 港股通（沪）
     */
    public void hk2shStock() {
        threadPoolTaskExecutor.execute(new Runnable() {
            @Override
            public void run() {
                eastMoneyhkStock(EAST_MONEY_HK2SH_URL, EastConstant.HK2SH, EastConstant.HK2SH);
            }
        });
    }

    /**
     * 港股通（深）
     */
    public void hk2szStock() {
        threadPoolTaskExecutor.execute(new Runnable() {
            @Override
            public void run() {
                eastMoneyhkStock(EAST_MONEY_HK2SZ_URL, EastConstant.HKSZ, EastConstant.HKSZ);
            }
        });
    }

    /**
     * AH股票信息
     */
    public void ahStock() {
        threadPoolTaskExecutor.execute(new Runnable() {
            @Override
            public void run() {
                eastMoneyhkStock(EAST_MONEY_AH_STOCK_URL, EastConstant.AH_STOCK, EastConstant.AH_STOCK);
            }
        });

    }

    /**
     * 沪港通
     */
    public void sh2hkStock() {
        threadPoolTaskExecutor.execute(new Runnable() {
            @Override
            public void run() {
                eastMoneyhkStock(EAST_MONEY_SH2HK_URL, EastConstant.SH2HK, EastConstant.SH2HK);
            }
        });
    }

    /**
     * 沪港通
     */
    public void sz2hkStock() {
        threadPoolTaskExecutor.execute(new Runnable() {
            @Override
            public void run() {
                eastMoneyhkStock(EAST_MONEY_SZ2HK_URL, EastConstant.SZ2HK, EastConstant.SZ2HK);
            }
        });
    }


    private void eastMoneyhkStock(String url, String market, String type) {
        try {
            WebClient webClient = HtmlRequestUtil.getWebClient(url);
            HtmlPage page = webClient.getPage(url);
            Document doc = Jsoup.parse(page.asXml());
            Elements pageEles = doc.getElementsByClass("paginate_button");
            String lastIndex = pageEles.get(pageEles.size() - 2).text();
            String currentIndex = "1";
            while (true) {
                //获取数据表格：table_wrapper-table
                Elements tableEles = doc.getElementsByClass("table_wrapper-table");
                Element tableEle = tableEles.last();
                Elements trEles = tableEle.getElementsByTag("tr");
                for (int i = 0; i < trEles.size(); i++) {
                    Element tr = trEles.get(i);
                    Elements tdEles = tr.getElementsByTag("td");
                    if (tdEles.isEmpty()) {
                        continue;
                    }
                    syncTradeData(tdEles, market, type);
                }
                //判断是否是最后一页
                if (currentIndex.equals(lastIndex)) {
                    break;
                }
                //获取当前页
                List<HtmlAnchor> htmlListItem = null;
                htmlListItem = page.getByXPath("//a[@class='next paginate_button']");
                if (htmlListItem.size() == 0) {
                    htmlListItem = page.getByXPath("//a[@class='next paginate_button disabled']");
                }
                HtmlAnchor nextPageAnchor = htmlListItem.get(0);
                HtmlPage nextPage = nextPageAnchor.click();
                doc = Jsoup.parse(nextPage.asXml());
                Elements currentPageEles = doc.getElementsByClass("paginate_button current");
                currentIndex = currentPageEles.last().text();
            }
            HtmlRequestUtil.closeWebClient(webClient);
        } catch (Exception e) {
            LOGGER.error("港股通（沪）同步数据异常：{}", e);
        }
    }

    private void syncTradeData(Elements tdEles, String market, String type) {
//        for (int i = 0; i < tdEles.size(); i++) {
//            System.out.print(tdEles.get(i).text());
//            System.out.print("|");
//        }
//        System.out.println();
        if (EastConstant.AH_STOCK.equals(type)) {
            syncAhStock(tdEles);
        } else {
            syncHKStock(tdEles, market);
        }
    }

    /**
     * 同步港股通  沪股通信息
     *
     * @param tdEles
     * @param market
     */
    private void syncHKStock(Elements tdEles, String market) {
        Map<String, Object> params = new HashMap<>();
        params.put("InstCode", tdEles.get(1).text());
        params.put("InstName", tdEles.get(2).text());
        params.put("Type", market);
        LOGGER.info(JSON.toJSONString(params));
        String ahResult = HttpRequestUtil.postWithJson(stockConnectUrl, JSON.toJSONString(params));
        LOGGER.info(ahResult);
    }

    /**
     * 同步AH股信息
     *
     * @param tdEles
     */
    private void syncAhStock(Elements tdEles) {
        Map<String, Object> params = new HashMap<>();
        params.put("AInstCode", tdEles.get(6).text());
        params.put("AInstName", tdEles.get(1).text());
        params.put("HInstCode", tdEles.get(2).text());
        LOGGER.info(JSON.toJSONString(params));
        String ahResult = HttpRequestUtil.postWithJson(stockAhRelUrl, JSON.toJSONString(params));
        LOGGER.info(ahResult);
    }


    /**
     * 东方财富昨收数据信息
     */
    public void eastMoneyYesterdayClosed() {
        String instResult = HttpRequestUtil.get(instUrl);
        JSONObject instJsonObject = JSON.parseObject(instResult);
        String code = instJsonObject.getString("code");
        if (!"0".equalsIgnoreCase(code)) {
            return;
        }
        String instStr = instJsonObject.getString("data");
        List<String> instList = com.alibaba.fastjson.JSON.parseArray(instStr, String.class);
        for (String inst : instList) {
            String yesterdayUrl = String.format(EAST_MONEY_YESTERDAY_CLOSED, inst, inst);

            String result = HttpRequestUtil.get(yesterdayUrl);
            sync2TransZuoShou(inst, result);
        }
    }


    private void sync2TransZuoShou(String symbol, String result) {
        JSONObject eastJsonObj = JSON.parseObject(result);
        int rc = eastJsonObj.getInteger("rc");
        if (rc != 0) {
            return;
        }
        String highStr = eastJsonObj.getJSONObject("data").getString("f44");
        BigDecimal high = DoubleUtil.divide(highStr, "100");
        String lastCloseStr = eastJsonObj.getJSONObject("data").getString("f60");
        BigDecimal lastClose = DoubleUtil.divide(lastCloseStr, "100");
        String currentStr = eastJsonObj.getJSONObject("data").getString("f43");
        BigDecimal current = DoubleUtil.divide(currentStr, "100");
        String openStr = eastJsonObj.getJSONObject("data").getString("f46");
        BigDecimal open = DoubleUtil.divide(openStr, "100");
        String lowStr = eastJsonObj.getJSONObject("data").getString("f45");
        BigDecimal low = DoubleUtil.divide(lowStr, "100");
        String time = eastJsonObj.getJSONObject("data").getString("f86") + "000";
        String amount = eastJsonObj.getJSONObject("data").getString("f48");
        String volume = eastJsonObj.getJSONObject("data").getString("f47");
        String high52w = "0";
        String low52w = "0";
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("symbol", symbol);
        resultMap.put("high", high == null ? "" : high.toString());
        resultMap.put("last_close", lastClose.toString());
        resultMap.put("current", current.toString());
        resultMap.put("open", open.toString());
        resultMap.put("low", low.toString() );
        resultMap.put("time", time);
        resultMap.put("amount", amount);
        resultMap.put("volume", volume);
        resultMap.put("high52w", high52w);
        resultMap.put("low52w", low52w);
        resultMap.put("type", "1");
        threadPoolTaskExecutor.execute(new Runnable() {
            @Override
            public void run() {
                HttpRequestUtil.postWithJson(eodUrl, com.alibaba.fastjson.JSON.toJSONString(resultMap));
            }
        });
    }


}
