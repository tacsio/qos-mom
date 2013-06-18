package service.marshalling;

import com.google.gson.Gson;

import distribution.domain.Message;
import distribution.domain.pubsub.Subscription;

public class JsonSerializer {

	private static JsonSerializer instance;

	private Gson gson;

	private JsonSerializer() {
		this.gson = new Gson();
	}

	public static JsonSerializer getInstance() {
		if (null == instance) {
			instance = new JsonSerializer();
		}
		return instance;
	}

	public String getJson(Object msg) {
		String json = this.gson.toJson(msg);
		return json;
	}

	public Message getMessage(String json) {
		Message msg = this.gson.fromJson(json, Message.class);
		return msg;
	}

	public Subscription getSubscription(String json) {
		Subscription sub = this.gson.fromJson(json, Subscription.class);
		return sub;
	}

}
