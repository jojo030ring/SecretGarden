package com.yezi.secretgarden.service;

import com.yezi.secretgarden.domain.User;
import com.yezi.secretgarden.domain.UserRegisterRequest;
import com.yezi.secretgarden.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    @Transactional
    public void registerUser(UserRegisterRequest userRegisterRequest) {
        User user = User.builder().id(userRegisterRequest.getId())
                .name(userRegisterRequest.getName())
                .email(userRegisterRequest.getEmail_id()+"@"+userRegisterRequest.getUser_domain())
                .password(userRegisterRequest.getPassword())
                .phonenum(userRegisterRequest.getPhonenum()).build();
        userRepository.save(user);

    }



}
