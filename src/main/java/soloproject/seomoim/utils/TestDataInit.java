package soloproject.seomoim.utils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import soloproject.seomoim.member.entity.Member;
import soloproject.seomoim.member.repository.MemberRepository;

import javax.annotation.PostConstruct;


@Component
@RequiredArgsConstructor
@Slf4j
public class TestDataInit {

    private final MemberRepository memberRepository;

    @PostConstruct
    public void init(){
        Member member = new Member();
        member.setEmail("test@gmail.com");
        member.setPassword("test1!");
        Member save = memberRepository.save(member);
        log.info("savedMember={}",save);
    }
}
