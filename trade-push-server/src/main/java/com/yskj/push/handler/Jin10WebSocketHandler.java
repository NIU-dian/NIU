package com.yskj.push.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author kai.tang@yintech.cn
 * @Date 2019/8/30 20:38
 * @Version 1.0.0
 */
@Component
public class Jin10WebSocketHandler implements WebSocketHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(Jin10WebSocketHandler.class);
    private static final List<WebSocketSession> users = new ArrayList<>();


    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        LOGGER.info("成功建立Websocket连接");
        users.add(session);
//        String username = session.getAttributes().get("user").toString();
//        // 判断session中用户信息
//        if(username!=null){
//            session.sendMessage(new TextMessage("已成功建立Websocket通信"));
//        }
    }

    @Override
    public void handleMessage(WebSocketSession webSocketSession, WebSocketMessage<?> webSocketMessage) throws Exception {

    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable e) throws Exception {
        if (session.isOpen()) {
            session.close();
        }
        LOGGER.error("连接出现错误:{}", e);
        users.remove(session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        LOGGER.debug("Websocket连接已关闭");
        users.remove(session);
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }

    /**
     * 给所有在线用户发送消息
     *
     * @param message
     */
    public static void sendMessageToAll(TextMessage message) {

        try {
            for (WebSocketSession user : users) {
                if (user.isOpen()) {
                    user.sendMessage(message);
                }
            }
        } catch (Exception e) {
            LOGGER.error("群发消息异常:{}", e);
        }

    }

    /**
     * 给某个用户发送消息
     *
     * @param userName
     * @param message
     */
    public static void sendMessageToUser(String userName, TextMessage message) {
        for (WebSocketSession user : users) {
            if (user.getAttributes().get("user").equals(userName)) {
                try {
                    if (user.isOpen()) {
                        user.sendMessage(message);
                    }
                } catch (Exception e) {
                    LOGGER.error("单独发送消息异常:{}", e);
                }
                break;
            }
        }
    }
}
