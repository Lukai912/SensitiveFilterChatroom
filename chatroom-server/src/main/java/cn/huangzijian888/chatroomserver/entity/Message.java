package cn.huangzijian888.chatroomserver.entity;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author huangzijian888
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
