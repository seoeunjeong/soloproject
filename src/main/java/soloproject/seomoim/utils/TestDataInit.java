package soloproject.seomoim.utils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import soloproject.seomoim.member.entity.Member;
import soloproject.seomoim.member.repository.MemberRepository;
import soloproject.seomoim.member.service.MemberService;
import soloproject.seomoim.moim.entitiy.Moim;
import soloproject.seomoim.moim.entitiy.MoimCategory;
import soloproject.seomoim.moim.repository.MoimRepository;

import javax.annotation.PostConstruct;


@Component
@RequiredArgsConstructor
@Slf4j
public class TestDataInit {

    private final MemberService memberService;
    private final MoimRepository moimRepository;

    @PostConstruct
    public void init(){
        Member member = new Member();
        member.setEmail("dmswjd4015@naver.com");
        member.setPassword("dkssud1!");
        member.setConfirmPassword("dkssud1!");
        member.setName("서은정");
        memberService.signup(member);

        Moim moim = new Moim();
        moim.setMember(member);
        moim.setMoimCategory(MoimCategory.EXERCISE);
        moim.setContent("오늘 8시에 같이 런닝해요");
        moim.setTitle("운동할사람 모집");
        moim.setRegion("온수역");
        moim.setTotalParticipantCount(8);
        moimRepository.save(moim);

    }
}
