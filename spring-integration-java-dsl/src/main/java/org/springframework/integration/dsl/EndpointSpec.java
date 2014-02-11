package org.springframework.integration.dsl;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.aopalliance.aop.Advice;

import org.springframework.integration.config.ConsumerEndpointFactoryBean;
import org.springframework.integration.handler.AbstractReplyProducingMessageHandler;
import org.springframework.integration.scheduling.PollerMetadata;
import org.springframework.messaging.MessageHandler;

/**
 * @author Artem Bilan
 * @since 4.0
 */
public abstract class EndpointSpec<S extends EndpointSpec<S, C>, C extends MessageHandler> {

	private final ConsumerEndpointFactoryBean endpointFactoryBean = new ConsumerEndpointFactoryBean();

	private final C messageHandler;

	private final List<Advice> adviceChain = new LinkedList<Advice>();

	private String id;

	EndpointSpec(C messageHandler) {
		this.messageHandler = messageHandler;
		this.endpointFactoryBean.setHandler(this.messageHandler);
		if (this.messageHandler instanceof AbstractReplyProducingMessageHandler) {
			((AbstractReplyProducingMessageHandler) this.messageHandler).setAdviceChain(this.adviceChain);
		}
		else {
			this.endpointFactoryBean.setAdviceChain(this.adviceChain);
		}
	}

	public S id(String id) {
		this.id = id;
		this.endpointFactoryBean.setBeanName(id);
		return _this();
	}

	public S phase(int phase) {
		this.endpointFactoryBean.setPhase(phase);
		return _this();
	}

	public S autoStartup(boolean autoStartup) {
		this.endpointFactoryBean.setAutoStartup(autoStartup);
		return _this();
	}

	public S advice(Advice... advice) {
		this.adviceChain.addAll(Arrays.asList(advice));
		return _this();
	}

	public S poller(PollerMetadata pollerMetadata) {
		this.endpointFactoryBean.setPollerMetadata(pollerMetadata);
		return _this();
	}

	String getId() {
		return id;
	}

	ConsumerEndpointFactoryBean getEndpoint() {
		return this.endpointFactoryBean;
	}

	C getHandler() {
		return this.messageHandler;
	}

	@SuppressWarnings("unchecked")
	protected S _this() {
		return (S) this;
	}

}
