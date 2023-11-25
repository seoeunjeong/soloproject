package soloproject.seomoim.moim.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import soloproject.seomoim.utils.PageResponseDto;
import soloproject.seomoim.moim.dto.MoimSearchDto;
import soloproject.seomoim.moim.mapper.MoimMapper;
import soloproject.seomoim.moim.dto.MoimDto;
import soloproject.seomoim.moim.entitiy.Moim;
import soloproject.seomoim.moim.service.MoimService;

import java.net.URI;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/moims")
public class MoimController {
    private final static String MOIM_DEFAULT_URL = "/moim/";

    private final MoimService moimService;
    private final MoimMapper mapper;

//    @GetMapping("/post")
//    public String postMoim(@Login Member loginMember, Model model){
//
//        model.addAttribute("loginMember", loginMember);
//
//        return "moims/postMoim";
//    }

    @PostMapping("/{member-id}")
    public String createMoim(@PathVariable("member-id") Long memberId,
                             @ModelAttribute MoimDto.Post createRequest) {
        Moim moim = mapper.moimPostDtoToMoim(createRequest);
        Long moimId = moimService.createMoim(memberId, moim);
        URI location = UriComponentsBuilder.newInstance()
                .path(MOIM_DEFAULT_URL + moimId)
                .build()
                .toUri();
        return "redirect:/";
    }
//    @PostMapping
//    public ResponseEntity createMoim(@RequestBody MoimDto.Post createRequest) {
//        Moim moim = mapper.moimPostDtoToMoim(createRequest);
//        Long moimId = moimService.createMoim(createRequest.getMemberId(), moim);
//        URI location = UriComponentsBuilder.newInstance()
//                .path(MOIM_DEFAULT_URL +moimId)
//                .build()
//                .toUri();
//        return ResponseEntity.created(location).build();
//    }
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


    @GetMapping("/{moim-id}")
    public ResponseEntity findMoim(@PathVariable("moim-id")Long moimId){
        Moim moim = moimService.findMoim(moimId);
        return new ResponseEntity(mapper.MoimToResponseDto(moim), HttpStatus.OK);
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
