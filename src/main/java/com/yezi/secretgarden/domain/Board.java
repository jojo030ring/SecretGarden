package com.yezi.secretgarden.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="freeboard")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="freeboard_id")
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User user;
    private String title;
    private String content;
    private LocalDateTime regdate;

    // 연관관계 메서드
    public void setUser(User user) {
        this.user=user;
        user.getBoardList().add(this);
    }

    /**
     * 생성 메서드
     * @param user
     * @param title
     * @param content
     * @return
     */
    public static Board createFreeBoard(User user, String title, String content) {
        Board freeboard = new Board();
        freeboard.setUser(user);
        freeboard.setTitle(title);
        freeboard.setContent(content);
        freeboard.setRegdate(LocalDateTime.now());
        return freeboard;
    }




}
