package service.storage.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import service.storage.MessageStorage;

public class RedisStorage implements MessageStorage {

	private volatile JedisPool pool;
	private static RedisStorage instance;

	private final String host = "localhost";
	private final int port = 6379;

	private RedisStorage() {
		this.pool = new JedisPool(new JedisPoolConfig(), host, port);
	}

	public static RedisStorage getInstance() {
		if (null == instance) {
			instance = new RedisStorage();
		}
		return instance;
	}

	protected Jedis getConnection() {
		Jedis resource = null;
		resource = this.pool.getResource();

		return resource;
	}

	protected void closeConnection() {
		this.pool.destroy();
	}

	public void put(String key, String value) {
		Jedis resource = this.getConnection();
		resource.rpush(key, value);
		this.returnResource(resource);
	}

	private void returnResource(Jedis resource) {
		this.pool.returnResource(resource);
		
	}
}
