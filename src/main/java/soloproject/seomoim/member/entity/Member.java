package soloproject.seomoim.member.entity;

import lombok.Getter;
import lombok.Setter;
import soloproject.seomoim.like.LikeMoim;
import soloproject.seomoim.moim.BaseEntitiy;
import soloproject.seomoim.moim.entitiy.Moim;
import soloproject.seomoim.moim.entitiy.MoimMember;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Member extends BaseEntitiy {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="member_id")
    private Long id;

    @Column(unique = true)
    private String email;

    private String password;

    @Transient
    private String confirmPassword;

    private String name;

    // 기본값으로 null을 가진 Integer 필드
    private Integer age;
    // 기본값으로 null을 가진 Character 필드
    private Character gender;

    @Enumerated(EnumType.STRING)
    private ROLE role = ROLE.UNVERIFIED_MEMBER;

    private String address;

    //멤버는 여러 모임을 만들 수 있다.
    @OneToMany(mappedBy = "member")
    private List<Moim> createMoims = new ArrayList<>();

    //멤버는 여러 모임에 참여 할수있다.
    @OneToMany(mappedBy = "member")
    private List<MoimMember> participationMoims= new ArrayList<>();

    //멤버는 여러 모임을 좋아요 추가할수있다.
    @OneToMany(mappedBy = "member")
    private List<LikeMoim> likeMoims = new ArrayList<>();


    //연관관계 편의 메소드
    public Member() {
    }
    public Member(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
