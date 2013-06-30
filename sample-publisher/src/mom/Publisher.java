package mom;

import java.util.Random;

import distribution.channel.pubsub.TopicChannel;
import distribution.message.Message;

public class Publisher {

	public Publisher() {
		TopicChannel channel = TopicChannel.getInstance();
		Random random = new Random();
		for (int i = 0; i < 100; i++) {
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			Message msg = new Message();
			msg.setPayload("availability:" + random.nextInt(100));
			channel.publish("qos", msg);
			System.out.println("msg published: " + msg.getPayload());
		}
	}
	
	public static void main(String[] args) {
		new Publisher();
	}
}
