package soloproject.seomoim.member.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import soloproject.seomoim.chat.room.ChatRoom;
import soloproject.seomoim.moim.like.LikeMoim;
import soloproject.seomoim.member.profileImage.ProfileImage;
import soloproject.seomoim.utils.BaseEntity;
import soloproject.seomoim.moim.entitiy.Moim;
import soloproject.seomoim.moim.entitiy.MoimMember;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@NoArgsConstructor
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(unique = true)
    private String email;

    private String password;

    @Transient
    private String confirmPassword;

    private String name;

    private Integer age;

    private char gender;

    private String address;

    private double latitude;

    private double longitude;

    private String eupMyeonDong;

    @OneToOne(cascade = CascadeType.ALL,orphanRemoval = true)
    @JoinColumn(name = "profile_image_id")
    private ProfileImage profileImage;


    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name="member_roles",
    joinColumns=@JoinColumn(name="member_id"))
    @Column(name = "roles_name")
    private List<String> roles = new ArrayList<>();

    //멤버는 여러 모임을 만들 수 있다.
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Moim> createMoims = new ArrayList<>();

    //멤버는 여러 모임에 참여 할수있다.
    @OneToMany(mappedBy = "member",cascade = CascadeType.ALL)
    private List<MoimMember> joinMoims = new ArrayList<>();

    //멤버는 여러 모임을 좋아요 추가할수있다.
    @OneToMany(mappedBy = "member",cascade = CascadeType.ALL)
    private List<LikeMoim> likeMoims = new ArrayList<>();

    @OneToMany(mappedBy = "ownerMember")
    private List<ChatRoom> ownerRooms= new ArrayList<>();

    @OneToMany(mappedBy = "requestMember")
    private List<ChatRoom> requestRooms= new ArrayList<>();



    public Member(String email,String name,String profile) {
        this.email = email;
        this.name = name;
        ProfileImage profileImage = new ProfileImage();
        profileImage.setProfileImageUrl(profile);
        this.setProfileImage(profileImage);
    }
}
