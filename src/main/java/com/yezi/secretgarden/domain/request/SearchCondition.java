package com.yezi.secretgarden.domain.request;

import lombok.Builder;
import lombok.Getter;

@Getter
public class SearchCondition {
    private String keyword;
    private String category;

    @Builder
    public SearchCondition(String keyword, String category) {
        this.keyword=keyword;
        this.category=category;
    }
}
