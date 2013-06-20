package mom;

import distribution.message.Message;

public class QosSubscription extends distribution.channel.pubsub.Subscription {

	@Override
	public void onMessage(String topic, Message msg) {
		System.out.println(msg.toString());
	}

}
