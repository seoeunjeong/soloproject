package soloproject.seomoim.comment;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/comment")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;


    @PostMapping("/create")
    public ResponseEntity createComment(@RequestBody CommentDto.Post createRequest){
        Long save = commentService.save(createRequest);
        return ResponseEntity.ok(save);
    }
    @PatchMapping("/update/{comment-id}")
    public void updateComment(@PathVariable("comment-id")Long commentId,
                                     @RequestBody CommentDto.Update updateRequest){
        Comment update = commentService.update(commentId, updateRequest);
    }

    @DeleteMapping("/delete/{comment-id}")
    public void deleteComment(@PathVariable("comment-id") Long commentId){
       commentService.delete(commentId);

    }
    @GetMapping("/{member-id}")
    public ResponseEntity findCommentBymember(@PathVariable("member-id")Long memberId){
        List<Comment> commentByMember = commentService.findCommentByMember(memberId);
        CommentDto.Response response = new CommentDto.Response();
        response.setMemberId(memberId);
        response.setCommentList(
                commentByMember
                .stream()
                        .map(comment -> comment.getContent().toString())
                        .collect(Collectors.toList()));
        return new ResponseEntity(response, HttpStatus.OK);
    }

    @GetMapping("/all/{moim-id}")
    public ResponseEntity findCommentByMoim(@PathVariable("moim-id")Long moimId){
        List<Comment> commentByMoim = commentService.findCommentByMoim(moimId);
        CommentDto.Response response = new CommentDto.Response();
        response.setMoimId(moimId);
        response.setCommentList(
              commentByMoim
                        .stream()
                        .map(comment -> comment.getContent().toString())
                        .collect(Collectors.toList()));
        return new ResponseEntity(response, HttpStatus.OK);
    }



}
