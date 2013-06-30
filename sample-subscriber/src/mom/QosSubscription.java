package mom;

import util.Constants;
import database.SqliteDatabase;
import distribution.message.Message;

public class QosSubscription extends distribution.message.Subscription {

	private SqliteDatabase database;
	
	public QosSubscription() {
		this.database = new SqliteDatabase();
	}
	
	@Override
	public void onMessage(String topic, Message msg) {
		System.out.println("Received " + msg.toString());
		String metric = msg.getPayload().split(":")[0];
		String source = msg.getHeaders().get(Constants.SOURCE);
		double value = Double.parseDouble(msg.getPayload().split(":")[1]);
		long timestamp = Long.parseLong(msg.getHeaders().get(Constants.TIMESTAMP));
		
		this.database.insertQosData(metric, source, value, timestamp);
	}

}
