package soloproject.seomoim.moim.like;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import soloproject.seomoim.moim.entitiy.Moim;
import soloproject.seomoim.moim.mapper.MoimMapper;
import soloproject.seomoim.moim.service.MoimService;

@RestController
@RequestMapping("/likes")
@RequiredArgsConstructor
public class LikeMoimController {

    public final LikeMoimService likeMoimService;
    public final MoimService moimService;
    public final MoimMapper mapper;

    @PostMapping("/{moim-Id}/{member-Id}")
    public ResponseEntity postLike(@PathVariable("moim-Id") Long moimId,
                                   @PathVariable("member-Id") Long memberId) {
        likeMoimService.like(moimId, memberId);
        Moim moim = moimService.findMoim(moimId);
        return new ResponseEntity<>(mapper.moimToResponseDto(moim), HttpStatus.OK);
    }

    @DeleteMapping("/{moim-Id}/{member-Id}")
    public ResponseEntity deleteLike(@PathVariable("moim-Id") Long moimId,
                                     @PathVariable("member-Id") Long memberId) {
        likeMoimService.cancelLike(memberId, moimId);
        Moim moim = moimService.findMoim(moimId);
        return new ResponseEntity<>(mapper.moimToResponseDto(moim), HttpStatus.OK);
    }
}
