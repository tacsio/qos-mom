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

public class Broker implements Runnable {

	private static Broker instance;

	private int localPort;
	private String localIp;

	private int serverPort;
	private String serverHost;
	private QueueChannel queueChannel;
	private TopicChannel topicChannel;

	public static Broker getBroker(String serverHost, int serverPort) {
		if (null == instance) {
			instance = new Broker(serverHost, serverPort);
			Thread listener = new Thread(instance);
			listener.start();
		}
		return instance;
	}

	private Broker(String serverHost, int serverPort) {
		this.serverPort = serverPort;
		this.serverHost = serverHost;
	}

	public void setQueueChannel(QueueChannel queueChannel) {
		this.queueChannel = queueChannel;
	}

	public void setTopicChannel(TopicChannel topicChannel) {
		this.topicChannel = topicChannel;
	}

	public String getLocalIp() {
		return this.localIp;
	}

	public int getLocalPort() {
		return this.localPort;
	}

	public void send(Message msg) throws UnknownHostException, IOException {
		String json = JsonSerializer.getInstance().getJson(msg);
		Socket client = new Socket(this.serverHost, this.serverPort);

		ObjectOutputStream out = new ObjectOutputStream(
				client.getOutputStream());
		out.writeObject(json);
		out.flush();
		client.close();
	}

	@Override
	public void run() {
		// retorno de msg sao todas de subiscricoes
		System.out.println("Start listener");

		try {
			ServerSocket listenSocket = new ServerSocket(this.serverPort);

			this.localIp = listenSocket.getInetAddress().getHostAddress();
			this.localPort = listenSocket.getLocalPort();
			// TODO: verificar porta

			Socket connection;
			ObjectInputStream input;
			// ObjectOutputStream output;

			while (true) {
				connection = listenSocket.accept();
				// output = new
				// ObjectOutputStream(connection.getOutputStream());
				input = new ObjectInputStream(connection.getInputStream());
				String jsonMessage = (String) input.readObject();
				Message message = JsonSerializer.getInstance().getMessage(
						jsonMessage);

				if (message.getHeaders().get(Constants.CHANNEL)
						.equals(Constants.CHANNEL_PTP)) {
					// receive msg
				} else if (message.getHeaders().get(Constants.CHANNEL)
						.equals(Constants.CHANNEL_TOPIC)) {
					this.topicChannel.updateSubscribers(message);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
}
