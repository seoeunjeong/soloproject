package soloproject.seomoim.profileImage;

import lombok.Getter;
import lombok.Setter;
import soloproject.seomoim.member.entity.Member;

import javax.persistence.*;

@Entity
@Getter @Setter
public class ProfileImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "member_id", unique = true)
    private Member member;

    private String profileImageUrl;

    private String uuid;
}
