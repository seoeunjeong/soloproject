package soloproject.seomoim.moim.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import soloproject.seomoim.moim.mapper.MoimMapper;
import soloproject.seomoim.moim.dto.MoimDto;
import soloproject.seomoim.moim.entitiy.Moim;
import soloproject.seomoim.moim.service.MoimService;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/moim")
public class MoimController {
    private final static String MOIM_DEFAULT_URL = "/moim/";

    private final MoimService moimService;
    private final MoimMapper mapper;

    @PostMapping("/create")
    public ResponseEntity createMoim(@RequestBody MoimDto.Post createRequest) {
        Moim moim = mapper.moimPostDtoToMoim(createRequest);
        Long moimId = moimService.createMoim(createRequest.getMemberId(), moim);
        URI location = UriComponentsBuilder.newInstance()
                .path(MOIM_DEFAULT_URL +moimId)
                .build()
                .toUri();
        return ResponseEntity.created(location).build();
    }
    @PatchMapping("/update/{moim-id}")
    public ResponseEntity updateMoim(@PathVariable("moim-id") Long moimId,
                                     @RequestBody MoimDto.Update updateRequest){
        Moim moim = mapper.moimUpdateDtoToMoim(updateRequest);
        Moim updateMoim = moimService.updateMoim(moimId, moim);
        return new ResponseEntity<>(mapper.MoimToResponseDto(updateMoim),HttpStatus.OK);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/delete/{moim-id}")
    public void delete(@PathVariable("moim-id")Long moimId){
        moimService.deleteMoim(moimId);
    }

    //모임 가입 로직
    @PostMapping("/join/{moim-id}/{member-id}")
    public ResponseEntity joinMoim(@PathVariable("moim-id")Long moimId,
                                   @PathVariable("member-id")Long memberId){
        Moim moim = moimService.joinMoim(moimId, memberId);
        return new ResponseEntity<>(mapper.MoimToResponseDto(moim), HttpStatus.OK);
    }
}
