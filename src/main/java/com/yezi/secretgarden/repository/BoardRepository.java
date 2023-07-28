package com.yezi.secretgarden.repository;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.impl.JPAUpdateClause;
import com.yezi.secretgarden.domain.Board;
import com.yezi.secretgarden.domain.QBoard;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class BoardRepository {
    @PersistenceContext
    EntityManager em;
    private final JPAQueryFactory jpaQueryFactory;

    public void savePost(Board board) {
        em.persist(board);
    }

    public void deletePost(Board board) {
        em.remove(board);
    }

    public void modifyPost(Board board) {
        em.persist(board);
    }

    public Board getBoard(Long boardId) {

        return em.find(Board.class, boardId);

    }
    public void increaseCnt(Long boardId) {
        QBoard board = QBoard.board;
        jpaQueryFactory.update(board).set(board.cnt,board.cnt.add(1)).where(board.id.eq(boardId)).execute();
    }

    public List<Board> getBoardList() {
        QBoard board=QBoard.board; // QBoard board = new QBoard("board1"), board1은 별칭
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        return queryFactory.selectFrom(board)
                .orderBy(board.id.desc())
                .fetch();
    }




}
