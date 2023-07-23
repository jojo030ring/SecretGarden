package com.yezi.secretgarden;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class SecretgardenApplication {

	public static void main(String[] args) {
		SpringApplication.run(SecretgardenApplication.class, args);
	}
	// 패스워드 암호화를 위한
	@Bean // 해당 메서드의 리턴되는 메서드를 bean으로 등록
	public BCryptPasswordEncoder encodePwd() {
		return new BCryptPasswordEncoder();
	}
}
