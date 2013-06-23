package mom;

import distribution.message.Message;

public class QosSubscription extends distribution.message.Subscription {

	@Override
	public void onMessage(String topic, Message msg) {
		System.out.println("Received " + msg.toString());
	}

}
