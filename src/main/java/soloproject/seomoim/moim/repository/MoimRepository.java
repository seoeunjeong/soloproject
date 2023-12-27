package soloproject.seomoim.moim.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import soloproject.seomoim.member.entity.Member;
import soloproject.seomoim.moim.entitiy.Moim;
import soloproject.seomoim.moim.repository.querydsl.MoimRepositoryCustom;

import java.time.LocalDateTime;
import java.util.List;

public interface MoimRepository extends JpaRepository<Moim,Long> , MoimRepositoryCustom{

    /* memberId 로 회원이 만든 모임 list 조회*/
    List<Moim> findMoimsByMember(Member member);

    List<Moim> findByStartedAtBetween(LocalDateTime startOfDay, LocalDateTime endOfDay);
    @Query("SELECT m From Moim m WHERE m.likeCount >:likeCount ORDER BY m.likeCount DESC")
    List<Moim> findByLikeCountGreaterThan(@Param("likeCount") int likeCount, Pageable pageable);
}

