package soloproject.seomoim.like;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/like")
@RequiredArgsConstructor
public class LikeMoimController {

    public final LikeMoimService likeService;

    @PostMapping("{member-Id}/{moim-Id}")
    public Long postLike(@PathVariable("member-Id")Long memberId,
                         @PathVariable("moim-Id")Long moimId){
           return likeService.save(memberId,moimId);
    }


//    @DeleteMapping("{member-Id}/{moim-Id}")
//    public Long deleteLike(@PathVariable("member-Id") Long memberId,
//                           @PathVariable("moim-id") Long moidId){
//
//        likeService.delete(memberId,moimId);
//    }

}
