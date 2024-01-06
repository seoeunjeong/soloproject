package soloproject.seomoim.security.oauth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import soloproject.seomoim.member.entity.Member;
import soloproject.seomoim.member.repository.MemberRepository;
import soloproject.seomoim.member.profileImage.ProfileImage;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Slf4j
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final MemberRepository memberRepository;
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException{

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        log.info("authorities={}", authorities);

        List<GrantedAuthority> updatedAuthorities = authorities.stream()
                .map(authority -> {
                    if (authority.getAuthority().equals("ROLE_USER")) {
                        return new SimpleGrantedAuthority("ROLE_AUTH_USER");
                    } else {
                        return authority;
                    }
                })
                .collect(Collectors.toList());


        OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();

        String email = oauth2User.getAttribute("email");
        String name = oauth2User.getAttribute("name");
        String picture = oauth2User.getAttribute("picture");


        Optional<Member> optionalMember = memberRepository.findByEmail(email);

        if (optionalMember.isEmpty()) {
            Member memberDto = createMemberDto(email, name, picture);
            memberRepository.save(memberDto);
        }else{
            Member member = optionalMember.get();
            member.setName(name);
            ProfileImage profileImage = new ProfileImage();
            profileImage.setProfileImageUrl(picture);
            member.setProfileImage(profileImage);
        }

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(authentication.getPrincipal(), authentication.getCredentials(), updatedAuthorities);

        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

        response.sendRedirect("/");

    }

    private Member createMemberDto(String email, String name, String picture) {
        Member member = new Member();
        member.setName(name);
        member.setEmail(email);
        member.setRoles(List.of("AUTH_USER"));
        ProfileImage profileImage = new ProfileImage();
        profileImage.setProfileImageUrl(picture);
        UUID uuid = UUID.randomUUID();
        profileImage.setUuid(uuid.toString());
        member.setProfileImage(profileImage);
        return member;
    }
}
