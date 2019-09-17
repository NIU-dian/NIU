package com.yskj.push.task;

import com.yskj.push.service.impl.XueqiuService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author kai.tang
 * @version 1.0.0
 * @date 2019-08-07 15:20
 */
@Component("xueqiuTask")
public class XueqiuTask {

    private static final Logger LOGGER = LoggerFactory.getLogger(XueqiuTask.class);

    @Autowired
    private XueqiuService xueqiuService;


    /**
     * 股票昨收信息
     */
//    @Scheduled(cron = "0 21 14 * * *")
    public void zuoshouTask() {
        LOGGER.info("########昨收数据爬去开始########");
        xueqiuService.getQuoteRequest();
        LOGGER.info("########昨收数据爬去结束########");
    }


}
