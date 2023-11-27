package soloproject.seomoim.moim.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import soloproject.seomoim.exception.BusinessLogicException;
import soloproject.seomoim.exception.ExceptionCode;
import soloproject.seomoim.member.entity.Member;
import soloproject.seomoim.member.repository.MemberRepository;
import soloproject.seomoim.member.service.MemberService;
import soloproject.seomoim.security.CustomUserDetailsService;
import soloproject.seomoim.utils.PageResponseDto;
import soloproject.seomoim.moim.dto.MoimSearchDto;
import soloproject.seomoim.moim.mapper.MoimMapper;
import soloproject.seomoim.moim.dto.MoimDto;
import soloproject.seomoim.moim.entitiy.Moim;
import soloproject.seomoim.moim.service.MoimService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.net.URI;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/moims")
@Slf4j
public class MoimController {
    private final static String MOIM_DEFAULT_URL = "/moims/";
    private final MoimService moimService;
    private final MoimMapper mapper;
    private final MemberRepository memberRepository;

    @PostMapping("/{member-id}")
    public String createMoim(@PathVariable("member-id") Long memberId,
                             @Valid @ModelAttribute MoimDto.Post PostRequest) {
        Moim moim = mapper.moimPostDtoToMoim(PostRequest);
        Long moimId = moimService.createMoim(memberId, moim);
        URI location = UriComponentsBuilder.newInstance()
                .path(MOIM_DEFAULT_URL + moimId)
                .build()
                .toUri();
        return "redirect:/loginHome";
    }

    //모임 상세페이지 조회
    @GetMapping("/{moim-id}")
    public String findMoim(@PathVariable("moim-id")Long moimId,
                           Model model, Authentication authentication){
        Object principal = authentication.getPrincipal();
        Optional<Member> optionalMember = memberRepository.findByEmail(principal.toString());
        Member member = optionalMember.orElseThrow(() -> new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND));
        model.addAttribute("member", member);
        Moim moim = moimService.findMoim(moimId);
        model.addAttribute("moim",moim);
        return "moims/detail";
    }

    @PatchMapping("/{moim-id}")
    public ResponseEntity updateMoim(@PathVariable("moim-id") Long moimId,
                                     @RequestBody MoimDto.Update updateRequest){
        Moim moim = mapper.moimUpdateDtoToMoim(updateRequest);
        Moim updateMoim = moimService.updateMoim(moimId, moim);
        return new ResponseEntity<>(mapper.MoimToResponseDto(updateMoim),HttpStatus.OK);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/delete/{moim-id}")
    public void deleteMoim(@PathVariable("moim-id")Long moimId){
        moimService.deleteMoim(moimId);
    }




    /*전체모임조회(페이지네이션,생성일기준 내림차순 정렬)*/
    @GetMapping("/all")
    public ResponseEntity findAll(@RequestParam int page,@RequestParam int size){
        Page<Moim> pageMoims = moimService.findAllbyPage(page - 1, size);
        List<Moim> moims = pageMoims.getContent();
        return new ResponseEntity<>(new PageResponseDto(mapper.moimsToResponseDtos(moims), pageMoims), HttpStatus.OK);
    }


    /* 키워드로 검색 */
    @GetMapping("/search")
    @ResponseBody
    public ResponseEntity findSearchMoims(@RequestBody MoimSearchDto moimSearchDto) {
//                                          @RequestParam int page,@RequestParam int size){
        Page<Moim> pageMoims = moimService.findAllbyPage(10,10);
        List<Moim> moims = pageMoims.getContent();
        return new ResponseEntity<>(new PageResponseDto<>(mapper.moimsToResponseDtos(moims), pageMoims), HttpStatus.OK);
    }

    /*
     * 위치조회/카테고리조회/키워드-동적 쿼리 작성 쿼리DSL)
     * 위치(String,카테고리 enum,String keyword)
     */
//    @GetMapping("/search")
//    @ResponseBody
//    public ResponseEntity findSearchMoims(@RequestBody MoimSearchDto moimSearchDto) {
////                                          @RequestParam int page,@RequestParam int size){
//        Page<Moim> pageMoims = moimService.findAllSearch(moimSearchDto, moimSearchDto.getPage() - 1, moimSearchDto.getSize());
//        List<Moim> moims = pageMoims.getContent();
//        return new ResponseEntity<>(new PageResponseDto<>(mapper.moimsToResponseDtos(moims), pageMoims), HttpStatus.OK);
//    }



    /*모임참여로직작성*/
    @PostMapping("/{moim-id}/{member-id}")
    public ResponseEntity joinMoim(@PathVariable("moim-id")Long moimId,
                                   @PathVariable("member-id")Long memberId){
        Moim moim = moimService.joinMoim(moimId, memberId);
        return new ResponseEntity<>(mapper.MoimToResponseDto(moim), HttpStatus.OK);
    }


    /*모임참여취소로직작성*/
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/delete/{moim-id}/{member-id}")
    public void notJoinMoim(@PathVariable("moim-id") Long moimId,
                            @PathVariable("member-id") Long memberId) {
        moimService.notJoinMoim(moimId, memberId);
    }




}
