package com.sist.web.chat.websocket.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import com.sist.web.chat.websocket.interceptor.JwtChannelInterceptor;
import lombok.extern.slf4j.Slf4j;


@Configuration
@Slf4j
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
	
	private final JwtChannelInterceptor interceptor;
	
	@Bean
    public ThreadPoolTaskScheduler messageBrokerTaskScheduler() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(1);
        scheduler.setThreadNamePrefix("wss-heartbeat-thread-");
        scheduler.initialize();
        return scheduler;
    }
	
	public WebSocketConfig(JwtChannelInterceptor interceptor) {
		this.interceptor = interceptor;
	}

	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		registry.addEndpoint("/ws")
				.setAllowedOrigins("*")
				.withSockJS();
	}

	@Override
	public void configureClientInboundChannel(ChannelRegistration registration) {
		registration.interceptors(interceptor);
	}

	@Override
	public void configureMessageBroker(MessageBrokerRegistry registry) {
		long [] heartbeat = new long[] {10000, 10000};
		registry.setApplicationDestinationPrefixes("/pub"); // client -> server
		registry.enableSimpleBroker("/sub")
				.setHeartbeatValue(heartbeat)
				.setTaskScheduler(messageBrokerTaskScheduler()); // server -> client
	}
	
	
}
