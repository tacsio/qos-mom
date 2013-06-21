package distribution.channel.pubsub;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import infrastructure.Broker;
import service.marshalling.JsonSerializer;
import util.Configuration;
import util.Constants;
import distribution.message.Message;

public class TopicChannel {

	private static TopicChannel instance;
	private Broker broker;
	private Configuration config;
	private Map<String, List<Subscription>> subscriptions;

	public static TopicChannel getInstance() {
		if (null == instance) {
			instance = new TopicChannel();
		}
		return instance;
	}

	private TopicChannel() {
		this.subscriptions = new ConcurrentHashMap<String, List<Subscription>>();
		this.config = Configuration.load();
		this.broker = Broker.getBroker(this.config.getServerHost(),
				this.config.getServerPubSubPort());
		this.broker.setTopicChannel(this);
	}

	public void publish(String topic, Message msg) {
		Map<String, String> headers = msg.getHeaders();
		headers.put(Constants.CHANNEL, Constants.CHANNEL_TOPIC);
		headers.put(Constants.TOPIC_NAME, topic);
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

	public void subscribe(String topic, Subscription subscription) {
		if (!this.subscriptions.containsKey(topic)) {
			List<Subscription> subList = new ArrayList<Subscription>();
			this.subscriptions.put(topic, subList);
		}
		// Armazena calback (vira do subscription chamada do método onMessage)
		// o mapa de callback por topico eh chamado via updateSubscrivers
		subscription.setSource(String.format("%s:%s", this.broker.getLocalIp(),
				this.broker.getLocalPort()));
		subscription.setTopic(topic);
		this.subscriptions.get(topic).add(subscription);

		Message subscriptionMsg = new Message();
		Map<String, String> headers = new HashMap<String, String>();
		headers.put(Constants.MESSAGE_TYPE, Constants.SUBSCRIPTION_TYPE);
		subscriptionMsg.setHeaders(headers);
		subscriptionMsg.setPayload(JsonSerializer.getInstance().getJson(subscription));
		try {
			this.broker.send(subscriptionMsg);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public synchronized void updateSubscribers(Message msg) {
		String topicName = msg.getHeaders().get(Constants.TOPIC_NAME);
		for (Subscription s : this.subscriptions.get(topicName)) {
			s.onMessage(topicName, msg);
		}
	}
}
