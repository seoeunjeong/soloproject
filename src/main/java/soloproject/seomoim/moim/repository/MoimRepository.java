package soloproject.seomoim.moim.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import soloproject.seomoim.moim.dto.MoimSearchDto;
import soloproject.seomoim.moim.entitiy.Moim;
import soloproject.seomoim.moim.repository.querydsl.MoimRepositoryCustom;

public interface MoimRepository extends JpaRepository<Moim,Long> , MoimRepositoryCustom{

}
