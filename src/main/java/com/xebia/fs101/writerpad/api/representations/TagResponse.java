package com.xebia.fs101.writerpad.api.representations;


public class TagResponse {

    private String tag;
    private int occurence;

    public TagResponse(String tag, Long occurence) {
        this.tag = tag;
        this.occurence = occurence.intValue();
    }

    public String getTag() {
        return tag;
    }

    public int getOccurence() {
        return occurence;
    }
}
