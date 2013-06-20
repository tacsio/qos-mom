package json;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import service.marshalling.JsonSerializer;
import distribution.channel.pubsub.Subscription;
import distribution.message.Message;

public class SerializerSpec {

	private JsonSerializer serializer = JsonSerializer.getInstance();

	@Test
	public void testMessageSerialization() {
		Message msg = this.getMessage();
		String json = serializer.getJson(msg);
		assertNotNull(json);

		Message received = serializer.getMessage(json);
		assertNotNull(received);

		assertEquals(msg.toString(), received.toString());
	}
	
	@Test
	public void testSubscriptionSerialization() {
		Subscription sub = this.getSubscription();
		String json = serializer.getJson(sub);
		assertNotNull(json);

		Subscription received = serializer.getSubscription(json);
		assertNotNull(received);

		assertEquals(sub.toString(), received.toString());
	}

	private Subscription getSubscription() {
		Subscription sub = new Subscription();
		sub.setTopic("qos");
		
		return sub;
	}

	private Message getMessage() {
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("source", "localhost");
		headers.put("type", "qos");

		String payload = "response-time: 30, availability: 40";

		Message msg = new Message();
		msg.setHeaders(headers);
		msg.setPayload(payload);

		return msg;
	}
}
