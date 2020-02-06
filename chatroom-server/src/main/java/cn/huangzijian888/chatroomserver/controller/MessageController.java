package cn.huangzijian888.chatroomserver.controller;

import cn.huangzijian888.chatroomserver.entity.Message;
import cn.huangzijian888.chatroomserver.util.SensitiveFilter;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

/**
 * @author huangzijian888
 */
@Controller
public class MessageController {

    @MessageMapping("/message")
    @SendTo("/topic/message")
    public Message message(Message message) {
        String result = SensitiveFilter.FILTER.filter(message.getContent(), '*');
        message.setContent(result);
        System.out.println(message);
        return message;
    }
}
