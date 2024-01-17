package soloproject.seomoim.chat.room;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import soloproject.seomoim.chat.message.*;
import soloproject.seomoim.member.entity.Member;
import soloproject.seomoim.member.loginCheck.LoginMember;
import soloproject.seomoim.member.service.MemberService;
import soloproject.seomoim.utils.UriCreator;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Controller
@RequestMapping("/chat")
@RequiredArgsConstructor
public class ChatRoomController {

    private final ChatRoomService chatRoomService;
    private final MemberService memberService;
    private final ChatMessageRepository chatMessageRepository;
    private final static String CHAT_ROOM_DEFAULT_URL = "/chat/room";

    @PostMapping("/room")
    @ResponseBody
    public ResponseEntity createRoom(@RequestParam("receiver") Long ownerMemberId,
                                     @RequestParam("sender") Long requestMemberId) {
        Long createdRoomId = chatRoomService.createRoom(ownerMemberId, requestMemberId);
        URI location = UriCreator.createUri(CHAT_ROOM_DEFAULT_URL, createdRoomId);
        return ResponseEntity.created(location).build();
    }

    // 채팅방 입장
    @GetMapping("/room/{room-id}")
    public String chatRoom(@PathVariable("room-id") Long roomId,
                           @LoginMember String email, Model model) {
        Member loginMember = memberService.findMemberByEmail(email);
        model.addAttribute("loginMemberId", loginMember.getId());
        ChatRoom findChatRoom = chatRoomService.findChatRoomById(roomId);
        model.addAttribute("room", findChatRoom);
        List<ChatMessage> allChatMessage = chatMessageRepository.findByChatRoom(findChatRoom);
        model.addAttribute("allChat", allChatMessage);

        List<ChatMessage> unReadMessage = allChatMessage.stream()
                .filter(chatMessage -> chatMessage.getReadStatus() == ReadStatus.UNREAD)
                .collect(Collectors.toList());
        List<ChatMessageDto.Response> readStatusDtos = new ArrayList<>();

        for(ChatMessage message: unReadMessage) {
            if (loginMember != message.getSender()) {
                ChatMessageDto.Response readStatusDto = new ChatMessageDto.Response();
                readStatusDto.setMessageId(message.getId());
                readStatusDto.setSenderId(message.getSender().getId());
                readStatusDtos.add(readStatusDto);
            }
        }
        model.addAttribute("readStatusDtos",readStatusDtos);
        return "moims/chatRoom";
    }

    //모든 채팅방조회
    @GetMapping("/room/all/{member-id}")
    public void getJoinedChatRooms(@PathVariable("member-id") Long memberId,
                                      Model model){
        List<ChatRoom> joinedChatRooms = chatRoomService.findJoinedChatRooms(memberId);
        model.addAttribute("joinedChatRooms",joinedChatRooms);

    }



}