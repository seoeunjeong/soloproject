package soloproject.seomoim.moim.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import soloproject.seomoim.moim.dto.MoimDto;
import soloproject.seomoim.moim.entitiy.Moim;
import soloproject.seomoim.moim.service.MoimService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/moim")
public class MoimController {

    private final MoimService moimService;
    @PostMapping("/create")
    public Long createMoim(@RequestBody MoimDto.Post createRequest){
        Moim moim = new Moim();
        moim.setTitle(createRequest.getTitle());
        moim.setContent(createRequest.getContent());
        moim.setParticipantCount(createRequest.getParticipantCount());
        moim.setRegion(createRequest.getRegion());
        moim.setMoimCategory(createRequest.getMoimCategory());
        moimService

    }

}
