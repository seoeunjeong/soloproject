package soloproject.seomoim.chat;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import soloproject.seomoim.member.entity.Member;

import java.util.Collection;
import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    @Query("SELECT c FROM ChatRoom c JOIN c.members m WHERE m = :member")
    Optional<ChatRoom> findByMember(@Param("member") Member member);

}
