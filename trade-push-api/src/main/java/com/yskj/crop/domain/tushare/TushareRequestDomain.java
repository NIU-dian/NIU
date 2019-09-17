package com.yskj.crop.domain.tushare;

public class TushareRequestDomain {


    /**
     * 股票代码
     */
    private String ts_code;

    /**
     * 公告日期
     */
    private String ann_date;

    /**
     * 公告开始日期
     */
    private String start_date;

    /**
     * 公告结束日期
     */
    private String end_date;

    /**
     * 报告期(每个季度最后一天的日期，比如20171231表示年报)
     */
    private String period;

    /**
     * 报告类型：见下方详细说明
     */
    private String report_type;

    /**
     * 公司类型：1一般工商业 2银行 3保险 4证券
     */
    private String comp_type;


    /**
     * 预告类型(预增/预减/扭亏/首亏/续亏/续盈/略增/略减)
     */
    private String type;

    /**
     * 股权登记日期
     */
    private String record_date;

    /**
     * 除权除息日
     */
    private String ex_date;

    /**
     * 实施公告日
     */
    private String imp_ann_date;


    /**
     * 计划披露日期
     */
    private String pre_date;


    /**
     * 实际披露日期
     */
    private String actual_date;

    /**
     * 新闻来源
     */
    private String src;

    /**
     * 类型SH沪股通SZ深股通
     */
    private String hs_type;

    /**
     * 是否最新 1 是 0 否 (默认1)
     */
    private String is_new;

    public String getTs_code() {
        return ts_code;
    }

    public void setTs_code(String ts_code) {
        this.ts_code = ts_code;
    }

    public String getAnn_date() {
        return ann_date;
    }

    public void setAnn_date(String ann_date) {
        this.ann_date = ann_date;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getReport_type() {
        return report_type;
    }

    public void setReport_type(String report_type) {
        this.report_type = report_type;
    }

    public String getComp_type() {
        return comp_type;
    }

    public void setComp_type(String comp_type) {
        this.comp_type = comp_type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRecord_date() {
        return record_date;
    }

    public void setRecord_date(String record_date) {
        this.record_date = record_date;
    }

    public String getEx_date() {
        return ex_date;
    }

    public void setEx_date(String ex_date) {
        this.ex_date = ex_date;
    }

    public String getImp_ann_date() {
        return imp_ann_date;
    }

    public void setImp_ann_date(String imp_ann_date) {
        this.imp_ann_date = imp_ann_date;
    }

    public String getPre_date() {
        return pre_date;
    }

    public void setPre_date(String pre_date) {
        this.pre_date = pre_date;
    }

    public String getActual_date() {
        return actual_date;
    }

    public void setActual_date(String actual_date) {
        this.actual_date = actual_date;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public String getHs_type() {
        return hs_type;
    }

    public void setHs_type(String hs_type) {
        this.hs_type = hs_type;
    }

    public String getIs_new() {
        return is_new;
    }

    public void setIs_new(String is_new) {
        this.is_new = is_new;
    }

    public TushareRequestDomain() {
    }

    public TushareRequestDomain(String ts_code) {
        this.ts_code = ts_code;
    }

    public TushareRequestDomain(String ts_code, String ann_date, String start_date, String end_date, String period, String report_type, String comp_type) {
        this.ts_code = ts_code;
        this.ann_date = ann_date;
        this.start_date = start_date;
        this.end_date = end_date;
        this.period = period;
        this.report_type = report_type;
        this.comp_type = comp_type;
    }

    public TushareRequestDomain(String ts_code, String ann_date, String start_date, String end_date, String period, String report_type, String comp_type, String type, String record_date, String ex_date, String imp_ann_date, String pre_date, String actual_date, String src, String hs_type, String is_new) {
        this.ts_code = ts_code;
        this.ann_date = ann_date;
        this.start_date = start_date;
        this.end_date = end_date;
        this.period = period;
        this.report_type = report_type;
        this.comp_type = comp_type;
        this.type = type;
        this.record_date = record_date;
        this.ex_date = ex_date;
        this.imp_ann_date = imp_ann_date;
        this.pre_date = pre_date;
        this.actual_date = actual_date;
        this.src = src;
        this.hs_type = hs_type;
        this.is_new = is_new;
    }
}
