package soloproject.seomoim.moim.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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

    @Transactional
    public Long createMoim(Long memberId,Moim moim) {
        Member member = memberService.findMember(memberId);
        moim.setMember(member);

        KakaoApiResponseDto kakaoApiResponseDto = kakaoAddressSearchService.requestAddressSearch(moim.getRegion());
        DocumentDto documentDto = kakaoApiResponseDto.getDocumentDtoList().get(0);

        moim.setLatitude(documentDto.getLatitude());
        moim.setLongitude(documentDto.getLongitude());

        Moim savedMoim = moimRepository.save(moim);

        joinMoim(savedMoim.getId(),memberId);

        return savedMoim.getId();
    }
    @Transactional
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

    @Transactional
    public void joinMoim(Long moimId, Long memberId) {
        MoimMember joinStatus = checkJoin(moimId, memberId);

        if (joinStatus.isStatus()) {
            throw new BusinessLogicException(ExceptionCode.ALREADY_JOIN_MOIM);
        }

        joinStatus.setStatus(true);
        joinStatus.getMoim().addParticipantCount();
//        moimMemberRepository.save(joinStatus); 변경감지사용?
    }

    /* 해당 모임에 회원의 참여여부를 확인하기위한 moimMember를 반환해준다. 없으면 참여하지않았기 때문에 false로 반환*/
    public MoimMember checkJoin(Long moimId, Long memberId) {
        MoimMember moimMember = moimMemberRepository.findByMoimAndMember(moimId, memberId)
                .orElseGet(() -> {
                    Moim moim = findMoim(moimId);
                    Member member = memberService.findMember(memberId);
                    MoimMember defaultMoimMember= new MoimMember();
                    defaultMoimMember.setMoim(moim);
                    defaultMoimMember.setMember(member);
                    defaultMoimMember.setStatus(false);
                    return defaultMoimMember;
                });
        return moimMember;
    }

    @Transactional
    public void cancelJoinMoim(Long moimId, Long memberId) {
        MoimMember findMoimMember = checkJoin(moimId, memberId);
        findMoimMember.setStatus(false);
        findMoimMember.getMoim().reduceParticipantCount();
    }

    public void deleteMoim(Long moimId){
        Moim moim = findMoim(moimId);
        moimRepository.delete(moim);
    }

    public Page<Moim> findAllbyPage(int page, int size) {
        Page<Moim> moims = moimRepository.findAll(PageRequest.of(page, size, Sort.by("createdAt").descending()));
        return moims;
    }
    public Page<Moim> findAllbySearch(MoimSearchDto moimSearchDto, int page, int size){
        Page<Moim> moims = moimRepository.searchAll(moimSearchDto, PageRequest.of(page, size));
        return moims;
    }


    public List<Moim> findTodayMoims(){
        LocalDate today = LocalDate.now();
        LocalDateTime startOfDay = LocalDateTime.of(today, LocalTime.MIN);
        LocalDateTime endOfDay = LocalDateTime.of(today, LocalTime.MAX);

        return moimRepository.findByStartedAtBetween(startOfDay, endOfDay);
    }

    public List<Moim> findPopularMoims() {
        return moimRepository.findByLikeCountGreaterThan(2,PageRequest.of(0,5));
    }

    //회원이 만든 moim 을 조회할수있다.
    public List<Moim> findMyCreatedMoim(Long memberId){
        Member member = memberService.findMember(memberId);
        return moimRepository.findMoimsByMember(member);
    }


    public Moim findMoim(Long moimId) {
        return moimRepository.findById(moimId).orElseThrow(()
                -> new BusinessLogicException(ExceptionCode.NOT_EXISTS_MOIM));
    }

    public List<Moim> findAll(){
        return moimRepository.findAll();
    }

}
