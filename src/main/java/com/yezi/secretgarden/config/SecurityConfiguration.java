package com.yezi.secretgarden.config;

import com.yezi.secretgarden.auth.JwtAuthenticationEntryPoint;
import com.yezi.secretgarden.auth.JwtAuthorizationAccessDeniedHandler;
import com.yezi.secretgarden.jwt.JwtAuthenticationFilter;
import com.yezi.secretgarden.jwt.JwtAuthorizationFilter;
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
        private final AuthenticationConfiguration authenticationConfiguration;
        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

            JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(authenticationManager(authenticationConfiguration));
            JwtAuthorizationFilter jwtAuthorizationFilter = new JwtAuthorizationFilter(authenticationManager(new AuthenticationConfiguration()));

        return http.csrf().disable()
                .addFilter(corsConfig.corsFilter())
                .httpBasic().disable() // httpbasic 설정 disable
                .formLogin().disable() // form login 설정 disable
                .authorizeRequests()
                .antMatchers("/diary/**","/board/**", "/","/post/**")
                .hasRole("USER")
                .anyRequest().permitAll()
                .and()
                // 세션 사용 안함
                .sessionManagement(sessionManagement->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                )


                .addFilterAt(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterAt(jwtAuthorizationFilter, BasicAuthenticationFilter.class)
                .exceptionHandling(httpSecurityExceptionHandlingConfigurer -> {
                    httpSecurityExceptionHandlingConfigurer.accessDeniedHandler(new JwtAuthorizationAccessDeniedHandler())
                            .authenticationEntryPoint(new JwtAuthenticationEntryPoint());
                })
                .logout()// logout 설정
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login") // logout 시도가 성공하면 /login으로 이동
                .deleteCookies("token")  // token이라는 쿠키를 삭제함
                .and()
                .build();


    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        // 정적 리소스 파일 및 register, 시큐리티 관리 대상에서 제외한다
        return web -> web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations()).
                antMatchers("/docs/5.0/assets/brand/bootstrap-logo.svg","/img/**","/register");
    }
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }




}
