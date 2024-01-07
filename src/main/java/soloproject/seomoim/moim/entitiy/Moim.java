package soloproject.seomoim.moim.entitiy;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import soloproject.seomoim.member.entity.Member;
import soloproject.seomoim.moim.like.LikeMoim;
import soloproject.seomoim.utils.BaseEntity;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;


@Entity
@Getter
@Setter
public class Moim extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "moim_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "member_id")
    private Member member;

    private String title;

    private String content;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime startedAt;

    private int dDay;

    private int totalParticipantCount;

    private int participantCount;

    private String placeName;

    private String placeAddress;

    private double latitude;

    private double longitude;

    private String eupMyeonDong;

    @Enumerated(EnumType.STRING)
    private MoimCategory moimCategory;

    private int likeCount;

    @OneToMany(mappedBy = "moim",cascade = CascadeType.ALL)
    private List<MoimMember> participants = new ArrayList<>();


    @OneToMany(mappedBy = "moim",cascade = CascadeType.ALL)
    private List<LikeMoim> likeList = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private MoimStatus moimStatus = MoimStatus.MOIM_OPEN;

    public Moim() {

    }

    //양방향 연관관계에서 한쪽에만 엔티티만 추가해주는 실수를 하더라도 다른쪽 엔티티를 추가
    public void setMember(Member member) {
        this.member = member;
        if (!this.member.getCreateMoimList().contains(this)) {
            this.member.getCreateMoimList().add(this);
        }
    }
    public void setParticipants(MoimMember moimMember) {
        this.participants.add(moimMember);
        if (moimMember.getMoim() != this) {
            moimMember.setMoim(this);
        }
        addParticipantCount();
    }

    public void addParticipantCount() {
        this.participantCount += 1;
        if (totalParticipantCount == participantCount)
            this.moimStatus = MoimStatus.MOIM_ClOSE;
    }

    /*참여인원줄이기*/
    public void reduceParticipantCount() {
        this.participantCount -= 1;
        if (totalParticipantCount - participantCount == 1) {
            this.moimStatus = MoimStatus.MOIM_OPEN;
        }
    }

    public void likeCountUp() {

        likeCount += 1;
    }

    public void likeCountDown() {

        this.likeCount = likeCount - 1;
    }


    public int calculateDDay() {
        LocalDate currentDate = LocalDate.now();
        return (int) ChronoUnit.DAYS.between(currentDate, this.startedAt);
    }
}
