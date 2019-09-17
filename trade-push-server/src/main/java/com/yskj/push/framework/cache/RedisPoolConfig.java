package com.yskj.push.framework.cache;

import redis.clients.jedis.JedisPoolConfig;

public class RedisPoolConfig extends JedisPoolConfig {
	public RedisPoolConfig() {
		super();
        super.setTestOnBorrow(true);
        super.setTestOnReturn(true);
        super.setTestWhileIdle(true);
	}
	public void setMaxIdle(int maxIdle){
        super.setMaxIdle(maxIdle);
	}
	public void setMaxActive(int maxActive){
        super.setMaxTotal(maxActive);
	}
	public void setMaxWait(int maxWait){
        super.setMaxWaitMillis(maxWait);
	}
}
