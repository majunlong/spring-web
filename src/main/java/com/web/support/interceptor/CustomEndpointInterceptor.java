package com.web.support.interceptor;

import org.springframework.ws.context.DefaultMessageContext;
import org.springframework.ws.context.MessageContext;
import org.springframework.ws.server.EndpointInterceptor;
import org.springframework.ws.server.endpoint.MethodEndpoint;

public class CustomEndpointInterceptor implements EndpointInterceptor {

	@Override
	public boolean handleRequest(MessageContext messageContext, Object endpoint) throws Exception {
		MethodEndpoint methodEndpoint = (MethodEndpoint) endpoint;
		System.out.println(methodEndpoint.getBean().getClass().getName());
		DefaultMessageContext defaultMessageContext = (DefaultMessageContext) messageContext;
		System.out.println(defaultMessageContext.containsProperty("name"));
		return true;
	}

	@Override
	public boolean handleResponse(MessageContext messageContext, Object endpoint) throws Exception {
		return true;
	}

	@Override
	public boolean handleFault(MessageContext messageContext, Object endpoint) throws Exception {
		return true;
	}

	@Override
	public void afterCompletion(MessageContext messageContext, Object endpoint, Exception ex) throws Exception {
	}

}
