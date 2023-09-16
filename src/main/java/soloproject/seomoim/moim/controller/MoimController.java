package soloproject.seomoim.moim.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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

}
