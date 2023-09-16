package soloproject.seomoim.moim.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import soloproject.seomoim.member.domain.Member;
import soloproject.seomoim.member.repository.MemberRepository;
import soloproject.seomoim.member.service.MemberService;
import soloproject.seomoim.moim.entitiy.Moim;
import soloproject.seomoim.moim.repository.MoimRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MoimService {

    private final MoimRepository moimRepository;

    private final MemberService memberService;

    public Long createMoim(Long memberId,Moim moim){
        Member member = memberService.findMember(memberId);
        moim.setMember(member);
        Moim saveMoim = moimRepository.save(moim);
        return saveMoim.getId();
    }
}
