package com.ubuntu.chatroomserver.controller;

import com.ubuntu.chatroomserver.entity.Message;
import com.ubuntu.chatroomserver.util.SensitiveWordFilter;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

/**
 * @author luowendong
 */
@Controller
public class MessageController {

    @MessageMapping("/message")
    @SendTo("/topic/message")
    public Message message(Message message) {

        String result = SensitiveWordFilter.FILTER_INSTANCE.doFilter(message.getContent());
        message.setContent(result);
        System.out.println(message);
        return message;
    }
}
