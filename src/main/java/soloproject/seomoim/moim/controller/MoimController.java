package soloproject.seomoim.moim.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import soloproject.seomoim.moim.like.LikeMoim;
import soloproject.seomoim.moim.like.LikeMoimService;
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

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/moims")
@Validated
public class MoimController {

    private final MoimService moimService;
    private final MoimMapper mapper;
    private final MemberService memberService;
    private final DistanceService distanceService;
    private final LikeMoimService likeMoimService;
    private final ObjectMapper objectMapper;

    @GetMapping("/post-form")
    public String postMoimForm(@RequestParam(value = "place_name", required = false) String placeName,
                               @RequestParam(value = "place_address", required = false) String placeAddress,
                               HttpServletRequest request, HttpServletResponse response, Model model) throws JsonProcessingException {

        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("myCookie")) {
                String decode = URLDecoder.decode(cookie.getValue(), StandardCharsets.UTF_8);
                MoimDto.Post cookieDate = objectMapper.readValue(decode, MoimDto.Post.class);
                cookieDate.setPlaceName(placeName);
                cookieDate.setPlaceAddress(placeAddress);
                model.addAttribute("post", cookieDate);
                cookie.setMaxAge(0);
                response.addCookie(cookie);
                return "moims/postForm";
            }
        }
        MoimDto.Post placePost = new MoimDto.Post();
        if (placeAddress != null && placeName != null) {
            placePost.setPlaceName(placeName);
            placePost.setPlaceAddress(placeAddress);
        }
        model.addAttribute("post", placePost);

        return "moims/postForm";
    }


    @PostMapping("/post")
    public String postMoim(@Validated @ModelAttribute MoimDto.Post post,
                           BindingResult bindingResult) throws Exception {
        if (bindingResult.hasErrors()) {
            return "moims/postForm";
        }

        Moim moim = mapper.moimPostDtoToMoim(post);
        Long moimId = moimService.createMoim(post.getMemberId(), moim);
        return "redirect:/moims/" + moimId;
    }

    @GetMapping("/place-search-page")
    public String searchPageForm() {
        return "moims/placeSearchPage";
    }

//  todo! 모임등록시 장소검색 페이지로 이동할때 데이터값 유지

    @PostMapping("/cookieData")
    public void addressSearchPage(@ModelAttribute MoimDto.Post post,
                                  HttpServletResponse response) {

        String encodePostDto = null;
        try {
            String postDto = objectMapper.writeValueAsString(post);
            encodePostDto = URLEncoder.encode(postDto, StandardCharsets.UTF_8);
        } catch (JsonProcessingException e) {
            log.info("error={}", e.getMessage());
        }
        Cookie cookie = new Cookie("myCookie", encodePostDto);
        response.addCookie(cookie);
    }



    @GetMapping("/{moim-id}")
    public String MoimDetailPage(@PathVariable("moim-id") Long moimId,
                                 @AuthenticationPrincipal CustomUserDetails userDetails,
                                 Model model) {
        Moim moim = moimService.findMoim(moimId);
        model.addAttribute("moim", mapper.moimToResponseDto(moim));

        Member findMember = memberService.findByEmail(userDetails.getEmail());
        MoimMember joinStatus = moimService.checkJoin(findMember, moim);
        LikeMoim likeMoim = likeMoimService.checkLike(findMember, moim);
        model.addAttribute("memberId", findMember.getId());
        model.addAttribute("likeStatus", likeMoim.isStatus());
        model.addAttribute("joinStatus",joinStatus.isStatus());

        return "moims/detailPage";
    }





    @GetMapping("/update-form/{moim-id}")
    public String updateMoimForm(
                                 @PathVariable("moim-id") Long moimId,
                                 Model model) {
        Moim moim = moimService.findMoim(moimId);

        model.addAttribute("moim", moim);

        return "moims/editForm";
    }




    @PostMapping("/{moim-id}")
    public String updateMoim(@PathVariable("moim-id") Long moimId,
                             @ModelAttribute MoimDto.Update updateRequest) {
        Moim moim = mapper.moimUpdateDtoToMoim(updateRequest);
        moimService.updateMoim(moimId, moim);

        return "redirect:/moims/" + moimId;

    }





    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{moim-id}")
    public void deleteMoim(@PathVariable("moim-id") Long moimId) {
        moimService.deleteMoim(moimId);
    }





    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/{moim-id}/{member-id}")
    public void joinMoim(@PathVariable("moim-id") Long moimId,
                         @PathVariable("member-id") Long memberId) throws Exception {
       moimService.joinMoim(moimId, memberId);
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
        PageResponseDto<MoimDto.Response> pageResponseDto = new PageResponseDto<>(mapper.moimsToResponseDtos(moims), pageMoims);
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


    @GetMapping("/nearby/{member-id}")
    public String findNearbyMoims(@PathVariable("member-id") Long memberId,
             RedirectAttributes redirectAttributes) {
        Member member = memberService.findMember(memberId);
        if (member.getAddress() == null) {
            return "redirect:/";
        }
        List<Moim> nearbyMoims = distanceService.findNearbyMoims(member);

        redirectAttributes.addFlashAttribute("moims", nearbyMoims);

        return "redirect:/";
    }


    //오늘모임
    @GetMapping("/today")
    public List<Moim> getTodayMoims() {
        return moimService.findTodayMoims();

    }

    //인기있는모임
    @GetMapping("/popular")
    public List<Moim> getPopularMoims() {
        return moimService.findPopularMoims();
    }




    //모든요청에 데이터 추가
    @ModelAttribute("moimCategorys")
    public MoimCategory[] moimCategorys() {
        return MoimCategory.values();
    }
    @ModelAttribute("memberId")
    public Long loginMember(@AuthenticationPrincipal CustomUserDetails userDetails){
        Member loginMember = memberService.findByEmail(userDetails.getEmail());
        return loginMember.getId();
    }

}
