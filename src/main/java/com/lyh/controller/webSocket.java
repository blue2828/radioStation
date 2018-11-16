package com.lyh.controller;

import com.lyh.entity.responseMsg;
import com.lyh.entity.socketMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class webSocket {
    @MessageMapping("/hello")
    @SendTo("/topic/resMsg")
    public responseMsg say (socketMessage message) {
        return new responseMsg("hello," + message.getContent());
    }
}
