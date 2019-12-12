package com.xebia.fs101.writerpad.api.response;

import com.xebia.fs101.writerpad.domain.ReadTime;

public class ReadTimeResponse {
    private String articleID;
    private ReadTime readTime;

    public ReadTimeResponse(String articleID, ReadTime readTime) {
        this.articleID = articleID;
        this.readTime = readTime;
    }

    public String getArticleID() {
        return articleID;
    }

    public ReadTime getReadTime() {
        return readTime;
    }
}



