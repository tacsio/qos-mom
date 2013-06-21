package distribution.channel.pubsub;

import distribution.message.Message;

public abstract class Subscription {

	private String topic;

	//ip:port
	private String source;

	public abstract void onMessage(String topic, Message msg);

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}
	
	public String toString() {
		return "Subscription [topic=" + topic + ", source=" + source + "]";
	}

}
