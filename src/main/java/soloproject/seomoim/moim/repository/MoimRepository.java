package soloproject.seomoim.moim.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import soloproject.seomoim.member.entity.Member;
import soloproject.seomoim.moim.entitiy.Moim;
import soloproject.seomoim.moim.repository.querydsl.MoimRepositoryCustom;

import java.util.List;

public interface MoimRepository extends JpaRepository<Moim,Long> , MoimRepositoryCustom{

    /* memberId 로 회원이 만든 모임 list 조회*/
    List<Moim> findMoimsByMember(Member member);

}

