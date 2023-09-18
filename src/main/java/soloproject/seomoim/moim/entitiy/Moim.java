package soloproject.seomoim.moim.entitiy;

import lombok.Getter;
import lombok.Setter;
import soloproject.seomoim.member.domain.Member;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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

    @OneToMany(mappedBy = "moim",cascade = CascadeType.PERSIST)
    private List<MoimMember> participant= new ArrayList<>();

    private int totalParticipantCount;

    private int participantCount;
    private String region;

    @Enumerated(EnumType.STRING)
    private MoimCategory moimCategory;

    public void addParticipant(Moim moim,Member member){
        MoimMember moimMember = new MoimMember();
        moimMember.setMoim(moim);
        moimMember.setMember(member);
        participant.add(moimMember);
        this.participantCount += 1;
    }

}
