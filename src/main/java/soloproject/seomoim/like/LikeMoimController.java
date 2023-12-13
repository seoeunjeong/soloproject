package soloproject.seomoim.like;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/likes")
@RequiredArgsConstructor
public class LikeMoimController {

    public final LikeMoimService likeMoimService;

    @PostMapping("/{moim-Id}/{member-Id}")
    public String postLike(@PathVariable("moim-Id") Long moimId,
                           @PathVariable("member-Id") Long memberId) {
        likeMoimService.like(moimId, memberId);
        return "redirect:/moims/"+moimId;
    }

    @DeleteMapping("/{member-Id}/{moim-Id}")
    public void deleteLike(@PathVariable("member-Id") Long memberId,
                           @PathVariable("moim-Id") Long moimId) {
        likeMoimService.cancelLike(memberId, moimId);
    }

}
