package soloproject.seomoim.utils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import soloproject.seomoim.member.entity.Member;
import soloproject.seomoim.member.service.MemberService;
import soloproject.seomoim.moim.entitiy.Moim;
import soloproject.seomoim.moim.entitiy.MoimCategory;
import soloproject.seomoim.moim.service.MoimService;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;


@Component
@RequiredArgsConstructor
@Slf4j
public class TestDataInit {

    private final MemberService memberService;
    private final MoimService moimService;

    @PostConstruct
    public void init(){
        Member member = new Member();
        member.setEmail("dmswjd4015@naver.com");
        member.setPassword("dkssud1!");
        member.setConfirmPassword("dkssud1!");
        member.setName("서은정");
        Long id = memberService.signup(member);

        Moim moim = new Moim();
        moim.setMoimCategory(MoimCategory.EXERCISE);
        moim.setContent("오늘 8시에 같이 런닝해요");
        moim.setStartedAt(LocalDateTime.now());
        moim.setTitle("운동할사람 모집");
        moim.setRegion("서울 구로구 경인로 5");
        moim.setTotalParticipantCount(8);
        moimService.createMoim(id,moim);

        Moim moim1 = new Moim();
        moim1.setMoimCategory(MoimCategory.STUDY);
        moim1.setContent("같이코딩공부하실분");
        moim1.setStartedAt(LocalDateTime.now());
        moim1.setTitle("스터티모임원구해요");
        moim1.setRegion("경기 광명시 가락골길 7");
        moim1.setTotalParticipantCount(2);
        moimService.createMoim(id,moim1);

        Moim moim2 = new Moim();
        moim2.setMoimCategory(MoimCategory.DRINK);
        moim2.setContent("맛있는거에 한잔 캬");
        moim2.setStartedAt(LocalDateTime.now());
        moim2.setTitle("치킨드실분들");
        moim2.setRegion("경기 가평군 설악면 가마소길 7");
        moim2.setTotalParticipantCount(2);
        moimService.createMoim(id,moim2);

        Moim moim3 = new Moim();
        moim3.setMoimCategory(MoimCategory.TRAVEL);
        moim3.setContent("힘내봅시다 파이팅");
        moim3.setStartedAt(LocalDateTime.now());
        moim3.setTitle("관악산등산가실분");
        moim3.setRegion("서울 관악구 과천대로 851 (한울아파트)");
        moim3.setTotalParticipantCount(4);
        moimService.createMoim(id,moim3);

        Moim moim4 = new Moim();
        moim4.setMoimCategory(MoimCategory.EXERCISE);
        moim4.setContent("오늘 8시에 같이 런닝해요");
        moim4.setStartedAt(LocalDateTime.now());
        moim4.setTitle("운동할사람 모집");
        moim4.setRegion("인천 연수구 경원대로 지하 285 (원인재역)");
        moim4.setTotalParticipantCount(8);
        moimService.createMoim(id,moim4);
/*
        카카오API 같은 주소에대한 제한 있나?
        Moim moim5 = new Moim();
        moim5.setMoimCategory(MoimCategory.EXERCISE);
        moim5.setContent("오늘 8시에 같이 런닝해요");
        moim5.setStartedAt(LocalDateTime.now());
        moim5.setTitle("운동할사람 모집");
        moim5.setRegion("온수역");
        moim5.setTotalParticipantCount(8);
        moimService.createMoim(id, moim5);
*/

    }
}
