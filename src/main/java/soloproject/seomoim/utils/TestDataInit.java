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
    private final MoimService moimService;
    private final MoimMapper mapper;

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

        MoimDto.Post post = new MoimDto.Post();
        post.setMoimCategory(MoimCategory.EXERCISE);
        post.setContent("오늘 8시에 같이 런닝해요");
        post.setStartedAt(LocalDate.of(2023,12,20));
        post.setTitle("운동할사람 모집");
        post.setPlaceAddress("경기 광명시 가락골길 7");
        post.setTotalParticipantCount(8);
        post.setMemberId(id);
        Moim moim = mapper.moimPostDtoToMoim(post);
        moimService.createMoim(id,moim);

        MoimDto.Post post1 = new MoimDto.Post();
        post1.setMoimCategory(MoimCategory.STUDY);
        post1.setContent("같이 코딩 공부하실분");
        post1.setStartedAt(LocalDate.of(2023,12,22));
        post1.setTitle("스터티 모임원 구해요");
        post1.setPlaceAddress("경기 광명시 가락골길 7");
        post1.setTotalParticipantCount(2);
        post1.setMemberId(id);
        Moim moim1 = mapper.moimPostDtoToMoim(post1);
        moimService.createMoim(id,moim1);

        MoimDto.Post post2 = new MoimDto.Post();
        post2.setMoimCategory(MoimCategory.DRINK);
        post2.setContent("맛있는거에 한잔 캬");
        post2.setStartedAt(LocalDate.of(2023,12,13));
        post2.setTitle("치킨드실분들");
        post2.setPlaceAddress("경기 가평군 설악면 가마소길 7");
        post2.setTotalParticipantCount(2);
        post2.setMemberId(id);
        Moim moim2 = mapper.moimPostDtoToMoim(post2);
        moimService.createMoim(id,moim2);
//
//        Moim moim3 = new Moim();
//        moim3.setMoimCategory(MoimCategory.TRAVEL);
//        moim3.setContent("힘내봅시다 파이팅");
//        moim3.setStartedAt(LocalDateTime.of(2023,12,13,05,00));
//        moim3.setTitle("관악산 등산 가실분");
//        moim3.setRegion("서울 관악구 과천대로 851 (한울아파트)");
//        moim3.setTotalParticipantCount(4);
//        moimService.createMoim(id,moim3);
//
//        Moim moim4 = new Moim();
//        moim4.setMoimCategory(MoimCategory.STUDY);
//        moim4.setContent("취준 면접스터디 같이해요 ");
//        moim4.setStartedAt(LocalDateTime.of(2023,12,14,03,00));
//        moim4.setTitle("취준 면접스터디 ");
//        moim4.setRegion("인천 연수구 경원대로 지하 285 (원인재역)");
//        moim4.setTotalParticipantCount(8);
//        moimService.createMoim(id,moim4);

    }
}
