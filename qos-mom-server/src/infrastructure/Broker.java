package infrastructure;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

import service.marshalling.JsonSerializer;
import util.Constants;
import distribution.channel.ptp.QueueChannel;
import distribution.channel.pubsub.TopicChannel;
import distribution.message.Message;

public class Broker implements Runnable {

	private int port;
	private QueueChannel queueRegion;
	private TopicChannel topicRegion;

	public Broker(QueueChannel queueRegion, int port) {
		this.port = port;
		this.queueRegion = queueRegion;
	}
	
	public Broker(TopicChannel topicRegion, int port) {
		this.port = port;
		this.topicRegion = topicRegion;
	}
	
	public void send() {
		
	}

	@Override
	public void run() {
		System.out.println("Start server");
		//receive msgs
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
				if (message.getHeaders().get(Constants.CHANNEL)
						.equals(Constants.CHANNEL_PTP)) {

					String queueName = message.getHeaders().get(
							Constants.QUEUE_NAME);
					this.queueRegion.add(queueName, message);
				} else if (message.getHeaders().get(Constants.CHANNEL)
						.equals(Constants.CHANNEL_PTP)) {
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
