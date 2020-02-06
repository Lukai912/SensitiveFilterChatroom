package com.ubuntu.chatroomserver.entity;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author luowendong
 */
@Data
public class Message {
    @Autowired
    private String name;

    @Autowired
    private String content;

    public String getContent(){
        return content;
    }
    public void setContent(String msg){
        content = msg;
    }
}
