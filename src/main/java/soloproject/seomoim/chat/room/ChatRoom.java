package soloproject.seomoim.chat.room;

import lombok.Getter;
import lombok.Setter;
import soloproject.seomoim.chat.message.ChatMessage;
import soloproject.seomoim.member.entity.Member;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class ChatRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "ownerMember_id")
    private Member ownerMember;

    @ManyToOne
    @JoinColumn(name = "requestMember_id")
    private Member requestMember;

    @OneToMany(mappedBy = "chatRoom")
    private List<ChatMessage> messages = new ArrayList<>();

    //연관관계 편의메소드 연관관계 주인이 아닌쪽 데이터도 넣어주기//
    public void addOwnerMember(Member member) {
        this.ownerMember= member;
        member.getOwnerRooms().add(this);
    }
    public void addRequestMember(Member member) {
        this.requestMember= member;
        member.getRequestRooms().add(this);
    }
}

