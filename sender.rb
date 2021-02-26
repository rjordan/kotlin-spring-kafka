#!/usr/bin/env ruby
require 'bundler/inline'
require 'json'

gemfile do
  source 'https://rubygems.org'
  gem 'ruby-kafka'
  gem 'faker'
end

kafka = Kafka.new(["192.168.33.1:9092"], client_id: "ruby1")

print("Sending messages")
loop do
  payload = { name: Faker::Name.name }
  kafka.deliver_message(payload.to_json, topic: "topic1", partition_key: SecureRandom.uuid)
  print(".")
  sleep(2)
end
