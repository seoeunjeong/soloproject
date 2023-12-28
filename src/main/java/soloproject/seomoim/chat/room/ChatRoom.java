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

//    @ManyToMany(cascade = CascadeType.ALL)
//    @JoinTable(
//            name = "member_chatRoom",
//            joinColumns = @JoinColumn(name = "room_id"),
//            inverseJoinColumns = @JoinColumn(name = "member_id")
//    )
//    private List<Member> members = new ArrayList<>();
//
    @ManyToOne
    @JoinColumn(name = "ownerMember_id")
    private Member ownerMember;

    @ManyToOne
    @JoinColumn(name = "requestMember_id")
    private Member requestMember;

    @OneToMany(mappedBy = "chatRoom")
    private List<ChatMessage> messages = new ArrayList<>();

    //연관관계 주인이 아닌쪽 연관관계 편의 메소드로 데이터 넣어주기 ㅎㅎㅎㅎ
//    public void addMember(Member member) {
//        this.members.add(member);
//        member.getChatRooms().add(this);
//    }

    public static class Dto{
        @Setter
        @Getter
        private Long roomId;
    }

}

