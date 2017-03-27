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
public class Rating implements Serializable {

    private int max;
    private float average;
    private String stars;
    private int min;
    public void setMax(int max) {
         this.max = max;
     }
     public int getMax() {
         return max;
     }

    public void setAverage(float average) {
         this.average = average;
     }
     public float getAverage() {
         return average;
     }

    public void setStars(String stars) {
         this.stars = stars;
     }
     public String getStars() {
         return stars;
     }

    public void setMin(int min) {
         this.min = min;
     }
     public int getMin() {
         return min;
     }

}