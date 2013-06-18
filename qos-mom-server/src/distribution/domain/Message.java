package distribution.domain;

import java.util.Map;

public class Message {

	private Map<String, String> headers;

	private String payload;

	public Map<String, String> getHeaders() {
		return headers;
	}

	public void setHeaders(Map<String, String> headers) {
		this.headers = headers;
	}

	public String getPayload() {
		return payload;
	}

	public void setPayload(String payload) {
		this.payload = payload;
	}

	@Override
	public String toString() {
		StringBuffer headersRepr = new StringBuffer();
		for(String key : headers.keySet()){
			headersRepr.append(key+": " + headers.get(key)+ ";");
		}
		return "Message [headers=" + headers + ", payload=" + payload + "]";
	}
}
