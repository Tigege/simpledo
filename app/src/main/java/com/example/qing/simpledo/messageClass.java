package com.example.qing.simpledo;

public class messageClass {

    private Boolean check;     //是否选中
    private String editmessages;  //消息

    public messageClass(Boolean check,String editmessages){
        this.check=check;
        this.editmessages=editmessages;
    }

    public Boolean getCheck() {
        return check;
    }

    public String getEditmessages() {
        return editmessages;
    }

    public void setCheck(Boolean check) {
        this.check = check;
    }

    public void setEditmessages(String editmessages) {
        this.editmessages = editmessages;
    }
}
