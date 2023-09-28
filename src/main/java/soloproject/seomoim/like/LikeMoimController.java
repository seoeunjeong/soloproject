package soloproject.seomoim.like;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/like")
@RequiredArgsConstructor
public class LikeMoimController {

    public final LikeMoimService likeService;

    @PostMapping("/{member-Id}/{moim-Id}")
    public Long postLike(@PathVariable("member-Id")Long memberId,
                         @PathVariable("moim-Id")Long moimId){
           return likeService.save(memberId,moimId);
    }


    @DeleteMapping("/cancel/{member-Id}/{moim-Id}")
    public void deleteLike(@PathVariable("member-Id") Long memberId,
                           @PathVariable("moim-Id") Long moimId){

        likeService.cancelLike(memberId,moimId);
    }

}
