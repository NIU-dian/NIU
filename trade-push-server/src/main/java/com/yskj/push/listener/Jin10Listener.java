package com.yskj.push.listener;

import com.alibaba.fastjson.JSON;
import com.gargoylesoftware.htmlunit.WebClientInternals;
import com.gargoylesoftware.htmlunit.javascript.host.WebSocket;
import com.yskj.push.handler.Jin10WebSocketHandler;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.WebSocketListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.socket.TextMessage;

/**
 * @Author kai.tang@yintech.cn
 * @Date 2019/8/30 16:53
 * @Version 1.0.0
 */
public class  Jin10Listener implements WebClientInternals.Listener{

    private static final Logger LOGGER = LoggerFactory.getLogger(Jin10Listener.class);

    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Override
    public void webSocketCreated(WebSocket webSocket) {
        webSocket.setWebSocketListener(new WebSocketListener() {
            @Override
            public void onWebSocketBinary(byte[] bytes, int i, int i1) {
                LOGGER.info(new String(bytes));
            }

            @Override
            public void onWebSocketText(String s) {
                LOGGER.info(s);
                threadPoolTaskExecutor.execute(new Runnable() {
                    @Override
                    public void run() {
                        Jin10WebSocketHandler.sendMessageToAll(new TextMessage(s));
                    }
                });
            }

            @Override
            public void onWebSocketClose(int i, String s) {
                LOGGER.info("close");
            }

            @Override
            public void onWebSocketConnect(Session session) {
                LOGGER.info("connect");
            }

            @Override
            public void onWebSocketError(Throwable throwable) {
                LOGGER.info("error");
            }
        });
    }
}
