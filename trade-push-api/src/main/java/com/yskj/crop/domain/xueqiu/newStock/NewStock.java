package com.yskj.crop.domain.xueqiu.newStock;

import java.util.List;

/**
 * @Author kai.tang@yintech.cn
 * @Date 2019/6/27 16:05
 * @Version 1.0.0
 */
public class NewStock {

    private List<NewStockItem> items;

    private int count;

    public List<NewStockItem> getItems() {
        return items;
    }

    public void setItems(List<NewStockItem> items) {
        this.items = items;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
