package com.zyrox.model.log;

/**
 * Created by Jonny on 7/25/2019
 **/
public class LogField {

    private final String title;
    private final LogFieldType logFieldType;
    private final Object data;

    public LogField(String title, LogFieldType logFieldType, Object data) {
        this.title = title;
        this.logFieldType = logFieldType;
        this.data = data;
    }

    public String getTitle() {
        return title;
    }

    public LogFieldType getLogFieldType() {
        return logFieldType;
    }

    public Object getData() {
        return data;
    }

}
