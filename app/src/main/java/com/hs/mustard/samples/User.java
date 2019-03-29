package com.hs.mustard.samples;

import android.graphics.Bitmap;

/**
 * Created by hs on 2017/12/11.
 */

public class User {
    private int id;
    private String phone;
    private String name;
    private int sex;
    private String imgpath;
    private String real_name;
    private String birthday;
    private String email;
    private int age;
    private Bitmap image;
    private int imageId;
    private String imageUrl;
    private int numFriends;
    public User(String name, int imageId, int numFriends){
        this.name = name;
        this.imageId = imageId;
        this.numFriends = numFriends;
    }
    public int getImageId(){return imageId;}
    public String getImageUrl(){return imageUrl;}
    public int getNumFriends(){return numFriends;}

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getSex() {
        return sex;
    }
    public void setSex(int sex) {
        this.sex = sex;
    }
    public String getImgpath() {
        return imgpath;
    }
    public void setImgpath(String imgpath) {
        this.imgpath = imgpath;
    }
    public Bitmap getImage() {
        return image;
    }
    public void setImage(Bitmap image) {
        this.image = image;
    }
    public String getReal_name() {
        return real_name;
    }
    public void setReal_name(String real_name) {
        this.real_name = real_name;
    }
    public String getBirthday() {
        return birthday;
    }
    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public int getAge() {
        return age;
    }
    public void setAge(int age) {
        this.age = age;
    }
    @Override
    public String toString() {
        return "user [phone=" + phone + ", name=" + name + ", sex=" + sex + "]";
    }
}
