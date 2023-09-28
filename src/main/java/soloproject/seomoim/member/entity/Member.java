package soloproject.seomoim.member.entity;

import lombok.Getter;
import lombok.Setter;
import soloproject.seomoim.comment.Comment;
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

    private String name;

    private int age;

    private char gender;

    private String region;

    //멤버는 여러 모임을 만들수있다
    @OneToMany(mappedBy = "member")
    private List<Moim> createMoims = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<MoimMember> participationMoims= new ArrayList<>();

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
