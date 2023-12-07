package soloproject.seomoim.security;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.oauth2.client.CommonOAuth2Provider;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.web.SecurityFilterChain;
import soloproject.seomoim.member.service.MemberService;
import soloproject.seomoim.security.FormLogin.CustomAuthorityUtils;
import soloproject.seomoim.security.oauth.Oauth2SuccessHandler;


@Configuration
@RequiredArgsConstructor
public class SecurityConfiguration {

    @Value("${spring.security.oauth2.client.registration.google.clientId}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.google.clientSecret}")
    private String clientSecret;

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
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID"))
//                .oauth2Login(oauth2Login -> oauth2Login
//                        .defaultSuccessUrl("/", true))
                .authorizeHttpRequests(authorize -> authorize
                        .antMatchers("/img/**", "/css/**","/auth/google","/login-form", "/signup-form", "/email/**", "/members/**").permitAll()
                        .antMatchers("/moims/post/**").hasRole("AUTH_USER")
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

    @Bean
    public ClientRegistrationRepository clientRegistrationRepository() {
        var clientRegistration = clientRegistration();

        return new InMemoryClientRegistrationRepository(clientRegistration);
//        나를 !메모리에저장해놓고사용
    }

/*ClientRegistration은 한마디로 OAuth 2 Client에 대한 등록 정보를 표현하는 객체 바로 나!*/
    private ClientRegistration clientRegistration() {
        // (4-1)
        return CommonOAuth2Provider
                .GOOGLE
                .getBuilder("google")
                .clientId(clientId)
                .clientSecret(clientSecret)
                .build();
    }
}
