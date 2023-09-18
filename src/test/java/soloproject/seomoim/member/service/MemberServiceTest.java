package soloproject.seomoim.member.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import soloproject.seomoim.member.domain.Member;
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
        member.setEmail("dmswjd4015@naver.com");
        member.setPassword("1111");

        //when
        Long signupId = memberService.signup(member);
        //then

        memberRepository.findById(signupId);

        assertThat(member.getId()).isEqualTo(signupId);
    }

    @Test
    public void 아이디중복검사테스트() throws Exception{
    //given
        Member member1 = new Member();
        member1.setEmail("dmswjd4015@naver.com");

        Member member2 = new Member();
        member2.setEmail("dmswjd4015@naver.com");

        memberService.signup(member1);
        //when
        //then
        assertThrows(IllegalStateException.class,()->memberService.signup(member2));
    }

    @Test
    public void 회원정보업데이트테스트() throws Exception{
    //given
        Member member = new Member();
        member.setEmail("dmswjd4015@naver.com");
        member.setPassword("1111");
        Long signupId = memberService.signup(member);
        //when
        Member member1 = memberService.findMember(signupId);
        member1.setName("서은정");
        member1.setGender("여성");
        member1.setAge(33);

        //then
        assertThat(member.getName()).isEqualTo("서은정");
        assertThat(member.getRegion()).isNull();
        assertThat(member.getGender()).isEqualTo("여성");
        assertThat(member.getAge()).isEqualTo(33);
    }

    @Test
    public void 회원삭제테스트() throws Exception{
    //given
        Member member = new Member();
        member.setEmail("dmswjd4015@naver.com");
        member.setPassword("1111");
        Long signupId = memberService.signup(member);
        //when
        memberService.delete(signupId);

        //then
        assertThrows(IllegalStateException.class,()->memberService.findMember(signupId));
    }

}