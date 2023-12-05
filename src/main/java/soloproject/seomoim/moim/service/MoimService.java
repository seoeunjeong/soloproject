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


    //회원이 만든 moim 을 조회할수있다.

    public List<Moim> findCreatedMoim(Long memberId){
        Member member = memberService.findMember(memberId);
        return moimRepository.findMoimsByMember(member);
    }
    //모임삭제

    public void deleteMoim(Long moimId){
        Moim moim = findMoim(moimId);
        moimRepository.delete(moim);
    }
    public List<Moim> findAll(){
        return moimRepository.findAll();
    }


    /*회원이 모임에 참여하는 로직 moimMember table에 저장*/

    public Moim joinMoim(Long moimId, Long memberId) {
        MoimMember join = moimMemberRepository.findByMoimAndMember(moimId, memberId);

        if (join!=null) {
            throw new BusinessLogicException(ExceptionCode.ALREADY_JOIN_MOIM);
        }

        Moim moim = findMoim(moimId);
        Member member = memberService.findMember(memberId);

        moim.joinMoim(moim, member);

        return moim;
    }
    /*회원이 모임에 참여취소 로직 moimMember table에 삭제*/

    public void notJoinMoim(Long moimId,Long memberId){
        Moim moim = findMoim(moimId);
        moim.reduceCount();
        MoimMember findMoimMember = moimMemberRepository.findByMoimAndMember(moim.getId(), memberId);
        moimMemberRepository.delete(findMoimMember);
    }
    /*모임 전체조회 페이지네이션 */

    public Page<Moim> findAllbyPage(int page,int size){
        Page<Moim> moims = moimRepository.findAll(PageRequest.of(page, size, Sort.by("createdAt").descending()));
        return moims;
    }

    public Moim findMoim(Long moimId) {
        return moimRepository.findById(moimId).orElseThrow(() -> new BusinessLogicException(ExceptionCode.NOT_EXISTS_MOIM));
    }
}
