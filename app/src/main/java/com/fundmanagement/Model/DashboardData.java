package com.fundmanagement.Model;

public class DashboardData {
    String main_text;
    String sub_text;
    int image;

    public DashboardData(String main_text, String sub_text, int image) {
        this.main_text = main_text;
        this.sub_text = sub_text;
        this.image = image;
    }

    public String getMain_text() {
        return main_text;
    }

    public String getSub_text() {
        return sub_text;
    }

    public int getImage() {
        return image;
    }
}
