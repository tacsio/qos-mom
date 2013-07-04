package distribution.message;

import util.Constants;
import util.Network;

public class ContentEnricher {

	public static void enrichMessage(Message message){
		message.getHeaders().put(Constants.SOURCE, Network.getLocalIpAddress());
		message.getHeaders().put(Constants.TIMESTAMP, System.currentTimeMillis()+"");
	}
}
