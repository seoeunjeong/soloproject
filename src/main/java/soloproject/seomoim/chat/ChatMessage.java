package soloproject.seomoim.chat;

import lombok.Getter;
import lombok.Setter;
import soloproject.seomoim.member.entity.Member;
import soloproject.seomoim.utils.BaseEntity;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class ChatMessage extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "message_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "receiver_id",nullable = false)
    private Member receiver;

    @ManyToOne
    @JoinColumn(name = "sender_id" ,nullable = false)
    private Member sender;

    private String content;

    @ManyToOne
    @JoinColumn(name = "room_id")
    private ChatRoom chatRoom;

}


