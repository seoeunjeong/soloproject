package soloproject.seomoim.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Member {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="member_id")
    private Long id;
    @Column(unique = true)
    private String email;
    private String password;
    private String name;
    private LocalDateTime birthday;
    private String Gender;
    private String region;

    @OneToMany(mappedBy = "member")
    private List<MemberMoim> moimList = new ArrayList<>();

    protected Member() {
    }

    public Member(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
