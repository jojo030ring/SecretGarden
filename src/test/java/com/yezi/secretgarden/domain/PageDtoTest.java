package com.yezi.secretgarden.domain;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.parameters.P;

import static org.junit.jupiter.api.Assertions.*;

class PageDtoTest {

    @Test
    void isPrev() {
    }

    @Test
    void isNext() {
    }

    @Test
    void getPage() {
    }

    @Test
    void getStartPage() {
    }

    @Test
    void getEndPage() {
    }

    @Test
    void getTotalBoardCnt() {
    }

    @Test
    void getTotalPage() {
    }

    @Test
    void getPageLimit() {
    }

    @Test
    void testToString() {
        PageDto dto = PageDto.builder().page(3).pageLimit(2).totalBoardCnt(10).build();
        System.out.println(dto);
    }

    @Test
    void builder() {
    }
}