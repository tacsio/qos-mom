package distribution.domain.ptp;

import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingDeque;

import distribution.domain.Message;

public class QueueRegion {

	private Map<String, Queue<Message>> queues;

	public QueueRegion() {
		this.queues = new ConcurrentHashMap<String, Queue<Message>>();
	}

	public void add(String queueName, Message msg) {
		if (!this.queues.containsKey(queueName)) {
			Queue<Message> queue = new LinkedBlockingDeque<Message>();
			this.queues.put(queueName, queue);
		}
		this.queues.get(queueName).add(msg);
	}

	public Message receive(String queueName) {
		Message msg = null;
		if (this.queues.containsKey(queueName)) {
			msg = this.queues.get(queueName).poll();
		}
		return msg;
	}
}
