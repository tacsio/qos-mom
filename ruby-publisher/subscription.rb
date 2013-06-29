require 'json'
require_relative 'message'

class Subscription

	attr_accessor :topic, :ip, :port

	def initialize 
		@topic = ""
		@ip = ""
		@port = 0
	end

	def on_message(topic, msg)
		
	end

	def to_json
		hash = {}
        self.instance_variables.each do |var|
            hash[var.to_s.gsub('@','')] = self.instance_variable_get var
        end
        hash.to_json
	end
end