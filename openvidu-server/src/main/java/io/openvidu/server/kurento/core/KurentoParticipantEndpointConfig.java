/*
 * (C) Copyright 2017-2020 OpenVidu (https://openvidu.io)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package io.openvidu.server.kurento.core;

import org.kurento.client.BaseRtpEndpoint;
import org.kurento.client.Endpoint;
import org.kurento.client.PlayerEndpoint;
import org.kurento.client.WebRtcEndpoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import io.openvidu.server.cdr.CallDetailRecord;
import io.openvidu.server.cdr.mqtt.MQTTRecord;
import io.openvidu.server.config.InfoHandler;
import io.openvidu.server.kurento.endpoint.KmsEvent;
import io.openvidu.server.kurento.endpoint.KmsMediaEvent;
import io.openvidu.server.kurento.endpoint.MediaEndpoint;

public class KurentoParticipantEndpointConfig {

	protected static final Logger log = LoggerFactory.getLogger(KurentoParticipantEndpointConfig.class);

	@Autowired
	protected InfoHandler infoHandler;

	@Autowired
	protected CallDetailRecord CDR;

	@Autowired
	protected MQTTRecord mqttR;

	public void addEndpointListeners(MediaEndpoint endpoint, String typeOfEndpoint) {

		// WebRtcEndpoint events
		if (endpoint.getWebEndpoint() != null) {

			final WebRtcEndpoint finalEndpoint = endpoint.getWebEndpoint();

			finalEndpoint.addIceGatheringDoneListener(event -> {
				String msg = "KMS event [IceGatheringDone] -> endpoint: " + endpoint.getEndpointName() + " ("
						+ typeOfEndpoint + ") | timestamp: " + event.getTimestampMillis();
				KmsEvent kmsEvent = new KmsEvent(event, endpoint.getOwner(), endpoint.getEndpointName(),
						endpoint.createdAt());
				endpoint.kmsEvents.add(kmsEvent);
				this.CDR.log(kmsEvent);
				this.infoHandler.sendInfo(msg);
				log.info(msg);
				// this.mqttR.log(kmsEvent, "IceGatheringDone");
			});

			finalEndpoint.addNewCandidatePairSelectedListener(event -> {
				endpoint.selectedLocalIceCandidate = event.getCandidatePair().getLocalCandidate();
				endpoint.selectedRemoteIceCandidate = event.getCandidatePair().getRemoteCandidate();
				String msg = "KMS event [NewCandidatePairSelected]: -> endpoint: " + endpoint.getEndpointName() + " ("
						+ typeOfEndpoint + ") | local: " + endpoint.selectedLocalIceCandidate + " | remote: "
						+ endpoint.selectedRemoteIceCandidate + " | timestamp: " + event.getTimestampMillis();
				KmsEvent kmsEvent = new KmsEvent(event, endpoint.getOwner(), endpoint.getEndpointName(),
						endpoint.createdAt());
				endpoint.kmsEvents.add(kmsEvent);
				this.CDR.log(kmsEvent);
				this.infoHandler.sendInfo(msg);
				log.info(msg);
				// this.mqttR.log(kmsEvent, "NewCandidatePairSelected");
			});

			finalEndpoint.addIceComponentStateChangeListener(event -> {
				String msg = "KMS event [IceComponentStateChange]: -> endpoint: " + endpoint.getEndpointName() + " ("
						+ typeOfEndpoint + ") | state: " + event.getState().name() + " | componentId: "
						+ event.getComponentId() + " | streamId: " + event.getStreamId() + " | timestamp: "
						+ event.getTimestampMillis();
				KmsEvent kmsEvent = new KmsEvent(event, endpoint.getOwner(), endpoint.getEndpointName(),
						endpoint.createdAt());
				endpoint.kmsEvents.add(kmsEvent);
				this.CDR.log(kmsEvent);
				this.infoHandler.sendInfo(msg);
				log.info(msg);
				// this.mqttR.log(kmsEvent, "IceComponentStateChange");
			});

			finalEndpoint.addDataChannelOpenListener(event -> {
				String msg = "KMS event [DataChannelOpenEvent]: -> endpoint: " + endpoint.getEndpointName() + " ("
						+ typeOfEndpoint + ") | channelId: " + event.getChannelId() + " | timestamp: "
						+ event.getTimestampMillis();
				KmsEvent kmsEvent = new KmsEvent(event, endpoint.getOwner(), endpoint.getEndpointName(),
						endpoint.createdAt());
				endpoint.kmsEvents.add(kmsEvent);
				this.CDR.log(kmsEvent);
				this.infoHandler.sendInfo(msg);
				log.info(msg);
				// this.mqttR.log(kmsEvent, "DataChannelOpenEvent");
			});

			finalEndpoint.addDataChannelCloseListener(event -> {
				String msg = "KMS event [DataChannelCloseEvent]: -> endpoint: " + endpoint.getEndpointName() + " ("
						+ typeOfEndpoint + ") | channelId: " + event.getChannelId() + " | timestamp: "
						+ event.getTimestampMillis();
				KmsEvent kmsEvent = new KmsEvent(event, endpoint.getOwner(), endpoint.getEndpointName(),
						endpoint.createdAt());
				endpoint.kmsEvents.add(kmsEvent);
				this.CDR.log(kmsEvent);
				this.infoHandler.sendInfo(msg);
				log.info(msg);
				// this.mqttR.log(kmsEvent, "DataChannelCloseEvent");
			});

		}

		// PlayerEndpoint events
		if (endpoint.getPlayerEndpoint() != null) {

			final PlayerEndpoint finalEndpoint = endpoint.getPlayerEndpoint();

			finalEndpoint.addEndOfStreamListener(event -> {
				String msg = "KMS event [EndOfStreamEvent]: -> endpoint: " + endpoint.getEndpointName() + " ("
						+ typeOfEndpoint + ") | timestamp: " + event.getTimestampMillis();
				KmsEvent kmsEvent = new KmsEvent(event, endpoint.getOwner(), endpoint.getEndpointName(),
						endpoint.createdAt());
				endpoint.kmsEvents.add(kmsEvent);
				this.CDR.log(kmsEvent);
				this.infoHandler.sendInfo(msg);
				log.info(msg);
				// this.mqttR.log(kmsEvent, "EndOfStreamEvent");
			});

			finalEndpoint.addUriEndpointStateChangedListener(event -> {
				String msg = "KMS event [UriEndpointStateChangedEvent]: -> endpoint: " + endpoint.getEndpointName()
						+ " (" + typeOfEndpoint + ") | state: " + event.getState().name() + " | timestamp: "
						+ event.getTimestampMillis();
				KmsEvent kmsEvent = new KmsEvent(event, endpoint.getOwner(), endpoint.getEndpointName(),
						endpoint.createdAt());
				endpoint.kmsEvents.add(kmsEvent);
				this.CDR.log(kmsEvent);
				this.infoHandler.sendInfo(msg);
				log.info(msg);
				// this.mqttR.log(kmsEvent, "UriEndpointStateChangedEvent");
			});

		}

		// BaseRtpEndpoint events
		if (endpoint.getWebEndpoint() != null || endpoint.getRtpEndpoint() != null) {

			final BaseRtpEndpoint finalEndpoint = ((BaseRtpEndpoint) endpoint.getEndpoint());

			finalEndpoint.addConnectionStateChangedListener(event -> {
				String msg = "KMS event [ConnectionStateChanged]: -> endpoint: " + endpoint.getEndpointName() + " ("
						+ typeOfEndpoint + ") | oldState: " + event.getOldState() + " | newState: "
						+ event.getNewState() + " | timestamp: " + event.getTimestampMillis();
				KmsEvent kmsEvent = new KmsEvent(event, endpoint.getOwner(), endpoint.getEndpointName(),
						endpoint.createdAt());
				endpoint.kmsEvents.add(kmsEvent);
				this.CDR.log(kmsEvent);
				this.infoHandler.sendInfo(msg);
				log.info(msg);
				this.mqttR.log(kmsEvent, "ConnectionStateChanged");
			});

			finalEndpoint.addMediaStateChangedListener(event -> {
				String msg = "KMS event [MediaStateChangedEvent]: -> endpoint: " + endpoint.getEndpointName() + " ("
						+ typeOfEndpoint + ") | oldState: " + event.getOldState() + " | newState: "
						+ event.getNewState() + " | timestamp: " + event.getTimestampMillis();
				KmsEvent kmsEvent = new KmsEvent(event, endpoint.getOwner(), endpoint.getEndpointName(),
						endpoint.createdAt());
				endpoint.kmsEvents.add(kmsEvent);
				this.CDR.log(kmsEvent);
				this.infoHandler.sendInfo(msg);
				log.info(msg);
				// this.mqttR.log(kmsEvent, "MediaStateChangedEvent");
			});

		}

		// Endpoint events
		final Endpoint finalEndpoint = endpoint.getEndpoint();

		finalEndpoint.addMediaFlowInStateChangeListener(event -> {
			String msg = "KMS event [MediaFlowInStateChange] -> endpoint: " + endpoint.getEndpointName() + " ("
					+ typeOfEndpoint + ") | state: " + event.getState() + " | pad: " + event.getPadName()
					+ " | mediaType: " + event.getMediaType() + " | timestamp: " + event.getTimestampMillis();
			KmsEvent kmsEvent = new KmsMediaEvent(event, endpoint.getOwner(), endpoint.getEndpointName(),
					event.getMediaType(), endpoint.createdAt());
			endpoint.kmsEvents.add(kmsEvent);
			this.CDR.log(kmsEvent);
			this.infoHandler.sendInfo(msg);
			log.info(msg);
			this.mqttR.log(kmsEvent, "MediaFlowInStateChange");
		});

		finalEndpoint.addMediaFlowOutStateChangeListener(event -> {
			String msg = "KMS event [MediaFlowOutStateChange] -> endpoint: " + endpoint.getEndpointName() + " ("
					+ typeOfEndpoint + ") | state: " + event.getState() + " | pad: " + event.getPadName()
					+ " | mediaType: " + event.getMediaType() + " | timestamp: " + event.getTimestampMillis();
			KmsEvent kmsEvent = new KmsMediaEvent(event, endpoint.getOwner(), endpoint.getEndpointName(),
					event.getMediaType(), endpoint.createdAt());
			endpoint.kmsEvents.add(kmsEvent);
			this.CDR.log(kmsEvent);
			this.infoHandler.sendInfo(msg);
			log.info(msg);
			// this.mqttR.log(kmsEvent, "MediaFlowOutStateChange");
		});

		finalEndpoint.addErrorListener(event -> {
			String msg = "KMS event [ERROR]: -> endpoint: " + endpoint.getEndpointName() + " (" + typeOfEndpoint
					+ ") | errorCode: " + event.getErrorCode() + " | description: " + event.getDescription()
					+ " | timestamp: " + event.getTimestampMillis();
			KmsEvent kmsEvent = new KmsEvent(event, endpoint.getOwner(), endpoint.getEndpointName(),
					endpoint.createdAt());
			endpoint.kmsEvents.add(kmsEvent);
			this.CDR.log(kmsEvent);
			this.infoHandler.sendInfo(msg);
			log.error(msg);
			this.mqttR.log(kmsEvent, "ERROR");
		});

		finalEndpoint.addMediaTranscodingStateChangeListener(event -> {
			String msg = "KMS event [MediaTranscodingStateChange]: -> endpoint: " + endpoint.getEndpointName() + " ("
					+ typeOfEndpoint + ") | state: " + event.getState().name() + " | mediaType: " + event.getMediaType()
					+ " | binName: " + event.getBinName() + " | timestamp: " + event.getTimestampMillis();
			KmsEvent kmsEvent = new KmsMediaEvent(event, endpoint.getOwner(), endpoint.getEndpointName(),
					event.getMediaType(), endpoint.createdAt());
			endpoint.kmsEvents.add(kmsEvent);
			this.CDR.log(kmsEvent);
			this.infoHandler.sendInfo(msg);
			log.info(msg);
			// this.mqttR.log(kmsEvent, "MediaTranscodingStateChange");
		});
	}

	public CallDetailRecord getCdr() {
		return this.CDR;
	}

}
