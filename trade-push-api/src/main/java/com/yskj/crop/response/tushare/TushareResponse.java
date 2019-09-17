/**
 * Copyright 2019 bejson.com
 */
package com.yskj.crop.response.tushare;

import java.util.List;

/**
 * Auto-generated: 2019-05-09 17:25:3
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class TushareResponse {

    private List<String> fields;
    private List<List<String>> items;

    public void setFields(List<String> fields) {
        this.fields = fields;
    }

    public List<String> getFields() {
        return fields;
    }

    public void setItems(List<List<String>> items) {
        this.items = items;
    }

    public List<List<String>> getItems() {
        return items;
    }

}