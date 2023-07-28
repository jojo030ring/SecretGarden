package com.yezi.secretgarden.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {
    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true); // 내 서버가 응답할 때 json을 자바스크립트에서 처리할 수 있게 할지를 설정
                                            // ajax를 요청하면 그 응답을 자바스크립트에서 받을 수 있게 할건지 아닌지를 결정하는데,
                                            // 활성화 되어있으면 자바스크립트로 요청을 했을 때 응답이 오지 않는다


        config.addAllowedOrigin("*");       // 모든 ip의 응답을 허용
        config.addAllowedHeader("*");       // 모든 header에 응답을 허용
        config.addAllowedMethod("*");       // 모든 method에 응답을 허용
        source.registerCorsConfiguration("/**",config);

        return new CorsFilter(source);

    }
}
