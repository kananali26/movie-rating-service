package com.sky.movieratingservice.domain;

import java.util.Collections;
import java.util.List;
import lombok.Getter;

@Getter
public class PagedResult<T> {
    private final List<T> content;
    private final int pageNumber;
    private final int pageSize;
    private final long totalElements;
    private final int totalPages;

    public PagedResult(List<T> content, int pageNumber, int pageSize, long totalElements) {
        this.content = content != null ? content : Collections.emptyList();
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
        this.totalElements = totalElements;
        this.totalPages = pageSize > 0 ? (int) Math.ceil((double) totalElements / pageSize) : 0;
    }

    public List<T> getContent() {
        return Collections.unmodifiableList(content);
    }


    /**
     * @return Whether the current page is the first page
     */
    public boolean isFirst() {
        return pageNumber == 0;
    }

    /**
     * @return Whether the current page is the last page
     */
    public boolean isLast() {
        return pageNumber == totalPages - 1;
    }

    /**
     * @return Whether there is a next page
     */
    public boolean hasNext() {
        return pageNumber < totalPages - 1;
    }

    /**
     * @return Whether there is a previous page
     */
    public boolean hasPrevious() {
        return pageNumber > 0;
    }
}