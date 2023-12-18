package soloproject.seomoim.moim.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import soloproject.seomoim.member.entity.Member;
import soloproject.seomoim.moim.entitiy.Moim;
import soloproject.seomoim.moim.entitiy.MoimMember;

import java.util.List;
import java.util.Optional;

/*모임멤버레파지토리가 꼭 필요할까 생각해보자
모임 ID로 검색시 참여회원 알수있고
회원 Id로 조회시 참여한 모임알수있다.*/
public interface MoimMemberRepository extends JpaRepository<MoimMember,Long> {

//     //회원id와 모임 id와 일치하는 거 찾아서 참여취소, 참여시에 이미참여했는지 여부확인
//     @Query("select mm from MoimMember mm where mm.moim.id=:moimId and mm.member.id=:memberId")
//     Optional<MoimMember> findByMoimAndMember(@Param("moimId") Long moimId, @Param("memberId") Long memberId);
     Optional<MoimMember> findByMemberAndMoim(Member member, Moim moim);

     @Query("select m.member from MoimMember m where m.moim.id=:moimId")
     List<Member> findJoinMembers(@Param("moimId") Long moimId);

     @Query("select m from MoimMember m where m.member.id = :memberId")
     List<MoimMember> findJoinMoims(@Param("memberId") Long memberId);

}
