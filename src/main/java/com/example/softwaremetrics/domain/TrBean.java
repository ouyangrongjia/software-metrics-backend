package com.example.softwaremetrics.domain;

import lombok.Data;

@Data
public class TrBean {
    String file;
    String LOC;
    String CP;
    String CC;

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public TrBean(String file, String LOC, String CP, String CC) {
        this.file = file;
        this.LOC = LOC;
        this.CP = CP;
        this.CC = CC;
    }

    public TrBean(String LOC, String CP, String CC) {
        this.LOC = LOC;
        this.CP = CP;
        this.CC = CC;
    }
}
