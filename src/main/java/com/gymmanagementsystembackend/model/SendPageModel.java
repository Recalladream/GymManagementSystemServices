package com.gymmanagementsystembackend.model;

import java.util.Arrays;

public class SendPageModel {
    private Object pageList;
    private int total;

    public Object getPageList() {
        return pageList;
    }

    public void setPageList(Object pageList) {
        this.pageList = pageList;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    @Override
    public String toString() {
        return "SendPageModel{" +
                "pageList=" + pageList +
                ", total=" + total +
                '}';
    }
}
