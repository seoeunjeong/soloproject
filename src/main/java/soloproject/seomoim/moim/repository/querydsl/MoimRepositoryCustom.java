package soloproject.seomoim.moim.repository.querydsl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import soloproject.seomoim.moim.dto.MoimSearchDto;
import soloproject.seomoim.moim.entitiy.Moim;

import java.util.List;

public interface MoimRepositoryCustom {

    Page<Moim> searchAll(MoimSearchDto searchDto,Pageable pageable);

    List<Moim> keywordSearch(MoimSearchDto searchDto);
}
