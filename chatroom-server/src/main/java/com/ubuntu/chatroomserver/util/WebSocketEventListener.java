package com.ubuntu.chatroomserver.util;

import com.ubuntu.chatroomserver.config.WebSocketConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.io.IOException;

@Component
public class WebSocketEventListener {
    volatile int onlineNum = 0;
    @Autowired
    private SimpMessageSendingOperations messagingTemplate;

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event) {
        System.out.println("httpSession key： 新的连接");
        onlineNum++;
        if(onlineNum == 1){
            WebSocketConfig.room_time = String.valueOf(System.currentTimeMillis());
        }
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) throws IOException {
        System.out.println("httpSession key： 断开连接");
        onlineNum--;
        if(onlineNum <= 0){
            System.out.println("空房间");
            WebSocketConfig.room_time = String.valueOf(System.currentTimeMillis());
        }
    }
}
