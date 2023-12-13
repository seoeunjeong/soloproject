package soloproject.seomoim.moim.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import soloproject.seomoim.like.LikeMoim;
import soloproject.seomoim.like.LikeMoimService;
import soloproject.seomoim.member.entity.Member;
import soloproject.seomoim.member.service.MemberService;
import soloproject.seomoim.moim.dto.MoimSearchDto;
import soloproject.seomoim.moim.entitiy.MoimCategory;
import soloproject.seomoim.moim.entitiy.MoimMember;
import soloproject.seomoim.recommend.DistanceService;
import soloproject.seomoim.security.FormLogin.CustomUserDetails;
import soloproject.seomoim.moim.mapper.MoimMapper;
import soloproject.seomoim.moim.dto.MoimDto;
import soloproject.seomoim.moim.entitiy.Moim;
import soloproject.seomoim.moim.service.MoimService;
import soloproject.seomoim.utils.PageResponseDto;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/moims")
@Slf4j
@Validated
public class MoimController {

    private final MoimService moimService;
    private final MoimMapper mapper;
    private final MemberService memberService;
    private final DistanceService distanceService;
    private final LikeMoimService likeMoimService;


    @GetMapping("/post-form")
    public String postMoimForm(@AuthenticationPrincipal CustomUserDetails userDetails,
                               Model model) {

        Member requestMember = memberService.findByEmail(userDetails.getUsername());

        model.addAttribute("memberId", requestMember.getId());
        model.addAttribute("post", new MoimDto.Post());

        return "moims/postForm";
    }


    @PostMapping("/post")
    public String postMoim(@Validated @ModelAttribute MoimDto.Post post,
                           BindingResult bindingResult,
                           Model model) {

        Long memberId = post.getMemberId();
        model.addAttribute("memberId",memberId);

        if (bindingResult.hasErrors()) {
            return "moims/postForm";
        }

        Moim moim = mapper.moimPostDtoToMoim(post);

        log.info("post.getMemberId()={}",post.getMemberId());
        Long moimId = moimService.createMoim(post.getMemberId(),moim);

        return "redirect:/moims/"+moimId;
    }

    @GetMapping("/{moim-id}")
    public String MoimDetailPage(@PathVariable("moim-id") Long moimId,
                                 @AuthenticationPrincipal CustomUserDetails userDetails,
                                 Model model) {
        Moim moim = moimService.findMoim(moimId);
        model.addAttribute("moim",mapper.moimToResponseDto(moim));

        Member findMember = memberService.findByEmail(userDetails.getEmail());
        MoimMember joinStatus = moimService.checkJoin(moimId, findMember.getId());
        LikeMoim likeMoim = likeMoimService.checkLike(findMember, moim);
        model.addAttribute("memberId",findMember.getId());
        model.addAttribute("joinStatus",joinStatus.isStatus());
        model.addAttribute("likeStatus",likeMoim.isStatus());

        return "moims/detailPage";
    }

    @PostMapping("/{moim-id}")
    public ResponseEntity updateMoim(@PathVariable("moim-id") Long moimId,
                                     @RequestBody MoimDto.Update updateRequest) {
        Moim moim = mapper.moimUpdateDtoToMoim(updateRequest);
        Moim updateMoim = moimService.updateMoim(moimId, moim);
        return new ResponseEntity<>(mapper.moimToResponseDto(updateMoim), HttpStatus.OK);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{moim-id}")
    public void deleteMoim(@PathVariable("moim-id") Long moimId) {
        moimService.deleteMoim(moimId);
    }

    @PostMapping("/{moim-id}/{member-id}")
    public String joinMoim(@PathVariable("moim-id") Long moimId,
                           @PathVariable("member-id") Long memberId){
        moimService.joinMoim(moimId, memberId);

        return "redirect:/moims/"+moimId;
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{moim-id}/{member-id}")
    public void notJoinMoim(@PathVariable("moim-id") Long moimId,
                            @PathVariable("member-id") Long memberId) {
        moimService.cancelJoinMoim(moimId, memberId);
    }

    /*전체모임조회(페이지네이션,생성일기준 내림차순 정렬)*/
    @GetMapping("/all")
    public String findAll(@RequestParam(defaultValue = "1") int page,
                          @RequestParam(defaultValue = "10") int size,
                          Model model) {
        Page<Moim> pageMoims = moimService.findAllbyPage(page - 1, size);
        List<Moim> moims = pageMoims.getContent();
        PageResponseDto pageResponseDto = new PageResponseDto(mapper.moimsToResponseDtos(moims), pageMoims);
        model.addAttribute("moims", pageResponseDto);
        return "moims/total";
    }

    @PostMapping("/search")
    public String findSearchMoims(@ModelAttribute MoimSearchDto moimSearchDto,
                                  @RequestParam(defaultValue = "1") int page,
                                  @RequestParam(defaultValue = "10") int size,
                                  Model model) {
        Page<Moim> pageMoims = moimService.findAllbySearch(moimSearchDto, page - 1, size);
        List<Moim> moims = pageMoims.getContent();
        PageResponseDto<MoimDto.Response> pageResponseDto = new PageResponseDto<>(mapper.moimsToResponseDtos(moims), pageMoims);
        model.addAttribute("moims", pageResponseDto);
        return "home/searchHome";
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

    //today모임
    @GetMapping("/today")
    public List<Moim> getTodayMoims(){
        return moimService.findTodayMoims();

    }

    //인기있는모임
    @GetMapping("/popular")
    public List<Moim> getPopularMoims(){
        return moimService.findPopularMoims();
    }


    @ModelAttribute("moimCategorys")
    public MoimCategory[] moimCategorys() {

        return MoimCategory.values();
    }


}
