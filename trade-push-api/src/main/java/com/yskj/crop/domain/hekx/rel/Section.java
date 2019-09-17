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
public class Section {

    private List<String> subtitle;
    private List<List<String>> item;
    public void setSubtitle(List<String> subtitle) {
         this.subtitle = subtitle;
     }
     public List<String> getSubtitle() {
         return subtitle;
     }

    public void setItem(List<List<String>> item) {
         this.item = item;
     }
     public List<List<String>> getItem() {
         return item;
     }

}