package soloproject.seomoim.moim.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import soloproject.seomoim.moim.entitiy.Moim;
import soloproject.seomoim.moim.repository.querydsl.MoimRepositoryCustom;

import java.time.LocalDateTime;
import java.util.List;

public interface MoimRepository extends JpaRepository<Moim,Long> , MoimRepositoryCustom{

    List<Moim> findByStartedAtBetween(LocalDateTime startOfDay, LocalDateTime endOfDay);

    @Query("SELECT m FROM Moim m ORDER BY m.likeCount DESC")
    List<Moim> findLikeCountTop5(Pageable pageable);

    @Query("SELECT m From Moim m WHERE m.likeCount >:likeCount ORDER BY m.likeCount DESC")
    List<Moim> findByLikeCountGreaterThan(@Param("likeCount") int likeCount, Pageable pageable);

}

