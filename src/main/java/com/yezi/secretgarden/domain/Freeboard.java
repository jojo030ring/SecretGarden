package com.yezi.secretgarden.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name="freeboard")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Freeboard {
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
    public static Freeboard createFreeBoard(User user, String title, String content) {
        Freeboard freeboard = new Freeboard();
        freeboard.setUser(user);
        freeboard.setTitle(title);
        freeboard.setContent(content);
        freeboard.setRegdate(LocalDateTime.now());
        return freeboard;
    }




}
