package com.yezi.secretgarden.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yezi.secretgarden.domain.Board;
import com.yezi.secretgarden.domain.PageDto;
import com.yezi.secretgarden.domain.QBoard;
import com.yezi.secretgarden.domain.request.SearchCondition;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.function.Supplier;

import static com.yezi.secretgarden.domain.QBoard.board;

@Repository
@RequiredArgsConstructor
public class SearchRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public List<Board> getSearchPagingList(SearchCondition sc, PageDto pageDto) {
        QBoard board = QBoard.board;
        List<Board> searchList = jpaQueryFactory.selectFrom(board).where(isSearchable(sc))
                .orderBy(board.regdate.desc())
                .offset(0+(pageDto.getPage()-1)* pageDto.getPageLimit())
                .limit(pageDto.getPageLimit())
                .fetch();
        return searchList;
    }


    public Long getSearchCnt(SearchCondition sc) {
        QBoard board = QBoard.board;
        Long searchCnt = jpaQueryFactory.selectFrom(board).where(isSearchable(sc))
                .orderBy(board.regdate.desc())
                .fetchCount();
        return searchCnt;
    }


    /**
     * BooleanBuilder를 safe하게 만들기 위한 메소드
     * BooleanBuilder >> 쿼리 조건설정인 where절의 조건을 생성해주는 것이라고 생각하면 됨
     * null이 오면 exception 이 발생하기 때문에 해당 부분을 try-catch로 감싸 처리해준다.
     * @param f
     * @return
     */
    BooleanBuilder isSearchable(SearchCondition sc) {
        if (sc.getCategory().equals("title")) {
            return titleCt(sc.getKeyword());
        }
        else if(sc.getCategory().equals("content")) {
            return contentCt(sc.getKeyword());
        }
        else {
            return titleCt(sc.getKeyword()).or(contentCt(sc.getKeyword()));
        }
    }
    BooleanBuilder nullSafeBuilder (Supplier<BooleanExpression> f) {
        try {
            return new BooleanBuilder(f.get());
        } catch(Exception e) {
            return new BooleanBuilder();
        }
    }

    /**
     * eq 메서드는 반드시 null check를 해주어야 한다
     *
     *
     */


    BooleanBuilder titleCt(String title) {
        return nullSafeBuilder(() ->
            board.title.contains(title)
        );
    }

    BooleanBuilder contentCt(String content) {
        return nullSafeBuilder(()->board.content.contains(content));
    }
}

