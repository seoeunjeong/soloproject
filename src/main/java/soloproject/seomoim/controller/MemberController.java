package soloproject.seomoim.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import soloproject.seomoim.domain.Member;
import soloproject.seomoim.dto.MemberDto;
import soloproject.seomoim.service.MemberService;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {
    private final static String MEMBER_DEFAULT_URL = "/member/";

    private final MemberService memberService;

    @PostMapping("/sign-up")
    public ResponseEntity signUp(@RequestBody @Valid MemberDto.Post signupRequest){
        Member member = new Member(signupRequest.getEmail(), signupRequest.getPassword());
        Long signupId = memberService.signup(member);
        URI location = UriComponentsBuilder.newInstance()
                .path(MEMBER_DEFAULT_URL +signupId)
                .build()
                .toUri();
        return  ResponseEntity.created(location).build();
    }

    @GetMapping("/{member-id}")
    public ResponseEntity getMembers(@PathVariable("member-id") Long memberId){
        Member findMember = memberService.findMember(memberId);
        MemberDto.ResponseDto responseDto =
                new MemberDto.ResponseDto(
                        findMember.getEmail(),
                        findMember.getName(),
                        findMember.getAge(),
                        findMember.getGender(),
                        findMember.getRegion());
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/{member-id}")
    public void updateMember(@PathVariable("member-id")Long memberId,
                             @RequestBody MemberDto.Update updateRequest){

        Member member = new Member();
        member.setAge(updateRequest.getAge());
        member.setName(updateRequest.getName());
        //데이터 없는데 널포인트 익셉션 안나는 이유 뭐예영? null을 사용할수있다. null데이터에 또 . 을찍으면발생함!
        member.setGender(updateRequest.getGender());
        member.setRegion(updateRequest.getRegion());
        memberService.update(memberId,member);

    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{member-id}")
    public void deleteMember(@PathVariable("member-id")Long memberId){
        memberService.delete(memberId);
    }


}
