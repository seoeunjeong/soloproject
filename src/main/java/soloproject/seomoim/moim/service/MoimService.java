package soloproject.seomoim.moim.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
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
    public Long createMoim(Long memberId,Moim moim) throws Exception {
        Member member = memberService.findMember(memberId);
        moim.setMember(member);
        if(member.getCreateMoims().size()>3){
            throw new IllegalStateException("모임은 최대 3개까지 개설 가능합니다.");
        }

        KakaoApiResponseDto kakaoApiResponseDto = kakaoAddressSearchService.requestAddressSearch(moim.getPlaceAddress());
        DocumentDto documentDto = kakaoApiResponseDto.getDocumentDtoList().get(0);

        moim.setLatitude(documentDto.getLatitude());
        moim.setLongitude(documentDto.getLongitude());

        //moim.dDay 컬럼 추가
        int dDay = moim.calculateDDay();

        moim.setDDay(dDay);

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
         Optional.ofNullable(moim.getPlaceAddress())
                .ifPresent(placeAddress -> findMoim.setPlaceAddress(placeAddress));
         Optional.ofNullable(moim.getMoimCategory())
                .ifPresent(moimCategory -> findMoim.setMoimCategory(moimCategory));
        Optional.ofNullable(moim.getStartedAt())
                .ifPresent(startedAt->findMoim.setStartedAt(startedAt));
        Optional.ofNullable(moim.getDDay())
                .ifPresent(dDay->findMoim.setDDay(dDay));
         return findMoim;
    }

    @Transactional
    public MoimMember joinMoim(Long moimId, Long memberId) throws Exception{
        Moim moim = findMoim(moimId);
        Member member = memberService.findMember(memberId);
        MoimMember joinStatus = checkJoin(member,moim);

        if (joinStatus.isStatus()) {
            throw new BusinessLogicException(ExceptionCode.ALREADY_JOIN_MOIM);
        }
        if(moim.getTotalParticipantCount()==moim.getParticipantCount()){
            throw new BusinessLogicException(ExceptionCode.NOT_JOIN_MOIM);
        }
        joinStatus.setStatus(true);
        joinStatus.getMoim().addParticipantCount();
//        moimMemberRepository.save(joinStatus); 변경감지사용?
        return joinStatus;
    }

    /* 해당 모임에 회원의 참여여부를 확인하기위한 moimMember를 반환해준다. 없으면 참여하지않았기 때문에 false로 반환*/
    @Transactional(readOnly = true)
    public MoimMember checkJoin(Member member,Moim moim) {
        MoimMember moimMember = moimMemberRepository.findByMemberAndMoim(member,moim)
                .orElseGet(() -> {
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
        Moim moim = findMoim(moimId);
        Member member = memberService.findMember(memberId);
        MoimMember findMoimMember = checkJoin(member,moim);
        findMoimMember.setStatus(false);
        findMoimMember.getMoim().reduceParticipantCount();
    }

    @Transactional
    public void deleteMoim(String loginMemberEmail,Long moimId){
        Member loginMember = memberService.findByEmail(loginMemberEmail);
        Moim moim = findMoim(moimId);
        if (loginMember != moim.getMember()) {
            throw new IllegalStateException("모임장이 아니면 모임을 삭제 할 수없습니다.");
        }

        boolean allMoimMembersFalse = moim.getParticipants().stream()
                .filter(moimMember -> !moimMember.getMember().equals(loginMember)) // loginMember 제외
                .allMatch(moimMember -> !moimMember.isStatus());

        Optional<MoimMember> byMemberAndMoim = moimMemberRepository.findByMemberAndMoim(loginMember,moim);
        moimMemberRepository.delete(byMemberAndMoim.get());
        // Moim을 삭제 조건 확인
        if (allMoimMembersFalse) {
            moimRepository.delete(moim);
        } else {
            throw new IllegalStateException("참여한멤버가 있으면 모임을 삭제할수 없습니다.");
        }
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
        return moimRepository.findByStartedAt(today);
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


//스케줄링을 통하여 매일 자정 dday업로드.
    @Scheduled(cron = "0 0 0 * * ?")
    public void updateDDay() {
        List<Moim> allMoim = findAll();

        for (Moim moim : allMoim) {
            int dDay = moim.calculateDDay();
            moim.setDDay(dDay);
            moimRepository.save(moim);
        }
    }
}
