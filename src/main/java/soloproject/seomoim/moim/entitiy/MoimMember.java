package soloproject.seomoim.moim.entitiy;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import soloproject.seomoim.member.entity.Member;
import soloproject.seomoim.utils.BaseEntity;

import javax.persistence.*;


@Entity
@Getter @Setter
@Table(name = "moim_member")
@NoArgsConstructor
public class MoimMember extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "moimMember_id")
    private Long id;

    /*외래키를 가지고있는곳이 주인! 반대쪽에서 데이터 주입시 주의하자*/
    @ManyToOne
    @JoinColumn(name = "moim_id")
    private Moim moim;


    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    private boolean status = false;

    public void setMoim(Moim moim) {
        this.moim = moim;
        if (!this.moim.getParticipants().contains(this)) {
            this.moim.getParticipants().add(this);
        }
    }

    public void setMember(Member member) {
        this.member = member;
        if (!this.member.getJoinMoimList().contains(this)) {
            this.member.getJoinMoimList().add(this);
        }
    }
    
    
    public boolean isCreatedByMember(Member member){
         return moim.getMember()== member;
    }
}



