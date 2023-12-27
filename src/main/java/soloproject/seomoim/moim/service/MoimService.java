package soloproject.seomoim.moim.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import soloproject.seomoim.KakaoApi.dto.DocumentDto;
import soloproject.seomoim.KakaoApi.dto.KakaoApiResponseDto;
import soloproject.seomoim.KakaoApi.service.KakaoAddressSearchService;
import soloproject.seomoim.advice.exception.BusinessLogicException;
import soloproject.seomoim.advice.exception.ExceptionCode;
import soloproject.seomoim.member.entity.Member;
import soloproject.seomoim.member.service.MemberService;
import soloproject.seomoim.moim.dto.MoimSearchDto;
import soloproject.seomoim.moim.entitiy.Moim;
import soloproject.seomoim.moim.entitiy.MoimMember;
import soloproject.seomoim.moim.repository.MoimMemberRepository;
import soloproject.seomoim.moim.repository.MoimRepository;

import java.time.LocalDateTime;
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
    public Long createMoim(Long memberId, Moim moim){
        Member member = memberService.findMember(memberId);
        moim.setMember(member);

        if (member.getCreateMoims().size() > 3) {
            throw new IllegalStateException("모임은 최대 3개까지 개설 가능합니다.");
        }

        KakaoApiResponseDto kakaoApiResponseDto = kakaoAddressSearchService.requestAddressSearch(moim.getPlaceAddress());
        DocumentDto documentDto = kakaoApiResponseDto.getDocumentDtoList().get(0);
        moim.setLatitude(documentDto.getLatitude());
        moim.setLongitude(documentDto.getLongitude());
        moim.setAddressDong(documentDto.getRegion3DepthName());

        int dDay = moim.calculateDDay();
        moim.setDDay(dDay);

        Moim savedMoim = moimRepository.save(moim);

        joinMoim(savedMoim.getId(), memberId);

        return savedMoim.getId();
    }

    @Transactional
    public void updateMoim(Long moimId, Moim moim) {
        Moim findMoim = findMoim(moimId);
        Optional.ofNullable(moim.getTitle())
                .ifPresent(findMoim::setTitle);
        Optional.ofNullable(moim.getContent())
                .ifPresent(findMoim::setContent);
        Optional.ofNullable(moim.getTotalParticipantCount())
                .ifPresent(findMoim::setTotalParticipantCount);
        Optional.ofNullable(moim.getPlaceName())
                .ifPresent(findMoim::setPlaceName);
        Optional.ofNullable(moim.getPlaceAddress())
                .ifPresent(placeAddress->{
                    findMoim.setPlaceAddress(placeAddress);
                    DocumentDto documentDto = kakaoAddressSearchService.requestAddressSearch(placeAddress).getDocumentDtoList().get(0);
                    findMoim.setAddressDong(documentDto.getRegion3DepthName());
                });
        Optional.ofNullable(moim.getMoimCategory())
                .ifPresent(findMoim::setMoimCategory);
        Optional.ofNullable(moim.getStartedAt())
                .ifPresent(startedAt -> {
                    findMoim.setStartedAt(startedAt);
                    findMoim.setDDay(findMoim.calculateDDay());
                });
    }

    @Transactional
    public void deleteMoim(String loginMemberEmail, Long moimId) {
        Member loginMember = memberService.findByEmail(loginMemberEmail);
        Moim moim = findMoim(moimId);
        if (loginMember != moim.getMember()) {
            throw new IllegalStateException("모임장이 아니면 모임을 삭제 할 수없습니다.");
        }

        boolean noParticipants = moim.getParticipants().stream()
                .filter(moimMember -> !moimMember.getMember().equals(loginMember))
                .noneMatch(MoimMember::isStatus);

        if (noParticipants) {
            Optional<MoimMember> moimLeaderJoinStatus = moimMemberRepository.findByMemberAndMoim(moim.getMember(), moim);
            moimLeaderJoinStatus.ifPresent(moimMember -> {moimMemberRepository.delete(moimMember);});
            moimMemberRepository.delete(moimLeaderJoinStatus.get());
            moimRepository.delete(moim);
        } else {
            throw new IllegalStateException("참여한 멤버가 있으면 모임을 삭제할 수 없습니다.");
        }
    }

    @Transactional
    public void joinMoim(Long moimId, Long memberId){
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
    }

    @Transactional
    public void cancelJoinMoim(Long moimId, Long memberId) {
        Moim moim = findMoim(moimId);
        Member member = memberService.findMember(memberId);

        if(moim.getMember()==member){
            throw new IllegalStateException("모임장은 모임 참여를 취소할 수 없습니다.");
        }
        MoimMember findMoimMember = checkJoin(member,moim);
        findMoimMember.setStatus(false);
        findMoimMember.getMoim().reduceParticipantCount();
    }


    /* 해당 모임에 회원의 참여여부를 확인하기위한 메소드. 없으면 참여하지 않았기 때문에 false로 반환*/
    @Transactional
    public MoimMember checkJoin(Member member,Moim moim) {
        return moimMemberRepository.findByMemberAndMoim(member,moim)
                .orElseGet(() -> {
                    MoimMember defaultMoimMember= new MoimMember();
                    defaultMoimMember.setMoim(moim);
                    defaultMoimMember.setMember(member);
                    defaultMoimMember.setStatus(false);
                    return defaultMoimMember;
                });
    }

    public Page<Moim> findAllByPage(int page, int size) {
        return moimRepository.findAll(PageRequest.of(page, size, Sort.by("createdAt").descending()));
    }

    public Page<Moim> findAllBySearch(MoimSearchDto moimSearchDto, int page, int size){
        return moimRepository.searchAll(moimSearchDto, PageRequest.of(page, size));
    }


    public List<Moim> findTodayMoims(){
        LocalDateTime startOfDay = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime endOfDay = LocalDateTime.now().withHour(23).withMinute(59).withSecond(59).withNano(999999999);
        return moimRepository.findByStartedAtBetween(startOfDay,endOfDay);
    }

    public List<Moim> findPopularMoims() {
        return moimRepository.findByLikeCountGreaterThan(2,PageRequest.of(0,5));
    }

    public Moim findMoim(Long moimId) {
        return moimRepository.findById(moimId).orElseThrow(()
                -> new BusinessLogicException(ExceptionCode.NOT_EXISTS_MOIM));
    }

    public List<Moim> findAll() {
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

    //회원이 만든 moim 을 조회할수있다.
//    public List<Moim> findMyCreatedMoim(Long memberId){
//        Member member = memberService.findMember(memberId);
//        return moimRepository.findMoimsByMember(member);
//    }
}
