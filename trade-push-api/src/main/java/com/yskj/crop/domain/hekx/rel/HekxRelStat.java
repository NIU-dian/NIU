/**
  * Copyright 2019 bejson.com 
  */
package com.yskj.crop.domain.hekx.rel;
import java.util.List;

/**
 * Auto-generated: 2019-07-10 9:53:29
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class HekxRelStat {

    private String category;
    private List<String> tablehead;
    private List<Section> section;
    public void setCategory(String category) {
         this.category = category;
     }
     public String getCategory() {
         return category;
     }

    public void setTablehead(List<String> tablehead) {
         this.tablehead = tablehead;
     }
     public List<String> getTablehead() {
         return tablehead;
     }

    public void setSection(List<Section> section) {
         this.section = section;
     }
     public List<Section> getSection() {
         return section;
     }

}