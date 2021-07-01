package io.openvidu.server.cdr.mqtt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonObject;

import io.openvidu.server.cdr.CDREvent;
import io.openvidu.server.config.OpenviduConfig;
import io.openvidu.server.kurento.endpoint.KmsEvent;
import io.openvidu.server.summary.SessionSummary;

import java.util.UUID;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class MQTTRecord implements MQTTLogger {

	protected static final Logger log = LoggerFactory.getLogger(MQTTRecord.class);
	private boolean isLoadconfig = false;

	private boolean eventMQTTlog;
	private String topic;
	private Integer qos;
	private String broker;
	private String domainName;
	private int maxInflight;
	MqttClient sampleClient;

	public MQTTRecord(OpenviduConfig openviduConfig) {
		if (isLoadconfig)
			return;
		loadConfig(openviduConfig);
	}

	private synchronized void loadConfig(OpenviduConfig openviduConfig) {
		if (isLoadconfig)
			return;
		eventMQTTlog = openviduConfig.isEventMQTTlog();
		topic = openviduConfig.getEventMQTTstartTopic();
		qos = openviduConfig.getEventMQTTqos();
		broker = openviduConfig.getEventMQTTbroker();
		domainName = openviduConfig.getDomainOrPublicIp();
		maxInflight = openviduConfig.getEventMQTTMaxInflight();
		if (eventMQTTlog)
			try {
				sampleClient = getMQTTconnect();
				isLoadconfig = true;
			} catch (MqttException e) {
				log.error(e.getMessage(), e);
			}
	}

	private MqttClient getMQTTconnect() throws MqttException {
		String clientId = UUID.randomUUID().toString();
		MemoryPersistence persistence = new MemoryPersistence();
		MqttClient sampleClient = new MqttClient(broker, clientId, persistence);
		MqttConnectOptions connOpts = new MqttConnectOptions();
		connOpts.setAutomaticReconnect(true);
		connOpts.setCleanSession(false);
		connOpts.setMaxInflight(maxInflight);
		sampleClient.connect(connOpts);
		return sampleClient;
	}

	@Override
	public void log(CDREvent event) {
		// TODO Auto-generated method stub

	}

	@Override
	public void log(KmsEvent event, String topic) {
		if (isSendLog()) {
			try {
				JsonObject jsonObject = event.toJson();
				jsonObject.addProperty("domainOrPublic", domainName);
				String content = jsonObject.toString();
				MqttMessage message = new MqttMessage(content.getBytes());
				message.setQos(qos);
				if (message == null)
					log.warn("Message mqtt is null");
				if (topic == null)
					log.warn("Topic mqtt is null");
				sampleClient.publish(this.topic + "/" + topic, message);
			} catch (MqttException e) {
				log.warn("Cannot send message mqtt " + e.getMessage(), e);
			} catch (NullPointerException e) {
				log.warn("Mqtt null pointer" + e.getMessage(), e);
			}
		}
	}

	@Override
	public void log(SessionSummary sessionSummary) {
		// TODO Auto-generated method stub

	}

	private boolean isSendLog() {
		if (isLoadconfig) {
			log.info("MQTT can send.");
			return eventMQTTlog;
		} else {
			log.info("Cannot send log to mqtt because not load config.");
			return false;
		}
	}

}
