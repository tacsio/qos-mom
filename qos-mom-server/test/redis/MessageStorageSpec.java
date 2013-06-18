package redis;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import service.storage.MessageStorage;
import service.storage.redis.RedisStorage;

public class MessageStorageSpec {

	private MessageStorage storage;
	
	@Before
	public void setUp(){
		this.storage = RedisStorage.getInstance();
	}
	
	@Test
	public void testPut() {
		this.storage.put("foo", "bar");
		assertTrue(true);
	}

}
