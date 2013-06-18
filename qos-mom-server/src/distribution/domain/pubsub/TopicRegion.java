package distribution.domain.pubsub;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import distribution.domain.Message;

public class TopicRegion {

	private Map<String, List<Subscription>> subscriptions;

	public TopicRegion() {
		this.subscriptions = new ConcurrentHashMap<String, List<Subscription>>();
	}

	public void publish(Message msg, String topic) {
		
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
