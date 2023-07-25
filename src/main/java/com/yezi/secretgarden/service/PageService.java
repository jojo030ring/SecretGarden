package com.yezi.secretgarden.service;

import com.yezi.secretgarden.domain.Board;
import com.yezi.secretgarden.repository.PageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PageService {
    private final PageRepository pageRepository;
    public final int BOARD_LIMIT=2;
    public final int PAGE_LIMIT=10;
    @Transactional
    public List<Board> getPage(int page,int limit) {
        return pageRepository.paging(page,BOARD_LIMIT);
    }
    @Transactional
    public int getTotalCount() {
        return pageRepository.totalCnt();
    }
}
