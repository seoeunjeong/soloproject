package soloproject.seomoim.chat;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ChatController {

    @GetMapping("/chat-form")
    public String chatForm(){
        return "moims/chat";
    }

}
