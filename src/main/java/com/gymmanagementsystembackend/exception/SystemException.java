package com.boot.exception;

public class SystemException extends RuntimeException{
    private String code;

    public SystemException(String code,String message) {
        super(message);
        this.code = code;
    }

    public SystemException(String code,String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
