package soloproject.seomoim.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import soloproject.seomoim.member.domain.Member;
import soloproject.seomoim.moim.entitiy.Moim;
import soloproject.seomoim.moim.entitiy.MoimMember;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member,Long> {
    Optional<Member> findByEmail(String email);

    //참여한 모임 조회
    @Query("select DISTINCT m.participationMoims from Member m join m.participationMoims where m.id = :memberId")
    List<MoimMember> findByParticipationMoims(@Param("memberId") Long id);

}
