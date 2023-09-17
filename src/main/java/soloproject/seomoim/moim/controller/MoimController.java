package soloproject.seomoim.moim.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import soloproject.seomoim.moim.mapper.MoimMapper;
import soloproject.seomoim.moim.dto.MoimDto;
import soloproject.seomoim.moim.entitiy.Moim;
import soloproject.seomoim.moim.service.MoimService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/moim")
public class MoimController {

    private final MoimService moimService;
    private final MoimMapper mapper;

    @PostMapping("/create")
    public Long createMoim(@RequestBody MoimDto.Post createRequest) {
        Moim moim = mapper.moimPostDtoToMoim(createRequest);
        return moimService.createMoim(createRequest.getMemberId(), moim);
    }
    @PatchMapping("/update/{moim-id}")
    public ResponseEntity updateMoim(@PathVariable("moim-id") Long moimId,
                                     @RequestBody MoimDto.Update updateRequest){
        Moim moim = mapper.moimUpdateDtoToMoim(updateRequest);
        Moim updateMoim = moimService.updateMoim(moimId, moim);
        return new ResponseEntity<>(mapper.MoimToResponseDto(updateMoim),HttpStatus.OK);
    }

}
