package soloproject.seomoim.moim.like;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import soloproject.seomoim.member.entity.Member;
import soloproject.seomoim.member.service.MemberService;
import soloproject.seomoim.moim.entitiy.Moim;
import soloproject.seomoim.moim.service.MoimService;


@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class LikeMoimService {

    private final LikeMoimRepository likeMoimRepository;
    private final MemberService memberService;
    private final MoimService moimService;

    @Transactional
    public void like(Long moimId, Long memberId) {
        Moim moim = moimService.findMoim(moimId);
        Member member = memberService.findMemberById(memberId);
        LikeMoim likeMoim = checkLike(member, moim);
        if (likeMoim.isStatus()) {
            throw new IllegalStateException("이미 좋아요한 모임입니다");
        }
        likeMoim.setStatus(true);
        likeMoim.getMoim().likeCountUp();
        likeMoimRepository.save(likeMoim);
    }

    public void cancelLike(long moimId, long memberId) {
        Moim moim = moimService.findMoim(moimId);
        Member member = memberService.findMemberById(memberId);
        LikeMoim likeMoim = checkLike(member, moim);
        if (!likeMoim.isStatus()) {
            throw new IllegalStateException("좋아요 하지 않은 모임입니다");
        }
        likeMoim.setStatus(false);
        moim.likeCountDown();
    }

    @Transactional
    public LikeMoim checkLike(Member member, Moim moim) {
        return likeMoimRepository.findLikeMoimByMemberAndMoim(member, moim)
                .orElseGet(() -> {
                    LikeMoim likeMoim = new LikeMoim();
                    likeMoim.setMoim(moim);
                    likeMoim.setMember(member);
                    likeMoim.setStatus(false);
                    return likeMoim;
                });
    }
}
