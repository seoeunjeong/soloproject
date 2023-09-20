package soloproject.seomoim.moim.service;


import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import soloproject.seomoim.member.entity.Member;
import soloproject.seomoim.member.service.MemberService;
import soloproject.seomoim.moim.dto.MoimMemberDto;
import soloproject.seomoim.moim.dto.MoimSearchDto;
import soloproject.seomoim.moim.entitiy.Moim;
import soloproject.seomoim.moim.entitiy.MoimCategory;
import soloproject.seomoim.moim.repository.MoimRepository;

import java.util.List;

@SpringBootTest
@Transactional
class MoimServiceTest {

    @Autowired MoimService moimService;
    @Autowired MoimRepository moimRepository;
    @Autowired
    MemberService memberService;


    @Test
    public void 모임등록테스트() throws Exception{
    //given
        Member member = new Member();
        member.setEmail("dmswjd4015@naver.com");

        Moim moim = new Moim();
        moim.setMember(member);
        moim.setTitle("축구할 사람");
        moim.setContent("일요일 10시에 고척에서 축구할 사람");
        moim.setMoimCategory(MoimCategory.EXERCISE);
        moim.setTotalParticipantCount(11);
        moim.setRegion("구로역");

        //when
        Moim savedMoim = moimRepository.save(moim);
        Moim findMoim = moimRepository.findById(savedMoim.getId()).get();
        //then

        Assertions.assertThat(savedMoim).isEqualTo(findMoim);
    }

    @Test
    public void 모임조회테스트() throws Exception{
        MoimSearchDto moimSearchDto = new MoimSearchDto();
        moimSearchDto.setKeyword("고척");
        //given
        Member member = new Member();
        member.setEmail("dmswjd4015@naver.com");

        memberService.signup(member);

        Moim moim = new Moim();
        moim.setMember(member);
        moim.setTitle("축구할 사람");
        moim.setContent("일요일 10시에 고척에서 축구할 사람");
        moim.setMoimCategory(MoimCategory.EXERCISE);
        moim.setTotalParticipantCount(11);
        moim.setRegion("구로역");

        //when
        Moim savedMoim = moimRepository.save(moim);
        Page<Moim> allSearch = moimService.findAllSearch(moimSearchDto, 1, 20);
        //then

        List<Moim> moims = allSearch.getContent();
        Assertions.assertThat(moims.get(0)).isEqualTo(savedMoim);
    }
}