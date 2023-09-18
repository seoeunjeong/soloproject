package soloproject.seomoim.moim.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import soloproject.seomoim.member.domain.Member;
import soloproject.seomoim.member.repository.MemberRepository;
import soloproject.seomoim.member.service.MemberService;
import soloproject.seomoim.moim.dto.MoimDto;
import soloproject.seomoim.moim.entitiy.Moim;
import soloproject.seomoim.moim.entitiy.MoimCategory;
import soloproject.seomoim.moim.entitiy.MoimMember;
import soloproject.seomoim.moim.repository.MoimRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class MoimService {

    private final MoimRepository moimRepository;

    private final MemberService memberService;

    public Long createMoim(Long memberId,Moim moim){
        Member member = memberService.findMember(memberId);
        moim.setMember(member);
        Moim saveMoim = moimRepository.save(moim);
        return saveMoim.getId();
    }


    public Moim updateMoim(Long moimId, Moim moim){
        Moim findMoim = findMoim(moimId);
        Optional.ofNullable(moim.getTitle())
                .ifPresent(title->findMoim.setTitle(title));
        Optional.ofNullable(moim.getContent())
                .ifPresent(content -> findMoim.setContent(content));
         Optional.ofNullable(moim.getTotalParticipantCount())
                .ifPresent(totalParticipantCount -> findMoim.setTotalParticipantCount(totalParticipantCount));
         Optional.ofNullable(moim.getRegion())
                .ifPresent(region -> findMoim.setRegion(region));
         Optional.ofNullable(moim.getMoimCategory())
                .ifPresent(moimCategory -> findMoim.setMoimCategory(moimCategory));
         return findMoim;
    }

    public Moim findMoim(Long moimId){
        Optional<Moim> findMoin = moimRepository.findById(moimId);
        return findMoin.orElseThrow(()->new IllegalStateException("존재하지않는 모임입니다"));
    }

    public void deleteMoim(Long moimId){
        Moim moim = findMoim(moimId);
        moimRepository.delete(moim);
    }

    //회원이 모임에 가입하는 로직

    @Transactional
    public Moim joinMoim(Long moimId,Long memberId){
        Moim moim = findMoim(moimId);
        Member member = memberService.findMember(memberId);
        moim.addParticipant(moim,member);
        return moim;
    }
}
