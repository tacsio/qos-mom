package distribution.channel.ptp;

import java.util.Map;

import util.Configuration;
import util.Constants;
import infrastructure.Broker;
import distribution.message.Message;

public class QueueChannel {

	private Broker broker;
	private Configuration config;

	public QueueChannel() {
		this.config = Configuration.load();
		this.broker = new Broker(this.config.getServerHost(),
				this.config.getServerPort());
	}

	public void send(String queueName, Message msg) {
		// transform msg
		Map<String, String> headers = msg.getHeaders();
		headers.put(Constants.CHANNEL, Constants.CHANNEL_PTP);
		headers.put(Constants.QUEUE_NAME, queueName);
		msg.setHeaders(headers);

		this.broker.send(msg);
	}

}
