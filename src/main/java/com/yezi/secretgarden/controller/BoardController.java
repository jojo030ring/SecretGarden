package com.yezi.secretgarden.controller;

import com.yezi.secretgarden.auth.PrincipalDetails;
import com.yezi.secretgarden.domain.Board;
import com.yezi.secretgarden.domain.PageDto;
import com.yezi.secretgarden.domain.User;
import com.yezi.secretgarden.domain.request.BoardRegisterRequest;
import com.yezi.secretgarden.domain.request.SearchCondition;
import com.yezi.secretgarden.service.BoardService;
import com.yezi.secretgarden.service.PageService;
import com.yezi.secretgarden.service.SearchService;
import com.yezi.secretgarden.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;


@Controller
@RequiredArgsConstructor
@Slf4j
public class BoardController {
    private final UserService userService;
    private final BoardService boardService;
    private final PageService pageService;
    private final SearchService searchService;

    @GetMapping("/board")
    // @RequestParam : query param형식의 파라미터 받아옴
    public String boardList(Model m,@RequestParam(defaultValue = "1") int page) {
        int totalBoardCnt = pageService.getTotalCount();
        int limit = pageService.BOARD_LIMIT;
        m.addAttribute("boardList",pageService.getPage(page,limit));
        PageDto pageDto = PageDto.builder().page(page).pageLimit(limit).totalBoardCnt(totalBoardCnt).build();
        m.addAttribute("pageDto",pageDto);

        return "board";
    }
    @GetMapping("/board/{id}")
    public String getBoard(HttpServletRequest request, HttpServletResponse response, Model m, @PathVariable("id") Long id, RedirectAttributes ra) throws IOException {

        Board board = boardService.getBoard(id);
        String userId = board.getUser().getUsername();
        if(board==null) {
            doInvalidRequestAction(request,response);
        }
        m.addAttribute("userId", userId);
        m.addAttribute("board",board);
        m.addAttribute("MODE","READ_MODE");
        return "board_reg";

    }

    @GetMapping("/post")
    public String postForm(Principal principal, Model m) {

        String id = principal.getName();
        log.warn("BoardController _ Principal.getName() : "+id);
        m.addAttribute("userId",id);
        m.addAttribute("MODE","POST_MODE");

        return "board_reg";
    }

    @PostMapping("/post")
    @ResponseBody
    public ResponseEntity<HashMap<String, String>> boardPost(Authentication authentication, @RequestBody @Valid BoardRegisterRequest bRRequest, Model m) {
        // JPA 영속상태때문에 바로 추출할 수가 없다.
        User user = getUserInfo(authentication);
        System.out.println("user.getUsername() = " + user.getUsername());
        bRRequest.setId(user.getUsername());
        System.out.println("bRRequest.getId() = " + bRRequest.getId());
        boardService.savePost(user,bRRequest);
        HashMap<String,String> map = new HashMap<>();
        map.put("msg","게시글 등록을 완료했습니다.");
        map.put("url","/secretgarden/board");
        m.addAttribute("MODE","POST_MODE");
        return new ResponseEntity<HashMap<String,String>>(map,HttpStatus.OK);
    }

    @GetMapping("/modify/{id}")
    public String modifyForm(Principal principal, @PathVariable("id") Long id, HttpServletRequest request, HttpServletResponse response, Model m) throws IOException {
        Board board = boardService.getBoard(id);
        String userId = principal.getName();

        if(board==null || !requestValidUser(userId,board)) {
            doInvalidRequestAction(request,response);
        }

        m.addAttribute("board",board);
        m.addAttribute("id",id);
        m.addAttribute("MODE","MODIFY_MODE");
        return "board_reg";

    }

    @PostMapping("/modify/{id}")
    @ResponseBody
    public ResponseEntity<HashMap<String, String>> modify( @PathVariable("id") Long id, @RequestBody BoardRegisterRequest brr) throws IOException {
        boardService.modifyPost(id, brr);
        HashMap<String, String> map = new HashMap<>();
        map.put("msg","수정이 완료됐습니다.");
        map.put("url","/secretgarden/board");
        return new ResponseEntity<HashMap<String, String>>(map,HttpStatus.OK);


    }
    @GetMapping("/delete/{id}")
    @ResponseBody
    public ResponseEntity<HashMap<String, String>> deletePost(Principal principal,HttpServletRequest request, HttpServletResponse response, @PathVariable("id") Long id) {
        // JPA 영속상태때문에 바로 추출할 수가 없다.
        HashMap<String,String> map = new HashMap<>();
        String redirectPath = "/secretgarden/board";

        Board board = boardService.getBoard(id);
        String userId = principal.getName();

        if(board==null || !requestValidUser(userId,board)) {
            map.put("msg", "삭제에 실패하였습니다.");
            map.put("url", redirectPath);
            return new ResponseEntity<HashMap<String,String>>(map,HttpStatus.BAD_REQUEST);

        }
        map.put("msg","게시글이 삭제되었습니다.");
        map.put("url",redirectPath);
        boardService.deletePost(id);
        return new ResponseEntity<HashMap<String,String>>(map,HttpStatus.OK);
    }

    @GetMapping("/search")
    public String getSearchList(@RequestParam String category , @RequestParam String keyword, @RequestParam(defaultValue = "1") int page, Model m) {
        // searchService
        SearchCondition sc = SearchCondition.builder().keyword(keyword).category(category).build();
        Long searchCnt = searchService.getSearchCnt(sc);
        PageDto pageDto = PageDto.builder().page(page).pageLimit(pageService.BOARD_LIMIT).totalBoardCnt(searchCnt.intValue()).build();
        List<Board> list = searchService.search(sc,pageDto);
        System.out.println("list.toString() = " + list.toString());
        // attribute
        m.addAttribute("boardList",list);
        m.addAttribute("pageDto",pageDto);
        m.addAttribute("category",category);
        m.addAttribute("keyword",keyword);
        


        return "board";
    }



    public boolean requestValidUser(String userId, Board board) {
        return board.getUser().getUsername().equals(userId);
    }

    public void doInvalidRequestAction(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("text/html; charset=UTF-8");
        PrintWriter out = null;
        try {
            out = response.getWriter();
            out.println("<script>alert('잘못된 접근입니다.'); location.href='/secretgarden/board';</script>");
            out.flush();
        } catch (IOException e) {
            out.println("<script>alert('오류가 발생하였습니다.'); location.href='/secretgarden/board';</script>");
            out.flush();
            throw new RuntimeException(e);
        }

    }

    public User getUserInfo(Authentication auth) {
         String id = ((PrincipalDetails)(auth.getPrincipal())).getUsername();
        // JPA 영속상태때문에 한 번 더 쿼리를 해와야함
        User user =  userService.findUser(id);

        return user;
    }

}
