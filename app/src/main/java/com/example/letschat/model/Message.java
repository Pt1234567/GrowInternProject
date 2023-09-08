package com.example.letschat.model;

public class Message {
    String messageId;
    String message;
    String senderId;
    String imgURL;

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getImgURL() {
        return imgURL;
    }

    public void setImgURL(String imgURL) {
        this.imgURL = imgURL;
    }

    public Long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Long timeStamp) {
        this.timeStamp = timeStamp;
    }

    Long timeStamp;

    public Message(){

    }
    public Message(String message,String senderId,Long timeStamp){
        this.message=message;
        this.senderId=senderId;
        this.timeStamp=timeStamp;
    }
}
