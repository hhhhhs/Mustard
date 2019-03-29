package com.hs.mustard.samples;

public class Talk {
    private String name;
    private int imageId;
    private String time;

    public Talk(String name, int imageId, String time){
        this.name = name;
        this.imageId = imageId;
        this.time = time;
    }
    public String getName(){return name;}
    public int getImageId(){return imageId;}
    public String getTime(){return time;}
}
