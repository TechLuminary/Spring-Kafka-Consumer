package com.techluminary.spring.kafkaconsumer;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "kafkaConsumerProps")
public class KafkaConsumerProps {

	private String jaasFileDirectory;
	private String keytabFileLocation;
	private String securityKrb5Realm;
	private String securityKrb5Kdc;
	private String securityKrb5Debug;
	private String consumerGroup;
	private String serviceId;
	
	public String getJaasFileDirectory() {
		return jaasFileDirectory;
	}
	public void setJaasFileDirectory(String jaasFileDirectory) {
		this.jaasFileDirectory = jaasFileDirectory;
	}
	public String getKeytabFileLocation() {
		return keytabFileLocation;
	}
	public void setKeytabFileLocation(String keytabFileLocation) {
		this.keytabFileLocation = keytabFileLocation;
	}
	public String getSecurityKrb5Realm() {
		return securityKrb5Realm;
	}
	public void setSecurityKrb5Realm(String securityKrb5Realm) {
		this.securityKrb5Realm = securityKrb5Realm;
	}
	public String getSecurityKrb5Kdc() {
		return securityKrb5Kdc;
	}
	public void setSecurityKrb5Kdc(String securityKrb5Kdc) {
		this.securityKrb5Kdc = securityKrb5Kdc;
	}
	public String getSecurityKrb5Debug() {
		return securityKrb5Debug;
	}
	public void setSecurityKrb5Debug(String securityKrb5Debug) {
		this.securityKrb5Debug = securityKrb5Debug;
	}
	public String getConsumerGroup() {
		return consumerGroup;
	}
	public void setConsumerGroup(String consumerGroup) {
		this.consumerGroup = consumerGroup;
	}
	public String getServiceId() {
		return serviceId;
	}
	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}
	
	
}
