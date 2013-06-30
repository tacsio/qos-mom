require_relative 'topic_channel'
require_relative 'message'

topic_channel = TopicChannel.new

100.times do 
	message = Message.new
	response_time = rand(2**10 +1)
	message.payload = "response-time:#{response_time}"

	topic_channel.publish('qos', message)
	sleep 1
end