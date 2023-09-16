package soloproject.seomoim.member.domain;

import lombok.Getter;
import lombok.Setter;
import soloproject.seomoim.moim.entitiy.Moim;

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

    @OneToMany(mappedBy = "member")
    private List<Moim> moims = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<MemberMoim> moimList = new ArrayList<>();

    //연관관계 편의 메소드
    public void addMoimList(MemberMoim memberMoim){
        this.moimList.add(memberMoim);
        memberMoim.setMember(this);
    }
    public Member() {
    }

    public Member(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
