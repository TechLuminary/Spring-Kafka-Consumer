package com.techluminary.spring.kafkaconsumer;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.Map;

import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.common.config.SaslConfigs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;

@Configuration
@EnableKafka
public class KafkaListenerConfig {

	@Value("${keytab}")
	private String keytab; //this can come from vault
	
	@Autowired
	private KafkaConsumerProps kafkaConsumerProps;
	
	@Autowired
	private KafkaProperties kafkaProperties;
	
	@Autowired
	private ConsumerFactory<GenericRecord, GenericRecord> consumerFactory;
	
	
	@Bean
	public ConsumerFactory<GenericRecord, GenericRecord> consumerFactory() throws IOException{
		
		System.setProperty("java.security.krb5.realm", kafkaConsumerProps.getSecurityKrb5Realm());
		System.setProperty("java.security.krb5.kdc", kafkaConsumerProps.getSecurityKrb5Kdc());
		System.setProperty("java.security.krb5.debug", kafkaConsumerProps.getSecurityKrb5Debug());
		System.setProperty("java.security.krb5.conf", "krb5/conf/path");
		
		File jaasFileDir = new File(kafkaConsumerProps.getJaasFileDirectory());
		if(!jaasFileDir.exists()) throw new RuntimeException("Jaas file directory does not exist.");
		
		byte[] bytes = Base64.getDecoder().decode(keytab.getBytes());
		Path destFile = Paths.get(kafkaConsumerProps.getJaasFileDirectory(), "Principal.keytab");
		Files.write(destFile, bytes);
		
		String principal = kafkaConsumerProps.getServiceId() + "@" + kafkaConsumerProps.getSecurityKrb5Realm();
		Map<String, Object> props = kafkaProperties.buildConsumerProperties();
		props.put(SaslConfigs.SASL_JAAS_CONFIG, "com.sun.security.auth.module.krb5LoginModule required \n"
				+ "    useKeyTab=true \n"
				+ "    storeKey=true \n"
				+ "    refreshKrb5Config=true \n"
				+ "    useTicketCache=false \n"
				+ "    keytab=\"" + kafkaConsumerProps.getJaasFileDirectory() + "Principal.keytab" + "\"\n"
				+ "    principal=\"" + principal + "\";");
	   return new DefaultKafkaConsumerFactory<>(props);	
	}
	
	@Bean
	public ConcurrentKafkaListenerContainerFactory<GenericRecord, GenericRecord> kafkfaListenerContainerFactory(){
		ConcurrentKafkaListenerContainerFactory<GenericRecord, GenericRecord> factory = new ConcurrentKafkaListenerContainerFactory<>();
		factory.setConcurrency(1);
		factory.setConsumerFactory(consumerFactory);
		factory.setBatchListener(true);
		factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE);
		return factory;	
	}
	
	
}
