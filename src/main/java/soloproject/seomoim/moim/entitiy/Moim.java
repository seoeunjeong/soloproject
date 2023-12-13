package soloproject.seomoim.moim.entitiy;

import lombok.Getter;
import lombok.Setter;
import soloproject.seomoim.member.entity.Member;
import soloproject.seomoim.utils.BaseEntity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


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
    private List<MoimMember> participants= new ArrayList<>();

    @Column(columnDefinition = "TINYINT(1)")
    private boolean open =true;
    public Moim() {

    }

    //양방향 연관관계에서 한쪽에만 엔티티만 추가해주는 실수를 하더라도 다른쪽 엔티티를 추가
    public void setMember(Member member) {
        this.member = member;
        if (!this.member.getCreateMoims().contains(this)) {
            this.member.getCreateMoims().add(this);
        }
    }

    public void setParticipants(MoimMember moimMember) {
        this.participants.add(moimMember);
        if (moimMember.getMoim() != this) {
            moimMember.setMoim(this);
        };
        addParticipantCount();
    }

    public void addParticipantCount(){
        this.participantCount += 1;
    }
    /*참여인원줄이기*/
    public void reduceParticipantCount(){

        this.participantCount -=1;
    }

    public void likeCountUp(){
        likeCount +=1;
    }

    public void likeCountDown(){

        this.likeCount= likeCount-1;
    }
}
