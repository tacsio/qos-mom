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
import distribution.filter.MessageFilter;
import distribution.message.Message;

public class Broker implements Runnable {

	private int port;
	private MessageFilter messageFilter;

	public Broker(MessageFilter messageFilter, int port) {
		this.port = port;
		this.messageFilter = messageFilter;
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
				this.messageFilter.filterMessages(jsonMessage);
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
