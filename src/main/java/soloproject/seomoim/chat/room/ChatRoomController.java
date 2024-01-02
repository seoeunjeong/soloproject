package soloproject.seomoim.chat.room;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import soloproject.seomoim.chat.message.ChatMessage;
import soloproject.seomoim.chat.message.ChatMessageRepository;
import soloproject.seomoim.chat.message.ReadStatus;
import soloproject.seomoim.member.entity.Member;
import soloproject.seomoim.member.loginCheck.AuthenticationdUser;
import soloproject.seomoim.member.service.MemberService;
import soloproject.seomoim.utils.UriCreator;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/chat")
@RequiredArgsConstructor
public class ChatRoomController {

    private final ChatRoomService chatRoomService;
    private final MemberService memberService;
    private final ChatMessageRepository chatMessageRepository;

    private final static String CHAT_ROOM_DEFAULT_URL = "/chat/room";

    // 채팅방 생성
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
                           @AuthenticationdUser String email, Model model) {

        Member loginMember = memberService.findByEmail(email);
        model.addAttribute("loginMemberId", loginMember.getId());
        ChatRoom findChatRoom = chatRoomService.findChatRoomById(roomId);
        model.addAttribute("room", findChatRoom);
//        List<ChatMessage> allMessage = findChatRoom.getMessages();
        List<ChatMessage> allChatMessage = chatMessageRepository.findByChatRoom(findChatRoom);
        model.addAttribute("allChat", allChatMessage);


        for(ChatMessage message: allChatMessage){
            if(loginMember!=message.getSender()){
                message.setReadStatus(ReadStatus.READ);
                chatMessageRepository.save(message);
            }
        }
        /*채팅방입장할때 DB에 저장된 메세지를 읽는건 서버에서 처리 클라이언트가어떻게 알지?*/


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