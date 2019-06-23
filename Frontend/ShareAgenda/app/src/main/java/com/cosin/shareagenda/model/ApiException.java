package com.cosin.shareagenda.model;

public class ApiException extends Exception {
    private String msg;
    private int code;
    ApiException(String msg, int code) {
        super(code + ": " + msg);
        this.msg = msg;
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public int getCode() {
        return code;
    }
}
