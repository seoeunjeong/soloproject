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
    public void init(){
        Member member = new Member();
        member.setEmail("dmswjd4015@naver.com");
        member.setPassword("dkssud1!");
        member.setConfirmPassword("dkssud1!");
        member.setName("서은정");
        memberService.signup(member);

        Member member2 = new Member();
        member2.setEmail("2juillet.acc@gmail.com");
        member2.setPassword("dkssud1!");
        member2.setConfirmPassword("dkssud1!");
        member2.setName("서은정");
        Long memberId2 = memberService.signup(member2);

        Member member3 = new Member();
        member3.setRoles(List.of("AUTH_USER"));
        memberService.update(memberId2,member3);


        MoimDto.Post post = new MoimDto.Post();
        post.setMoimCategory(MoimCategory.EAT);
        post.setStartedAt(LocalDateTime.now());
        post.setTitle("고기먹자ㅎㅎㅎㅎ");
        post.setContent("삼겹살 한우 차돌박이 !!!!!!!!");
        post.setPlaceName("고바우");
        post.setPlaceAddress("서울 구로구 부일로1길 9");
        post.setTotalParticipantCount(5);
        moimService.createMoim(memberId2, mapper.moimPostDtoToMoim(post));

        MoimDto.Post post2 = new MoimDto.Post();
        post2.setMoimCategory(MoimCategory.STUDY);
        post2.setStartedAt(LocalDateTime.of(2024, 1, 8, 19, 30));
        post2.setTitle("코딩테스트 공부");
        post2.setContent("모여서 각자 공부!");
        post2.setPlaceName("스타벅스 역곡역DT점");
        post2.setPlaceAddress("경기 부천시 소사구 경인로 485");
        post2.setTotalParticipantCount(3);
        moimService.createMoim(memberId2, mapper.moimPostDtoToMoim(post2));

        MoimDto.Post post3 = new MoimDto.Post();
        post3.setMoimCategory(MoimCategory.EXERCISE);
        post3.setStartedAt(LocalDateTime.of(2024, 1, 10, 20, 00));
        post3.setTitle("헬스 필라테스 ㅎㅎㅎㅎ");
        post3.setContent("하체 뿌시기!!!");
        post3.setPlaceName("스포애니 역곡역점");
        post3.setPlaceAddress("경기 부천시 소사구 경인로 527");
        post3.setTotalParticipantCount(5);
        moimService.createMoim(memberId2, mapper.moimPostDtoToMoim(post3));

        
    }
}
