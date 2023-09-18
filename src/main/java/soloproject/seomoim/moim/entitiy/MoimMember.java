package soloproject.seomoim.moim.entitiy;

import lombok.Getter;
import lombok.Setter;
import soloproject.seomoim.member.domain.Member;

import javax.persistence.*;


@Entity
@Getter @Setter
@Table(name = "moim_member")
public class MoimMember {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="moim_member_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "moim_id")
    private Moim moim;

    @ManyToOne
    @JoinColumn(name="member_id")
    private Member member;
}
