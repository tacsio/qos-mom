package distribution.channel.pubsub;

import infrastructure.Broker;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import distribution.message.Message;
import distribution.message.Subscription;

public class TopicChannel {

	private volatile Map<String, List<Subscription>> subscriptions;
	private Broker broker;

	public TopicChannel() {
		this.subscriptions = new ConcurrentHashMap<String, List<Subscription>>();
	}

	public synchronized void updateSubscribers(String topic, Message msg) {
		for(Subscription s : this.subscriptions.get(topic)){
			try {
				this.broker.send(msg, s.getIp(), s.getPort());
			} catch (UnknownHostException e) {
				e.printStackTrace();
				// TODO: add on non sended list
			} catch (IOException e) {
				e.printStackTrace();
				// TODO: add on non sended list
			}
		}
	}

	public synchronized void subscribe(Subscription subscription) {
		List<Subscription> subscList = this.subscriptions.get(subscription
				.getTopic());
		if (null == subscList) {
			subscList = new ArrayList<Subscription>();
		}
		subscList.add(subscription);
		this.subscriptions.put(subscription.getTopic(), subscList);
	}
}
