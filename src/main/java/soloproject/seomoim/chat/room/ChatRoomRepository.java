package soloproject.seomoim.chat.room;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import soloproject.seomoim.member.entity.Member;

import java.util.List;
import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    //member의 모든 채팅방조회
    @Query("SELECT c FROM ChatRoom c WHERE c.ownerMember = :member or c.requestMember =:member")
    List<ChatRoom> findByMember(@Param("member") Member member);

    Optional<ChatRoom> findByOwnerMemberAndRequestMember(Member ownerMember, Member requestMember);


}
