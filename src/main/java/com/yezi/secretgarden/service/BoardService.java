package com.yezi.secretgarden.service;

import com.yezi.secretgarden.domain.Board;
import com.yezi.secretgarden.domain.User;
import com.yezi.secretgarden.domain.request.BoardRegisterRequest;
import com.yezi.secretgarden.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
    @Transactional
    public void savePost(User user, BoardRegisterRequest bRRequest) {
        Board board = Board.createFreeBoard(user,bRRequest.getTitle(),bRRequest.getContent());
        boardRepository.savePost(board);
    }

    @Transactional
    public void modifyPost(Long boardId, BoardRegisterRequest post) {
        Board targetPost = boardRepository.getBoard(boardId);
        System.out.println(targetPost);
        targetPost.setTitle(post.getTitle());
        targetPost.setContent(post.getContent());

    }
    @Transactional
    public void deletePost(Long boardId) {
        Board targetPost = boardRepository.getBoard(boardId);
        boardRepository.deletePost(targetPost);
    }

    @Transactional
    public List<Board> getBoardList() {
        return boardRepository.getBoardList();
    }

    @Transactional
    public Board getBoard(Long id) {
        return boardRepository.getBoard(id);
    }

    @Transactional
    public void addCount(Long id) {
        Board board = boardRepository.getBoard(id);
        board.setCnt(board.getCnt()+1);

    }

}
