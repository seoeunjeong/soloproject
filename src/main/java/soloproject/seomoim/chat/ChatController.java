package soloproject.seomoim.chat;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import soloproject.seomoim.member.service.MemberService;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@Controller
@Slf4j
@RequiredArgsConstructor
public class ChatController {
    private final MemberService memberService;
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/chatMessage")
    public void sendMessage(@RequestBody Message message) {
        log.info("message={}, message.sender={}", message.getContent(), message.getSender());
//        String encodedRoomId = URLEncoder.encode(message.getRoomId(), "UTF-8");
        messagingTemplate.convertAndSend("/sub/chatMessage/" +message.getRoomId(), message);
    }


    @MessageMapping("/alarm")
    public void sendAlarm(@RequestBody Message message){
        log.info("message={}, message.sender={}", message.getContent(), message.getSender());

        messagingTemplate.convertAndSend("/sub/alarm", message);
    }
}