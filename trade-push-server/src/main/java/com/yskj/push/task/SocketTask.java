package com.yskj.push.task;

import com.yskj.push.handler.Jin10WebSocketHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;

/**
 * @Author kai.tang@yintech.cn
 * @Date 2019/9/9 17:41
 * @Version 1.0.0
 */
@Component("socketTask")
public class SocketTask {

    private static final Logger LOGGER = LoggerFactory.getLogger(SocketTask.class);

    /**
     * 每分钟爬取一次
     */
    @Scheduled(cron = "0/10 * * * * *")
    public void pushRel(){
//        LOGGER.info("########资金流向定时任务开始########");
//        Jin10WebSocketHandler.sendMessageToAll(new TextMessage("hello"));
//        LOGGER.info("########资金流向定时任务结束########");
    }

}
