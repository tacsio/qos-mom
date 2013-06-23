package distribution.channel.ptp;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Map;

import util.Configuration;
import util.Constants;
import infrastructure.Broker;
import distribution.message.Message;

public class QueueChannel {

	private static QueueChannel instance;
	private Broker broker;
	private Configuration config;

	public static QueueChannel getInstance() {
		if (null == instance) {
			instance = new QueueChannel();
		}
		return instance;
	}

	private QueueChannel() {
		this.config = Configuration.load();
		this.broker = Broker.getBroker(this.config.getServerHost(),
				this.config.getServerPtpPort(), this.config.getListenerPort());
		this.broker.setQueueChannel(this);
	}

	public void send(String queueName, Message msg) {
		// transform msg
		Map<String, String> headers = msg.getHeaders();
		headers.put(Constants.CHANNEL, Constants.CHANNEL_PTP);
		headers.put(Constants.QUEUE_NAME, queueName);
		msg.setHeaders(headers);

		try {
			this.broker.send(msg);
		} catch (UnknownHostException e) {
			e.printStackTrace();
			// TODO: add on non sended list
		} catch (IOException e) {
			e.printStackTrace();
			// TODO: add on non sended list
		}
	}
}
