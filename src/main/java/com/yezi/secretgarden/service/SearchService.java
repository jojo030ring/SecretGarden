package com.yezi.secretgarden.service;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yezi.secretgarden.domain.Board;
import com.yezi.secretgarden.domain.PageDto;
import com.yezi.secretgarden.domain.request.SearchCondition;
import com.yezi.secretgarden.repository.SearchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchService {
    private final SearchRepository searchRepository;
    @Transactional
    public List<Board> search(SearchCondition sc, PageDto pageDto) {
        List<Board> searchList = searchRepository.getSearchPagingList(sc,pageDto);
        return searchList;
    }

    @Transactional
    public Long getSearchCnt(SearchCondition sc) {
        return searchRepository.getSearchCnt(sc);
    }
}
