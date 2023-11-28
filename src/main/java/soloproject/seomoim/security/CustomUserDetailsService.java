package soloproject.seomoim.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import soloproject.seomoim.exception.BusinessLogicException;
import soloproject.seomoim.exception.ExceptionCode;
import soloproject.seomoim.member.entity.Member;
import soloproject.seomoim.member.repository.MemberRepository;

import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;
    private final CustomAuthorityUtils customAuthorityUtils;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        log.info("username=" + username);
        Optional<Member> optionalMember = memberRepository.findByEmail(username);
        log.info("optionalMember=" + optionalMember);

        Member findMember = optionalMember.orElseThrow(() -> new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND));

        return new CustomUserDetails(findMember,customAuthorityUtils);
    }

}
