package infrastructure;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import service.marshalling.JsonSerializer;
import distribution.message.Message;

public class Broker {

	private int serverPort;
	private String serverHost;

	public Broker(String serverHost, int serverPort) {
		this.serverPort = serverPort;
		this.serverHost = serverHost;
	}

	public void send(Message msg) {
		try {
			String json = JsonSerializer.getInstance().getJson(msg);
			
			Socket client = new Socket(this.serverHost, this.serverPort);
			
			ObjectOutputStream out = new  ObjectOutputStream(client.getOutputStream());
			out.writeObject(json);
			out.flush();
			client.close();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
