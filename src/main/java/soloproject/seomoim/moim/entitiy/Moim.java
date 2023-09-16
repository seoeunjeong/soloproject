package soloproject.seomoim.moim.entitiy;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.lang.reflect.Member;

@Entity
@Getter @Setter
public class Moim {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="moim_id")
    private Long id;

    private String title;
    private String content;
    private int participantCount;
    private String region;

    @Enumerated(EnumType.STRING)
    private MoimCategory moimCategory;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;
//
//    @OneToMany(mappedBy = "moim")
//    private List<MemberMoim> memberMoims =new ArrayList<>();
//
//
}
