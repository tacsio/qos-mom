package queue;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import distribution.domain.Message;
import distribution.domain.ptp.QueueRegion;

public class QueueRegionSpec {

	private QueueRegion queue;
	
	@Before
	public void setUp() {
		this.queue = new QueueRegion();
	}
	
	@Test
	public void testQueueInsertionAndConsumption() {
		String queueName = "test";
		Message sended1 = this.getMessage();
		this.queue.add(queueName, sended1);
		Message sended2 = this.getMessage();
		sended2.setPayload("test");
		this.queue.add(queueName, sended2);
		
		Message polled = this.queue.receive(queueName);
		assertEquals(sended1.toString(), polled.toString());
		assertNotSame(sended1.toString(), polled.toString());
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
