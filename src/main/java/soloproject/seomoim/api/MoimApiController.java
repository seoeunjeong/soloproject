package soloproject.seomoim.api;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import soloproject.seomoim.moim.dto.MoimSearchDto;
import soloproject.seomoim.moim.entitiy.Moim;
import soloproject.seomoim.moim.mapper.MoimMapper;
import soloproject.seomoim.moim.service.MoimService;
import soloproject.seomoim.utils.PageResponseDto;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MoimApiController {

    private final MoimService moimService;
    private final MoimMapper mapper;

    @GetMapping("/moims/{moim-id}")
    public ResponseEntity getDetail(@PathVariable("moim-id") Long moimId) {
        Moim moim = moimService.findMoim(moimId);
        return new ResponseEntity<>(mapper.moimToResponseDto(moim), HttpStatus.OK);
    }


    @PostMapping("/search")
    @ResponseBody
    public ResponseEntity findSearchMoims(@ModelAttribute MoimSearchDto moimSearchDto,
                                          @RequestParam(defaultValue = "1") int page,
                                          @RequestParam(defaultValue = "10") int size) {
        Page<Moim> pageMoims = moimService.findAllBySearch(moimSearchDto, page - 1, size);
        List<Moim> moims = pageMoims.getContent();
        return new ResponseEntity<>(new PageResponseDto<>(mapper.moimsToResponseDtos(moims), pageMoims), HttpStatus.OK);
    }

}
