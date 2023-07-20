package com.yezi.secretgarden.service;

import com.yezi.secretgarden.domain.User;
import com.yezi.secretgarden.domain.UserRegisterRequest;
import com.yezi.secretgarden.exception.InValidPwException;
import com.yezi.secretgarden.exception.InValidRegisterUserException;
import com.yezi.secretgarden.repository.ModifyRegisterRequest;
import com.yezi.secretgarden.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder pwdEncoder;
    /**
     * 회원가입시 사용하는 메서드
     * @param userRegisterRequest : 사용자로부터 받은 폼 데이터를 따로 클래스로 만들어주었음
     * 
     */
    @Transactional
    public void registerUser(UserRegisterRequest userRegisterRequest) {
        String rawPassword = userRegisterRequest.getPassword();
        String encPassword = pwdEncoder.encode(rawPassword); // 패스워드 암호화
        User user = User.builder().id(userRegisterRequest.getId())
                .name(userRegisterRequest.getName())
                .email(userRegisterRequest.getEmail_id()+"@"+userRegisterRequest.getUser_domain())
                .password(encPassword)
                .phonenum(userRegisterRequest.getPhonenum()).build();
        user.setRole("ROLE_USER");
        userRepository.save(user);

    }

    /**
     * 회원가입, 로그인 등에서 유저 정보를 확인할 때 사용하는 메서드
     * @param user_id
     * @return
     */
    @Transactional
    public User findUser(String user_id) {
        return userRepository.findOne(user_id);
    }

    /**
     *
     */
    @Transactional
    public void modifyUser(ModifyRegisterRequest modifyRegisterRequest, String id) throws InValidPwException {
        // 변경 감지 기능을 사용하여 수정작업을 진행
        // 영속성 컨텍스트로 등록
        User user = findUser(id);
        String previousPw = user.getPassword();
        if(previousPw.equals(modifyRegisterRequest.getPw())) {
            throw new InValidPwException();
        }
        user.setEmail(modifyRegisterRequest.getEmail_id()+"@"+modifyRegisterRequest.getUser_domain());
        user.setPhonenum(modifyRegisterRequest.getPhonenum());
        // transaction이 끝나고 commit되면 자동으로 업데이트 됨



    }




}
