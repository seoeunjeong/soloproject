package soloproject.seomoim.comment;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import soloproject.seomoim.member.entity.Member;
import soloproject.seomoim.member.service.MemberService;
import soloproject.seomoim.moim.entitiy.Moim;
import soloproject.seomoim.moim.service.MoimService;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentService {

    private final CommentRepository commentRepository;
    private final MemberService memberService;
    private final MoimService moimService;

    public Long save(CommentDto.Post request){
        Moim moim = moimService.findMoim(request.getMoimId());
        Member member = memberService.findMember(request.getMemberId());
        Comment comment = new Comment();
        comment.setMember(member);
        comment.setMoim(moim);
        comment.setContent(request.getContent());
        Comment savedComment = commentRepository.save(comment);
        return savedComment.getId();
    }


    public Comment update(Long commentId,CommentDto.Update request) {
        Comment findComment = findComment(commentId);
        Optional.ofNullable(request.getContent())
                .ifPresent(content -> findComment.setContent(content));
        return findComment;
    }

    public void delete(Long id){
        Comment comment = findComment(id);
        commentRepository.delete(comment);
    }

    public Comment findComment(Long id){
        Optional<Comment> findComment = commentRepository.findById(id);
        return findComment.orElseThrow(() -> new IllegalStateException("존재하지않는 댓글입니다"));
    }

    public List<Comment> findCommentByMember(Long memberid){
        return commentRepository.findByMember_id(memberid);
    }

    public List<Comment> findCommentByMoim(Long moimid){

        return commentRepository.findByMoim_id(moimid);
    }
}
