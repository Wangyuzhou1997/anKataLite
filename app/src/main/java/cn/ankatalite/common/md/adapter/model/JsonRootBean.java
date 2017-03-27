/**
  * Copyright 2017 bejson.com 
  */
package cn.ankatalite.common.md.adapter.model;
import java.util.List;

/**
 * Auto-generated: 2017-03-08 0:12:26
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class JsonRootBean {

    private int count;
    private int start;
    private int total;
    private List<Subjects> subjects;
    private String title;
    public void setCount(int count) {
         this.count = count;
     }
     public int getCount() {
         return count;
     }

    public void setStart(int start) {
         this.start = start;
     }
     public int getStart() {
         return start;
     }

    public void setTotal(int total) {
         this.total = total;
     }
     public int getTotal() {
         return total;
     }

    public void setSubjects(List<Subjects> subjects) {
         this.subjects = subjects;
     }
     public List<Subjects> getSubjects() {
         return subjects;
     }

    public void setTitle(String title) {
         this.title = title;
     }
     public String getTitle() {
         return title;
     }

}