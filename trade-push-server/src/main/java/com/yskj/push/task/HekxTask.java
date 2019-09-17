package com.yskj.push.task;

import com.yskj.push.service.impl.HekxDailyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author kai.tang
 * @version 1.0.0
 * @date 2019-08-07 11:56
 */
@Component("hekxTask")
public class HekxTask {

    private static final Logger LOGGER = LoggerFactory.getLogger(HekxTask.class);

    @Autowired
    private HekxDailyService hekxDailyService;

    /**
     * 每天 17点
     */
//    @Scheduled(cron = "00 00 06 * * *")
    public void pushHK_SZ_SH(){
        hekxDailyService.pushHK_SH_SZ();
    }

    /**
     * 每分钟爬取一次
     */
    @Scheduled(cron = "00 0/1 * * * *")
    public void pushRel(){
        LOGGER.info("########资金流向定时任务开始########");
        hekxDailyService.pushSseSZ2HK();
        hekxDailyService.pushSseHU2HK();
        hekxDailyService.pushNBSZ();
        hekxDailyService.pushNBSH();
        LOGGER.info("########资金流向定时任务结束########");
    }
}
