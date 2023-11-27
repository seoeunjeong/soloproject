package soloproject.seomoim.like;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/like")
@RequiredArgsConstructor
public class LikeMoimController {

    public final LikeMoimService likeMoimService;

    @PostMapping("/{member-Id}/{moim-Id}")
    public String postLike(@PathVariable("member-Id") Long memberId,
                           @PathVariable("moim-Id") Long moimId) {
        likeMoimService.like(memberId, moimId);
        return "ok";
    }

    @DeleteMapping("/{member-Id}/{moim-Id}")
    public void deleteLike(@PathVariable("member-Id") Long memberId,
                           @PathVariable("moim-Id") Long moimId) {
        likeMoimService.cancelLike(memberId, moimId);
    }

}
