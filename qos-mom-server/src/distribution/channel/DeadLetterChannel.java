package distribution.channel;

import service.storage.redis.RedisStorage;
import util.Constants;
import distribution.message.Message;

public class DeadLetterChannel {

	public static void publish(String topic, Message msg) {
		System.out.println("Message not sended: " + msg);
		DeadLetterChannel.saveMessage(topic, msg);
	}
	
	private static void saveMessage(String topic, Message msg) {
		String ip = msg.getHeaders().get(Constants.SOURCE);
		String timestamp = msg.getHeaders().get(Constants.TIMESTAMP);
		String key = "dead#" + topic + "#" + ip + "#" + timestamp;
		try{
			RedisStorage.getInstance().put(key, msg.getPayload());
		} catch (Exception e) {
			System.out.println("Database off");
		}
	}
	
}
