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

    public void save(User user) {
        em.persist(user);
    }
    public User findOne(String id) {
        return em.find(User.class, id);

    }


}
