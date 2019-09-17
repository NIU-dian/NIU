package com.yskj.crop.constant;

/**
 * @Author kai.tang@yintech.cn
 * @Date 2019/5/14 15:20
 * @Version 1.0.0
 */
public interface TradeConstant {


    /**
     * 日K
     */
    Integer DAILY = 1;

    /**
     * 分钟K
     */
    Integer MINUTE = 2;


    /**
     * 雪球股票K线请求地址
     */
    String XUE_QIU_K_LINE_URL = "https://stock.xueqiu.com/v5/stock/chart/kline.json";


    String XUE_QIU_HQ_HTML_URL = "https://xueqiu.com/hq";

    /**
     * 雪球股票分时信息请求地址
     */
    String XUE_QIU_MINUTE_URL = "https://stock.xueqiu.com/v5/stock/chart/minute.json";


    /**
     * 获取昨收信息
     */
    String XUE_QIU_ZUO_SHOU_URL = "https://stock.xueqiu.com/v5/stock/quote.json";

    String XUE_QIU_NEW_STOCK_INFO_URL = "https://xueqiu.com/service/v5/stock/preipo/us/list";

    String XUE_QIU_SZSH_NEW_STOCK_INFO_URL = "https://xueqiu.com/service/v5/stock/preipo/cn/query";

    String XUE_QIU_HK_NEW_STOCK_INFO_URL = "https://xueqiu.com/service/v5/stock/preipo/hk/query";


    /**
     * 股票详情
     */
    String XUE_QIU_NEW_STOCK_DETAIL_URL = "https://stock.xueqiu.com/v5/stock/quote.json";

    /**
     * 同步到行情服务
     */
    String MARKET_API_URL = "http://test-quotes.fdzq.com:8644/insert";

    /**
     * tushare域名接口
     */
    String TUSHARE_API_URL = "http://api.tushare.pro";

    /**
     * tushare请求token
     */
    String TUSHARE_TOKEN = "fbcf6bc3c06c5213581512d7a3c7494248cf503a239e5f97dc413d3a";

    /**
     * 新浪k线接口
     */
    String SINA_GC_URL = "https://stock2.finance.sina.com.cn/futures/api/jsonp.php";
}
