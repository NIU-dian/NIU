/**
  * Copyright 2019 bejson.com 
  */
package com.yskj.crop.domain.hekx.daily;
import java.util.Date;
import java.util.List;

/**
 * Auto-generated: 2019-07-05 19:18:0
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class HekxDailyStat {

    private int id;
    private Date date;
    private String market;
    private int tradingDay;
    private List<Content> content;
    public void setId(int id) {
         this.id = id;
     }
     public int getId() {
         return id;
     }

    public void setDate(Date date) {
         this.date = date;
     }
     public Date getDate() {
         return date;
     }

    public void setMarket(String market) {
         this.market = market;
     }
     public String getMarket() {
         return market;
     }

    public void setTradingDay(int tradingDay) {
         this.tradingDay = tradingDay;
     }
     public int getTradingDay() {
         return tradingDay;
     }

    public void setContent(List<Content> content) {
         this.content = content;
     }
     public List<Content> getContent() {
         return content;
     }

}