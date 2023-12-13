//package soloproject.seomoim.security.oauth;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.oauth2.core.user.OAuth2User;
//import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
//import soloproject.seomoim.member.entity.Member;
//import soloproject.seomoim.member.service.MemberService;
//import soloproject.seomoim.security.FormLogin.CustomAuthorityUtils;
//
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.util.List;
//
//
//@RequiredArgsConstructor
//public class Oauth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
//    private final CustomAuthorityUtils authorityUtils;
//    private final MemberService memberService;
//
//    @Override
//    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
//        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
//        String email = String.valueOf(oAuth2User.getAttributes().get("email"));
//        List<String> roles = authorityUtils.createRoles(email);
//
//    }
//
//    private void saveMember(String email){
//        Member member = new Member();
//        member.setEmail(email);
//        memberService.saveMember(member);
//    }
//}


