package distribution.message;


public abstract class Subscription {

	private String topic;

	private int port;
	
	private String ip;

	public abstract void onMessage(String topic, Message msg);

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}
}
