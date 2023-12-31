package soloproject.seomoim.security;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.oauth2.client.CommonOAuth2Provider;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.web.SecurityFilterChain;
import soloproject.seomoim.member.repository.MemberRepository;
import soloproject.seomoim.security.oauth.OAuth2SuccessHandler;



@Configuration
@RequiredArgsConstructor
public class SecurityConfiguration {

    @Value("${spring.security.oauth2.client.registration.google.clientId}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.google.clientSecret}")
    private String clientSecret;

    private final MemberRepository memberRepository;

    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
        http
                .headers(headers -> headers.frameOptions().sameOrigin())
                .csrf().disable()
                .formLogin(login -> login
                        .loginPage("/login-form")
                        .loginProcessingUrl("/process_login")
                        .defaultSuccessUrl("/", true)
                        .failureUrl("/login-form?error"))
                .oauth2Login(oauth2 -> oauth2
                        .successHandler(new OAuth2SuccessHandler(memberRepository)))
                .authorizeHttpRequests(authorize -> authorize
                        .antMatchers("/img/**", "/css/**","/auth/google","/login-form", "/signup-form",
                                "/email/**","/moims/search/**").permitAll()
                        .antMatchers("/moims/post/**").hasRole("AUTH_USER")
                        .antMatchers(HttpMethod.GET, "/moims/**").permitAll()
                        .antMatchers(HttpMethod.POST, "/moims/**").hasRole("AUTH_USER")
                        .antMatchers("/members").permitAll()
                        .anyRequest().authenticated());

        return http.build();
    }


    @Bean
    public PasswordEncoder passwordEncoder() {

        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public ClientRegistrationRepository clientRegistrationRepository() {
        var clientRegistration = clientRegistration();

        return new InMemoryClientRegistrationRepository(clientRegistration);
    }

    private ClientRegistration clientRegistration() {
        return CommonOAuth2Provider
                .GOOGLE
                .getBuilder("google")
                .clientId(clientId)
                .clientSecret(clientSecret)
                .build();
    }

}
