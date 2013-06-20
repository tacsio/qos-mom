package distribution.channel.pubsub;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import distribution.message.Message;

public class TopicChannel {

	private Map<String, List<Subscription>> subscriptions;

	public TopicChannel() {
		this.subscriptions = new ConcurrentHashMap<String, List<Subscription>>();
	}

	public synchronized void publish(String topic, Message msg) {
		
	}

	public void subscribe(Subscription subscription) {
		List<Subscription> subscList = this.subscriptions.get(subscription
				.getTopic());
		if (null == subscList) {
			subscList = new ArrayList<Subscription>();
		}
		subscList.add(subscription);
		this.subscriptions.put(subscription.getTopic(), subscList);
	}
}
