require 'yaml'
require_relative 'broker'
require_relative 'message'

CONFIG = YAML.load_file('config.yml')

class TopicChannel

	def initialize
		host = CONFIG['config']['server-host']
		port = CONFIG['config']['pubsub-port']
		@broker = Broker.new(host, port)
	end

	def publish(topic, msg)
		msg.headers = Hash.new unless msg.headers
		msg.headers[:channel] = 'topic'
		msg.headers[:topic_name] = topic

		@broker.send msg
	end
end