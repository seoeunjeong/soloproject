package soloproject.seomoim.security.oauth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import soloproject.seomoim.member.entity.Member;
import soloproject.seomoim.member.repository.MemberRepository;
import soloproject.seomoim.profileImage.ProfileImage;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Slf4j
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final MemberRepository memberRepository;
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        // 새로운 권한을 추가하거나 기존 권한을 변경
        List<GrantedAuthority> updatedAuthorities = authorities.stream()
                .map(authority -> {
                    if ("SCOPE_https://www.googleapis.com/auth/userinfo.email".equals(authority.getAuthority())) {
                        // 예시: "SCOPE_https://www.googleapis.com/auth/userinfo.email" 권한을 "ROLE_ADMIN"으로 변경
                        return new SimpleGrantedAuthority("ROLE_AUTH_USER");
                    } else {
                        return authority;
                    }
                })
                .collect(Collectors.toList());


        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(authentication.getPrincipal(), authentication.getCredentials(), updatedAuthorities);

        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);


        OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();

        String email = (String) oauth2User.getAttribute("email");
        String name = (String) oauth2User.getAttribute("name");
        String picture = (String) oauth2User.getAttribute("picture");

        log.info("email={}", email.toString());
        log.info("name={}", name.toString());
        log.info("picture={}", picture.toString());

        Optional<Member> optionalMember = memberRepository.findByEmail(email);

        if (!optionalMember.isPresent()) {
            Member memberDto = createMemberDto(email, name, picture);
            memberRepository.save(memberDto);
        }else{
            Member member = optionalMember.get();
            member.setName(name);
            ProfileImage profileImage = new ProfileImage();
            profileImage.setProfileImageUrl(picture);
            profileImage.setMember(member);
            member.setProfileImage(profileImage);
        }

        HttpSession session = request.getSession(true);
        session.setAttribute("oauthSession","11111111111");
        response.sendRedirect("/");

    }

    private Member createMemberDto(String email, String name, String picture) {
        Member member = new Member();
        member.setName(name);
        member.setEmail(email);
        ProfileImage profileImage = new ProfileImage();
        profileImage.setProfileImageUrl(picture);
        member.setProfileImage(profileImage);
        member.setRoles(List.of("AUTH_USER"));
        return member;
    }
}
