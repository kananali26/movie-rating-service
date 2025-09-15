package com.sky.movieratingservice.domain;

import java.util.Collections;
import java.util.List;

public record PaginatedResult<T>(List<T> content, int pageNumber, int pageSize, long totalElements) {

    public PaginatedResult(List<T> content, int pageNumber, int pageSize, long totalElements) {
        this.content = content != null ? content : Collections.emptyList();
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
        this.totalElements = totalElements;
    }

    @Override
    public List<T> content() {
        return Collections.unmodifiableList(content);
    }
}
