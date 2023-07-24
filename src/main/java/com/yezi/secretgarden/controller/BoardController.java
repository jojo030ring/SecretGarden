package com.yezi.secretgarden.controller;

import com.yezi.secretgarden.domain.Board;
import com.yezi.secretgarden.domain.User;
import com.yezi.secretgarden.domain.request.BoardRegisterRequest;
import com.yezi.secretgarden.jwt.JwtTokenUtil;
import com.yezi.secretgarden.service.BoardService;
import com.yezi.secretgarden.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.websocket.server.PathParam;
import java.util.HashMap;

@Controller
@RequiredArgsConstructor
@RequestMapping("/secretgarden")
public class BoardController {
    private JwtTokenUtil jwtTokenUtil=new JwtTokenUtil();

    private final BoardService boardService;
    private final UserService userService;

    @GetMapping("/board")
    public String boardList(Model m) {
        m.addAttribute("boardList",boardService.getBoardList());
        return "board";
    }
    @GetMapping("/board/{id}")
    public String getBoard(Model m, @PathVariable("id") Long id) {
        Board board = boardService.getBoard(id);
        System.out.println(board);
        m.addAttribute("board",board);
        m.addAttribute("MODE","READ_MODE");
        return "board_reg";

    }

    @GetMapping("/post")
    public String postForm(HttpServletRequest request, Model m) {
        String id = jwtTokenUtil.getLoginId(request);
        System.out.println("id = " + id);
        m.addAttribute("id",id);
        return "board_reg";
    }

    @PostMapping("/post")
    @ResponseBody
    public ResponseEntity<HashMap<String, String>> boardPost(@RequestBody BoardRegisterRequest bRRequest) {
        System.out.println(bRRequest.getId());
        System.out.println(bRRequest.getTitle());
        System.out.println(bRRequest.getContent());
        User user = userService.findUser(bRRequest.getId());

        boardService.savePost(user,bRRequest);
        HashMap<String,String> map = new HashMap<>();
        map.put("msg","게시글 등록을 완료했습니다.");
        map.put("url","/secretgarden/board");
        return new ResponseEntity<HashMap<String,String>>(map,HttpStatus.OK);
    }



}
