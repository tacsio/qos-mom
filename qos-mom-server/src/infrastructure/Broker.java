package infrastructure;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
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
		
		OutputStream out = socket.getOutputStream();
		out.write(json.getBytes());
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

			while (true) {
				connection = listenSocket.accept();
				String jsonMessage = getStringFromInputStream(connection.getInputStream());
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
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static String getStringFromInputStream(InputStream is) {

		BufferedReader br = null;
		StringBuilder sb = new StringBuilder();

		String line;
		try {

			br = new BufferedReader(new InputStreamReader(is));
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return sb.toString();

	}
}
