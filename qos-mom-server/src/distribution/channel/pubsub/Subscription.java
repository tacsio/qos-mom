package distribution.channel.pubsub;

public class Subscription {

	private String topic;

	private String source;
	

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
