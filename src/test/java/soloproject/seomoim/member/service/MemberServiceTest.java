package soloproject.seomoim.member.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import soloproject.seomoim.advice.exception.BusinessLogicException;
import soloproject.seomoim.member.entity.Member;
import soloproject.seomoim.member.repository.MemberRepository;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberServiceTest {

    @Autowired MemberService memberService;
    @Autowired MemberRepository memberRepository;

    @Test
    public void 회원가입테스트() throws Exception{
        //given
        Member member = new Member();
        member.setEmail("dmswjd@naver.com");
        member.setPassword("dkssud1!");
        member.setConfirmPassword("dkssud1!");

        //when
        Long signupId = memberService.signup(member);

        //then
        Member findMember = memberRepository.findById(signupId).get();

        assertThat(findMember.getId()).isEqualTo(signupId);
    }

    @Test
    public void 아이디중복검사테스트() throws Exception {
        //given
        Member member1 = new Member();
        member1.setEmail("dmswjd@naver.com");
        member1.setPassword("1111");
        member1.setConfirmPassword("1111");


        Member member2 = new Member();
        member2.setEmail("dmswjd@naver.com");
        member2.setPassword("1111");
        member2.setConfirmPassword("1111");
        //when
        memberService.signup(member1);

        //then
        assertThrows(BusinessLogicException.class, () -> memberService.signup(member2));
    }

    @Test
    public void 회원정보업데이트테스트() throws Exception{
    //given
        Member member = new Member();
        member.setEmail("dmswjd@naver.com");
        member.setPassword("1111");
        member.setConfirmPassword("1111");
        Long signupId = memberService.signup(member);
        //when
        Member member1 = memberService.findMember(signupId);
        member1.setName("서은정");
        member1.setGender('여');
        member1.setAge(33);

        //then
        assertThat(member.getName()).isEqualTo("서은정");
        assertThat(member.getAddress()).isNull();
        assertThat(member.getGender()).isEqualTo('여');
        assertThat(member.getAge()).isEqualTo(33);
    }

    @Test
    public void 회원삭제테스트() throws Exception{
        //given
        Member member = new Member();
        member.setEmail("dmswjd@naver.com");
        member.setPassword("1111");
        member.setConfirmPassword("1111");
        Long signupId = memberService.signup(member);
        //when
        memberService.delete(signupId);

        //then
        assertThrows(BusinessLogicException.class,()->memberService.findMember(signupId));
    }

}