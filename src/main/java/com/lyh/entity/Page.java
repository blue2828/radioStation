package com.lyh.entity;

public class Page implements java.io.Serializable {
    private int page, limit, start;
    public Page() {
    }

    public Page(int page, int limit) {
        this.page = page;
        this.limit = limit;
    }
    public int getStart () {
        return (this.page - 1) * this.limit;
    }
    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }
}
