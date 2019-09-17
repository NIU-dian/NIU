/**
 * Copyright 2019 bejson.com
 */
package com.yskj.crop.domain.xueqiu;

import java.util.List;

/**
 * Auto-generated: 2019-06-25 16:8:21
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class MinuteInfoResult {

    private double last_close;
    private List<Item> items;
    private int items_size;

    public void setLast_close(double last_close) {
        this.last_close = last_close;
    }

    public double getLast_close() {
        return last_close;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems_size(int items_size) {
        this.items_size = items_size;
    }

    public int getItems_size() {
        return items_size;
    }

}