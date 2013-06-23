package infrastructure;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

import service.marshalling.JsonSerializer;
import util.Constants;
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
		try {
			this.localIp = Inet4Address.getLocalHost().getHostAddress();
			if(this.localIp.equals("127.0.0.1") || this.localIp.equals("0.0.0.0")){
				this.localIp = this.getLocalIpAddress();
			}
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
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

		ObjectOutputStream out = new ObjectOutputStream(
				client.getOutputStream());
		out.writeObject(json);
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
					// TODO: receive msg ptp
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

	private String getLocalIpAddress() throws SocketException {

		String ip = null;
		Enumeration<NetworkInterface> interfaces = NetworkInterface
				.getNetworkInterfaces();
		while (interfaces.hasMoreElements()) {
			NetworkInterface current = interfaces.nextElement();
			if (!current.isUp()
					|| current.isLoopback()
					|| current.isVirtual()
					|| !(current.getDisplayName().equals("eth0") || current
							.getDisplayName().equals("wlan0")))
				continue;
			Enumeration<InetAddress> addresses = current.getInetAddresses();
			while (addresses.hasMoreElements()) {
				InetAddress current_addr = addresses.nextElement();
				if (current_addr.isLoopbackAddress())
					continue;
				if (current.getDisplayName().equals("eth0")
						|| current.getDisplayName().equals("wlan0")) {
					ip = current_addr.getHostAddress();
				}
			}
		}

		return ip;
	}
}
