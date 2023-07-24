package com.yezi.secretgarden.repository;

import com.yezi.secretgarden.domain.Board;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class BoardRepository {
    @PersistenceContext
    EntityManager em;

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
}
