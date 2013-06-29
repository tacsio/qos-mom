package distribution.channel.pubsub;

import infrastructure.Broker;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import util.Configuration;

import distribution.message.Message;
import distribution.message.Subscription;

public class TopicChannel {

	private volatile Map<String, List<Subscription>> subscriptions;
	private Broker broker;
	private Configuration config;

	public TopicChannel() {
		this.subscriptions = new ConcurrentHashMap<String, List<Subscription>>();
		this.config = Configuration.load();
		this.broker = new Broker(this, config.getServerPubSubPort());
		Thread listener = new Thread(this.broker);
		listener.start();
	}

	public synchronized void updateSubscribers(String topic, Message msg) {
		if(null != this.subscriptions.get(topic)){
			for (Subscription s : this.subscriptions.get(topic)) {
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
		} else {
			System.out.println("No subscribers for " + msg);
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
		//TODO>:REMOVE
		System.out.println(String.format("Client: %s:%s on %s",
				subscription.getIp(), subscription.getPort(),
				subscription.getTopic()));
	}
}
