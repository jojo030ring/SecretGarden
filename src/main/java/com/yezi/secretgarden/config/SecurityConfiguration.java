//package com.yezi.secretgarden.config;
//
//import com.yezi.secretgarden.auth.AuthenticationEntryPoint;
//import com.yezi.secretgarden.auth.AuthorizationAccessDeniedHandler;
//import com.yezi.secretgarden.auth.LoginFailHandler;
//import com.yezi.secretgarden.domain.User;
//import com.yezi.secretgarden.jwt.JwtAuthenticationFilter;
//import com.yezi.secretgarden.jwt.JwtAuthorizationFilter;
//import com.yezi.secretgarden.jwt.JwtTokenUtil;
//import com.yezi.secretgarden.repository.UserRepository;
//import com.yezi.secretgarden.service.UserService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.HttpMethod;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
//import org.springframework.security.config.annotation.authentication.configuration.GlobalAuthenticationConfigurerAdapter;
//import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
//import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
//import org.springframework.web.util.WebUtils;
//
//import javax.servlet.http.Cookie;
//import java.io.IOException;
//
//@Configuration
//@EnableWebSecurity
//@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true) // secured annotation 활성화 / pre + postAuthorize 어노테이션 활성화
//@RequiredArgsConstructor
//public class SecurityConfiguration {
//    private final CorsConfig corsConfig;
//    private final LoginFailHandler loginFailHandler;
//    private final AuthenticationConfiguration authenticationConfiguration;
//    private final UserService userService;
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//
//        JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(authenticationManager(authenticationConfiguration));
//        jwtAuthenticationFilter.setFilterProcessesUrl("/secretgarden/login");
//
//        JwtAuthorizationFilter jwtAuthorizationFilter = new JwtAuthorizationFilter(authenticationManager(new AuthenticationConfiguration()));
//
//        return http.csrf().disable()
////
//                // 전통적인 방식의 form 로그인 않 함, httpBasic 방식의 로그인 안 함 >> jwt
//                .formLogin().disable()
//                .httpBasic().disable()
//                .authorizeRequests()
//                .antMatchers("/secretgarden/diary/**","/secretgarden/board/**", "/secretgarden/","/secretgarden/post/**")
//                .authenticated()
//                .anyRequest().permitAll()
//                .and()
//                // 세션 사용 안함
//                .sessionManagement(sessionManagement->
//                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//
//                )
//
//                // 로그아웃시 쿠키 삭제
////                .logout(logout-> logout.logoutUrl("/secretgarden/logout")
////                        .addLogoutHandler((request, response, authentication) -> {
////                            JwtTokenUtil util = new JwtTokenUtil();
////                            String stringToken = util.decodeFromCookieToJWT(request);
////                            Cookie token = WebUtils.getCookie(request,"token");
////                            if(stringToken != null) {
////                                token.setMaxAge(0);
////                                response.addCookie(token);
////
////                            }
////                            try {
////                                response.sendRedirect("redirect:/secretgarden/login");
////                            } catch (IOException e) {
////                                throw new RuntimeException(e);
////                            }
////
////
////                        })
////                )
//
//                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
//                .addFilterBefore(jwtAuthorizationFilter, BasicAuthenticationFilter.class)
//                .exceptionHandling(httpSecurityExceptionHandlingConfigurer -> {
//                    httpSecurityExceptionHandlingConfigurer.accessDeniedHandler(new AuthorizationAccessDeniedHandler())
//                            .authenticationEntryPoint(new AuthenticationEntryPoint());
//                })
//                .build();
//
//
//    }
//
//    @Bean
//    public WebSecurityCustomizer webSecurityCustomizer() {
//
//        // 정적 리소스 파일 및 register, login경로는 시큐리티 관리 대상에서 제외한다
//        return web -> web.ignoring().antMatchers("secretgarden/login","secretgarden/register","/docs/5.0/assets/brand/bootstrap-logo.svg","/img/**","/resource/**","/templates/**", "/static/**","resources/**", "/secretgarden/register","/secretgarden/login");
//
//    }
//    @Bean
//    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
//        authenticationConfiguration.setGlobalAuthenticationConfigurers(
//                new GlobalAuthenticationConfigurerAdapter().configure();
//
//        );
//        return authenticationConfiguration.getAuthenticationManager();
//    }
//
//
//
//
//}
