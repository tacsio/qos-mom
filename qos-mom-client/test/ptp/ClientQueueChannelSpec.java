package ptp;

//import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import distribution.channel.ptp.QueueChannel;
import distribution.message.Message;

public class ClientQueueChannelSpec {

	private QueueChannel queue;

	@Before
	public void setUp() {
		this.queue = QueueChannel.getInstance();
	}

	@Test
	public void testSendMessage() {
		this.queue.send("test", getMessage());
		this.queue.send("test2", getMessage());
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
