package soloproject.seomoim.utils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import soloproject.seomoim.member.entity.Member;
import soloproject.seomoim.member.service.MemberService;
import soloproject.seomoim.moim.dto.MoimDto;
import soloproject.seomoim.moim.entitiy.Moim;
import soloproject.seomoim.moim.entitiy.MoimCategory;
import soloproject.seomoim.moim.mapper.MoimMapper;
import soloproject.seomoim.moim.service.MoimService;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


@Component
@RequiredArgsConstructor
@Slf4j
public class TestDataInit {

    private final MemberService memberService;

    @PostConstruct
    public void init() throws Exception {
        Member member = new Member();
        member.setEmail("dmswjd4015@naver.com");
        member.setPassword("dkssud1!");
        member.setConfirmPassword("dkssud1!");
        member.setName("서은정");
        Long id = memberService.signup(member);

        Member member2 = new Member();
        member2.setEmail("2juillet.acc@gmail.com");
        member2.setPassword("dkssud1!");
        member2.setConfirmPassword("dkssud1!");
        member2.setName("서은정");
        Long member2Id = memberService.signup(member2);
        Member member3 = new Member();
        member3.setRoles(List.of("AUTH_USER"));
        memberService.update(member2Id,member3);

    }
}
