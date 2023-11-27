package soloproject.seomoim.moim.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import soloproject.seomoim.KakaoApi.dto.DocumentDto;
import soloproject.seomoim.KakaoApi.dto.KakaoApiResponseDto;
import soloproject.seomoim.KakaoApi.service.KakaoAddressSearchService;
import soloproject.seomoim.exception.BusinessLogicException;
import soloproject.seomoim.exception.ExceptionCode;
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
    private final KakaoAddressSearchService kakaoAddressSearchService;

    public Long createMoim(Long memberId,Moim moim){
        Member member = memberService.findMember(memberId);
        moim.setMember(member);
        KakaoApiResponseDto kakaoApiResponseDto = kakaoAddressSearchService.requestAddressSearch(moim.getRegion());
        DocumentDto documentDto = kakaoApiResponseDto.getDocumentDtoList().get(0);
        moim.setLatitude(documentDto.getLatitude());
        moim.setLongitude(documentDto.getLongitude());
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
        return findMoin.orElseThrow(()->new BusinessLogicException(ExceptionCode.NOT_EXISTS_MOIM));
    }


    public List<Moim> findCreatedMoim(Member member){
        return moimRepository.findMoimsByMember(member);
    }
    public void deleteMoim(Long moimId){
        Moim moim = findMoim(moimId);
        moimRepository.delete(moim);
    }

    /*
     * 전체모임 페이지 네이션 구현*/
    public Page<Moim> findAllbyPage(int page,int size){
        Page<Moim> moims = moimRepository.findAll(PageRequest.of(page, size, Sort.by("createdAt").descending()));
        return moims;
    }

    public List<Moim> findAll(){
        return moimRepository.findAll();
    }


//    모임 검색,페이지네이션
//    public Page<Moim> findAllSearch(MoimSearchDto moimSearchDto, int page, int size) {
//        Page<Moim> moims = moimRepository.searchAll(moimSearchDto, PageRequest.of(page, size));
//        return moims;
//    }

    /*회원이 모임에 가입하는 로직 moimMember table에 저장*/
    @Transactional
    public Moim joinMoim(Long moimId, Long memberId) {
        MoimMember byMoimAndMember = moimMemberRepository.findByMoimAndMember(moimId, memberId);
        if (byMoimAndMember!=null) {
            throw new BusinessLogicException(ExceptionCode.ALREADY_JOIN_MOIM);
        }
        Moim moim = findMoim(moimId);
        Member member = memberService.findMember(memberId);
        moim.joinMoim(moim, member);
        return moim;
    }

    public void notJoinMoim(Long moimId,Long memberId){
        Moim moim = findMoim(moimId);
        moim.reduceCount();
        MoimMember findMoimMember = moimMemberRepository.findByMoimAndMember(moim.getId(), memberId);
        moimMemberRepository.delete(findMoimMember);
    }

    public Page<Moim> findAllSearch(MoimSearchDto moimSearchDto, int i, int i1) {
        return null;
    }
}
