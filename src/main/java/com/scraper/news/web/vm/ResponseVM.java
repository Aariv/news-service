package com.scraper.news.web.vm;

import lombok.Data;

@Data
public class ResponseVM {
    private String status;
    private int statusCode;
    private String data;
    private String message;

    public ResponseVM() {
        this.status = "success";
        this.statusCode = 200;
        this.data = null;
        this.message = "Success";
    }
}
