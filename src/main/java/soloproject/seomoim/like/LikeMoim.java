package soloproject.seomoim.like;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import soloproject.seomoim.member.entity.Member;
import soloproject.seomoim.moim.entitiy.Moim;

import javax.persistence.*;

@Entity
@Getter @Setter
@NoArgsConstructor
public class LikeMoim {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "likeMoim_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "moim_id")
    private Moim moim;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private boolean status;

    public LikeMoim(Moim moim, Member member) {
        this.moim = moim;
        this.member = member;
    }
}
