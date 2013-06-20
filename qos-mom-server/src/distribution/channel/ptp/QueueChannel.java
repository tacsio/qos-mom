package distribution.channel.ptp;

import infrastructure.Broker;

import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingDeque;

import util.Configuration;
import distribution.message.Message;

public class QueueChannel {

	private Map<String, Queue<Message>> queues;
	private Broker broker;
	private Configuration config;

	public QueueChannel() {
		this.queues = new ConcurrentHashMap<String, Queue<Message>>();
		this.config = Configuration.load();
		this.broker = new Broker(this, config.getPort());
		Thread listener = new Thread(this.broker);
		listener.start();
	}

	public synchronized void add(String queueName, Message msg) {
		if (!this.queues.containsKey(queueName)) {
			Queue<Message> queue = new LinkedBlockingDeque<Message>();
			this.queues.put(queueName, queue);
		}
		this.queues.get(queueName).add(msg);
	}

	public synchronized Message receive(String queueName) {
		Message msg = null;
		if (this.queues.containsKey(queueName)) {
			msg = this.queues.get(queueName).poll();
		}
		return msg;
	}
}
