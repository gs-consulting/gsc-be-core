package jp.co.goalist.gsc.configurations;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import jp.co.goalist.gsc.middlewares.ApiKeyAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class CrawlerSecurityConfig {
    
    @Value("${gsc-be-core.crawler.api-key}")
    private String crawlerApiKey;

    @Bean
    public SecurityFilterChain crawlerFilterChain(HttpSecurity http) throws Exception {
        return http
            .securityMatcher("/crawler/**")
            .addFilterBefore(new ApiKeyAuthenticationFilter(crawlerApiKey), 
                           UsernamePasswordAuthenticationFilter.class)
            .authorizeHttpRequests(authz -> authz
                .anyRequest().authenticated()
            )
            .csrf(AbstractHttpConfigurer::disable)
            .build();
    }
}
