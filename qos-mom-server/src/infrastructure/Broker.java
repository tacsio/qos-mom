package infrastructure;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

import service.marshalling.JsonSerializer;
import util.Constants;
import distribution.domain.Message;
import distribution.domain.ptp.QueueRegion;
import distribution.domain.pubsub.TopicRegion;

public class Broker implements Runnable {

	private int port;
	private QueueRegion queueRegion;
	private TopicRegion topicRegion;

	public Broker(int port) {
		this.port = port;
		this.queueRegion = new QueueRegion();
	}

	@Override
	public void run() {

		try {
			ServerSocket listenSocket = new ServerSocket(this.port);
			Socket connection;
			ObjectInputStream input;
			//ObjectOutputStream output;

			while (true) {
				connection = listenSocket.accept();
				//output = new ObjectOutputStream(connection.getOutputStream());
				input = new ObjectInputStream(connection.getInputStream());
				String jsonMessage = (String) input.readObject();
				Message message = JsonSerializer.getInstance().getMessage(
						jsonMessage);
				if (message.getHeaders().get(Constants.DOMAIN)
						.equals(Constants.DOMAIN_PTP)) {

					String queueName = message.getHeaders().get(
							Constants.QUEUE_NAME);
					this.queueRegion.add(queueName, message);
				} else if (message.getHeaders().get(Constants.DOMAIN)
						.equals(Constants.DOMAIN_PTP)) {
					String topicName = message.getHeaders().get(
							Constants.TOPIC_NAME);
					this.topicRegion.publish(topicName, message);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
}
