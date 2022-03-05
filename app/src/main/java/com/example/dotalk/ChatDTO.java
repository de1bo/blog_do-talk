package com.example.dotalk;

public class ChatDTO {

    private String userName;
    private String message;
    private int viewType; //chatroom 안에서 채팅 틀을 구현하기 위한 값

    public ChatDTO(String userName, String message,int viewType) {
        this.userName = userName;
        this.message = message;
        this.viewType = viewType;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUserName() {
        return userName;
    }

    public String getMessage() {
        return message;
    }
    public int getViewType(){
        return  viewType;
    }
}