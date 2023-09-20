package soloproject.seomoim.moim.entitiy;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import soloproject.seomoim.member.entity.Member;
import soloproject.seomoim.moim.BaseEntitiy;

import javax.persistence.*;


@Entity
@Getter @Setter
@Table(name = "moim_member")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MoimMember extends BaseEntitiy {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="moim_member_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "moim_id")
    private Moim moim;

    //테스트에서 사용하기위해 persist 추가
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name="member_id")
    private Member member;
}
