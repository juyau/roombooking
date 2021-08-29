package org.thebreak.roombooking.common.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
@AllArgsConstructor
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

}
