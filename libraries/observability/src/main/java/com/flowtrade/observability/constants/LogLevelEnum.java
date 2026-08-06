package com.flowtrade.observability.constants;

public enum LogLevelEnum {
    TRACE("TRACE"),
    DEBUG("DEBUG"),
    INFO("INFO"),
    WARN("WARN"),
    ERROR("ERROR");

    private final String logLevel;

    private LogLevelEnum(String logLevel){
        this.logLevel = logLevel;
    }

    public String getLogLevel(){
        return this.logLevel;
    }
}
