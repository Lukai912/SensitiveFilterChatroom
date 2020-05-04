package com.ubuntu.chatroomserver.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ubuntu.chatroomserver.entity.Message;
import com.ubuntu.filter.SensitiveWordFilter;
import com.ubuntu.chatroomserver.util.TextUtils;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.io.IOException;

/**
 * @author luowendong
 */
@Controller
public class MessageController {

    @MessageMapping("/message")
    @SendTo("/topic/message")
    public Message message(Message message) throws IOException {
        String result = SensitiveWordFilter.FILTER_INSTANCE.doFilter(message.getContent());
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(message);
        System.out.println(json);
        TextUtils.writeDataToFile(json);
        message.setContent(result);
        System.out.println(message);
        return message;
    }
}
