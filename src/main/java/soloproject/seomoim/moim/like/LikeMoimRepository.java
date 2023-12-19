package soloproject.seomoim.moim.like;

import org.springframework.data.jpa.repository.JpaRepository;
import soloproject.seomoim.member.entity.Member;
import soloproject.seomoim.moim.entitiy.Moim;

import java.util.Optional;

public interface LikeMoimRepository extends JpaRepository<LikeMoim, Long> {
    Optional<LikeMoim> findLikeMoimByMemberAndMoim(Member member, Moim moim);
}
