package com.yezi.secretgarden.config;

import com.yezi.secretgarden.auth.AuthenticationEntryPoint;
import com.yezi.secretgarden.auth.AuthorizationAccessDeniedHandler;
import com.yezi.secretgarden.auth.LoginFailHandler;
import com.yezi.secretgarden.jwt.JwtAuthenticationFilter;
import com.yezi.secretgarden.jwt.JwtAuthorizationFilter;
import com.yezi.secretgarden.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true) // secured annotation 활성화 / pre + postAuthorize 어노테이션 활성화
@RequiredArgsConstructor
public class SecurityConfiguration {
        private final CorsConfig corsConfig;
        private final LoginFailHandler loginFailHandler;
        private final AuthenticationConfiguration authenticationConfiguration;
        private final UserService userService;
        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

            JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(authenticationManager(authenticationConfiguration));

            JwtAuthorizationFilter jwtAuthorizationFilter = new JwtAuthorizationFilter(authenticationManager(new AuthenticationConfiguration()));

        return http.csrf().disable()
//
                // 전통적인 방식의 form 로그인 않 함, httpBasic 방식의 로그인 안 함 >> jwt

                .httpBasic().disable()
                .formLogin().disable()
                .authorizeRequests()
                .antMatchers("/diary/**","/board/**", "/","/post/**")
                .authenticated()
                .anyRequest().permitAll()
                .and()
                // 세션 사용 안함
                .sessionManagement(sessionManagement->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                )


                .addFilterAt(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)

                .addFilterAt(jwtAuthorizationFilter, BasicAuthenticationFilter.class)

                .exceptionHandling(httpSecurityExceptionHandlingConfigurer -> {
                    httpSecurityExceptionHandlingConfigurer.accessDeniedHandler(new AuthorizationAccessDeniedHandler())
                            .authenticationEntryPoint(new AuthenticationEntryPoint());
                })
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login")
                .deleteCookies("token")
                .and()
                .build();


    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {

        // 정적 리소스 파일 및 register, login경로는 시큐리티 관리 대상에서 제외한다
        return web -> web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations()).
                antMatchers("/docs/5.0/assets/brand/bootstrap-logo.svg","/img/**","/register");

    }
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }




}
