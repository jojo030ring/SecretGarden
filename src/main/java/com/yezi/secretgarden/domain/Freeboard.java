package com.yezi.secretgarden.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
public class Freeboard {
    @Id
    @GeneratedValue
    @Column(name="freeboard_id")
    private Long id;
    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;
    private String title;
    private String content;
    private LocalDateTime regdate;



}
