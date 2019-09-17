package com.yskj.push.framework.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.TimeoutException;

/**
 * @author kai.tang
 * @version 1.0
 * @date 2017/3/23 12:59
 */
//@Service("readWriteRedisService")
public class ReadWriteRedisServiceImpl implements ReadWriteRedisService {

//    @Autowired
//    @Resource(name = "writeRedisService")
    private IRedisService writeRedisService;

//    @Autowired
//    @Resource(name = "readRedisService")
    private IRedisService readRedisService;

    private Logger logger = LoggerFactory.getLogger(ReadWriteRedisServiceImpl.class);


    @Override
    public void set(String key, String value, int expired) {
        try {
            writeRedisService.set(key, value, expired);
        } catch (TimeoutException e) {
            logger.error("redis write error:{}", e);
        }
    }

    @Override
    public String get(String key) {
        try {
            return readRedisService.get(key);
        } catch (TimeoutException e) {
            logger.error("redis read error:{}", e);
        }
        return null;
    }

    @Override
    public void del(String key) {
        try {
            logger.info("redis_cache_del key:{}", key);
            writeRedisService.del(key);
        } catch (TimeoutException e) {
            logger.error("redis read error:{}", e);
        }
    }

    @Override
    public Long incr(String key, Integer exipre) {
        try {
            logger.info("redis_cache_incr key:{}", key);
            writeRedisService.incr(key, exipre);
        } catch (TimeoutException e) {
            logger.error("redis read error:{}", e);
        }
        return null;
    }
}
