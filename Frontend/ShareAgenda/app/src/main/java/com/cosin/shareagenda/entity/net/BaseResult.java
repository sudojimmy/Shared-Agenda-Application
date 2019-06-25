package com.cosin.shareagenda.entity.net;

import java.io.Serializable;

public class BaseResult implements Serializable {
    protected String errmsg = "";

    public String getErrmsg() {
        return this.errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }
}
