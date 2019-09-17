package com.yskj.push.task;

import com.yskj.push.framework.util.DateUtil;
import com.yskj.push.service.impl.TushareService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author kai.tang
 * @version 1.0.0
 * @date 2019-08-07 14:59
 */
@Component("tushareTask")
public class TushareTask {

    private static final Logger LOGGER = LoggerFactory.getLogger(TushareTask.class);

    @Autowired
    private TushareService tushareService;


    /**
     * 每一分钟执行一次新闻资讯的信息获取
     */
//    @Scheduled(cron = "00 0/3 * * * *")
    public void newsTask() {
        LOGGER.info("TushareTask.newsTask start ....");
        String startDate = DateUtil.getFormatDate(new Date(), "yyyy-MM-dd");
        startDate = startDate + " 00:00:00";
        String endDate = DateUtil.getFormatDate(new Date(), "yyyy-MM-dd");
        endDate = endDate + " 23:59:59";
        tushareService.dailyTushareNewsData(startDate, endDate);
        LOGGER.info("TushareTask.newsTask end ....");
    }

    /**
     * 每天17点爬一次其他数据
     */
//    @Scheduled(cron = "00 00 17 * * *")
    public void thshareDataTask() {
        LOGGER.info("TushareTask.newsTask start ....");
        String startDate = DateUtil.getFormatDate(new Date(), "yyyy-MM-dd");
        startDate = startDate + " 00:00:00";
        String endDate = DateUtil.getFormatDate(new Date(), "yyyy-MM-dd");
        endDate = endDate + " 23:59:59";
        String annDate = DateUtil.getFormatDate(new Date(), "yyyyMMdd");
        tushareService.dailyTushareData(startDate, endDate, annDate);
        LOGGER.info("TushareTask.newsTask end ....");
    }
}
