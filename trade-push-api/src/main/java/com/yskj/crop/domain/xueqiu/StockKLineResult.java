package com.yskj.crop.domain.xueqiu;

import java.util.List;

/**
 * @Author kai.tang@yintech.cn
 * @Date 2019/5/13 10:48
 * @Version 1.0.0
 */
public class StockKLineResult {

    /**
     * 股票代码
     */
    private String symbol;

    /**
     * 列名
     */
    private List<String> column;

    /**
     * 元素
     */
    private List<List<String>> item;


    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public List<String> getColumn() {
        return column;
    }

    public void setColumn(List<String> column) {
        this.column = column;
    }

    public List<List<String>> getItem() {
        return item;
    }

    public void setItem(List<List<String>> item) {
        this.item = item;
    }
}
