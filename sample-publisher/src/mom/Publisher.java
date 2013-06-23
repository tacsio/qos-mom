package mom;

import distribution.channel.pubsub.TopicChannel;
import distribution.message.Message;

public class Publisher {

	public Publisher() {
		TopicChannel channel = TopicChannel.getInstance();
		int i =0;
		//for (int i = 0; i < 10; i++) {
			Message msg = new Message();
			msg.setPayload("Hello World " + i);
			channel.publish("test", msg);
			System.out.println("msg published: " + msg.getPayload());
		//}
	}
	
	public static void main(String[] args) {
		new Publisher();
	}
}
