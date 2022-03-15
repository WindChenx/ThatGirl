package com.example.thatgirl.entity;

import com.google.gson.Gson;

import java.util.List;

public class Girl {
        private String desc;
        private String url;

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "Girl{" +
                "desc='" + desc + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
