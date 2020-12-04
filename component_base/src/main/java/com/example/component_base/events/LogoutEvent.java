package com.example.component_base.events;

import java.util.Date;

//登出成功事件
public class LogoutEvent {
    private Date date;
    private int status;
    private String msg;

    public LogoutEvent(Date date, int status, String msg) {
        this.date = date;
        this.status = status;
        this.msg = msg;
    }

    public Date getDate() {
        return date;
    }

    public int getStatus() {
        return status;
    }

    public String getMsg() {
        return msg;
    }
}
