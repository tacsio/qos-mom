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
import util.Network;
import distribution.channel.ptp.QueueChannel;
import distribution.channel.pubsub.TopicChannel;
import distribution.message.Message;

public class Broker implements Runnable {

	private static Broker instance;

	private int listenerPort;
	private String localIp;
	private int serverPort;

	private String serverHost;
	private QueueChannel queueChannel;
	private TopicChannel topicChannel;

	public static Broker getBroker(String serverHost, int serverPort,
			int listenerPort) {
		if (null == instance) {
			instance = new Broker(serverHost, serverPort, listenerPort);
			Thread listener = new Thread(instance);
			listener.start();
		}
		return instance;
	}

	private Broker(String serverHost, int serverPort, int listenerPort) {
		this.serverPort = serverPort;
		this.serverHost = serverHost;
		this.listenerPort = listenerPort;
		this.localIp = Network.getLocalIpAddress();
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

	public int getListenerPort() {
		return this.listenerPort;
	}

	public void send(Message msg) throws UnknownHostException, IOException {
		String json = JsonSerializer.getInstance().getJson(msg);
		Socket client = new Socket(this.serverHost, this.serverPort);

		OutputStream out = client.getOutputStream();
		out.write(json.getBytes());
		out.flush();
		client.close();
	}

	@Override
	public void run() {
		// retorno de msg sao todas de subiscricoes
		System.out.println("Start listener on " + this.listenerPort);

		try {
			ServerSocket listenSocket = new ServerSocket(this.listenerPort);
			Socket connection;

			while (true) {
				connection = listenSocket.accept();
				String jsonMessage = getStringFromInputStream(connection.getInputStream());
				Message message = JsonSerializer.getInstance().getMessage(
						jsonMessage);

				if (message.getHeaders().get(Constants.CHANNEL)
						.equals(Constants.CHANNEL_PTP)) {
					this.queueChannel.updateReceivers(message);
				} else if (message.getHeaders().get(Constants.CHANNEL)
						.equals(Constants.CHANNEL_TOPIC)) {
					this.topicChannel.updateSubscribers(message);
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
