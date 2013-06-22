package mom;

import distribution.channel.pubsub.TopicChannel;
import distribution.message.Subscription;

public class Subscriber {

	public Subscriber() {
		TopicChannel channel = TopicChannel.getInstance();
		Subscription subscription = new QosSubscription();
		channel.subscribe("teste", subscription);
	}
}
