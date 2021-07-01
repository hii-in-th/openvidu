package io.openvidu.server.cdr.mqtt;

import io.openvidu.server.cdr.CDREvent;
import io.openvidu.server.kurento.endpoint.KmsEvent;
import io.openvidu.server.summary.SessionSummary;

public interface MQTTLogger {
	public void log(CDREvent event);

	public void log(KmsEvent event, String topic);

	public void log(SessionSummary sessionSummary);
}
