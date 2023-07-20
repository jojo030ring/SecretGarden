package com.yezi.secretgarden.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity // 스프링 시큐리티 필터가 스프링 필터 체인에 등록이 됨
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true) // secured annotation 활성화 / pre + postAuthorize 어노테이션 활성화

public class SecurityConfig extends WebSecurityConfigurerAdapter {

    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.authorizeRequests().antMatchers("/secretgarden/").authenticated()
//                .antMatchers("/secretgarden/manager/**").hasAuthority("ROLE_MANAGER")
//                .antMatchers("/secretgarden/admin/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_MANAGER")
//                .anyRequest().permitAll()
                .antMatchers("/secretgarden/board/**","/secretgarden/diary/**").authenticated()
                .and()
                .formLogin()
                .loginPage("/secretgarden/login") // 권한이 없는 경우 로그인 페이지로 자동으로 이동시킴
                .usernameParameter("id") // PrincipalDetailsService에서 load... 함수에 들어가는 username 파라미터의 이름을 id로 정해줌
                .loginProcessingUrl("/secretgarden/login") // login 주소가 호출이 되면 시큐리티가 낚아채서 대신 로그인을 진행해줌 >> controller에 /login 적어주지 않아도 됨
                .defaultSuccessUrl("/secretgarden/") // 로그인에 성공하면 /로 이동
                .failureUrl("/secretgarden/register");
    }
    // 패스워드 암호화를 위한
    @Bean // 해당 메서드의 리턴되는 메서드를 bean으로 등록
    public BCryptPasswordEncoder encodePwd() {
        return new BCryptPasswordEncoder();
    }
}
