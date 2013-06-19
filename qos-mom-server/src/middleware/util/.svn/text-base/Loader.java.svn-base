package middleware.util;

import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import middleware.Protocolo;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class Loader {

	private DocumentBuilderFactory dbf;
	private DocumentBuilder db;
	private Document doc;
	private String xmlConfig;

	public Loader(Configuracao c) throws ParserConfigurationException,
			SAXException, IOException {
		dbf = DocumentBuilderFactory.newInstance();
		db = dbf.newDocumentBuilder();
		xmlConfig = "config.xml";

		load(c);
	}

	public void load(Configuracao c) throws SAXException, IOException {

		this.doc = db.parse(xmlConfig);
		Element raiz = doc.getDocumentElement();

		NodeList configuracoes = raiz.getElementsByTagName("config");
		String protocolo = configuracoes.item(Protocolo.TCP).getFirstChild()
				.getNodeValue();

		String host;
		int port;
		byte protocol = 0;

		if (protocolo.equalsIgnoreCase("TCP")) {
			protocol = Protocolo.TCP;
		} else if (protocolo.equalsIgnoreCase("UDP")) {
			protocol = Protocolo.UDP;
		} else if (protocolo.equalsIgnoreCase("HTTP")) {
			protocol = Protocolo.HTTP;
		}
		port = Integer.parseInt(configuracoes.item(Protocolo.UDP)
				.getFirstChild().getNodeValue());
		host = configuracoes.item(Protocolo.HTTP).getFirstChild()
				.getNodeValue();

		c.setHost(host);
		c.setProtocol(protocol);
		if (protocol == Protocolo.HTTP) {
			c.setPort(80);
		} else {
			c.setPort(port);
		}
	}

}
