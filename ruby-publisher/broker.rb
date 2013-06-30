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

	def local_ip
		Socket.ip_address_list.detect { |i| i.ipv4_private?}.ip_address 
	end
end