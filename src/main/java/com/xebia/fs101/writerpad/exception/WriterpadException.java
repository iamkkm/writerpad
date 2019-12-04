package com.xebia.fs101.writerpad.exception;

public class WriterpadException extends RuntimeException {
    private Exception ex;
    private Object context;

    public WriterpadException(Exception ex) {
        super(ex);
    }

    public WriterpadException(Object context, Exception ex) {
        super(ex);
        this.context = context;
    }

    public Object getContext() {
        return context;
    }

}
