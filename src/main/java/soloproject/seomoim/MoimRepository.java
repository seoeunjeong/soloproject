package soloproject.seomoim;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MoimRepository extends JpaRepository<Moim,Long> {
}
