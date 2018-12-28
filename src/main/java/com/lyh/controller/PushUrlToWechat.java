package com.lyh.controller;

import com.lyh.service.IRecordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArraySet;

@ServerEndpoint(value = "/pushUrl")
@Component("pushUrlToWechat")
@CrossOrigin
@EnableJms
public class PushUrlToWechat {
    //静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
    private static int onlineCount = 0;
    //concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。
    private static CopyOnWriteArraySet<PushUrlToWechat> webSocketSet = new CopyOnWriteArraySet<PushUrlToWechat>();
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private static String filePath;
    private Session session;  //与某个客户端的连接会话，需要通过它来给客户端发送数据

    /**
     * 连接建立成功调用的方法*/
    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
        webSocketSet.add(this);     //加入set中
        addOnlineCount();           //在线数加1
        logger.info("有新连接加入！当前在线人数为" + getOnlineCount());
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        webSocketSet.remove(this);  //从set中删除
        subOnlineCount();           //在线数减1
        logger.info("有一连接关闭！当前在线人数为" + getOnlineCount());
    }
    @JmsListener(destination = "pushUrl")
    public void receiveQueue(String text) {
        logger.error("websocket中的jms的text" + text);
        setFilePath(text);
        for (PushUrlToWechat item : webSocketSet) {
            try {
                item.sendMessage(getFilePath() + "||" + text);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息*/
    @OnMessage
    public void onMessage(String message, Session session) throws Exception{
        logger.info("来自客户端的消息:" + message);
        //decoderBase64File(message, null, null);
        //群发消息

        int count = 0;
        for (PushUrlToWechat item : webSocketSet) {
            try {
                item.sendMessage(message);
                count ++;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
        //this.session.getAsyncRemote().sendText(message);
    }
    public static void sendInfo(String message) throws IOException {
        for (PushUrlToWechat item : webSocketSet) {
            try {
                item.sendMessage(message);
            } catch (IOException e) {
                continue;
            }
        }
    }

    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    public static synchronized void addOnlineCount() {
        PushUrlToWechat.onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        PushUrlToWechat.onlineCount--;
    }

    public static synchronized  String getFilePath () {
        return filePath;
    }

    public static synchronized void setFilePath (String filePath) {
        PushUrlToWechat.filePath = filePath;
    }
}
