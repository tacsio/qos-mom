require 'json'

class Message

	attr_accessor :headers, :payload

	def initialize 
		@headers = {}
		@payload = ""
	end

	def to_json
		hash = {}
		self.instance_variables.each do |var|
      hash[var.to_s.gsub('@','')] = self.instance_variable_get var
    end
    hash.to_json
	end
end