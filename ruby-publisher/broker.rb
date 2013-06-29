require 'socket'
require_relative 'message'

class Broker

	attr_accessor :ip, :port

	def initialize ip, port
		@ip = ip
		@port = port
	end

	def send(msg)
		sock = TCPSocket.new @ip, @port
		sock.send(msg.to_json, 0)
		sock.close
	end
end


m = Message.new
m.headers['channel'] = 'topic'
m.headers['topic_name'] = 'test'
m.payload = 'Hello ruby'

b = Broker.new '192.168.1.100', 6561
b.send(m)