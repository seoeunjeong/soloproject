package soloproject.seomoim.security;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@RequiredArgsConstructor
public class SecurityConfiguration {

    @Value("${spring.security.oauth2.client.registration.google.clientId}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.google.clientSecret}")
    private String clientSecret;

    private final CustomOauth2UserService customOauth2UserService;

    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
        http
                .headers(headers -> headers.frameOptions().sameOrigin())
                .csrf().disable()
                .formLogin(login -> login
                        .loginPage("/login-form")
                        .defaultSuccessUrl("/", true)
                        .loginProcessingUrl("/process_login")
                        .failureUrl("/login-form?error"))
                .logout(logout -> logout
                        .logoutUrl("/members/logout")
                        .logoutSuccessUrl("/")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID"))
//                .oauth2Login(oauth2Login -> oauth2Login
//                        .loginPage("/members/loginFrom")
//                        .defaultSuccessUrl("/oauth/loginHome", true)
//                        .userInfoEndpoint(userInfo -> userInfo.userService(customOauth2UserService)))
                .authorizeHttpRequests(authorize -> authorize
                        .antMatchers("/img/**", "/css/**", "/login-form", "/signup-form", "/email/**", "/members/**").permitAll()
                        .antMatchers("/moims/postForm/**").hasRole("AUTH_USER")
                        .antMatchers("/").hasAnyRole("USER", "AUTH_USER")
                        .anyRequest().authenticated());

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    //    /*카카오 API 전송할때는 cors 상관없는건가*/
//    @Bean
//    CorsConfigurationSource corsConfigurationSource() {
//        var configuration = new CorsConfiguration();
//        configuration.setAllowedOrigins(Arrays.asList("*"));
//        configuration.setAllowedMethods(Arrays.asList("*"));
//        configuration.setAllowedHeaders(Arrays.asList("*"));
//        configuration.setExposedHeaders(Arrays.asList("Authorization"));
//
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", configuration);
//        return source;
//    }

}
