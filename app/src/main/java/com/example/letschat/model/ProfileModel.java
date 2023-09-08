package com.example.letschat.model;

public class ProfileModel {
    private String name;

    private String phoneNum;
    private String uid;
    private  String imgUri;
    public ProfileModel(){

}
public ProfileModel(String name, String phoneNum, String uid){
        this.name=name;
        this.phoneNum=phoneNum;
        this.uid=uid;
}
    public ProfileModel(String name, String phoneNum, String uid, String imgUri) {
        this.name = name;
        this.phoneNum = phoneNum;
        this.uid = uid;
        this.imgUri = imgUri;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getImgUri() {
        return imgUri;
    }

    public void setImgUri(String imgUri) {
        this.imgUri = imgUri;
    }


}
