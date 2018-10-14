package com.drozdps.chat.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.ExpiringSession;
import org.springframework.session.web.socket.config.annotation.AbstractSessionWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.scheduling.annotation.EnableScheduling;


@Configuration
@EnableScheduling
@EnableWebSocketMessageBroker
public class SessionConfiguration extends AbstractSessionWebSocketMessageBrokerConfigurer<ExpiringSession> {

	@Value("${chat.chat.relay.host}")
	private String relayHost;

	@Value("${chat.chat.relay.port}")
	private Integer relayPort;

	protected void configureStompEndpoints(StompEndpointRegistry registry) {
		registry.addEndpoint("/ws").withSockJS();
	}

	// Don't use standard Spring Broker
	// use Redis key-value store instead
	public void configureMessageBroker(MessageBrokerRegistry registry) {
		registry.enableStompBrokerRelay("/queue/", "/topic/")
			.setUserDestinationBroadcast("/topic/unresolved.user.dest")
			.setUserRegistryBroadcast("/topic/registry.broadcast")
			.setRelayPort(relayPort)
			.setRelayHost(relayHost);

		registry.setApplicationDestinationPrefixes("/room");
	}
}
