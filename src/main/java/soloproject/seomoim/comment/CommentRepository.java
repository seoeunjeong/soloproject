package soloproject.seomoim.comment;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment,Long> {

    List<Comment> findByMember_id(Long memberId);

    List<Comment> findByMoim_id(Long moimid);
}
