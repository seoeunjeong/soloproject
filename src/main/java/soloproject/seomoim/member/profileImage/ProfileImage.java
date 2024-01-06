package soloproject.seomoim.member.profileImage;

import lombok.Getter;
import lombok.Setter;
import soloproject.seomoim.member.entity.Member;

import javax.persistence.*;

@Entity
@Getter @Setter
public class ProfileImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="profile_image_id")
    private Long id;

    private String profileImageUrl;

    private String uuid;

}
