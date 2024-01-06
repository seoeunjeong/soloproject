package soloproject.seomoim.chat.room;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import soloproject.seomoim.member.entity.Member;
import soloproject.seomoim.member.service.MemberService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final MemberService memberService;

    //채팅방 crud

    public Long createRoom(Long ownerMemberId, Long requestMemberId) {
        Member ownerMember = memberService.findMemberById(ownerMemberId);
        Member requestMember = memberService.findMemberById(requestMemberId);
        if (ownerMember == requestMember) {
            throw new IllegalStateException("본인에게 1:1대화는 불가능합니다.");
        }
        ChatRoom chatRoom = checkChatRoomAndCreate(ownerMember, requestMember);
        ChatRoom createdChatRoom = chatRoomRepository.save(chatRoom);

        return createdChatRoom.getId();

    }

    public List<ChatRoom> findJoinedChatRooms(Long memberId) {
        Member member = memberService.findMemberById(memberId);
        return chatRoomRepository.findByMember(member);
    }


    public ChatRoom findChatRoomById(long roomId){
        return chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalStateException("존재하지 않는 채팅방입니다."));
    }

    public ChatRoom checkChatRoomAndCreate(Member ownerMember, Member requestMember) {
        return chatRoomRepository.findByOwnerMemberAndRequestMember(ownerMember, requestMember)
                .orElseGet(() -> {
                    ChatRoom chatRoom = new ChatRoom();
                    chatRoom.addOwnerMember(ownerMember);
                    chatRoom.addRequestMember(requestMember);
                    return chatRoom;
                });
    }

    public ChatRoom checkChatRoomExistence(Member ownerMember, Member requestMember) {
        return chatRoomRepository.findByOwnerMemberAndRequestMember(ownerMember, requestMember)
                .orElseThrow(() -> new IllegalStateException("존재하지 않는 채팅방입니다."));

    }

}