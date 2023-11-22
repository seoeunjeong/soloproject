package soloproject.seomoim.moim.repository.querydsl;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import soloproject.seomoim.moim.dto.MoimSearchDto;
import soloproject.seomoim.moim.entitiy.Moim;
import soloproject.seomoim.moim.entitiy.MoimCategory;
import soloproject.seomoim.moim.entitiy.QMoim;

import java.util.List;

import static soloproject.seomoim.moim.entitiy.QMoim.*;

public class MoimRepositoryImpl implements MoimRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public MoimRepositoryImpl(JPAQueryFactory queryFactory)
    {
        this.queryFactory = queryFactory;
    }

    /*모임 위치,카테고리,검색어 기반 조회*/
    @Override
    public Page<Moim> searchAll(MoimSearchDto searchDto,Pageable pageable) {
        QueryResults<Moim> results = queryFactory
                .selectFrom(moim)
                .where(moimRegion(searchDto.getRegion()),
                        moimCategory(searchDto.getMoimCategory())
//                        moimKeyword(searchDto.getKeyword())
                )
                .offset(pageable.getOffset())
//        페이지 요청 을 1페이지의 size 10개를 보고싶다고 하는것은 데이터 0부터 10까지 보고싶다는 말이다
                .limit(pageable.getPageSize())
                .fetchResults();

        List<Moim> content = results.getResults();
        long total = results.getTotal();

        return new PageImpl<>(content, pageable, total);
    }


//    /* or 조합*/
//    private BooleanExpression moimKeyword(String keyword){
//        return keyword != null ? moimTitleKeyword(keyword).or(moimContentKeyword(keyword)) : null;
//    }

    private BooleanExpression moimRegion(String region) {
        return region != null ? moim.region.eq(region) : null;
    }

    private BooleanExpression moimCategory(MoimCategory moimCategory) {
        return moimCategory != null ? moim.moimCategory.eq(moimCategory) : null;
    }
    private BooleanExpression moimTitleKeyword(String keyword) {
        return keyword != null ? moim.title.like("%"+keyword+"%"): null;
    }
//    private BooleanExpression moimContentKeyword(String keyword) {
//        return keyword != null ? moim.content.contains(keyword): null;
//    }

}
