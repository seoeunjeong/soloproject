package soloproject.seomoim.moim.entitiy;

import lombok.Getter;
import lombok.Setter;
import soloproject.seomoim.member.entity.Member;
import soloproject.seomoim.moim.BaseEntitiy;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Moim extends BaseEntitiy{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="moim_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private String title;

    private String content;

    private LocalDateTime startedAt;

    private int totalParticipantCount;

    private int participantCount;

    private String region;

    private double latitude;

    private double longitude;

    @Enumerated(EnumType.STRING)
    private MoimCategory moimCategory;

    private int likeCount;

    @OneToMany(mappedBy = "moim",cascade = CascadeType.PERSIST)
    private List<MoimMember> participant= new ArrayList<>();

    public Moim() {
    }

   /*모임참여 메소드*/
    public void joinMoim(Moim moim,Member member){
        MoimMember moimMember = new MoimMember();
        moimMember.setMoim(moim);
        moimMember.setMember(member);
        participant.add(moimMember);
        this.participantCount += 1;
    }

    /*참여인원줄이기*/
    public void reduceCount(){
        this.participantCount -=1;
    }

    public void likeCountUp(){
        likeCount +=1;
    }

    public void likeCountDown(){

        this.likeCount= likeCount-1;
    }
}
