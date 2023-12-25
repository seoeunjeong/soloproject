package soloproject.seomoim.moim.repository.querydsl;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import soloproject.seomoim.moim.dto.MoimSearchDto;
import soloproject.seomoim.moim.entitiy.Moim;
import soloproject.seomoim.moim.entitiy.MoimCategory;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static soloproject.seomoim.moim.entitiy.QMoim.*;

public class MoimRepositoryImpl implements MoimRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public MoimRepositoryImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    /*모임 날짜,카테고리,검색어 기반 조회 페이지네이션*/
    @Override
    public Page<Moim> searchAll(MoimSearchDto searchDto, Pageable pageable) {
        QueryResults<Moim> results = queryFactory
                .selectFrom(moim)
                .where(
                        moimStartedAt(searchDto.getStartedAt()),
                        moimCategory(searchDto.getMoimCategory()),
                        moimKeyword(searchDto.getKeyword())
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        List<Moim> content = results.getResults();

        long total = results.getTotal();

        return new PageImpl<>(content, pageable, total);
    }

    /* or 조합*/
    private BooleanExpression moimKeyword(String keyword){
        return keyword != null ? moimTitleKeyword(keyword).or(moimContentKeyword(keyword)) : null;
    }

    private BooleanExpression moimStartedAt(LocalDate searchDay) {
        LocalDateTime fromDay = searchDay.atStartOfDay();
        LocalDateTime toDay = searchDay.atTime(23, 59, 59, 999999999);
        return searchDay != null ? moim.startedAt.between(fromDay,toDay) : null;
    }

    private BooleanExpression moimCategory(MoimCategory moimCategory) {
        return moimCategory != null ? moim.moimCategory.eq(moimCategory) : null;
    }
    private BooleanExpression moimTitleKeyword(String keyword) {
        return keyword != null ? moim.title.like("%"+keyword+"%"): null;
    }
    private BooleanExpression moimContentKeyword(String keyword) {
        return keyword != null ? moim.content.contains(keyword): null;
    }

}
