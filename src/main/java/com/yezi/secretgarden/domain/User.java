package com.yezi.secretgarden.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class User {
    @Id
    @Column(name="user_id")
    private String userId;
    private String title;
    private String content;
    private LocalDateTime freeboardRegdate;

    @OneToMany(mappedBy = "freeboard")
    List<Freeboard> boardList = new ArrayList<>();

    @OneToMany(mappedBy = "diary")
    List<Diary> diaryList = new ArrayList<>();

    // 생성 메서드 //
}
