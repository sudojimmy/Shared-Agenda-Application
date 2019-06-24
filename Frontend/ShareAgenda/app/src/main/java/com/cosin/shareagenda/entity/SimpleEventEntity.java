package com.cosin.shareagenda.entity;

import java.io.Serializable;

public class SimpleEventEntity implements Serializable {
    private int quarter;
    private int during;
    private String name;

    public SimpleEventEntity(int quarter, int during, String name) {
        this.quarter = quarter;
        this.during = during;
        this.name = name;
    }

    public int getQuarter() {
        return quarter;
    }

    public void setQuarter(int quarter) {
        this.quarter = quarter;
    }

    public int getDuring() {
        return during;
    }

    public void setDuring(int during) {
        this.during = during;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
