package util;

import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement
@XmlType(name = "")
@XmlAccessorType(XmlAccessType.FIELD)
public class Configuration {

	private static final String CONFIG = "config.xml";

	@XmlElement(name = "server-host")
	private String serverHost;

	@XmlElement(name = "ptp-port")
	private int serverPubSubPort;

	@XmlElement(name ="pubsub-port")
	private int serverPtpPort;

	
	public String getServerHost() {
		return serverHost;
	}

	public int getServerPubSubPort() {
		return serverPubSubPort;
	}

	public int getServerPtpPort() {
		return serverPtpPort;
	}

	public static Configuration load() {
		File file = new File(CONFIG);
		Configuration config = null;
		try {
			config = (Configuration) JAXBContext
					.newInstance(Configuration.class).createUnmarshaller()
					.unmarshal(file);
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		
		return config;

	}
	
	public static void main(String[] args) {
		Configuration c = Configuration.load();
		System.out.println(c);
	}
}
