package com.yezi.secretgarden.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
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

}
