package soloproject.seomoim.login;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import soloproject.seomoim.member.entity.Member;
import soloproject.seomoim.member.repository.MemberRepository;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final MemberRepository memberRepository;

    /* return null이면 로그인실패 */
    public Member login(String loginId,String password){
        return memberRepository.findByEmail(loginId)
                .filter(m->m.getPassword().equals(password))
                .orElse(null);
    }
}
