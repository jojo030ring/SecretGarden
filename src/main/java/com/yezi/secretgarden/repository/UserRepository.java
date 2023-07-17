package com.yezi.secretgarden.repository;

import com.yezi.secretgarden.domain.User;
import com.yezi.secretgarden.domain.UserRegisterRequest;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class UserRepository {
    @PersistenceContext
    EntityManager em;
    @Transactional
    public void save(UserRegisterRequest uRRequest) {
        User user = User.builder().userId(uRRequest.getId())
                .email(uRRequest.getEmail_id()+"@"+uRRequest.getUser_domain())
                .name(uRRequest.getName())
                .password(uRRequest.getPassword())
                .phonenum(uRRequest.getPhonenum()).build();

        em.persist(user);
    }
    public User findOne(String id) {
        return em.find(User.class, id);

    }
}
