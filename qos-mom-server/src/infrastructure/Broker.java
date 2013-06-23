package infrastructure;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import service.marshalling.JsonSerializer;
import util.Constants;
import distribution.channel.ptp.QueueChannel;
import distribution.channel.pubsub.TopicChannel;
import distribution.message.Message;
import distribution.message.Subscription;

public class Broker implements Runnable {

	private int port;
	private QueueChannel queueChannel;
	private TopicChannel topicChannel;

	public Broker(QueueChannel queueRegion, int port) {
		this.port = port;
		this.queueChannel = queueRegion;
	}
	
	public Broker(TopicChannel topicRegion, int port) {
		this.port = port;
		this.topicChannel = topicRegion;
	}
	
	public void send(Message msg, String ip, int port) throws UnknownHostException, IOException {
		String json = JsonSerializer.getInstance().getJson(msg);
		Socket socket = new Socket(ip, port);
		
		ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
		out.writeObject(json);
		out.flush();
		socket.close();
	}

	@Override
	public void run() {
		System.out.println("Start server on " + this.port);
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
				
				//filter subscriptions (topic region)
				if(message.getHeaders().get(Constants.MESSAGE_TYPE).equals(Constants.SUBSCRIPTION_TYPE)){
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
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
}
