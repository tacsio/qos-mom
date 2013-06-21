package json;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import service.marshalling.JsonSerializer;
import distribution.channel.pubsub.Subscription;
import distribution.message.Message;

public class SerializerSpec {

	private JsonSerializer serializer = JsonSerializer.getInstance();
	
	@Test
	public void testSubscriptionSerialization() {
		Subscription sub = this.getSubscription();
		String json = serializer.getJson(sub);
		assertNotNull(json);
	}

	private Subscription getSubscription() {
		Subscription sub = new QosSubscription();
		sub.setTopic("qos");
		sub.setSource("localhost:15");
		
		return sub;
	}
}

class QosSubscription extends Subscription {

	@Override
	public void onMessage(String topic, Message msg) {
	}
	
}
