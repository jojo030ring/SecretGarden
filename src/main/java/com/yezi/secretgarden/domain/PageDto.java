package com.yezi.secretgarden.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.stereotype.Component;

@Getter
@Setter
@ToString
public class PageDto {
    boolean prev;
    boolean next;

    int page;

    int startPage;
    int endPage;

    int totalBoardCnt;
    int totalPage;
    int pageLimit;
    @Builder
    public PageDto(int page, int totalBoardCnt, int pageLimit) {
        this.totalBoardCnt=totalBoardCnt;
        this.page = page;
        this.totalPage = ((totalBoardCnt-1)/pageLimit) + 1;
        this.pageLimit=pageLimit;


        this.startPage = (int)((double)(page-1)/pageLimit)*pageLimit+1;
        this.endPage = (startPage+(pageLimit-1))>totalPage ? totalPage : startPage+pageLimit-1;

        this.prev = (this.startPage!=1);
        this.next = (endPage!=totalPage);
        System.out.println(this);

    }





}
