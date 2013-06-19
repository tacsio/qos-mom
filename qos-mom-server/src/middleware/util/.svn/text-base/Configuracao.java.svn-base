package middleware.util;

import middleware.Protocolo;

public class Configuracao {

	private String host;
	private int port;

	private byte protocol;

	public Configuracao(String host, int port, byte protocol) {
		this.port = port;
		this.host = host;
		this.protocol = protocol;
	}

	public Configuracao(String host, int port) {
		this.host = host;
		this.port = port;
		this.protocol = Protocolo.TCP;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public int getPort() {
		return port;
	}

	public byte getProtocol() {
		return protocol;
	}

	public void setProtocol(byte protocol) {
		this.protocol = protocol;
	}

}
