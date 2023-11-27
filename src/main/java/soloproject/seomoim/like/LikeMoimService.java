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

    private final LikeMoimRepository likeMoimRepository;
    private final MemberService memberService;
    private final MoimService moimService;

    public void like(Long memberId, Long moimId) {
        Member member = memberService.findMember(memberId);
        Moim moim = moimService.findMoim(moimId);
        checkLike(member,moim);
        LikeMoim likeMoim = new LikeMoim();
        likeMoim.setMoim(moim);
        likeMoim.setMember(member);
        LikeMoim saveLike = likeMoimRepository.save(likeMoim);
        moim.likeCountUp();
    }
    public void cancelLike(long memberId, long moimId) {
        Member member = memberService.findMember(memberId);
        Moim moim = moimService.findMoim(moimId);
        LikeMoim likeMoim = likeMoimRepository.findLikeMoimByMemberAndMoim(member, moim)
                .orElseThrow(() -> new IllegalStateException("This moim not like"));
        moim.likeCountDown();
        likeMoimRepository.delete(likeMoim);
    }

    private void checkLike(Member member, Moim moim) {
        if (likeMoimRepository.findLikeMoimByMemberAndMoim(member, moim) != null) {
            throw new IllegalStateException("This moim is already liked.");
        }
    }
}
