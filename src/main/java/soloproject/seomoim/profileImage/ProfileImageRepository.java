package soloproject.seomoim.profileImage;

import org.springframework.data.jpa.repository.JpaRepository;
import soloproject.seomoim.member.entity.Member;

import java.util.Optional;

public interface ProfileImageRepository extends JpaRepository<ProfileImage,Long> {

    Optional<ProfileImage> findByMember(Member member);
}
