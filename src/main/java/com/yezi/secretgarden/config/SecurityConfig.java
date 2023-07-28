package com.yezi.secretgarden.config;

import com.yezi.secretgarden.auth.AuthenticationEntryPoint;
import com.yezi.secretgarden.auth.AuthorizationAccessDeniedHandler;
import com.yezi.secretgarden.auth.LoginFailHandler;
import com.yezi.secretgarden.jwt.JwtAuthenticationFilter;
import com.yezi.secretgarden.jwt.JwtAuthorizationFilter;
import com.yezi.secretgarden.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.nio.file.Path;

//@Configuration
//@EnableWebSecurity // 스프링 시큐리티 필터가 스프링 필터 체인에 등록이 됨
//@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true) // secured annotation 활성화 / pre + postAuthorize 어노테이션 활성화
//public class SecurityConfig extends WebSecurityConfigurerAdapter {
//    @Autowired
//    private CorsConfig corsConfig;
//    @Autowired
//    private UserService userService;
//    @Autowired
//    LoginFailHandler loginFailHandler;
//    protected void configure(HttpSecurity http) throws Exception {
//
//
//    //        http
//    //
//    //
//    //                .addFilter(corsConfig.corsFilter())
//    //                .csrf().disable()
//    //                // 세션 사용하지 않음 > jwt 사용하지 않을 경우엔 빼주어야 함
//    //                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//    //                .and()
//    //
//    //                .formLogin().disable()
//    //                .httpBasic().disable()
//    //
//    //
//    //
//    //                /*
//    //                   크로스 오리진 요청이 와도 다 허용됨 @CrossOrigin(인증 x) / 시큐리티 필터에 등록 인증(O)
//    //                 * Spring Security 사용시 CORS 설정을 하기 위해서 Authentication Filter 인증보다 앞에 필터를 추가해주어야 한다.
//    //                 */
//    //                .authorizeRequests()
//    //                .antMatchers("/diary/**","/board/**", "/","/post/**")
//    //                .access("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')")
//    //                .anyRequest().permitAll()
//    //                .and()
//    //                .addFilterAt(new JwtAuthenticationFilter(authenticationManager()), UsernamePasswordAuthenticationFilter.class) // 파라미터가 하나 있는데 .. AuthenticationManager임 근데 이건 WebSecurityConfigurerAdapter에 들어있어서 메서드만 호출해주면 됨
//    //                .addFilterAt(new JwtAuthorizationFilter(authenticationManager()), BasicAuthenticationFilter.class)
//    //                .exceptionHandling()
//    //                .accessDeniedHandler(new AuthorizationAccessDeniedHandler())
//    //                .authenticationEntryPoint(new AuthenticationEntryPoint())
//    //                .and()
//    //                .logout()
//    //                .logoutUrl("/logout")
//    //                .deleteCookies("token");
//    //
//    //
//        JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(authenticationManager());
//
//        JwtAuthorizationFilter jwtAuthorizationFilter = new JwtAuthorizationFilter(authenticationManager());
//
//        http
//
//
//                .addFilter(corsConfig.corsFilter())
//                .csrf().disable()
//                // 세션 사용하지 않음 > jwt 사용하지 않을 경우엔 빼주어야 함
//                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
//                .and()
//
//                .formLogin().disable()
//                .httpBasic().disable()
//
//
//
//                /*
//                   크로스 오리진 요청이 와도 다 허용됨 @CrossOrigin(인증 x) / 시큐리티 필터에 등록 인증(O)
//                 * Spring Security 사용시 CORS 설정을 하기 위해서 Authentication Filter 인증보다 앞에 필터를 추가해주어야 한다.
//                 */
//                /*
//                 *   3번 설명 보자...
//                 */
//
//                .authorizeRequests()
//                .antMatchers("/diary/**","/board/**", "/","/post/**")
//                .access("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')")
//                .anyRequest().permitAll()
//                .and()
//                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class) // 파라미터가 하나 있는데 .. AuthenticationManager임 근데 이건 WebSecurityConfigurerAdapter에 들어있어서 메서드만 호출해주면 됨
//                .addFilterAt(jwtAuthorizationFilter, BasicAuthenticationFilter.class)
//                .exceptionHandling()
//                .accessDeniedHandler(new AuthorizationAccessDeniedHandler())
//                .authenticationEntryPoint(new AuthenticationEntryPoint())
//                .and()
//                .logout()
//                .logoutUrl("/logout")
//                .logoutSuccessUrl("/login")
//                .deleteCookies("token");
//
//
//    }
//
//    @Override
//    public void configure(WebSecurity web) throws Exception {
//
//        web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
//        web.ignoring().antMatchers("/docs/5.0/assets/brand/bootstrap-logo.svg","/img/**");
//
//
//    }
//
//}
