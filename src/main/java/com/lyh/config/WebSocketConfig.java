package com.lyh.config;

import com.lyh.controller.MyWebSocket;
import com.lyh.controller.WxChatSocket;
import com.lyh.service.message.JmsProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

@Configuration("myWebSocketConfig")
public class WebSocketConfig {
    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }
    @Autowired
    public void setJavaMailSender (JavaMailSender javaMailSender) {
        WxChatSocket.javaMailSender = javaMailSender;
    }
    @Autowired
    public void setJmsProducer (JmsProducer jmsProducer) {
        MyWebSocket.jmsProducer = jmsProducer;
    }

}
