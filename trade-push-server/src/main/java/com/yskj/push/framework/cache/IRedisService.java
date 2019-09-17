package com.yskj.push.framework.cache;

import redis.clients.jedis.JedisPubSub;
import redis.clients.jedis.Tuple;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeoutException;

/**
 * @Description: 缓存表单数据
 * @author: fuming.zhang@baidao.com
 * @date: 2014年3月25日 上午2:12:05
 */
public interface IRedisService {

	/*************************
	 * 对Key(键)操作命令集合 DEL KEYS RANDOMKEY TTL PTTL EXISTS MOVE RENAME RENAMENX
	 * TYPE EXPIRE PEXPIRE EXPIREAT PEXPIREAT PERSIST SORT OBJECT MIGRATE DUMP
	 * RESTORE
	 *************************/

	public Long del(String... keys) throws TimeoutException;

	public String get(String key) throws TimeoutException;

	public Boolean exists(String key) throws TimeoutException;

	public List<String> keys(String pattern) throws TimeoutException;

	public Long expire(String key, int seconds) throws TimeoutException;

	public Long ttl(String key) throws TimeoutException;

	/*************************
	 * 对String(字符串)操作命令集合 SET SETNX SETEX PSETEX SETRANGE MSET MSETNX APPEND GET
	 * MGET GETRANGE GETSET STRLEN DECR DECRBY INCR INCRBY INCRBYFLOAT SETBIT
	 * GETBIT BITOP BITCOUNT
	 *************************/

	public void set(String key, String json) throws TimeoutException;
	public void set(String key, String json, Integer exipre) throws TimeoutException;

	public Long decr(String key) throws TimeoutException;

	public Long incr(String key) throws TimeoutException;

	public Long incr(String key, Integer exipre) throws TimeoutException;

	public Long incrBy(String key, long integer) throws TimeoutException;

	/*************************
	 * 对Hash(哈希表)操作命令集合【key feild生成的hash值】 HSET HSETNX HMSET HGET HMGET HGETALL
	 * HDEL HLEN HEXISTS HINCRBY HINCRBYFLOAT HKEYS HVALS
	 *************************/

	public Long hlen(String key) throws TimeoutException;

	public Long hset(String key, String field, String value) throws TimeoutException;

	public Long hset(String key, String field, String value, Integer expire) throws TimeoutException;

	public String hget(String key, String field) throws TimeoutException;

	public String hmset(String key, Map<String, String> hash) throws TimeoutException;

	public List<String> hmget(final String key, final String... fields) throws TimeoutException;

	public Map<String, String> hgetAll(String key) throws TimeoutException;

	public Long hdel(String key, String field) throws TimeoutException;

	public List<String> hkeys(String pattern) throws TimeoutException;

	public Long hincrBy(String key, String field, long value) throws TimeoutException;

	/*************************
	 * 对List(列表)操作命令集合 LPUSH LPUSHX RPUSH RPUSHX LPOP RPOP BLPOP BRPOP LLEN
	 * LRANGE LREM LSET LTRIM LINDEX LINSERT RPOPLPUSH BRPOPLPUSH
	 *************************/

	public Long rpush(String key, String string) throws TimeoutException;

	public <T> void rpush(String key, List<String> oList) throws TimeoutException;

	public Long lpush(String key, String string) throws TimeoutException;

	public void lpush(String key, List<String> oList) throws TimeoutException;

	public List<String> lpop(String key, int size) throws TimeoutException;

	public String lpop(String key) throws TimeoutException;

	public String rpop(String key) throws TimeoutException;

	public List<String> blpop(int timeout, String... key) throws TimeoutException;

	public Long lrem(String key, int count, String value) throws TimeoutException;

	public String lindex(String key, int index) throws TimeoutException;

	public String lset(String key, int index, String value) throws TimeoutException;

	public Long llen(String key) throws TimeoutException;

	public String ltrim(String key, int start, int end) throws TimeoutException;

	public List<String> lrange(String key, int start, int end) throws TimeoutException;

	/*************************
	 * 对Set(集合)操作集合 SADD SREM SMEMBERS SISMEMBER SCARD SMOVE SPOP SRANDMEMBER
	 * SINTER SINTERSTORE SUNION SUNIONSTORE SDIFF SDIFFSTORE
	 * @return
	 *************************/

	public Long sadd(String key, String member) throws TimeoutException;

	public Long scard(String key) throws TimeoutException;

	public Set<String> smembers(String key) throws TimeoutException;

	public void srem(String key, String member) throws TimeoutException;

	public boolean sismember(String key, String member) throws TimeoutException;

	/*************************
	 * 有序集(Sorted set) ZADD ZREM ZCARD ZCOUNT ZSCORE ZINCRBY ZRANGE ZREVRANGE
	 * ZRANGEBYSCORE ZREVRANGEBYSCORE ZRANK ZREVRANK ZREMRANGEBYRANK
	 * ZREMRANGEBYSCORE ZINTERSTORE ZUNIONSTORE
	 *************************/

	public void zremrangeByScore(String key, Double start, Double end) throws TimeoutException;

	public void zadd(String key,String value,Double score)throws TimeoutException;

	public Set<String> zrangeByScore(String key,String min,String max,int offset ,int limit) throws TimeoutException;
	public Set<String> zrangeByScore(String key,Double min,Double max,int offset ,int limit) throws TimeoutException;
	public Set<String> zrevrangeByScore(String key,String min,String max,int offset ,int limit) throws TimeoutException;
	public Set<String> zrevrangeByScore(String key,Double min,Double max,int offset ,int limit) throws TimeoutException;

	public Set<Tuple> zrangeByScoreWithScores(String key, Double min, Double max, int offset , int limit) throws TimeoutException;

	public Long zcard(String key) throws TimeoutException;

	/*************************
	 * Pub/Sub(发布/订阅) PUBLISH SUBSCRIBE PSUBSCRIBE UNSUBSCRIBE PUNSUBSCRIBE
	 *************************/

	public Long publish(String channel, String message) throws TimeoutException;

	public void subscribe(JedisPubSub jedisPubSub, String... channels) throws TimeoutException;

	/*************************
	 * Transaction(事务) WATCH UNWATCH MULTI DISCARD EXEC
	 *************************/
	
	
}
