package util;

import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlType(name = "")
@XmlAccessorType(XmlAccessType.FIELD)
public class Configuration {

	private static final String CONFIG = "/qos-mom-server/config.xml";

	@XmlElement(name = "host")
	private String host;

	@XmlElement(name = "port")
	private int port;

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
}
