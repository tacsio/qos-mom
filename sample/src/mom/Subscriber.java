package mom;

import distribution.channel.pubsub.Subscription;
import distribution.channel.pubsub.TopicChannel;

public class Subscriber {

	public Subscriber() {
		TopicChannel channel = TopicChannel.getInstance();
		Subscription subscription = new QosSubscription();
		channel.subscribe("teste", subscription);
	}
}
