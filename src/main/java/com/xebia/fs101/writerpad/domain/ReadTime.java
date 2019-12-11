package com.xebia.fs101.writerpad.domain;

public class ReadTime {
    private int mins;
    private int seconds;

    public ReadTime() {
    }

    public ReadTime(int mins, int seconds) {
        this.mins = mins;
        this.seconds = seconds;
    }

    public int getMins() {
        return mins;
    }

    public int getSeconds() {
        return seconds;
    }
}
