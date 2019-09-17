package com.yskj.push.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.yskj.crop.constant.TushareConstant;
import com.yskj.crop.domain.tushare.TushareApiDomain;
import com.yskj.crop.domain.tushare.TushareRequestDomain;
import com.yskj.crop.response.tushare.TushareResponse;
import com.yskj.crop.response.tushare.TushareResult;
import com.yskj.push.dao.*;
import com.yskj.push.domain.*;
import com.yskj.push.framework.util.DateUtil;
import com.yskj.push.framework.util.HttpRequestUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

@Service
public class TushareService {

    private static final String TUSHARE_URL = "https://api.tushare.pro";

//    @Autowired
    private BalanceSheetMapper balanceSheetMapper;

//    @Autowired
    private CashFlowMapper cashFlowMapper;

//    @Autowired
    private DisclosureDateMapper disclosureDateMapper;

//    @Autowired
    private DividendMapper dividendMapper;

//    @Autowired
    private ExpressMapper expressMapper;

//    @Autowired
    private FinaAuditMapper finaAuditMapper;

//    @Autowired
    private FinaIndicatorMapper finaIndicatorMapper;

//    @Autowired
    private FinaMainbzMapper finaMainbzMapper;

//    @Autowired
    private ForecastMapper forecastMapper;

//    @Autowired
    private IncomeMapper incomeMapper;

//    @Autowired
    private NewsMapper newsMapper;

//    @Autowired
    private Top10HoldersMapper top10HoldersMapper;

//    @Autowired
    private Top10FloatHoldersMapper top10FloatHoldersMapper;

    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Value("${stock.ah.rel.url}")
    private String stockAhRelUrl = "http://test-quotes.fdzq.com:8071/stockAhRel";

    @Value("${tushare.api.token}")
    private String tushareToken = "1b0921dbc560255d855e11451fc0806be532eeeeef07359beb3d8529";

    @Value("${org.code}")
    private String orgCode = "tc";

    @Value("${trade.token}")
    private String tradeToken = "f41659f7-816c-4caf-b3da-8ae5cd39d7cd";

    private static final Logger LOGGER = LoggerFactory.getLogger(TushareService.class);

    public static void main(String[] args) {
        TushareService tushareService = new TushareService();
        TushareRequestDomain tushareRequestDomain = new TushareRequestDomain();
        tushareRequestDomain.setTs_code("600000.SH");
//        tushareService.getBalanceSheet();
//        tushareService.getCashFlow();
//        tushareService.getDisclosureDate();
//        tushareService.getDividend();
//        tushareService.getExpress();
//        tushareService.getFinaAudit();
//        tushareService.getFinaIndicator();
//        tushareService.getFinaMainbz();
//        tushareService.getForecast();
        tushareService.getIcome(tushareRequestDomain);
//        tushareService.getNews();
//        tushareService.getTop10Floatholders();
//        tushareService.getTop10Holders();
//        tushareService.getStockInformation();
//        System.out.println(tushareService.getStockList("sh"));
//        tushareService.getHSConst();
    }

    /**
     * 净利润
     */
    public void getIcome(TushareRequestDomain tushareRequestDomain) {

        String result = post(tushareToken, TushareConstant.INCOME, tushareRequestDomain);
        LOGGER.info("income info:{}", result);
        TushareResult<TushareResponse> tushareResult = JSON.parseObject(result, new TypeReference<TushareResult<TushareResponse>>() {
        });
        TushareResponse tushareResponse = tushareResult.getData();
        //是否有数据返回
        if (tushareResponse == null) {
            LOGGER.warn("没有查询到股票代码:[{}] 信息", tushareRequestDomain.getTs_code());
            return;
        }
        List<List<String>> table = tushareResponse.getItems();
        List<String> fieldList = tushareResponse.getFields();
        try {
            for (List<String> rows : table) {
                Object clazzObj = setObjectProperties(fieldList, rows, Income.class);
                Income income = new Income();
                BeanUtils.copyProperties(clazzObj, income);
                income.setCreateTime(new Date());
                incomeMapper.insert(income);
            }
        } catch (Exception e) {
            LOGGER.error("设置字段信息异常:{}", e);
        }

    }


    private Object setObjectProperties(List<String> fieldList, List<String> rows, Class clazz) throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        Object clazzObj = clazz.newInstance();
        for (int i = 0; i < fieldList.size(); i++) {
            String field = fieldList.get(i);
            String value = rows.get(i);
            String[] fieldSplitArr = field.split("_");
            StringBuilder sb = new StringBuilder();
            for (int j = 0; j < fieldSplitArr.length; j++) {
                String fieldSplit = fieldSplitArr[j];
                String temp = null;
                if (fieldSplit.length() == 1 && j == 0) {
                    temp = fieldSplit.substring(0, 1).toLowerCase();
                } else {
                    temp = fieldSplit.substring(0, 1).toUpperCase();
                }
                sb.append(temp).append(fieldSplit.substring(1));
            }
            Method method = clazz.getMethod("set" + sb.toString(), String.class);
            method.invoke(clazzObj, value);
        }
        return clazzObj;
    }


    /**
     * 资产负债
     */
    public void getBalanceSheet(TushareRequestDomain tushareRequestDomain) {

        String result = post(tushareToken, TushareConstant.BALANCE_SHEET, tushareRequestDomain);
        LOGGER.info("balanceSheet info:{}", result);
        TushareResult<TushareResponse> tushareResult = JSON.parseObject(result, new TypeReference<TushareResult<TushareResponse>>() {
        });
        TushareResponse tushareResponse = tushareResult.getData();
        //是否有数据返回
        if (tushareResponse == null) {
            LOGGER.warn("没有查询到股票代码:[{}] 信息", tushareRequestDomain.getTs_code());
            return;
        }
        List<List<String>> table = tushareResponse.getItems();
        List<String> fieldList = tushareResponse.getFields();
        try {
            for (List<String> rows : table) {
                    Object clazzObj = setObjectProperties(fieldList, rows, BalanceSheet.class);
                    BalanceSheet balanceSheet = new BalanceSheet();
                    BeanUtils.copyProperties(clazzObj, balanceSheet);
                    balanceSheet.setCreateTime(new Date());
                    balanceSheet.setUpdateTime(new Date());
                    balanceSheetMapper.insert(balanceSheet);
            }
        } catch (Exception e) {
            LOGGER.error("设置字段信息异常:{}", e);
        }

    }

    /**
     * 现金流量
     */
    public void getCashFlow(TushareRequestDomain tushareRequestDomain) {

        String result = post(tushareToken, TushareConstant.CASH_FLOW, tushareRequestDomain);
        LOGGER.info("cashFlow info:{}", result);
        TushareResult<TushareResponse> tushareResult = JSON.parseObject(result, new TypeReference<TushareResult<TushareResponse>>() {
        });
        TushareResponse tushareResponse = tushareResult.getData();
        //是否有数据返回
        if (tushareResponse == null) {
            LOGGER.warn("没有查询到股票代码:[{}] 信息", tushareRequestDomain.getTs_code());
            return;
        }
        List<List<String>> table = tushareResponse.getItems();
        List<String> fieldList = tushareResponse.getFields();
        try {
            for (List<String> rows : table) {
                    Object clazzObj = setObjectProperties(fieldList, rows, CashFlow.class);
                    CashFlow cashFlow = new CashFlow();
                    BeanUtils.copyProperties(clazzObj, cashFlow);
                    cashFlow.setCreateTime(new Date());
                    cashFlow.setUpdateTime(new Date());
                    cashFlowMapper.insert(cashFlow);
            }
        } catch (Exception e) {
            LOGGER.error("设置字段信息异常:{}", e);
        }
    }

    /**
     * 财报披露计划
     */
    public void getDisclosureDate(String tsCode, String endDate, String preDate, String actualDate) {
        TushareRequestDomain tushareRequestDomain = new TushareRequestDomain();
        tushareRequestDomain.setTs_code(tsCode);
        tushareRequestDomain.setEnd_date(endDate);
        tushareRequestDomain.setPre_date(preDate);
        tushareRequestDomain.setActual_date(actualDate);

        String result = post(tushareToken, TushareConstant.DISCLOSURE_DATE, tushareRequestDomain);
        LOGGER.info("disclosureDate info:{}", result);
        TushareResult<TushareResponse> tushareResult = JSON.parseObject(result, new TypeReference<TushareResult<TushareResponse>>() {
        });
        TushareResponse tushareResponse = tushareResult.getData();
        //是否有数据返回
        if (tushareResponse == null) {
            LOGGER.warn("没有查询到股票代码:[{}] 信息", tsCode);
            return;
        }
        List<List<String>> table = tushareResponse.getItems();
        List<String> fieldList = tushareResponse.getFields();
        try {
            for (List<String> rows : table) {
                    Object clazzObj = setObjectProperties(fieldList, rows, DisclosureDate.class);
                    DisclosureDate disclosureDate = new DisclosureDate();
                    BeanUtils.copyProperties(clazzObj, disclosureDate);
                    disclosureDate.setCreateTime(new Date());
                    disclosureDate.setUpdateTime(new Date());
                    disclosureDateMapper.insert(disclosureDate);
            }
        } catch (Exception e) {
            LOGGER.error("设置字段信息异常:{}", e);
        }

    }

    /**
     * 分红送股
     */
    public void getDividend(String tsCode, String annDate, String recordDate, String exDate, String impAnnDate) {
        TushareRequestDomain tushareRequestDomain = new TushareRequestDomain();
        tushareRequestDomain.setTs_code(tsCode);
        tushareRequestDomain.setAnn_date(annDate);
        tushareRequestDomain.setRecord_date(recordDate);
        tushareRequestDomain.setEx_date(exDate);
        tushareRequestDomain.setImp_ann_date(impAnnDate);

        String result = post(tushareToken, TushareConstant.DIVIDEND, tushareRequestDomain);
        LOGGER.info("dividend info:{}", result);
        TushareResult<TushareResponse> tushareResult = JSON.parseObject(result, new TypeReference<TushareResult<TushareResponse>>() {
        });
        TushareResponse tushareResponse = tushareResult.getData();
        //是否有数据返回
        if (tushareResponse == null) {
            LOGGER.warn("没有查询到股票代码:[{}] 信息", tsCode);
            return;
        }
        List<List<String>> table = tushareResponse.getItems();
        List<String> fieldList = tushareResponse.getFields();
        try {
            for (List<String> rows : table) {
                    Object clazzObj = setObjectProperties(fieldList, rows, Dividend.class);
                    Dividend dividend = new Dividend();
                    BeanUtils.copyProperties(clazzObj, dividend);
                    dividend.setCreateTime(new Date());
                    dividend.setUpdateTime(new Date());
                    dividendMapper.insert(dividend);
            }
        } catch (Exception e) {
            LOGGER.error("设置字段信息异常:{}", e);
        }
    }

    /**
     * 业绩快报
     */
    public void getExpress(String tsCode, String annDate, String startDate, String endDate, String period) {
        TushareRequestDomain tushareRequestDomain = new TushareRequestDomain();
        tushareRequestDomain.setTs_code(tsCode);
        tushareRequestDomain.setAnn_date(annDate);
        tushareRequestDomain.setStart_date(startDate);
        tushareRequestDomain.setEnd_date(endDate);
        tushareRequestDomain.setPeriod(period);

        String result = post(tushareToken, TushareConstant.EXPRESS, tushareRequestDomain);
        LOGGER.info("express info:{}", result);
        TushareResult<TushareResponse> tushareResult = JSON.parseObject(result, new TypeReference<TushareResult<TushareResponse>>() {
        });
        TushareResponse tushareResponse = tushareResult.getData();
        //是否有数据返回
        if (tushareResponse == null) {
            LOGGER.warn("没有查询到股票代码:[{}] 信息", tsCode);
            return;
        }
        List<List<String>> table = tushareResponse.getItems();
        List<String> fieldList = tushareResponse.getFields();
        try {
            for (List<String> rows : table) {
                    Object clazzObj = setObjectProperties(fieldList, rows, Express.class);
                    Express express = new Express();
                    BeanUtils.copyProperties(clazzObj, express);
                    express.setCreateTime(new Date());
                    express.setUpdateTime(new Date());
                    expressMapper.insert(express);
            }
        } catch (Exception e) {
            LOGGER.error("设置字段信息异常:{}", e);
        }

    }

    /**
     * 财务审计意见
     */
    public void getFinaAudit(String tsCode, String annDate, String startDate, String endDate, String period) {
        TushareRequestDomain tushareRequestDomain = new TushareRequestDomain();
        tushareRequestDomain.setTs_code(tsCode);
        tushareRequestDomain.setAnn_date(annDate);
        tushareRequestDomain.setStart_date(startDate);
        tushareRequestDomain.setEnd_date(endDate);
        tushareRequestDomain.setPeriod(period);

        String result = post(tushareToken, TushareConstant.FINA_AUDIT, tushareRequestDomain);
        LOGGER.info("finaAudit info:{}", result);
        TushareResult<TushareResponse> tushareResult = JSON.parseObject(result, new TypeReference<TushareResult<TushareResponse>>() {
        });
        TushareResponse tushareResponse = tushareResult.getData();
        //是否有数据返回
        if (tushareResponse == null) {
            LOGGER.warn("没有查询到股票代码:[{}] 信息", tsCode);
            return;
        }
        List<List<String>> table = tushareResponse.getItems();
        List<String> fieldList = tushareResponse.getFields();
        try {
            for (List<String> rows : table) {
                    Object clazzObj = setObjectProperties(fieldList, rows, FinaAudit.class);
                    FinaAudit finaAudit = new FinaAudit();
                    BeanUtils.copyProperties(clazzObj, finaAudit);
                    finaAudit.setUpdateTime(new Date());
                    finaAudit.setCreateTime(new Date());
                    finaAuditMapper.insert(finaAudit);
            }
        } catch (Exception e) {
            LOGGER.error("设置字段信息异常:{}", e);
        }

    }

    /**
     * 财务指标数据
     */
    public void getFinaIndicator(String tsCode, String annDate, String startDate, String endDate, String period) {
        TushareRequestDomain tushareRequestDomain = new TushareRequestDomain();
        tushareRequestDomain.setTs_code(tsCode);
        tushareRequestDomain.setAnn_date(annDate);
        tushareRequestDomain.setStart_date(startDate);
        tushareRequestDomain.setEnd_date(endDate);
        tushareRequestDomain.setPeriod(period);

        String result = post(tushareToken, TushareConstant.FINA_INDICATOR, tushareRequestDomain);
        LOGGER.info("finaIndicator info:{}", result);
        TushareResult<TushareResponse> tushareResult = JSON.parseObject(result, new TypeReference<TushareResult<TushareResponse>>() {
        });
        TushareResponse tushareResponse = tushareResult.getData();
        //是否有数据返回
        if (tushareResponse == null) {
            LOGGER.warn("没有查询到股票代码:[{}] 信息", tsCode);
            return;
        }
        List<List<String>> table = tushareResponse.getItems();
        List<String> fieldList = tushareResponse.getFields();
        try {
            for (List<String> rows : table) {
                    Object clazzObj = setObjectProperties(fieldList, rows, FinaIndicator.class);
                    FinaIndicator finaIndicator = new FinaIndicator();
                    BeanUtils.copyProperties(clazzObj, finaIndicator);
                    finaIndicator.setCreateTime(new Date());
                    finaIndicator.setUpdateTime(new Date());
                    finaIndicatorMapper.insert(finaIndicator);
            }
        } catch (Exception e) {
            LOGGER.error("设置字段信息异常:{}", e);
        }
    }

    /**
     * 主营业务构成
     */
    public void getFinaMainbz(String tsCode, String type, String startDate, String endDate, String period) {
        TushareRequestDomain tushareRequestDomain = new TushareRequestDomain();
        tushareRequestDomain.setTs_code(tsCode);
        tushareRequestDomain.setType(type);
        tushareRequestDomain.setStart_date(startDate);
        tushareRequestDomain.setEnd_date(endDate);
        tushareRequestDomain.setPeriod(period);

        String result = post(tushareToken, TushareConstant.FINA_MAINBZ, tushareRequestDomain);
        LOGGER.info("finaMainbz info:{}", result);
        TushareResult<TushareResponse> tushareResult = JSON.parseObject(result, new TypeReference<TushareResult<TushareResponse>>() {
        });
        TushareResponse tushareResponse = tushareResult.getData();
        //是否有数据返回
        if (tushareResponse == null) {
            LOGGER.warn("没有查询到股票代码:[{}] 信息", tsCode);
            return;
        }
        List<List<String>> table = tushareResponse.getItems();
        List<String> fieldList = tushareResponse.getFields();
        try {
            for (List<String> rows : table) {
                    Object clazzObj = setObjectProperties(fieldList, rows, FinaMainbz.class);
                    FinaMainbz finaMainbz = new FinaMainbz();
                    BeanUtils.copyProperties(clazzObj, finaMainbz);
                    finaMainbz.setCreateTime(new Date());
                    finaMainbz.setUpdateTime(new Date());
                    finaMainbzMapper.insert(finaMainbz);
            }
        } catch (Exception e) {
            LOGGER.error("设置字段信息异常:{}", e);
        }

    }

    /**
     * 业绩预告
     */
    public void getForecast(String tsCode, String annDate, String startDate, String endDate, String period, String type) {
        TushareRequestDomain tushareRequestDomain = new TushareRequestDomain();
        tushareRequestDomain.setTs_code(tsCode);
        tushareRequestDomain.setAnn_date(annDate);
        tushareRequestDomain.setStart_date(startDate);
        tushareRequestDomain.setEnd_date(endDate);
        tushareRequestDomain.setPeriod(period);
        tushareRequestDomain.setType(type);
        String result = post(tushareToken, TushareConstant.FORECAST, tushareRequestDomain);
        LOGGER.info("forecast info:{}", result);
        TushareResult<TushareResponse> tushareResult = JSON.parseObject(result, new TypeReference<TushareResult<TushareResponse>>() {
        });
        TushareResponse tushareResponse = tushareResult.getData();
        //是否有数据返回
        if (tushareResponse == null) {
            LOGGER.warn("没有查询到股票代码:[{}] 信息", tsCode);
            return;
        }
        List<List<String>> table = tushareResponse.getItems();
        List<String> fieldList = tushareResponse.getFields();
        try {
            for (List<String> rows : table) {
                    Object clazzObj = setObjectProperties(fieldList, rows, Forecast.class);
                    Forecast forecast = new Forecast();
                    BeanUtils.copyProperties(clazzObj, forecast);
                    forecast.setUpdateTime(new Date());
                    forecast.setCreateTime(new Date());
                    forecastMapper.insert(forecast);
            }
        } catch (Exception e) {
            LOGGER.error("设置字段信息异常:{}", e);
        }

    }

    /**
     * 新闻资讯
     */
    public void getNews(String startDate, String endDate, String src) {

        TushareRequestDomain tushareRequestDomain = new TushareRequestDomain();
        tushareRequestDomain.setStart_date(startDate);
        tushareRequestDomain.setEnd_date(endDate);
        tushareRequestDomain.setSrc(src);

        String result = post(tushareToken, TushareConstant.NEWS, tushareRequestDomain);
        LOGGER.info("news info:{}", result);
        TushareResult<TushareResponse> tushareResult = JSON.parseObject(result, new TypeReference<TushareResult<TushareResponse>>() {
        });
        TushareResponse tushareResponse = tushareResult.getData();
        //是否有数据返回
        if (tushareResponse == null) {
            LOGGER.warn("没有查询到信息");
            return;
        }
        List<List<String>> table = tushareResponse.getItems();
        List<String> fieldList = tushareResponse.getFields();
        for (List<String> rows : table) {
            try {
                Object clazzObj = setObjectProperties(fieldList, rows, News.class);
                News news = new News();
                BeanUtils.copyProperties(clazzObj, news);
                news.setChannels(src);
                news.setCreateTime(new Date());
                news.setUpdateTime(new Date());
                newsMapper.insert(news);
            } catch (Exception e) {
                LOGGER.error("设置字段信息异常:{}", e);
            }
        }
    }

    /**
     * 前十大股东
     */
    public void getTop10Holders(String tsCode, String annDate, String startDate, String endDate, String period) {
        TushareRequestDomain tushareRequestDomain = new TushareRequestDomain();
        tushareRequestDomain.setTs_code(tsCode);
        tushareRequestDomain.setAnn_date(annDate);
        tushareRequestDomain.setStart_date(startDate);
        tushareRequestDomain.setEnd_date(endDate);
        tushareRequestDomain.setPeriod(period);

        String result = post(tushareToken, TushareConstant.TOP10_HOLDERS, tushareRequestDomain);
        LOGGER.info("top10Holders info:{}", result);
        TushareResult<TushareResponse> tushareResult = JSON.parseObject(result, new TypeReference<TushareResult<TushareResponse>>() {
        });
        TushareResponse tushareResponse = tushareResult.getData();
        //是否有数据返回
        if (tushareResponse == null) {
            LOGGER.warn("没有查询到股票代码:[{}] 信息", tsCode);
            return;
        }
        List<List<String>> table = tushareResponse.getItems();
        List<String> fieldList = tushareResponse.getFields();
        try {
            for (List<String> rows : table) {
                    Object clazzObj = setObjectProperties(fieldList, rows, Top10Holders.class);
                    Top10Holders top10Holders = new Top10Holders();
                    BeanUtils.copyProperties(clazzObj, top10Holders);
                    top10Holders.setCreateTime(new Date());
                    top10Holders.setUpdateTime(new Date());
                    top10HoldersMapper.insert(top10Holders);
            }
        } catch (Exception e) {
            LOGGER.error("设置字段信息异常:{}", e);
        }

    }

    /**
     * 前十大流通股东
     */
    public void getTop10Floatholders(String tsCode, String annDate, String startDate, String endDate, String period) {
        TushareRequestDomain tushareRequestDomain = new TushareRequestDomain();
        tushareRequestDomain.setTs_code(tsCode);
        tushareRequestDomain.setAnn_date(annDate);
        tushareRequestDomain.setStart_date(startDate);
        tushareRequestDomain.setEnd_date(endDate);
        tushareRequestDomain.setPeriod(period);

        String result = post(tushareToken, TushareConstant.TOP10_FLOATHOLDERS, tushareRequestDomain);
        LOGGER.info("top10FloatHolders info:{}", result);
        TushareResult<TushareResponse> tushareResult = JSON.parseObject(result, new TypeReference<TushareResult<TushareResponse>>() {
        });
        TushareResponse tushareResponse = tushareResult.getData();
        //是否有数据返回
        if (tushareResponse == null) {
            LOGGER.warn("没有查询到股票代码:[{}] 信息", tsCode);
            return;
        }
        List<List<String>> table = tushareResponse.getItems();
        List<String> fieldList = tushareResponse.getFields();
        try {
            for (List<String> rows : table) {
                    Object clazzObj = setObjectProperties(fieldList, rows, Top10FloatHolders.class);
                    Top10FloatHolders top10FloatHolders = new Top10FloatHolders();
                    BeanUtils.copyProperties(clazzObj, top10FloatHolders);
                    top10FloatHolders.setCreateTime(new Date());
                    top10FloatHolders.setUpdateTime(new Date());
                    top10FloatHoldersMapper.insert(top10FloatHolders);
            }
        } catch (Exception e) {
            LOGGER.error("设置字段信息异常:{}", e);
        }
    }

    /**
     * 沪深股通成份股
     */
    public void getHSConst() {
        TushareRequestDomain tushareRequestDomain = new TushareRequestDomain();
        tushareRequestDomain.setHs_type("SZ");
        tushareRequestDomain.setIs_new("1");
        String result = post(tushareToken, TushareConstant.HS_CONST, tushareRequestDomain);
        LOGGER.info("balanceSheet info:{}", result);

        TushareResult<TushareResponse> tushareResult = JSON.parseObject(result, new TypeReference<TushareResult<TushareResponse>>() {
        });
        TushareResponse tushareResponse = tushareResult.getData();
        if (tushareResponse == null) {
            return;
        }

        List<List<String>> table = tushareResponse.getItems();
        for (List<String> rows : table) {
            Map<String, Object> params = new HashMap<>();
            String code = rows.get(0);
            params.put("AInstCode", code.substring(0, code.indexOf(".")));
            params.put("AInstName", "");
            params.put("HInstCode", "");
            LOGGER.info(JSON.toJSONString(params));
            String ahResult = HttpRequestUtil.postWithJson(stockAhRelUrl, JSON.toJSONString(params));
            LOGGER.info(ahResult);
        }

    }

    /**
     * 获取股票的资讯信息
     */
    public void initStockInformation() {
        String[] marketArray = {"sh", "sz"};
        //获取新闻资讯
        threadPoolTaskExecutor.execute(new Runnable() {
            @Override
            public void run() {
                String startDate = DateUtil.getFormatDate(new Date(), "yyyy-MM-dd");
                startDate = startDate + " 00:00:00";
                String endDate = DateUtil.getFormatDate(new Date(), "yyyy-MM-dd");
                endDate = endDate + " 23:59:59";
                String[] srcArry = {"sina", "wallstreetcn", "10jqka", "eastmoney", "yuncaijing"};
                for (String src : srcArry) {
                    getNews(startDate, endDate, src);
                }
            }
        });

        for (String market : marketArray) {
            List<String> stockList = getStockList(market);
            for (String stock : stockList) {
                String tsCode = stock + "." + market.toUpperCase();
                threadPoolTaskExecutor.execute(new Runnable() {
                    @Override
                    public void run() {
                        TushareRequestDomain tushareRequestDomain = new TushareRequestDomain();
                        tushareRequestDomain.setTs_code(tsCode);
                        getBalanceSheet(tushareRequestDomain);
                        getCashFlow(tushareRequestDomain);
                        getDisclosureDate(tsCode, "", "", "");
                        getDividend(tsCode, "", "", "", "");
                        getExpress(tsCode, "", "", "", "");
                        getFinaAudit(tsCode, "", "", "", "");
                        getFinaIndicator(tsCode, "", "", "", "");
                        getFinaMainbz(tsCode, "", "", "", "");
                        getForecast(tsCode, "", "", "", "", "");
                        getIcome(tushareRequestDomain);
                        getTop10Floatholders(tsCode, "", "", "", "");
                        getTop10Holders(tsCode, "", "", "", "");

                    }
                });

            }

        }
    }


    public String post(String token, String apiName, String fields, Object params) {
        TushareApiDomain apiDomain = new TushareApiDomain();
        apiDomain.setApi_name(apiName);
        apiDomain.setToken(token);
        apiDomain.setFields(fields);
        apiDomain.setParams(params);
        return HttpRequestUtil.postWithJson(TUSHARE_URL, JSON.toJSONString(apiDomain));
    }

    public String post(String token, String apiName, Object params) {
        return post(token, apiName, "", params);
    }


    private String getStockInfo(String market) {
        Map<String, Object> params = new HashMap<>();
        params.put("OrgCode", orgCode);
        params.put("Token", tradeToken);
        params.put("AppName", "spider");
        params.put("AppVer", "V1.0");
        params.put("AppType", "inner");
        params.put("Tag", "889");
        String loginResult = HttpRequestUtil.postWithJson("https://qas.fdzq.com/api/v30/login", JSON.toJSONString(params));
        JSONObject loginObject = JSONObject.parseObject(loginResult);
        if (!"0000".equals(loginObject.get("Code"))) {
            LOGGER.warn("获取股票代码登陆失败");
            return null;
        }
        String token = loginObject.getJSONObject("QuoteData").getJSONArray("AuthData").getJSONObject(0).getString("Token");
        Map<String, Object> stockParam = new HashMap<>();
        stockParam.put("ReqID", System.currentTimeMillis());
        stockParam.put("Market", market);
        stockParam.put("Inst", "");
        stockParam.put("ServiceType", "INSTRUMENTS");
        Map<String, Object> headsParam = new HashMap<>();
        headsParam.put("token", token);
        String stockList = HttpRequestUtil.postWithJson("https://qas.fdzq.com/api/v30/busi", JSON.toJSONString(stockParam), headsParam);
        return stockList;
    }

    private List<String> getStockList(String market) {
        String stockInfoJson = getStockInfo(market);
        if (StringUtils.isEmpty(market)) {
            LOGGER.warn("没有找到股票信息");
            return Collections.EMPTY_LIST;
        }
        JSONObject stockInfoObject = JSON.parseObject(stockInfoJson);
        JSONObject quoteDataObject = stockInfoObject.getJSONObject("QuoteData");
        JSONArray stockArray = quoteDataObject.getJSONArray("InstrumentData");
        return stockArray.toJavaList(String.class);
    }

    /**
     * 每日数据同步
     *
     * @param startDate
     * @param endDate
     * @param annDate
     */
    public void dailyTushareData(String startDate, String endDate, String annDate) {



        String[] marketArray = {"sh", "sz"};
        for (String market : marketArray) {
            List<String> stockList = getStockList(market);
            for (String stock : stockList) {
                String tsCode = stock + "." + market.toUpperCase();
                threadPoolTaskExecutor.execute(new Runnable() {
                    @Override
                    public void run() {
                        TushareRequestDomain tushareRequestDomain = new TushareRequestDomain();
                        tushareRequestDomain.setStart_date(startDate);
                        tushareRequestDomain.setEnd_date(endDate);
                        tushareRequestDomain.setTs_code(tsCode);
                        getBalanceSheet(tushareRequestDomain);
                        getCashFlow(tushareRequestDomain);
                        getDisclosureDate(tsCode, endDate, "", "");
                        getDividend(tsCode, annDate, "", "", "");
                        getExpress(tsCode, annDate, startDate, endDate, "");
                        getFinaAudit(tsCode, annDate, startDate, endDate, "");
                        getFinaIndicator(tsCode, annDate, startDate, endDate, "");
                        getFinaMainbz(tsCode, "", startDate, endDate, "");
                        getForecast(tsCode, annDate, startDate, endDate, "", "");
                        getIcome(tushareRequestDomain);
                        getTop10Floatholders(tsCode, annDate, startDate, endDate, "");
                        getTop10Holders(tsCode, annDate, startDate, endDate, "");

                    }
                });

            }

        }
    }


    /**
     * 每天定时获取新闻资讯信息
     * @param startDate
     * @param endDate
     */
    public void dailyTushareNewsData(String startDate, String endDate){
        //获取新闻资讯
        String[] srcArry = {"sina", "wallstreetcn", "10jqka", "eastmoney", "yuncaijing"};
        for (String src : srcArry) {
            threadPoolTaskExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    News news = newsMapper.selectLastData(src);
                    String newsStartDate = startDate;
                    if (news != null) {
                        Long newsTime = news.getNewsDatetime().getTime() + 1 * 1000;
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTimeInMillis(newsTime);
                        newsStartDate = DateUtil.getFormatDate(calendar.getTime() , "yyyy-MM-dd HH:mm:ss");
                    }
                    getNews(newsStartDate, endDate, src);
                }
            });
        }
    }
}
