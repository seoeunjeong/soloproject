package soloproject.seomoim.like;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import soloproject.seomoim.member.entity.Member;
import soloproject.seomoim.member.service.MemberService;
import soloproject.seomoim.moim.entitiy.Moim;
import soloproject.seomoim.moim.service.MoimService;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class LikeMoimService {

    private final LikeMoimRepository likeRepository;
    private final MemberService memberService;
    private final MoimService moimService;
    public Long save(Long memberId,Long moimId){
        Member member = memberService.findMember(memberId);
        Moim moim = moimService.findMoim(moimId);
        alreadyLike(member,moim);
        LikeMoim like = new LikeMoim();
        like.setMoim(moim);
        like.setMember(member);
        LikeMoim saveLike = likeRepository.save(like);
        moim.likeCountUp();
        return saveLike.getId();
    }


    public void cancelLike(long memberId,long moimId){
        Member member = memberService.findMember(memberId);
        Moim moim = moimService.findMoim(moimId);
        Optional<LikeMoim> findLike = likeRepository.findLikeMoimByMemberAndMoim(member, moim);
        LikeMoim likeMoim = findLike.orElseThrow(() -> new IllegalStateException("좋아요 정보가 없습니다"));
        moim.likeCountDown();
        likeRepository.delete(likeMoim);
    }

    private void alreadyLike(Member member,Moim moim){
        Optional<LikeMoim> likeMoimByMember = likeRepository.findLikeMoimByMemberAndMoim(member,moim);
        if(likeMoimByMember.isPresent()){
            throw new IllegalStateException("이미 좋아요한 모임입니다.");
        }
    }

}
