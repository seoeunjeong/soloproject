package soloproject.seomoim;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Moim {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="moim_id")
    private Long id;

    private String title;
    private String content;
    private int participantCount;
    private String region;

    @Enumerated(EnumType.STRING)
    private MoimCategory moimCategory;

    @OneToMany(mappedBy = "moim")
    private List<MemberMoim> memberMoims =new ArrayList<>();
}
