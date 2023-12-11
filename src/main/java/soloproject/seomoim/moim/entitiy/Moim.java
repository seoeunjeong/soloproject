package soloproject.seomoim.moim.entitiy;

import lombok.Getter;
import lombok.Setter;
import soloproject.seomoim.member.entity.Member;
import soloproject.seomoim.utils.BaseEntity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static soloproject.seomoim.moim.entitiy.QMoimMember.moimMember;

@Entity
@Getter
@Setter
public class Moim extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="moim_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
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

    private boolean open;
    public Moim() {

    }
//   연관관계 편의 메소드
//    public void setMember(Member member){
//        this.member=member;
//        member.getCreateMoims().add(this);
//
//    }
   /*모임참여 메소드*/
    public void addCount(){
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
