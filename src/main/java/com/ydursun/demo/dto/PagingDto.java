package com.ydursun.demo.dto;

import java.util.ArrayList;
import java.util.List;

public class PagingDto<T extends BaseDto> extends BaseDto {

    private int page;

    private int limit;

    private long totalCount;

    private int totalPage;

    private List<T> data;

    public PagingDto() {
        this.setPage(0);
        this.setLimit(0);
        this.setTotalCount(0L);
        this.setTotalPage(0);
        this.setData(new ArrayList<T>());
    }

    public PagingDto(int page, int limit) {
        this.setPage(page);
        this.setLimit(limit);
        this.setTotalCount(0L);
        this.setTotalPage(0);
        this.setData(new ArrayList<T>());
    }

    public PagingDto(long totalCount) {
        this.setPage(0);
        this.setLimit(0);
        this.setTotalPage(0);
        this.setTotalCount(totalCount);
        this.setData(new ArrayList<T>());
    }

    public PagingDto(int page, int limit, long totalCount) {
        this.setPage(page);
        this.setLimit(limit);
        this.setTotalCount(totalCount);
        this.setTotalPage(getTotalPage(limit, totalCount));
        this.setData(new ArrayList<T>());
    }

    public PagingDto(long totalCount, List<T> data) {
        this.setPage(0);
        this.setLimit(0);
        this.setTotalPage(0);
        this.setTotalCount(totalCount);
        this.setData(data);
    }

    public PagingDto(int page, int limit, long totalCount, List<T> data) {
        this.setPage(page);
        this.setLimit(limit);
        this.setTotalCount(totalCount);
        this.setData(data);
        this.setTotalPage(getTotalPage(limit, totalCount));
    }

    public static PagingDto createInstance() {
        return new PagingDto();
    }

    public static int getTotalPage(int limit, long totalCount) {
        int totalPage;
        if (totalCount % limit == 0) {
            totalPage = (int) (totalCount / limit);
        } else {
            totalPage = (int) (totalCount / limit + 1);
        }
        return totalPage;
    }

    public int getPage() {
        return page;
    }

    public PagingDto setPage(int page) {
        this.page = page;
        return this;
    }

    public int getLimit() {
        return limit;
    }

    public PagingDto setLimit(int limit) {
        this.limit = limit;
        return this;
    }

    public long getTotalCount() {
        return totalCount;
    }

    public PagingDto setTotalCount(long totalCount) {
        this.totalCount = totalCount;
        return this;
    }

    public List<T> getData() {
        return data;
    }

    public PagingDto setData(List<T> data) {
        this.data = data;
        return this;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    @Override
    public String toString() {
        return "PagingDto{" +
                "page=" + page +
                ", limit=" + limit +
                ", totalCount=" + totalCount +
                ", types=" + data +
                ", totalPage=" + totalPage +
                '}';
    }
}
