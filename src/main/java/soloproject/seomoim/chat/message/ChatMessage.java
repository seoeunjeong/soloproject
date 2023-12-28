package soloproject.seomoim.chat.message;

import lombok.Getter;
import lombok.Setter;
import soloproject.seomoim.chat.room.ChatRoom;
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
    @JoinColumn(name = "sender_id", nullable = false)
    private Member sender;

    private String content;

    @Column(columnDefinition = "TINYINT(1)")
    private boolean readStatus = false;

    @ManyToOne
    @JoinColumn(name = "room_id")
    private ChatRoom chatRoom;

}


