package soloproject.seomoim.moim.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import soloproject.seomoim.PageResponseDto;
import soloproject.seomoim.member.entity.Member;
import soloproject.seomoim.member.service.MemberService;
import soloproject.seomoim.moim.dto.MoimSearchDto;
import soloproject.seomoim.moim.entitiy.Moim;
import soloproject.seomoim.moim.entitiy.MoimMember;
import soloproject.seomoim.moim.repository.MoimMemberRepository;
import soloproject.seomoim.moim.repository.MoimRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class MoimService {

    private final MoimRepository moimRepository;

    private final MemberService memberService;

    private final MoimMemberRepository moimMemberRepository;

    public Long createMoim(Long memberId,Moim moim){
        Member member = memberService.findMember(memberId);
        moim.setMember(member);
        Moim saveMoim = moimRepository.save(moim);
        joinMoim(saveMoim.getId(), memberId);
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

    /*
     * 전체모임 페이지 네이션 구현*/
    public Page<Moim> fillAll(int page,int size){
        return moimRepository.findAll(PageRequest.of(page, size, Sort.by("createdAt").descending()));
    }

    /*모임 검색,페이지네이션*/
    public Page<Moim> findAllSearch(MoimSearchDto moimSearchDto,int page,int size) {
        Page<Moim> moims = moimRepository.searchAll(moimSearchDto, PageRequest.of(page,size));
        return moims;
    }

    //회원이 모임에 가입하는 로직
    @Transactional
    public Moim joinMoim(Long moimId, Long memberId) {
        Moim moim = findMoim(moimId);
        Member member = memberService.findMember(memberId);
        moim.joinMoim(moim, member);//여기서 메소드가 동작해서 조회한 모임의 정보가 변경된다.
        return moim;
    }

    public void notJoinMoim(Long moimId,Long memberId){
        Moim moim = findMoim(moimId);
        moim.reduceCount();
        MoimMember findMoimMember = moimMemberRepository.findByMoimAndMember(moim.getId(), memberId);
        moimMemberRepository.delete(findMoimMember);
    }

}
