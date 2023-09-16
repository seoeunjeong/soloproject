package soloproject.seomoim.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
@Table(name = "member_moim")
public class MemberMoim {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="member_moim_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "moim_id")
    private Moim moim;
}
