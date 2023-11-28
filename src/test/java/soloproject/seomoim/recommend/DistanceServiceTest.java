package soloproject.seomoim.recommend;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import soloproject.seomoim.member.entity.Member;
import soloproject.seomoim.member.service.MemberService;
import soloproject.seomoim.moim.entitiy.Moim;
import soloproject.seomoim.moim.entitiy.MoimCategory;
import soloproject.seomoim.moim.service.MoimService;

import java.util.List;


@SpringBootTest
class DistanceServiceTest {

    @Autowired
    MoimService moimService;
    @Autowired
    MemberService memberService;

    @Autowired
    DistanceService distanceService;

    @Commit
    @Test
    public void findNearbyMoims() throws Exception{
        //given
        Member member = new Member();
        member.setEmail("dmswl@gmail.com");
        member.setPassword("dkssud1!");
        member.setConfirmPassword("dkssud1!");
        member.setName("서은정");
        Long member1Id = memberService.signup(member);

        Moim moim = new Moim();
        moim.setTitle("안녕하세요 ㅎㅎㅎㅎㅎㅎ");
        moim.setContent("방가워욯ㅎㅎㅎㅎㅎㅎㅎㅎㅎㅎ");
        moim.setMoimCategory(MoimCategory.EXERCISE);
        moim.setRegion("서울 구로구 온수동 45-32");
        Long moim1 = moimService.createMoim(member1Id, moim);
        //when

        Member member2 = new Member();
        member2.setEmail("wjdals@gmail.com");
        member2.setPassword("dkssud1!");
        member2.setConfirmPassword("dkssud1!");
        member2.setName("서정민");
        Long member2Id = memberService.signup(member2);


        member2.setAddress("부산 남구 감만동 504-2");
        memberService.update(member2Id,member2);

        //then
        Member member1 = memberService.findMember(member2Id);
        List<Moim> nearbyMoims = distanceService.findNearbyMoims(member1);

        Assertions.assertThat(nearbyMoims).isEmpty();
    }

}