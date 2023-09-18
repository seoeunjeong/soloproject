package soloproject.seomoim.member.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import soloproject.seomoim.member.domain.Member;
import soloproject.seomoim.member.dto.MemberDto;
import soloproject.seomoim.member.mapper.MemberMapper;
import soloproject.seomoim.member.service.MemberService;
import soloproject.seomoim.moim.dto.MoimMemberDto;
import soloproject.seomoim.moim.entitiy.Moim;
import soloproject.seomoim.moim.entitiy.MoimMember;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

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

    //회원은 참여한 모임을 조회할수있다.

    @GetMapping("/moims/{member-id}")
    public ResponseEntity findMoims(@PathVariable("member-id")Long memberId){
        List<MoimMember> moimList = memberService.findParticipationMoim(memberId);
        List<MoimMemberDto.Response> response = moimList.stream()
                .map(moimMember -> new MoimMemberDto.Response(
                        moimMember.getMember().getId(),
                        moimMember.getMoim().getId(),
                        moimMember.getMoim().getTitle()))
                .collect(Collectors.toList());
        return new ResponseEntity(response, HttpStatus.OK);
    }


}
