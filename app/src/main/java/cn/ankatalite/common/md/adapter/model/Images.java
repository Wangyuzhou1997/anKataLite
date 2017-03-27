/**
  * Copyright 2017 bejson.com 
  */
package cn.ankatalite.common.md.adapter.model;

import java.io.Serializable;

/**
 * Auto-generated: 2017-03-08 0:12:26
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class Images implements Serializable {

    private String small;
    private String large;
    private String medium;
    public void setSmall(String small) {
         this.small = small;
     }
     public String getSmall() {
         return small;
     }

    public void setLarge(String large) {
         this.large = large;
     }
     public String getLarge() {
         return large;
     }

    public void setMedium(String medium) {
         this.medium = medium;
     }
     public String getMedium() {
         return medium;
     }

}