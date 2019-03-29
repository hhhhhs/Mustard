package com.hs.mustard.samples;
import java.util.List;

public class Result {

    private String stat;
    private List<Data> data;
    public void setStat(String stat) {
         this.stat = stat;
     }
     public String getStat() {
         return stat;
     }

    public void setData(List<Data> data) {
         this.data = data;
     }
     public List<Data> getData() {
         return data;
     }

}