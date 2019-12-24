package com.techluminary.spring.kafkaconsumer;

import java.util.List;
import java.util.Map;

import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.TopicPartition;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.listener.ConsumerSeekAware;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class Processor implements ConsumerSeekAware{
	
	@Value("${topic}")
	private String topicName;
	
	
	@KafkaListener(id = "${kafkaConsumerProps.consumerGroup}", topics = "${topic}")
	public void processor(List<ConsumerRecord<GenericRecord, GenericRecord>> batch, Acknowledgment ack) {
		processBatch(batch);
		ack.acknowledge();
	}

	private void processBatch(List<ConsumerRecord<GenericRecord, GenericRecord>> batch) {
		
		batch.forEach( record -> {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.readValue(record.value().toString(), Event.class);
		// TODO Implement what you want to do with the messages
		});
		
	}

	@Override
	public void registerSeekCallback(ConsumerSeekCallback callback) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPartitionsAssigned(Map<TopicPartition, Long> assignments, ConsumerSeekCallback callback) {
		assignments.keySet().stream()
				.filter(partition -> topicName.equals(partition.topic()))
				.forEach(partition -> callback.seekToBeginning(topicName, partition.partition()));
	}

	@Override
	public void onIdleContainer(Map<TopicPartition, Long> assignments, ConsumerSeekCallback callback) {
		// TODO Auto-generated method stub
		
	}

}
