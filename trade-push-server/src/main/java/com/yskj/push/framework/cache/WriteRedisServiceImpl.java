package com.yskj.push.framework.cache;

import org.apache.commons.lang3.StringUtils;
import redis.clients.jedis.*;

import java.util.*;
import java.util.concurrent.TimeoutException;

public class WriteRedisServiceImpl implements IRedisService {

	private JedisPool jedisPool;

	public static final String NOT_FOUND = "nil";

	public void setJedisPool(JedisPool jedisPool) {
		this.jedisPool = jedisPool;
	}

	/**
	 * 获取缓存池
	 *
	 * @Title: getJedis
	 * @Description:
	 * @return
	 * @throws TimeoutException
	 */
	public Jedis getJedis() throws TimeoutException {
		Jedis jedis = jedisPool.getResource();
		return jedis;
	}

	/**
	 * 释放缓存池
	 *
	 * @Title: releaseJedisInstance
	 * @Description:
	 * @param jedis
	 */
	public void releaseJedisInstance(Jedis jedis) {
		if (jedis != null) {
			jedis.close();
		}
	}

	/*************************
	 * 对Key(键)操作命令集合 DEL KEYS RANDOMKEY TTL PTTL EXISTS MOVE RENAME RENAMENX
	 * TYPE EXPIRE PEXPIRE EXPIREAT PEXPIREAT PERSIST SORT OBJECT MIGRATE DUMP
	 * RESTORE
	 *************************/

	public Long del(String... keys) throws TimeoutException {
		Long result = 0L;
		Jedis jedis = null;
		try {
			jedis = getJedis();
			result = jedis.del(keys);
		} finally {
			releaseJedisInstance(jedis);
		}
		return result;
	}

	public String get(String key) throws TimeoutException {
		String json = null;
		Jedis jedis = null;
		try {
			jedis = getJedis();
			json = jedis.get(key);
		} catch (TimeoutException e) {
			throw e;
		} finally {
			if (jedis != null) {
				releaseJedisInstance(jedis);
			}
		}
		return json;
	}

	public Boolean exists(String key) throws TimeoutException {
		Boolean result = Boolean.FALSE;
		Jedis jedis = null;
		try {
			jedis = getJedis();
			result = jedis.exists(key);
		} finally {
			releaseJedisInstance(jedis);
		}
		return result;
	}

	public List<String> keys(String pattern) throws TimeoutException {
		List<String> result = new ArrayList<String>();
		Set<String> set = new HashSet<String>();
		Jedis jedis = null;
		try {
			jedis = getJedis();
			set = jedis.keys(pattern);
			if (set != null && !set.isEmpty()) {
				final Iterator<String> ite = set.iterator();
				while (ite.hasNext()) {
					result.add((String) ite.next());
				}
			}
		} finally {
			releaseJedisInstance(jedis);
		}
		return result;
	}

	public Long expire(String key, int seconds) throws TimeoutException {
		Long result = 0L;
		Jedis jedis = null;
		try {
			jedis = getJedis();
			result = jedis.expire(key, seconds);
		} finally {
			releaseJedisInstance(jedis);
		}
		return result;
	}

	public Long ttl(String key) throws TimeoutException {
		Jedis jedis = null;
		try {
			jedis = getJedis();
			return jedis.ttl(key);
		} catch (TimeoutException e) {
			throw e;
		} finally {
			if (jedis != null) {
				releaseJedisInstance(jedis);
			}
		}
	}

	/*************************
	 * 对String(字符串)操作命令集合 SET SETNX SETEX PSETEX SETRANGE MSET MSETNX APPEND GET
	 * MGET GETRANGE GETSET STRLEN DECR DECRBY INCR INCRBY INCRBYFLOAT SETBIT
	 * GETBIT BITOP BITCOUNT
	 *************************/

	public void set(String key, String json) throws TimeoutException {
		Jedis jedis = null;
		try {
			jedis = getJedis();
			jedis.set(key, json);
		} catch (TimeoutException e) {
			throw e;
		} finally {
			releaseJedisInstance(jedis);
		}
	}
	public void set(String key, String json, Integer expire) throws TimeoutException {
		Jedis jedis = null;
		try {
			jedis = getJedis();
			Transaction transaction = jedis.multi();
			transaction.set(key, json);
			transaction.expire(key, expire);
			transaction.exec();
		} catch (TimeoutException e) {
			throw e;
		} finally {
			releaseJedisInstance(jedis);
		}
	}

	public Long decr(String key) throws TimeoutException {
		Jedis jedis = null;
		try {
			jedis = getJedis();
			return jedis.decr(key);
		} catch (TimeoutException e) {
			throw e;
		} finally {
			if (jedis != null) {
				releaseJedisInstance(jedis);
			}
		}
	}

	public Long incr(String key) throws TimeoutException {
		Jedis jedis = null;
		try {
			jedis = getJedis();
			return jedis.incr(key);
		} catch (TimeoutException e) {
			throw e;
		} finally {
			if (jedis != null) {
				releaseJedisInstance(jedis);
			}
		}
	}

	@Override
	public Long incr(String key, Integer exipre) throws TimeoutException {
		Jedis jedis = null;
		try {
			jedis = getJedis();
			Transaction transaction = jedis.multi();
			Response<Long> count = transaction.incr(key);
			transaction.expire(key, exipre);
			transaction.exec();
			return count.get();
		} catch (TimeoutException e) {
			throw e;
		} finally {
			releaseJedisInstance(jedis);
		}
	}

	public Long incrBy(String key, long integer) throws TimeoutException {
		Jedis jedis = null;
		try {
			jedis = getJedis();
			return jedis.incrBy(key, integer);
		} catch (TimeoutException e) {
			throw e;
		} finally {
			if (jedis != null) {
				releaseJedisInstance(jedis);
			}
		}
	}

	/*************************
	 * 对Hash(哈希表)操作命令集合【key feild生成的hash值】 HSET HSETNX HMSET HGET HMGET HGETALL
	 * HDEL HLEN HEXISTS HINCRBY HINCRBYFLOAT HKEYS HVALS
	 *************************/

	public Long hlen(String key) throws TimeoutException {
		Long len = 0L;
		Jedis jedis = null;
		try {
			jedis = getJedis();
			len = jedis.hlen(key);
		} finally {
			releaseJedisInstance(jedis);
		}
		return len;
	}

	public Long hset(String key, String field, String value) throws TimeoutException {
		Long result = 0L;
		Jedis jedis = null;
		try {
			jedis = getJedis();
			result = jedis.hset(key, field, value);
		} finally {
			releaseJedisInstance(jedis);
		}
		return result;
	}

	public Long hset(String key, String field, String value,Integer expire) throws TimeoutException {
		Long result = 0L;
		Jedis jedis = null;
		try {
			jedis = getJedis();
			Transaction transaction = jedis.multi();
			transaction.hset(key, field, value);
			transaction.expire(key, expire);
			transaction.exec();
		} finally {
			releaseJedisInstance(jedis);
		}
		return result;
	}

	public String hget(String key, String field) throws TimeoutException {
		String result = null;
		Jedis jedis = null;
		try {
			jedis = getJedis();
			String str = jedis.hget(key, field);
			if (!NOT_FOUND.equals(str)) {
				result = str;
			}
		} finally {
			releaseJedisInstance(jedis);
		}
		return result;
	}

	public String hmset(String key, Map<String, String> hash) throws TimeoutException {
		String result = null;
		Jedis jedis = null;
		try {
			jedis = getJedis();
			result = jedis.hmset(key, hash);
		} finally {
			releaseJedisInstance(jedis);
		}
		return result;
	}

	public List<String> hmget(final String key, final String... fields) throws TimeoutException {
		List<String> result = null;
		Jedis jedis = null;
		try {
			jedis = getJedis();
			result = jedis.hmget(key, fields);
			if (result != null && !result.isEmpty()) {
				result.remove(NOT_FOUND);
			}
		} finally {
			releaseJedisInstance(jedis);
		}
		return result;
	}

	public Map<String, String> hgetAll(String key) throws TimeoutException {
		Map<String, String> result = new HashMap<String, String>();
		Jedis jedis = null;
		try {
			jedis = getJedis();
			result = jedis.hgetAll(key);
		} finally {
			releaseJedisInstance(jedis);
		}
		return result;
	}

	public Long hdel(String key, String field) throws TimeoutException {
		Long result = 0L;
		Jedis jedis = null;
		try {
			jedis = getJedis();
			result = jedis.hdel(key, field);
		} finally {
			releaseJedisInstance(jedis);
		}
		return result;
	}

	public List<String> hkeys(String pattern) throws TimeoutException {
		List<String> result = new ArrayList<String>();
		Set<String> set = new HashSet<String>();
		Jedis jedis = null;
		try {
			jedis = getJedis();
			set = jedis.hkeys(pattern);
			if (set != null && !set.isEmpty()) {
				final Iterator<String> ite = set.iterator();
				while (ite.hasNext()) {
					result.add((String) ite.next());
				}
			}
		} finally {
			releaseJedisInstance(jedis);
		}
		return result;
	}

	public Long hincrBy(String key, String field, long value) throws TimeoutException {
		Jedis jedis = null;
		try {
			jedis = getJedis();
			return jedis.hincrBy(key, field, value);
		} catch (TimeoutException e) {
			throw e;
		} finally {
			if (jedis != null) {
				releaseJedisInstance(jedis);
			}
		}
	}

	/*************************
	 * 对List(列表)操作命令集合 LPUSH LPUSHX RPUSH RPUSHX LPOP RPOP BLPOP BRPOP LLEN
	 * LRANGE LREM LSET LTRIM LINDEX LINSERT RPOPLPUSH BRPOPLPUSH
	 *************************/

	public Long rpush(String key, String string) throws TimeoutException {
		Long result = 0L;
		Jedis jedis = null;
		try {
			jedis = getJedis();
			result = jedis.rpush(key, string);
		} finally {
			releaseJedisInstance(jedis);
		}
		return result;
	}

	public <T> void rpush(String key, List<String> oList) throws TimeoutException {
		Jedis jedis = null;
		try {
			jedis = getJedis();
			for (String s : oList) {
				jedis.rpush(key, s);
			}
		} catch (TimeoutException e) {
			throw e;
		} finally {
			releaseJedisInstance(jedis);
		}
	}

	public Long lpush(String key, String string) throws TimeoutException {
		Long result = 0L;
		Jedis jedis = null;
		try {
			jedis = getJedis();
			result = jedis.lpush(key, string);
		} finally {
			releaseJedisInstance(jedis);
		}
		return result;
	}

	public void lpush(String key, List<String> oList) throws TimeoutException {
		Jedis jedis = null;
		try {
			jedis = getJedis();
			for (String string : oList) {
				jedis.lpush(key, string);
			}
		} finally {
			releaseJedisInstance(jedis);
		}
	}

	public List<String> lpop(String key, int size) throws TimeoutException {
		String json = null;
		Jedis jedis = null;
		int count = 0;
		List<String> results = new ArrayList<>();

		try {
			jedis = getJedis();
			long exists = jedis.llen(key);
			count = (int) (exists > size ? size : exists);
			for (int i = 0; i < count; i++) {
				json = jedis.lpop(key);
				if (json != null) {
					results.add(json);
				}
			}
		} catch (TimeoutException e) {
			throw e;
		} finally {
			if (jedis != null) {
				releaseJedisInstance(jedis);
			}
		}

		return results;
	}

	public String lpop(String key) throws TimeoutException {
		Jedis jedis = null;
		try {
			jedis = getJedis();
			return jedis.lpop(key);
		} catch (TimeoutException e) {
			throw e;
		} finally {
			if (jedis != null) {
				releaseJedisInstance(jedis);
			}
		}
	}

	public String rpop(String key) throws TimeoutException {
		Jedis jedis = null;
		try {
			jedis = getJedis();
			return jedis.rpop(key);
		} catch (TimeoutException e) {
			throw e;
		} finally {
			if (jedis != null) {
				releaseJedisInstance(jedis);
			}
		}
	}

	public List<String> blpop(int timeout, String... key) throws TimeoutException {
		Jedis jedis = null;
		List<String> result = null;
		try {
			jedis = getJedis();
			result = jedis.blpop(timeout, key);
		} catch (TimeoutException e) {
			throw e;
		} finally {
			if (jedis != null) {
				releaseJedisInstance(jedis);
			}
		}
		return result;
	}

	public Long lrem(String key, int count, String value) throws TimeoutException {
		Jedis jedis = null;
		try {
			jedis = getJedis();
			return jedis.lrem(key, count, value);
		} catch (TimeoutException e) {
			throw e;
		} finally {
			if (jedis != null) {
				releaseJedisInstance(jedis);
			}
		}
	}

	public String lindex(String key, int index) throws TimeoutException {
		Jedis jedis = null;
		try {
			jedis = getJedis();
			return jedis.lindex(key, index);
		} catch (TimeoutException e) {
			throw e;
		} finally {
			if (jedis != null) {
				releaseJedisInstance(jedis);
			}
		}
	}

	public String lset(String key, int index, String value) throws TimeoutException {
		Jedis jedis = null;
		try {
			jedis = getJedis();
			return jedis.lset(key, index, value);
		} catch (TimeoutException e) {
			throw e;
		} finally {
			if (jedis != null) {
				releaseJedisInstance(jedis);
			}
		}
	}

	public Long llen(String key) throws TimeoutException {
		Long result = 0L;
		Jedis jedis = null;
		try {
			jedis = getJedis();
			result = jedis.llen(key);
		} finally {
			releaseJedisInstance(jedis);
		}
		return result;
	}

	public String ltrim(String key, int start, int end) throws TimeoutException {
		String result = null;
		Jedis jedis = null;
		try {
			jedis = getJedis();
			result = jedis.ltrim(key, start, end);
		} finally {
			releaseJedisInstance(jedis);
		}
		return result;
	}

	public List<String> lrange(String key, int start, int end) throws TimeoutException {
		List<String> result = new ArrayList<String>();
		Jedis jedis = null;
		try {
			jedis = getJedis();
			result = jedis.lrange(key, start, end);
		} finally {
			releaseJedisInstance(jedis);
		}
		return result;
	}

	/*************************
	 * 对Set(集合)操作集合 SADD SREM SMEMBERS SISMEMBER SCARD SMOVE SPOP SRANDMEMBER
	 * SINTER SINTERSTORE SUNION SUNIONSTORE SDIFF SDIFFSTORE
	 *************************/

	public Long sadd(String key, String member) throws TimeoutException {
		Jedis jedis = null;
		try {
			jedis = getJedis();
			return jedis.sadd(key, member);
		} catch (TimeoutException e) {
			throw e;
		} finally {
			if (jedis != null) {
				releaseJedisInstance(jedis);
			}
		}
	}

	public Long scard(String key) throws TimeoutException {
		Jedis jedis = null;
		try {
			jedis = getJedis();
			return jedis.scard(key);
		} catch (TimeoutException e) {
			throw e;
		} finally {
			if (jedis != null) {
				releaseJedisInstance(jedis);
			}
		}
	}

	public Set<String> smembers(String key) throws TimeoutException {
		Set<String> result = null;
		Jedis jedis = null;
		try {
			jedis = getJedis();
			result = jedis.smembers(key);
		} catch (TimeoutException e) {
			throw e;
		} finally {
			releaseJedisInstance(jedis);
		}
		return result;
	}

	public void srem(String key, String member) throws TimeoutException {
		if (StringUtils.isNotBlank(key) && StringUtils.isNotBlank(member)) {
			Jedis jedis = null;
			try {
				jedis = getJedis();
				jedis.srem(key, member);
			} catch (TimeoutException e) {
				throw e;
			} finally {
				if (jedis != null) {
					releaseJedisInstance(jedis);
				}
			}
		}
	}

	public boolean sismember(String key, String member) throws TimeoutException {
		boolean ismember = false;
		if (StringUtils.isNotBlank(key) && StringUtils.isNotBlank(member)) {
			Jedis jedis = null;
			try {
				jedis = getJedis();
				ismember = jedis.sismember(key, member);
			} catch (TimeoutException e) {
				throw e;
			} finally {
				if (jedis != null) {
					releaseJedisInstance(jedis);
				}
			}
		}
		return ismember;
	}

	/*************************
	 * 有序集(Sorted set) ZADD ZREM ZCARD ZCOUNT ZSCORE ZINCRBY ZRANGE ZREVRANGE
	 * ZRANGEBYSCORE ZREVRANGEBYSCORE ZRANK ZREVRANK ZREMRANGEBYRANK
	 * ZREMRANGEBYSCORE ZINTERSTORE ZUNIONSTORE
	 *************************/

	public void zremrangeByScore(String key, Double start, Double end) throws TimeoutException {
		Jedis jedis = null;
		try {
			jedis = getJedis();
			jedis.zremrangeByScore(key, start, end);
		} catch (TimeoutException e) {
			throw e;
		} finally {
			if (jedis != null) {
				releaseJedisInstance(jedis);
			}
		}
	}

	@Override
	public void zadd(String key, String value, Double score) throws TimeoutException {
		Jedis jedis = null;
		try {
			jedis = getJedis();
			jedis.zadd(key,score,value);
		} catch (TimeoutException e) {
			throw e;
		} finally {
			if (jedis != null) {
				releaseJedisInstance(jedis);
			}
		}
	}

	@Override
	public Set<String> zrangeByScore(String key, String min, String max, int offset, int limit) throws TimeoutException {
		Jedis jedis = null;
		try {
			jedis = getJedis();
			return jedis.zrangeByScore(key, min, max, offset, limit);
		} catch (TimeoutException e) {
			throw e;
		} finally {
			if (jedis != null) {
				releaseJedisInstance(jedis);
			}
		}
	}

	@Override
	public Set<String> zrevrangeByScore(String key, String min, String max, int offset, int limit) throws TimeoutException {
		Jedis jedis = null;
		try {
			jedis = getJedis();
			return jedis.zrevrangeByScore(key, min, max, offset, limit);
		} catch (TimeoutException e) {
			throw e;
		} finally {
			if (jedis != null) {
				releaseJedisInstance(jedis);
			}
		}
	}
	@Override
	public Set<String> zrevrangeByScore(String key, Double min, Double max, int offset, int limit) throws TimeoutException {
		Jedis jedis = null;
		try {
			jedis = getJedis();
			return jedis.zrevrangeByScore(key, min, max, offset, limit);
		} catch (TimeoutException e) {
			throw e;
		} finally {
			if (jedis != null) {
				releaseJedisInstance(jedis);
			}
		}
	}

	@Override
	public Set<String> zrangeByScore(String key, Double min,Double max,int offset ,int limit) throws TimeoutException {
		Jedis jedis = null;
		try {
			jedis = getJedis();
			return jedis.zrangeByScore(key, min, max, offset, limit);
		} catch (TimeoutException e) {
			throw e;
		} finally {
			if (jedis != null) {
				releaseJedisInstance(jedis);
			}
		}
	}

	@Override
	public Set<Tuple> zrangeByScoreWithScores(String key, Double min,Double max,int offset ,int limit) throws TimeoutException {
		Jedis jedis = null;
		try {
			jedis = getJedis();
			return jedis.zrangeByScoreWithScores(key, min, max, offset, limit);
		} catch (TimeoutException e) {
			throw e;
		} finally {
			if (jedis != null) {
				releaseJedisInstance(jedis);
			}
		}
	}

	@Override
	public Long zcard(String key) throws TimeoutException {
		Jedis jedis = null;
		try {
			jedis = getJedis();
			return jedis.zcard(key);
		} catch (TimeoutException e) {
			throw e;
		} finally {
			if (jedis != null) {
				releaseJedisInstance(jedis);
			}
		}
	}

	/*************************
	 * Pub/Sub(发布/订阅) PUBLISH SUBSCRIBE PSUBSCRIBE UNSUBSCRIBE PUNSUBSCRIBE
	 *************************/

	/**
	 * 将信息 message 发送到指定的频道 channel
	 *
	 * @Title: publish
	 * @Description:
	 * @param channel
	 * @param message
	 * @return
	 * @throws TimeoutException
	 */
	public Long publish(String channel, String message) throws TimeoutException {
		Jedis jedis = null;
		try {
			jedis = getJedis();
			return jedis.publish(channel, message);
		} catch (TimeoutException e) {
			throw e;
		} finally {
			if (jedis != null) {
				releaseJedisInstance(jedis);
			}
		}
	}

	/**
	 * 订阅给定的一个或多个频道的信息
	 *
	 * @Title: subscribe
	 * @Description:
	 * @param jedisPubSub
	 * @param channels
	 * @throws TimeoutException
	 */
	public void subscribe(JedisPubSub jedisPubSub, String... channels) throws TimeoutException {
		Jedis jedis = null;
		try {
			jedis = getJedis();
			jedis.subscribe(jedisPubSub, channels);
		} catch (TimeoutException e) {
			throw e;
		} finally {
			if (jedis != null) {
				releaseJedisInstance(jedis);
			}
		}
	}

	/*************************
	 * Transaction(事务) WATCH UNWATCH MULTI DISCARD EXEC
	 *************************/


}
