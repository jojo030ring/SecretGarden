package com.yezi.secretgarden.config;

import com.yezi.secretgarden.auth.LoginFailHandler;
import com.yezi.secretgarden.jwt.JwtAuthenticationFilter;
import com.yezi.secretgarden.jwt.JwtAuthorizationFilter;
import com.yezi.secretgarden.jwt.TokenProvider;
import com.yezi.secretgarden.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.SecurityExpressionHandler;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
@EnableWebSecurity // 스프링 시큐리티 필터가 스프링 필터 체인에 등록이 됨
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true) // secured annotation 활성화 / pre + postAuthorize 어노테이션 활성화
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private CorsConfig corsConfig;
    @Autowired
    private UserService userService;
    @Autowired
    LoginFailHandler loginFailHandler;
    protected void configure(HttpSecurity http) throws Exception {

        JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(authenticationManager());
        jwtAuthenticationFilter.setFilterProcessesUrl("/secretgarden/login");
        jwtAuthenticationFilter.setAuthenticationFailureHandler(new LoginFailHandler());
        http


                .addFilter(corsConfig.corsFilter())
                .csrf().disable()
                // 세션 사용하지 않음 > jwt 사용하지 않을 경우엔 빼주어야 함
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()

                .formLogin().disable()
                .httpBasic().disable()



                /*
                   크로스 오리진 요청이 와도 다 허용됨 @CrossOrigin(인증 x) / 시큐리티 필터에 등록 인증(O)
                 * Spring Security 사용시 CORS 설정을 하기 위해서 Authentication Filter 인증보다 앞에 필터를 추가해주어야 한다.
                 */
                /*
                 *   3번 설명 보자...
                 */

                .authorizeRequests()
                .antMatchers("/secretgarden/login", "/secretgarden/register").permitAll()
                .antMatchers("/secretgarden/diary/**","/secretgarden/board/**", "/secretgarden/")
                .access("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')")

                .and()
                .addFilterBefore(new JwtAuthenticationFilter(authenticationManager()), UsernamePasswordAuthenticationFilter.class) // 파라미터가 하나 있는데 .. AuthenticationManager임 근데 이건 WebSecurityConfigurerAdapter에 들어있어서 메서드만 호출해주면 됨
                .addFilterAt(new JwtAuthorizationFilter(authenticationManager(),userService), BasicAuthenticationFilter.class);


        // http.formLogin().loginPage("/secretgarden/login").loginProcessingUrl("/secretgarden/login").disable(); >> form login을 사용하지 않기 때문에 새로운 필터를 만들어야 함
/////// jwt 안 쓸 때의 스프링 시큐리티 ///////
//        http.authorizeRequests().antMatchers("/secretgarden/").authenticated()
////                .antMatchers("/secretgarden/manager/**").hasAuthority("ROLE_MANAGER")
////                .antMatchers("/secretgarden/admin/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_MANAGER")
////                .anyRequest().permitAll()
//                .antMatchers("/secretgarden/board/**","/secretgarden/diary/**").authenticated()
//                .and()
//                .formLogin()
//                .loginPage("/secretgarden/login") // 권한이 없는 경우 로그인 페이지로 자동으로 이동시킴
//                .usernameParameter("id") // PrincipalDetailsService에서 load... 함수에 들어가는 username 파라미터의 이름을 id로 정해줌
//                .loginProcessingUrl("/secretgarden/login") // login 주소가 호출이 되면 시큐리티가 낚아채서 대신 로그인을 진행해줌 >> controller에 /login 적어주지 않아도 됨
//                .defaultSuccessUrl("/secretgarden/") // 로그인에 성공하면 /로 이동
//                .failureUrl("/secretgarden/register");

    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/templates/**", "/static/**", "secretgarden/register");
    }

}
