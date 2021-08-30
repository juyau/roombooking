package org.thebreak.roombooking.common.response;


import org.springframework.data.domain.Page;

import java.util.List;

public class PageResult<T> {
    private long totalRows;
    private int totalPages;
    private int pageSize;       // request page size
    private int contentSize;    // actual returned content size
    private int currentPage;
    private List<T> content;


    public PageResult(Page page, List<T> list) {
        this.totalRows = page.getTotalElements();
        this.pageSize = page.getSize();
        this.totalPages = page.getTotalPages();
        this.contentSize = page.getNumberOfElements();
        this.currentPage = page.getNumber() + 1;
        this.content = list;
    }

    public PageResult(long totalRows, int totalPages, int pageSize, int contentSize, int currentPage, List<T> content) {
        this.totalRows = totalRows;
        this.totalPages = totalPages;
        this.pageSize = pageSize;
        this.contentSize = contentSize;
        this.currentPage = currentPage;
        this.content = content;
    }

    public long getTotalRows() {
        return totalRows;
    }

    public void setTotalRows(long totalRows) {
        this.totalRows = totalRows;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getContentSize() {
        return contentSize;
    }

    public void setContentSize(int contentSize) {
        this.contentSize = contentSize;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public List<T> getContent() {
        return content;
    }

    public void setContent(List<T> content) {
        this.content = content;
    }
}
