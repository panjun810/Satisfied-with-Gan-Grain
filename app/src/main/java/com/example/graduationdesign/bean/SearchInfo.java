package com.example.graduationdesign.bean;

import java.util.List;

public class SearchInfo {
    private String city;          //城市
    private List<String> county;  //区县

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public List<String> getCounty() {
        return county;
    }

    public void setCounty(List<String> county) {
        this.county = county;
    }
}
