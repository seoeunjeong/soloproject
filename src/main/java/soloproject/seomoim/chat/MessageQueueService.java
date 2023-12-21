//package soloproject.seomoim.chat;
//
//import org.springframework.stereotype.Service;
//
//import java.util.Queue;
//import java.util.concurrent.ConcurrentLinkedQueue;
//
//@Service
//public class MessageQueueService {
//    private Queue<Message> messageQueue= new ConcurrentLinkedQueue<>();
//
//    public void addMessage(Message message){
//        messageQueue.add(message);
//    }
//
//    public Message pollMessage(){
//        return messageQueue.poll();
//    }
//}
