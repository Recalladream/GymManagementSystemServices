package com.gymmanagementsystembackend.model;

public class RedisDataModel<T> {
    private String key;
    private Integer timeout;
    private T data;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Integer getTimeout() {
        return timeout;
    }

    public void setTimeout(Integer timeout) {
        this.timeout = timeout;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "RedisDataModel{" +
                "key='" + key + '\'' +
                ", timeout=" + timeout +
                ", data=" + data +
                '}';
    }
}
