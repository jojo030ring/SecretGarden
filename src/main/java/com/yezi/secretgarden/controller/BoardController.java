package com.yezi.secretgarden.controller;

import com.yezi.secretgarden.auth.PrincipalDetails;
import com.yezi.secretgarden.domain.Board;
import com.yezi.secretgarden.domain.User;
import com.yezi.secretgarden.domain.request.BoardRegisterRequest;
import com.yezi.secretgarden.jwt.JwtTokenUtil;
import com.yezi.secretgarden.service.BoardService;
import com.yezi.secretgarden.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.websocket.server.PathParam;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.Principal;
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
    public String getBoard(Principal principal,HttpServletRequest request, HttpServletResponse response, Model m, @PathVariable("id") Long id, RedirectAttributes ra) throws IOException {
        String userId = principal.getName();
        Board board = boardService.getBoard(id);
        if(board==null) {
            doInvalidRequestAction(request,response);
        }
        m.addAttribute("userId", userId);
        m.addAttribute("board",board);
        m.addAttribute("MODE","READ_MODE");
        return "board_reg";

    }

    @GetMapping("/post")
    public String postForm(Principal principal, HttpServletRequest request, Model m) {
        String id = principal.getName();
        System.out.println("id = " + id);
        m.addAttribute("id",id);
        m.addAttribute("MODE","POST_MODE");

        return "board_reg";
    }

    @PostMapping("/post")
    @ResponseBody
    public ResponseEntity<HashMap<String, String>> boardPost(Authentication authentication, @RequestBody BoardRegisterRequest bRRequest, Model m) {
        // JPA 영속상태때문에 바로 추출할 수가 없다.
        User user = getUserInfo(authentication);
        boardService.savePost(user,bRRequest);
        HashMap<String,String> map = new HashMap<>();
        map.put("msg","게시글 등록을 완료했습니다.");
        map.put("url","/secretgarden/board");
        m.addAttribute("MODE","POST_MODE");
        return new ResponseEntity<HashMap<String,String>>(map,HttpStatus.OK);
    }

    @GetMapping("/modify/{id}")
    public String modifyForm( Principal principal, @PathVariable("id") Long id, HttpServletRequest request, HttpServletResponse response, Model m) throws IOException {
        Board board = boardService.getBoard(id);
        String userId = principal.getName();

        if(board==null || !requestValidUser(userId,board)) {
            doInvalidRequestAction(request,response);
        }


        // 여기까지 들어왔다는 건 인가된 사용자라는 뜻이기에 cookie null check는 생략

        m.addAttribute("board",board);
        m.addAttribute("id",id);
        m.addAttribute("MODE","MODIFY_MODE");
        return "board_reg";

    }

    @PostMapping("/modify/{id}")
    @ResponseBody
    public ResponseEntity<HashMap<String, String>> modify( @PathVariable("id") Long id, @RequestBody BoardRegisterRequest brr, HttpServletRequest request, HttpServletResponse response, Model m) throws IOException {
        boardService.modifyPost(id, brr);
        System.out.println("is True? = " + "yes");
        HashMap<String, String> map = new HashMap<>();
        map.put("msg","수정이 완료됐습니다.");
        map.put("url","/secretgarden/board");
        return new ResponseEntity<HashMap<String, String>>(map,HttpStatus.OK);


    }
    @PostMapping("/delete/{id}")
    @ResponseBody
    public ResponseEntity<HashMap<String, String>> deletePost(Authentication authentication, @PathVariable("id") Long id, String userId, Model m) {
        // JPA 영속상태때문에 바로 추출할 수가 없다.
        User user = getUserInfo(authentication);
        HashMap<String,String> map = new HashMap<>();

        if (user.getUsername().equals(userId)) {
            map.put("msg","잘못된 요청입니다.");
            map.put("url","/secretgarden/board/"+id);
            return new ResponseEntity<HashMap<String, String>>(map,HttpStatus.BAD_REQUEST);
        }
        map.put("msg","게시글이 삭제되었습니다.");
        map.put("url","/secretgarden/board");
        boardService.deletePost(id);
        return new ResponseEntity<HashMap<String,String>>(map,HttpStatus.OK);
    }

    public boolean requestValidUser(String userId, Board board) {
        return board.getUser().getUsername().equals(userId);
    }

    public void doInvalidRequestAction(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html; charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.println("<script>alert('잘못된 접근입니다.'); location.href='/secretgarden/board';</script>");
        out.flush();
    }

    public User getUserInfo(Authentication auth) {
        User userFromPrincipal = ((PrincipalDetails)(auth.getPrincipal())).getUser();
        // JPA 영속상태때문에...
        User user = User.builder().username(userFromPrincipal.getUsername())
                .name(userFromPrincipal.getName()).password(userFromPrincipal.getPassword()).email(userFromPrincipal.getEmail())
                .phonenum(userFromPrincipal.getPhonenum()).build();
        return user;
    }

}
