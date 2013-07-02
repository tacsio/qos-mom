package distribution.filter;

import service.marshalling.JsonSerializer;
import util.Constants;
import distribution.channel.ptp.QueueChannel;
import distribution.channel.pubsub.TopicChannel;
import distribution.message.Message;
import distribution.message.Subscription;

public class MessageFilter {

	private QueueChannel queueChannel;
	private TopicChannel topicChannel;

	
	public MessageFilter(QueueChannel queueChannel) {
		this.queueChannel = queueChannel;
	}
	
	public MessageFilter(TopicChannel topicChannel) {
		this.topicChannel = topicChannel;
	}
	
	public synchronized void filterMessages(String jsonMessage){
		Message message = JsonSerializer.getInstance().getMessage(
				jsonMessage);
		
		//filter subscriptions (topic region)
		if(message.getHeaders().containsKey(Constants.MESSAGE_TYPE)
				&& message.getHeaders().get(Constants.MESSAGE_TYPE).equals(Constants.SUBSCRIPTION_TYPE)){
			//the message payload contains json serialized subscription
			Subscription subscription = JsonSerializer.getInstance().getSubscription(message.getPayload());
			this.topicChannel.subscribe(subscription);
		//filter channel queue
		} else if (message.getHeaders().get(Constants.CHANNEL)
				.equals(Constants.CHANNEL_PTP)) {

			String queueName = message.getHeaders().get(
					Constants.QUEUE_NAME);
			this.queueChannel.add(queueName, message);
		
		//filter channel topic
		} else if (message.getHeaders().get(Constants.CHANNEL)
				.equals(Constants.CHANNEL_TOPIC)) {
			String topicName = message.getHeaders().get(
					Constants.TOPIC_NAME);
			this.topicChannel.updateSubscribers(topicName, message);
		}
	}
}
