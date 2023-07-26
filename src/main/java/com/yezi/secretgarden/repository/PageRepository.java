package com.yezi.secretgarden.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yezi.secretgarden.domain.Board;
import com.yezi.secretgarden.domain.QBoard;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

import static com.yezi.secretgarden.domain.QBoard.board;

@Repository
@RequiredArgsConstructor
public class PageRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public List<Board> paging(int page, int limit){
        QBoard qBoard = board;

        List<Board> result = jpaQueryFactory
            .selectFrom(board)
            .orderBy(board.regdate.desc())
            .offset(0+(page-1)*limit)
            .limit(limit)
            .fetch();

        return result;

    }
    public int totalCnt() {
        QBoard qBoard = board;
        int totalSize=jpaQueryFactory.selectFrom(board)
                .fetch().size();
        return totalSize;
    }

}
