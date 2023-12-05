package soloproject.seomoim.moim.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import soloproject.seomoim.member.entity.Member;
import soloproject.seomoim.member.service.MemberService;
import soloproject.seomoim.moim.entitiy.MoimCategory;
import soloproject.seomoim.moim.repository.MoimMemberRepository;
import soloproject.seomoim.recommend.DistanceService;
import soloproject.seomoim.security.CustomUserDetails;
import soloproject.seomoim.utils.PageResponseDto;
import soloproject.seomoim.moim.dto.MoimSearchDto;
import soloproject.seomoim.moim.mapper.MoimMapper;
import soloproject.seomoim.moim.dto.MoimDto;
import soloproject.seomoim.moim.entitiy.Moim;
import soloproject.seomoim.moim.service.MoimService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/moims")
@Slf4j
@Validated
public class MoimController {

    private final MoimService moimService;
    private final MoimMapper mapper;
    private final MoimMemberRepository moimMemberRepository;
    private final MemberService memberService;
    private final DistanceService distanceService;


    @GetMapping("/postForm/{member-id}")
    public String postMoimForm(@PathVariable("member-id") Long memberId,
                               Model model) {
        model.addAttribute("memberId", memberId);

        return "moims/postFrom";
    }

    //모임 등록
    @PostMapping("/{member-id}")
    public String createMoim(@PathVariable("member-id") @Positive Long memberId,
                             @Valid @ModelAttribute MoimDto.Post PostRequest) {
        Moim moim = mapper.moimPostDtoToMoim(PostRequest);
        log.info("moim",moim.toString());
        moimService.createMoim(memberId, moim);
        return "redirect:/";
    }

    //모임 상세페이지
    //Todo 뷰에 Dto 전달로 분리할 것!
    @GetMapping("/{moim-id}")
    public String MoimDetailPage(@PathVariable("moim-id") Long moimId,
                                 Model model, @AuthenticationPrincipal CustomUserDetails userDetails,
                                 HttpServletResponse response) {
        model.addAttribute("member", userDetails);
        Moim moim = moimService.findMoim(moimId);
        log.info("moim.parti=" + moim.getParticipant());
        model.addAttribute("moim", moim);
        List<Member> joinMembers = moimMemberRepository.findJoinMembers(moimId);
        model.addAttribute("joinMembers", joinMembers);
        return "moims/detailPage";
    }


    //모임 참여
    @PostMapping("/{moim-id}/{member-id}")
    @ResponseBody
    public String joinMoim(@PathVariable("moim-id") Long moimId,
                           @PathVariable("member-id") Long memberId,
                           HttpServletRequest request,
                           HttpServletResponse response) {
        response.setHeader("Pragma", "no-cache");
        Moim moim = moimService.joinMoim(moimId, memberId);
        String referer = request.getHeader("Referer");

        return "{redirectUrl:" + referer + "}";


    }

    //모임 참여 취소
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{moim-id}/{member-id}")
    public void notJoinMoim(@PathVariable("moim-id") Long moimId,
                            @PathVariable("member-id") Long memberId) {
        moimService.notJoinMoim(moimId, memberId);
    }


    //근방 km 내의 거리 모임 추천
    @GetMapping("/nearby/{member-id}")
    public String findNearbyMoims(@PathVariable("member-id") Long memberId,
                                  Model model, RedirectAttributes redirectAttributes) {
        Member member = memberService.findMember(memberId);
        if (member.getAddress() == null) {
            return "redirect:/";
        }
        List<Moim> nearbyMoims = distanceService.findNearbyMoims(member);

        redirectAttributes.addFlashAttribute("moims", nearbyMoims);

        return "redirect:/";
    }


    @PatchMapping("/{moim-id}")
    public ResponseEntity updateMoim(@PathVariable("moim-id") Long moimId,
                                     @RequestBody MoimDto.Update updateRequest) {
        Moim moim = mapper.moimUpdateDtoToMoim(updateRequest);
        Moim updateMoim = moimService.updateMoim(moimId, moim);
        return new ResponseEntity<>(mapper.MoimToResponseDto(updateMoim), HttpStatus.OK);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/delete/{moim-id}")
    public void deleteMoim(@PathVariable("moim-id") Long moimId) {
        moimService.deleteMoim(moimId);
    }


    /*전체모임조회(페이지네이션,생성일기준 내림차순 정렬)*/
    @GetMapping("/all")
    public ResponseEntity findAll(@RequestParam int page, @RequestParam int size) {
        Page<Moim> pageMoims = moimService.findAllbyPage(page - 1, size);
        List<Moim> moims = pageMoims.getContent();
        return new ResponseEntity<>(new PageResponseDto(mapper.moimsToResponseDtos(moims), pageMoims), HttpStatus.OK);
    }


    /* 키워드로 검색 */
    @GetMapping("/keyword")
    @ResponseBody
    public ResponseEntity findSearchMoims(@RequestBody MoimSearchDto moimSearchDto) {
//                                          @RequestParam int page,@RequestParam int size){
        Page<Moim> pageMoims = moimService.findAllbyPage(10, 10);
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

    @ModelAttribute("moimCategorys")
    public MoimCategory[] moimCategorys() {
        return MoimCategory.values();
    }
}
