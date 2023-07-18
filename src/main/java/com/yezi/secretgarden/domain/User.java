package com.yezi.secretgarden.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="user")
@Getter
@Setter
@ToString
public class User {
    @Id
    @Column(name="user_id")
    private String id;
    private String password;
    private String name;
    private String email;
    private String phonenum;
    private LocalDateTime regDate;

    // 조인되는 엔티티 클래스의 필드명
    @OneToMany(mappedBy = "user")
    List<Freeboard> boardList = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    List<Diary> diaryList = new ArrayList<>();

    // 생성 메서드 //
    @Builder
    public User (String id, String password, String name, String email, String phonenum) {
        this.id=id;
        this.password=password;
        this.name = name;
        this.email=email;
        this.phonenum=phonenum;
        this.regDate=LocalDateTime.now();

    }


    public User() {

    }


}
