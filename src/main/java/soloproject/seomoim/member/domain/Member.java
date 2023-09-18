package soloproject.seomoim.member.domain;

import lombok.Getter;
import lombok.Setter;
import soloproject.seomoim.moim.entitiy.Moim;
import soloproject.seomoim.moim.entitiy.MoimMember;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Member {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="member_id")
    private Long id;

    @Column(unique = true)
    private String email;

    private String password;

    private String name;

    private int age;

    private String gender;

    private String region;

    //멤버는 여러 모임을 만들수있다
    @OneToMany(mappedBy = "member",fetch = FetchType.LAZY)
    private List<Moim> moims = new ArrayList<>();

    @OneToMany(mappedBy = "member",fetch = FetchType.LAZY)
    private List<MoimMember> participationMoims= new ArrayList<>();


    //연관관계 편의 메소드

    public Member() {
    }

    public Member(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
