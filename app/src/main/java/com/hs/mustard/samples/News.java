package com.hs.mustard.samples;

public class News {
    private String type;
    private String key;

    private String name;
    private int imageId;
    public News(String name, int imageId){
        this.name = name;
        this.imageId = imageId;
    }
    public String getName(){return name;}
    public int getImageId(){return imageId;}


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
