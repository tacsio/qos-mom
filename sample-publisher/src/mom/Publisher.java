package mom;

import distribution.channel.pubsub.TopicChannel;
import distribution.message.Message;

public class Publisher {

	public Publisher() {
		TopicChannel channel = TopicChannel.getInstance();

		for (int i = 0; i < 10; i++) {
			Message msg = new Message();
			msg.setPayload("Hello World " + i);
			channel.publish("test", msg);
		}
	}
	
	public static void main(String[] args) {
		new Publisher();
	}
}
