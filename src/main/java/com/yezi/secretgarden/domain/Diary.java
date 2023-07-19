package com.yezi.secretgarden.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="diary")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Diary {
    @Id
    @GeneratedValue
    @Column(name="diary_id")
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User user;
    private String title;
    private String content;
    private LocalDateTime diaryRegdate;

    // 연관관계 메서드
    public void setUser(User user) {
        this.user=user;
        user.getDiaryList().add(this);
    }

    /**
     * 생성 메서드
     * @param user
     * @param title
     * @param content
     * @return
     */
    public static Diary createDiary(User user, String title, String content) {
        Diary diary = new Diary();
        diary.setUser(user);
        diary.setTitle(title);
        diary.setContent(content);
        diary.setDiaryRegdate(LocalDateTime.now());
        return diary;
    }

}
