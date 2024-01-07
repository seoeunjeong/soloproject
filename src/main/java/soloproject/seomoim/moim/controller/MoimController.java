package soloproject.seomoim.moim.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import soloproject.seomoim.member.loginCheck.AuthenticationUser;
import soloproject.seomoim.moim.service.LatestViewMoimService;
import soloproject.seomoim.moim.like.LikeMoim;
import soloproject.seomoim.moim.like.LikeMoimService;
import soloproject.seomoim.member.entity.Member;
import soloproject.seomoim.member.service.MemberService;
import soloproject.seomoim.moim.dto.MoimSearchDto;
import soloproject.seomoim.moim.entitiy.MoimCategory;
import soloproject.seomoim.moim.entitiy.MoimMember;
import soloproject.seomoim.moim.service.NearByMoimService;
import soloproject.seomoim.moim.mapper.MoimMapper;
import soloproject.seomoim.moim.dto.MoimDto;
import soloproject.seomoim.moim.entitiy.Moim;
import soloproject.seomoim.moim.service.MoimService;
import soloproject.seomoim.utils.PageResponseDto;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/moims")
public class MoimController {

    private final MoimService moimService;
    private final MoimMapper mapper;
    private final MemberService memberService;
    private final LikeMoimService likeMoimService;
    private final ObjectMapper objectMapper;
    private static final String COOKIE_DATA = "cookieDto";
    private final LatestViewMoimService latestViewService;
    private final NearByMoimService nearByMoimService;


    @Value("${kakao.maps.appKey}")
    private String kakaoMapsAppKey;



    //    todo 모임장 위임기능!
    @GetMapping("/post")
    public String moimPostForm(@RequestParam(value = "place_name", required = false) String placeName,
                               @RequestParam(value = "place_address", required = false) String placeAddress,
                               HttpServletRequest request, HttpServletResponse response, Model model) throws IOException {
        MoimDto.Post post = new MoimDto.Post();
        MoimDto.Post cookieDate = readCookie(COOKIE_DATA, MoimDto.Post.class, request, response);

        if (cookieDate != null) {
            cookieDate.setPlaceName(placeName);
            cookieDate.setPlaceAddress(placeAddress);
            post = cookieDate;
        }

        if (placeAddress != null && placeName != null) {
            post.setPlaceName(placeName);
            post.setPlaceAddress(placeAddress);
        }
        model.addAttribute("post", post);

        return "moims/postForm";
    }


    @PostMapping("/post")
    public String postMoim(@Validated @ModelAttribute MoimDto.Post post,
                           BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors()) {
            return "moims/postForm";
        }
        try {
            Moim moim = mapper.moimPostDtoToMoim(post);
            Long moimId = moimService.createMoim(post.getMemberId(), moim);
            return "redirect:/moims/" + moimId;

        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "moims/postForm";
        }
    }


    @GetMapping("/{moim-id}")
    public String MoimDetailPage(@PathVariable("moim-id") Long moimId,
                                 @AuthenticationUser String email, Model model) {
        Moim moim = moimService.findMoim(moimId);

        model.addAttribute("moim", mapper.moimToResponseDto(moim));

        Member loginMember = memberService.findMemberByEmail(email);
        MoimMember joinStatus = moimService.checkJoin(loginMember, moim);
        LikeMoim likeMoim = likeMoimService.checkLike(loginMember, moim);
        model.addAttribute("likeStatus", likeMoim.isStatus());
        model.addAttribute("joinStatus",joinStatus.isStatus());

        latestViewService.addLatestPostForMember(loginMember.getId(),mapper.moimToResponseDto(moim));

        return "moims/detailPage";
    }


    @GetMapping("/edit-page/{moim-id}")
    public String moimEditPage(@PathVariable("moim-id") Long moimId, Model model) {
        Moim moim = moimService.findMoim(moimId);
        MoimDto.Response moimToResponseDto = mapper.moimToResponseDto(moim);
        model.addAttribute("moim", moimToResponseDto);
        return "moims/editPage";
    }

    @GetMapping("/edit/{moim-id}")
    public String moimEditForm(@PathVariable("moim-id") Long moimId,
                               @RequestParam(value = "place_name", required = false) String placeName,
                               @RequestParam(value = "place_address", required = false) String placeAddress,
                               HttpServletRequest request, HttpServletResponse response, Model model) throws IOException {

        Moim moim = moimService.findMoim(moimId);
        MoimDto.Update updateDto = mapper.moimToMoimUpdateDto(moim);

        MoimDto.Update cookieDate = readCookie(COOKIE_DATA, MoimDto.Update.class, request, response);

        if (cookieDate != null) {
            cookieDate.setPlaceName(placeName);
            cookieDate.setPlaceAddress(placeAddress);
            cookieDate.setMoimId(moimId);
            model.addAttribute("update", cookieDate);
            return "moims/editForm";
        }
        if (placeAddress != null && placeName != null) {
            updateDto.setPlaceName(placeName);
            updateDto.setPlaceAddress(placeAddress);
        }
        model.addAttribute("update", updateDto);
        return "moims/editForm";
    }

    @PostMapping("/edit/{moim-id}")
    public String updateMoim(@PathVariable("moim-id") Long moimId,
                             @Validated @ModelAttribute MoimDto.Update update,
                             BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors()) {
            return "moims/editForm";
        }
        try {
            Moim moim = mapper.moimUpdateDtoToMoim(update);
            moimService.updateMoim(moimId, moim);
            return "redirect:/moims/edit-page/" + update.getMoimId();

        } catch (Exception e) {
            model.addAttribute("error", "모임 수정 중 오류가 발생했습니다: " + e.getMessage());
            return "moims/editForm";
        }
    }

    @GetMapping("/place-search-page")
    public String searchPageForm(Model model) {
        model.addAttribute("kakaoMapsAppKey", kakaoMapsAppKey);

        return "moims/placeSearchPage";
    }


    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{moim-id}")
    public void deleteMoim(@PathVariable("moim-id") Long moimId,
                           @AuthenticationUser String email) {
        moimService.deleteMoim(email, moimId);
    }


    @PostMapping("/{moim-id}/{member-id}")
    @ResponseBody
    public ResponseEntity<MoimDto.Response> joinMoim(@PathVariable("moim-id") Long moimId,
                                   @PathVariable("member-id") Long memberId) {

        moimService.joinMoim(moimId, memberId);
        Moim moim = moimService.findMoim(moimId);
        return new ResponseEntity<>(mapper.moimToResponseDto(moim),HttpStatus.OK);
    }

    @DeleteMapping("/{moim-id}/{member-id}")
    @ResponseBody
    public ResponseEntity<MoimDto.Response> notJoinMoim(@PathVariable("moim-id") Long moimId,
                            @PathVariable("member-id") Long memberId) {
        moimService.cancelJoinMoim(moimId, memberId);
        Moim moim = moimService.findMoim(moimId);
        return new ResponseEntity<>(mapper.moimToResponseDto(moim),HttpStatus.OK);
    }


    @GetMapping("/allPage")
    public String allPage() {
        return "moims/totalPage";
    }


    /*전체모임조회(페이지네이션,생성일기준 내림차순 정렬)*/
    @GetMapping("/all")
    @ResponseBody
    public ResponseEntity<PageResponseDto<MoimDto.Response>> findAll(@RequestParam(defaultValue = "1") int page,
                                  @RequestParam(defaultValue = "10") int size) {
        Page<Moim> pageMoims = moimService.findAllByPage(page - 1, size);
        List<Moim> moims = pageMoims.getContent();
        PageResponseDto<MoimDto.Response> pageResponseDto = new PageResponseDto<>(mapper.moimsToResponseDtos(moims), pageMoims);
        return new ResponseEntity<>(pageResponseDto,HttpStatus.OK);

    }

    @PostMapping("/search")
    public String findSearchMoims(@ModelAttribute MoimSearchDto moimSearchDto,
                                  @RequestParam(defaultValue = "1") int page,
                                  @RequestParam(defaultValue = "10") int size,
                                  Model model) {
        Page<Moim> pageMoims = moimService.findAllBySearch(moimSearchDto, page - 1, size);
        List<Moim> moims = pageMoims.getContent();
        PageResponseDto<MoimDto.Response> pageResponseDto = new PageResponseDto<>(mapper.moimsToResponseDtos(moims), pageMoims);
        model.addAttribute("moims", pageResponseDto);
        model.addAttribute("moimSearchDto",moimSearchDto);
        return "home/search";
    }

    @GetMapping("/search-EupMyeonDong")
    public String searchEupMyeonDong(Model model){
        model.addAttribute("kakaoMapsAppKey", kakaoMapsAppKey);
        return "moims/addressSearchPage";
    }



    @GetMapping("/nearby/{member-id}")
    public String findNearbyMoims(@PathVariable("member-id") Long memberId,
             RedirectAttributes redirectAttributes) {
        Member member = memberService.findMemberById(memberId);
        if (member.getAddress() == null) {
            return "redirect:/";
        }
        List<Moim> nearbyMoims = nearByMoimService.findNearbyMoims(member);

        redirectAttributes.addFlashAttribute("moims", nearbyMoims);

        return "redirect:/";
    }

    @PostMapping("/delegate/{moim-id}/{member-id}")
    public String delegateLeader(@PathVariable("member-id") Long memberId,
                                 @PathVariable("moim-id") Long moimId,
                                 RedirectAttributes redirectAttributes) {
        moimService.delegateLeader(moimId,memberId);

        redirectAttributes.addAttribute("moim-id", moimId);

        return "redirect:moims/{moim-id}";
    }

    @GetMapping("/allToday")
    public String allToday() {
        return "moims/todayPage";
    }

    @GetMapping("/today")
    @ResponseBody
    public ResponseEntity<PageResponseDto<MoimDto.Response>> getTodayMoims(@RequestParam(defaultValue = "1") int page,
                                    @RequestParam(defaultValue = "5") int size,
                                    Model model) {
        Page<Moim> todayMoims = moimService.findTodayMoims(page - 1, size);
        List<Moim> moims = todayMoims.getContent();
        PageResponseDto<MoimDto.Response> pageResponseDto = new PageResponseDto<>(mapper.moimsToResponseDtos(moims),todayMoims);
        model.addAttribute("moims", pageResponseDto);

        return new ResponseEntity<>(pageResponseDto,HttpStatus.OK);

    }

    @GetMapping("/popular")
    public List<Moim> getPopularMoims() {
        return moimService.findPopularMoims();
    }



    //todo! 모임등록.수정시 장소검색 페이지로 이동-> 쿠키에 데이터값 유지
    @PostMapping("/set-cookie")
    public void createCookie(@ModelAttribute MoimDto.CookieDto cookieDto,
                             HttpServletResponse response) {
        if (cookieDto != null) {
            String encodeCookieDto = null;
            try {
                String stringCookieDto = objectMapper.writeValueAsString(cookieDto);
                encodeCookieDto = URLEncoder.encode(stringCookieDto, StandardCharsets.UTF_8);
            } catch (JsonProcessingException e) {
                log.error("JSON 처리 오류: {}", e.getMessage(), e);
            }
            Cookie cookie = new Cookie(COOKIE_DATA, encodeCookieDto);
            cookie.setPath("/");
            response.addCookie(cookie);
        }
    }

    private <T> T readCookie(String cookieName, Class<T> clazz, HttpServletRequest request, HttpServletResponse response) throws IOException {
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(cookieName)) {
                String decode = URLDecoder.decode(cookie.getValue(), StandardCharsets.UTF_8);
                T cookieData = objectMapper.readValue(decode, clazz);
                cookie.setPath("/");
                cookie.setMaxAge(0);
                response.addCookie(cookie);
                return cookieData;
            }
        }
        return null;
    }

    @ModelAttribute("moimCategorys")
    public MoimCategory[] moimCategorys() {
        return MoimCategory.values();
    }

    @ModelAttribute("loginMemberId")
    public Long loginMember(@AuthenticationUser String email){
        Member loginMember = memberService.findMemberByEmail(email);
        return loginMember.getId();
    }
}
