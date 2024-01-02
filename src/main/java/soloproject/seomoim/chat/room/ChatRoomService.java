package soloproject.seomoim.chat.room;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import soloproject.seomoim.member.entity.Member;
import soloproject.seomoim.member.service.MemberService;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final MemberService memberService;

    //채팅방 crud

    public Long createRoom(Long ownerMemberId,Long requestMemberId) {
        Member ownerMember = memberService.findMember(ownerMemberId);
        Member requestMember = memberService.findMember(requestMemberId);
        if(ownerMember==requestMember){
            throw new IllegalStateException("본인에게 1:1대화는 불가능합니다.");
        }
        ChatRoom chatRoom = checkChatRoomAndCreate(ownerMember, requestMember);
        ChatRoom createdChatRoom= chatRoomRepository.save(chatRoom);

        return createdChatRoom.getId();

    }

    public List<ChatRoom> findJoinedChatRooms(Long memberId) {
        Member member = memberService.findMember(memberId);
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
                    chatRoom.setOwnerMember(ownerMember);
                    chatRoom.setRequestMember(requestMember);
                    return chatRoom;
                });
    }


    public ChatRoom checkChatRoomExistence(Member ownerMember, Member requestMember) {
        return chatRoomRepository.findByOwnerMemberAndRequestMember(ownerMember, requestMember)
                .orElseThrow(() -> new IllegalStateException("존재하지 않는 채팅방입니다."));

    }

}