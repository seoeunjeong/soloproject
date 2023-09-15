package soloproject.seomoim.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import soloproject.seomoim.domain.Moim;

@Repository
public interface MoimRepository extends JpaRepository<Moim,Long> {
}
