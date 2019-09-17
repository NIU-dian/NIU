package com.yskj.crop.domain.xueqiu.newStock;

public class NewStockItem {
    private String symbol;

    private String name;

    private String exchange;

    private String list_date;

    private String shares;

    private String issue_price;

    private String issprice_min;

    private String issprice_max;

    private String created_at;

    private String updated_at;

    private String current;

    private String percent;

    private String chg;

    private Long onl_distr_date;

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getExchange() {
        return exchange;
    }

    public void setExchange(String exchange) {
        this.exchange = exchange;
    }

    public String getList_date() {
        return list_date;
    }

    public void setList_date(String list_date) {
        this.list_date = list_date;
    }

    public String getShares() {
        return shares;
    }

    public void setShares(String shares) {
        this.shares = shares;
    }

    public String getIssue_price() {
        return issue_price;
    }

    public void setIssue_price(String issue_price) {
        this.issue_price = issue_price;
    }

    public String getIssprice_min() {
        return issprice_min;
    }

    public void setIssprice_min(String issprice_min) {
        this.issprice_min = issprice_min;
    }

    public String getIssprice_max() {
        return issprice_max;
    }

    public void setIssprice_max(String issprice_max) {
        this.issprice_max = issprice_max;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getCurrent() {
        return current;
    }

    public void setCurrent(String current) {
        this.current = current;
    }

    public String getPercent() {
        return percent;
    }

    public void setPercent(String percent) {
        this.percent = percent;
    }

    public String getChg() {
        return chg;
    }

    public void setChg(String chg) {
        this.chg = chg;
    }

    public Long getOnl_distr_date() {
        return onl_distr_date;
    }

    public void setOnl_distr_date(Long onl_distr_date) {
        this.onl_distr_date = onl_distr_date;
    }
}