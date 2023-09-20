package soloproject.seomoim.moim.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import soloproject.seomoim.moim.entitiy.MoimMember;

/*모임멤버레파지토리가 꼭 필요할까 생각해보자*/
public interface MoimMemberRepository extends JpaRepository<MoimMember,Long> {

     @Query("select mm from MoimMember mm where mm.moim.id=:moimId and mm.member.id=:memberId")
     MoimMember findByMoimAndMember(@Param("moimId") Long moimId,@Param("memberId") Long memberId);
}
