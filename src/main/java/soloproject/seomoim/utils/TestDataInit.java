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
        post.setStartedAt(LocalDate.now());
        post.setTitle("안녕하세요 ㅎㅎㅎ");
        post.setContent("제발 채팅 구현하게 해주세요");
        post.setPlaceName("고바우");
        post.setPlaceAddress("서울 구로구 부일로1길 9");
        post.setTotalParticipantCount(10);
        Moim moim = mapper.moimPostDtoToMoim(post);

        moimService.createMoim(member2Id,moim);

    }
}
