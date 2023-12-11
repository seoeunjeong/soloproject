package soloproject.seomoim.like;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import soloproject.seomoim.member.entity.Member;
import soloproject.seomoim.member.service.MemberService;
import soloproject.seomoim.moim.entitiy.Moim;
import soloproject.seomoim.moim.service.MoimService;

import java.util.Optional;

import static soloproject.seomoim.like.QLikeMoim.likeMoim;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class LikeMoimService {

    private final LikeMoimRepository likeMoimRepository;
    private final MemberService memberService;
    private final MoimService moimService;

    public void like(Long memberId, Long moimId) {
        Member member = memberService.findMember(memberId);
        Moim moim = moimService.findMoim(moimId);
        LikeMoim likeMoim = checkLike(member, moim);
        LikeMoim saveLike = likeMoimRepository.save(likeMoim);
        moim.likeCountUp();
        saveLike.setStatus(true);
    }
    public void cancelLike(long memberId, long moimId) {
        Member member = memberService.findMember(memberId);
        Moim moim = moimService.findMoim(moimId);
        LikeMoim likeMoim = likeMoimRepository.findLikeMoimByMemberAndMoim(member, moim)
                .orElseThrow(() -> new IllegalStateException("This moim not like"));
        moim.likeCountDown();
        likeMoimRepository.delete(likeMoim);
    }

    @Transactional
    public LikeMoim checkLike(Member member, Moim moim) {
        return likeMoimRepository.findLikeMoimByMemberAndMoim(member, moim)
                .orElseGet(()->
                        new LikeMoim(moim,member));

    }
}
