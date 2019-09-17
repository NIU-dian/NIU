/**
 * Copyright 2019 bejson.com
 */
package com.yskj.crop.domain.tushare;

/**
 * Auto-generated: 2019-05-09 17:2:52
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class Daily {

    /**
     * 股票代码（二选一）
     */
    private String ts_code;

    /**
     * 交易日期（二选一）
     */
    private String trade_date;

    /**
     * 开始日期(YYYYMMDD)
     */
    private String start_date;

    /**
     * 结束日期(YYYYMMDD)
     */
    private String end_date;

    public void setTs_code(String ts_code) {
        this.ts_code = ts_code;
    }

    public String getTs_code() {
        return ts_code;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public String getTrade_date() {
        return trade_date;
    }

    public void setTrade_date(String trade_date) {
        this.trade_date = trade_date;
    }
}