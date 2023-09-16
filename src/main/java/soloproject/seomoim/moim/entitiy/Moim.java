package soloproject.seomoim.moim.entitiy;

import lombok.Getter;
import lombok.Setter;
import soloproject.seomoim.member.domain.Member;

import javax.persistence.*;

@Entity
@Getter @Setter
public class Moim {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="moim_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private String title;
    private String content;
    private int participantCount;
    private String region;

    @Enumerated(EnumType.STRING)
    private MoimCategory moimCategory;

//
//    @OneToMany(mappedBy = "moim")
//    private List<MemberMoim> memberMoims =new ArrayList<>();
//
//
}
