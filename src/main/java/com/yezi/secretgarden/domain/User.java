package com.yezi.secretgarden.domain;

import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Entity
@Table(name="users")
@Getter
@Setter
@ToString
@DynamicInsert
@NoArgsConstructor(access = AccessLevel.PROTECTED) // JPA 프록시 객체를 위함
public class User {
    @Id
    @Column(name="user_id")
    private String username;
    private String password;
    private String name;
    @Column(unique = true)
    private String email;
    private String phonenum;
    private LocalDateTime regDate;

    // 인증을 위한 컬럼
    @ColumnDefault("'ROLE_USER'")
    private String roles;


    // 조인되는 엔티티 클래스의 필드명
    @OneToMany(mappedBy = "user")
    List<Board> boardList = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    List<Diary> diaryList = new ArrayList<>();

    // 생성 메서드 - 빌더패턴//
    @Builder
    public User (String username, String password, String name, String email, String phonenum) {
        this.username=username;
        this.password=password;
        this.name = name;
        this.email=email;
        this.phonenum=phonenum;
        this.regDate=LocalDateTime.now();

    }

    public List<String> getRoleList() {
        if(this.roles.length()>0) {
            return Arrays.asList(this.roles.split(","));
        }
        return new ArrayList<>();
    }




}
