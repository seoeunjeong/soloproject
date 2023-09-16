package soloproject.seomoim.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import soloproject.seomoim.domain.Member;
import soloproject.seomoim.dto.MemberDto;
import soloproject.seomoim.mapper.MemberMapper;
import soloproject.seomoim.service.MemberService;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {
    private final static String MEMBER_DEFAULT_URL = "/member/";

    private final MemberService memberService;

    private final MemberMapper mapper;

    @PostMapping("/sign-up")
    public ResponseEntity signUp(@RequestBody @Valid MemberDto.Post signupRequest){
        Long signupId = memberService.signup(mapper.memberPostDtoToMember(signupRequest));
        URI location = UriComponentsBuilder.newInstance()
                .path(MEMBER_DEFAULT_URL +signupId)
                .build()
                .toUri();
        return  ResponseEntity.created(location).build();
    }

    @GetMapping("/{member-id}")
    public ResponseEntity getMembers(@PathVariable("member-id") Long memberId){
        Member findMember = memberService.findMember(memberId);
        return new ResponseEntity<>(mapper.memberToMemberResponseDto(findMember), HttpStatus.OK);
    }

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/{member-id}")
    public void updateMember(@PathVariable("member-id")Long memberId,
                             @RequestBody MemberDto.Update updateRequest){
        Member member = mapper.memberUpdateDtoToMember(updateRequest);
        memberService.update(memberId,member);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{member-id}")
    public void deleteMember(@PathVariable("member-id")Long memberId){
        memberService.delete(memberId);
    }


}
