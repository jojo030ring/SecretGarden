package com.yezi.secretgarden.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Diary {
    @Id
    @GeneratedValue
    @Column(name="diary_id")
    private Long id;
    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;
    private String title;
    private String content;
    private LocalDateTime diaryRegdate;

}
