package com.yskj.push.framework.cache;

/**
 * @author kai.tang
 * @version 1.0
 * @date 2017/3/23 12:58
 */
public interface ReadWriteRedisService {

    void set(String key, String value, int expired);

    String get(String key);

    void del(String s);

    Long incr(String key, Integer exipre);
}
