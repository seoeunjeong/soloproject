package soloproject.seomoim.member.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import soloproject.seomoim.exception.BusinessLogicException;
import soloproject.seomoim.exception.ExceptionCode;
import soloproject.seomoim.member.emailCertification.MailService;
import soloproject.seomoim.member.entity.Member;
import soloproject.seomoim.member.dto.MemberDto;
import soloproject.seomoim.member.mapper.MemberMapper;
import soloproject.seomoim.member.service.MemberService;
import soloproject.seomoim.utils.RedisUtil;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
@RequestMapping("/members")
@Slf4j
public class MemberController {
    private final static String MEMBER_DEFAULT_URL = "/members";
    private final MemberService memberService;
    private final MemberMapper mapper;
    private final MailService mailService;
    private final RedisUtil redisUtil;


    @GetMapping
    public String signUp(){
        return "/members/postForm";
    }

    @PostMapping
    public String signUp(@Valid @ModelAttribute MemberDto.Signup request){
        Long signupId = memberService.signup(mapper.memberSignUpDtoToMember(request));
        return "redirect:/";
    }


//    @PostMapping("/sign-up")
//    public ResponseEntity signUp2(@Valid @RequestBody MemberDto.Signup request){
//        Long signupId = memberService.signup(mapper.memberSignUpDtoToMember(request));
//        return ResponseEntity.created(UriCreator.createUri(MEMBER_DEFAULT_URL, signupId)).build();
//    }

    public void emailCertification(String email,String code){
        String certificationCode = (String) redisUtil.get(email);
        if(!code.equals(certificationCode)){
            throw new IllegalStateException("인증코드가 틀렸습니다");
        }
    }

    @GetMapping("/{member-id}/edit")
    public String profileEditFrom(@PathVariable("member-id")Long memberId,
                                  Model model){
        Member member = memberService.findMember(memberId);
        model.addAttribute("member", member);
        return "members/editFrom";

    }

    @PostMapping("/{member-id}/edit")
    public String updateProfile(@PathVariable("member-id") Long memberId,
                                RedirectAttributes redirectAttributes,
                                @Valid @ModelAttribute MemberDto.Update request){
        Member findMember = memberService.findMember(memberId);
        Member member = mapper.memberUpdateDtoToMember(request);
        memberService.update(memberId,member);
        redirectAttributes.addAttribute("memberId", memberId);
        redirectAttributes.addAttribute("status",true);
        return "redirect:/members/{memberId}";
    }
    
//    /*본인 프로필이 아니면 수정 불가능*/
//    @ResponseStatus(HttpStatus.OK)
//    @PatchMapping("/update/{member-id}")
//    public void updateProfile(@PathVariable("member-id") Long memberId,
//                              @Login Member loginMember,
//                              @Valid @RequestBody MemberDto.Update request) {
//        Member findMember = memberService.findMember(loginMember.getId());
//        if(findMember.getId()!=memberId){
//            throw new BusinessLogicException(ExceptionCode.INVALID_REQUEST);
//        }
//        Member member = mapper.memberUpdateDtoToMember(request);
//        memberService.update(memberId,member);
//    }

//
//    /* 본인이 아닌 회원 데이터 삭제불가능*/
//    @ResponseStatus(HttpStatus.NO_CONTENT)
//    @DeleteMapping("/delete/{member-id}")
//    public void deleteMember(@PathVariable("member-id")Long memberId,
//                             @Login Member loginMember){
//        Member findMember = memberService.findMember(loginMember.getId());
//
//        if(findMember.getId() !=memberId){
//            throw new BusinessLogicException(ExceptionCode.INVALID_REQUEST);
//        }
//        memberService.delete(memberId);
//    }

//    @GetMapping("/{member-id}")
//    public ResponseEntity getMembers(@PathVariable("member-id") Long memberId){
//        Member findMember = memberService.findMember(memberId);
//        return new ResponseEntity<>(mapper.memberToMemberResponseDto(findMember), HttpStatus.OK);
//    }

    @GetMapping("/{member-id}")
    public String getMember(@PathVariable("member-id") Long memberId,
                            Model model){
        Member member = memberService.findMember(memberId);
        model.addAttribute("member",member);
        model.addAttribute("status",true);

        return "members/myPage";

    }

    //회원은 참여한 모임을 조회할수있다.
//    @GetMapping("/moims/{member-id}")
//    public ResponseEntity findMoims(@PathVariable("member-id")Long memberId){
//        List<MoimMember> moimList = memberService.findParticipationMoim(memberId);
//        List<MoimMemberDto.Response> response = moimList.stream()
//                .map(moimMember -> new MoimMemberDto.Response(
//                        moimMember.getMember().getId(),
//                        moimMember.getMoim().getId(),
//                        moimMember.getMoim().getTitle()))
//                .collect(Collectors.toList());
//        return new ResponseEntity(response, HttpStatus.OK);
//    }

//    //모임조회

//    @GetMapping("/moims/{member-id}")
//    public ResponseEntity findMemberWithParticipationMoims(@PathVariable("member-id")Long memberId){
//        Member member = memberService.findMemberAndfindParticipationMoim(memberId);
//
//        MemberDto.ResponseDto responseDto =
//        return new ResponseEntity(responseDto, HttpStatus.OK);
//    }


}
