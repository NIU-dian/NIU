/**
  * Copyright 2019 bejson.com 
  */
package com.yskj.crop.domain.hekx.daily;
import java.util.List;

/**
 * Auto-generated: 2019-07-05 19:18:0
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class Table {

    private String classname;
    private List<List<String>> schema;
    private List<Tr> tr;
    public void setClassname(String classname) {
         this.classname = classname;
     }
     public String getClassname() {
         return classname;
     }

    public void setSchema(List<List<String>> schema) {
         this.schema = schema;
     }
     public List<List<String>> getSchema() {
         return schema;
     }

    public void setTr(List<Tr> tr) {
         this.tr = tr;
     }
     public List<Tr> getTr() {
         return tr;
     }

}