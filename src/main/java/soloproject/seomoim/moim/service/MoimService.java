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
    public Long createMoim(Long memberId, Moim moim) {

        Member member = memberService.findMember(memberId);
        moim.setMember(member);

        KakaoApiResponseDto kakaoApiResponseDto = kakaoAddressSearchService.requestAddressSearch(moim.getRegion());
        DocumentDto documentDto = kakaoApiResponseDto.getDocumentDtoList().get(0);

        moim.setLatitude(documentDto.getLatitude());
        moim.setLongitude(documentDto.getLongitude());

        Moim savedMoim = moimRepository.save(moim);

        joinMoim(savedMoim.getId(), memberId);

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
        MoimMember joinMoim = moimMemberRepository.findByMoimAndMember(moimId, memberId);
        if (joinMoim != null) {
            if (joinMoim.isStatus()) {
                throw new BusinessLogicException(ExceptionCode.ALREADY_JOIN_MOIM);
            }
            joinMoim.setStatus(true);
        } else {
            MoimMember moimMember = new MoimMember();
            Moim findMoim = findMoim(moimId);
            Member joinMember = memberService.findMember(memberId);
            moimMember.setMoim(findMoim);
            moimMember.setMember(joinMember);
            moimMember.setStatus(true);
            moimMemberRepository.save(moimMember);
            findMoim.addCount();
        }
    }

    @Transactional
    public void cancelJoinMoim(Long moimId, Long memberId) {
        MoimMember findMoimMember = moimMemberRepository.findByMoimAndMember(moimId, memberId);
        moimMemberRepository.delete(findMoimMember);
        Moim moim = findMoim(moimId);
        moim.reduceCount();
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

    public void deleteMoim(Long moimId){
        Moim moim = findMoim(moimId);
        moimRepository.delete(moim);
    }


    /*모임 전체조회 페이지네이션 */
    public Page<Moim> findAllbyPage(int page,int size){
        Page<Moim> moims = moimRepository.findAll(PageRequest.of(page, size, Sort.by("createdAt").descending()));
        return moims;
    }

    public List<Moim> findTodayMoims(){
        LocalDate today = LocalDate.now();
        LocalDateTime startOfDay = LocalDateTime.of(today, LocalTime.MIN);
        LocalDateTime endOfDay = LocalDateTime.of(today, LocalTime.MAX);

        return moimRepository.findByStartedAtBetween(startOfDay, endOfDay);
    }

}
