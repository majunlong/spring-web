package com.web.support.interceptor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.HandlerMethod;
import org.springframework.messaging.simp.SimpMessageMappingInfo;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.support.ChannelInterceptorAdapter;
import org.springframework.messaging.support.ExecutorSubscribableChannel;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.socket.messaging.WebSocketAnnotationMethodMessageHandler;

import com.web.support.annotation.Login;
import com.web.support.exception.NotLoginedException;

public class CustomChannelInterceptor extends ChannelInterceptorAdapter {

	private Map<MessageMappingInfo, HandlerMethod> handlerMethods;

	@Override
	public Message<?> preSend(Message<?> message, MessageChannel channel) {
		if (this.handlerMethods == null) {
			this.initHandlerMethods(channel);
		}
		HandlerMethod handlerMethod = getHandlerMethod(message);
		if (handlerMethod != null && (handlerMethod.getBeanType().isAnnotationPresent(Login.class) || handlerMethod.getMethod().isAnnotationPresent(Login.class))) {
			if (!this.isLogined(message)) {
				throw new NotLoginedException();
			}
		}
		return message;
	}

	private synchronized void initHandlerMethods(MessageChannel channel) {
		if (this.handlerMethods == null) {
			Map<SimpMessageMappingInfo, HandlerMethod> handlerMethods = null;
			ExecutorSubscribableChannel subscribableChannel = (ExecutorSubscribableChannel) channel;
			for (MessageHandler messageHandler : subscribableChannel.getSubscribers()) {
				if (messageHandler instanceof WebSocketAnnotationMethodMessageHandler) {
					WebSocketAnnotationMethodMessageHandler methodMessageHandler = (WebSocketAnnotationMethodMessageHandler) messageHandler;
					handlerMethods = methodMessageHandler.getHandlerMethods();
					break;
				}
			}
			this.handlerMethods = new HashMap<MessageMappingInfo, HandlerMethod>(handlerMethods.size());
			for (Entry<SimpMessageMappingInfo, HandlerMethod> entry : handlerMethods.entrySet()) {
				this.handlerMethods.put(MessageMappingInfo.newInstance(entry.getKey()), entry.getValue());
			}
		}
	}

	@SuppressWarnings("unchecked")
	private HandlerMethod getHandlerMethod(Message<?> message) {
		MessageHeaders headers = message.getHeaders();
		if (headers == null) {
			return null;
		}
		LinkedMultiValueMap<String, Object> nativeHeaders = (LinkedMultiValueMap<String, Object>) headers.get("nativeHeaders");
		if (nativeHeaders == null) {
			return null;
		}
		List<Object> destinations = nativeHeaders.get("destination");
		if (destinations == null) {
			return null;
		}
		String messagePath = destinations.get(0).toString();
		String messageType = headers.get("simpMessageType").toString();
		if (messageType.equals(SimpMessageType.SUBSCRIBE.name()) && (messagePath.indexOf("/topic") > -1 || messagePath.indexOf("/queue") > -1)) {
			return null;
		}
		boolean isBreak = false;
		for (Entry<MessageMappingInfo, HandlerMethod> entry : this.handlerMethods.entrySet()) {
			MessageMappingInfo messageMappingInfo = entry.getKey();
			if (messageType.equals(messageMappingInfo.getMessageType().name())) {
				String[] messageShardPath = messagePath.split("/");
				for (String[] shardPath : messageMappingInfo.getMessageShardPaths()) {
					isBreak = true;
					if (messageShardPath.length - 1 == shardPath.length) {
						for (int i = 1; i < shardPath.length; i++) {
							if (!shardPath[i].equals("*") && !shardPath[i].equals(messageShardPath[i + 1])) {
								isBreak = false;
							}
						}
						if (isBreak) {
							return entry.getValue();
						}
					}
				}
			}
		}
		throw new RuntimeException("HandlerMethod not found!");
	}

	@SuppressWarnings("unchecked")
	private boolean isLogined(Message<?> message) {
		MessageHeaders headers = message.getHeaders();
		if (headers == null) {
			return false;
		}
		Map<String, Object> sessionAttributes = (Map<String, Object>) headers.get("simpSessionAttributes");
		if (sessionAttributes == null) {
			return false;
		}
		String username = (String) sessionAttributes.get("name");
		if (username == null || "".equals(username)) {
			return false;
		}
		return true;
	}

	private static class MessageMappingInfo {

		private List<String[]> messageShardPaths;
		private SimpMessageType messageType;

		private MessageMappingInfo() {
		}

		public static MessageMappingInfo newInstance(SimpMessageMappingInfo simpMessageMappingInfo) {
			MessageMappingInfo messageMappingInfo = new MessageMappingInfo();
			messageMappingInfo.messageType = simpMessageMappingInfo.getMessageTypeMessageCondition().getMessageType();
			Set<String> messagePaths = simpMessageMappingInfo.getDestinationConditions().getPatterns();
			messageMappingInfo.messageShardPaths = new ArrayList<String[]>(messagePaths.size());
			for (String path : messagePaths) {
				messageMappingInfo.messageShardPaths.add(path.replaceAll("\\{\\w+\\}", "*").split("/"));
			}
			return messageMappingInfo;
		}

		public List<String[]> getMessageShardPaths() {
			return this.messageShardPaths;
		}

		public SimpMessageType getMessageType() {
			return this.messageType;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((messageShardPaths == null) ? 0 : messageShardPaths.hashCode());
			result = prime * result + ((messageType == null) ? 0 : messageType.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			MessageMappingInfo other = (MessageMappingInfo) obj;
			if (messageShardPaths == null) {
				if (other.messageShardPaths != null)
					return false;
			} else if (!messageShardPaths.equals(other.messageShardPaths))
				return false;
			if (messageType != other.messageType)
				return false;
			return true;
		}

		@Override
		public String toString() {
			StringBuilder builer = new StringBuilder();
			builer.append("MessageMappingInfo [messageShardPaths={");
			for (int i = 0; i < this.messageShardPaths.size() - 1; i++) {
				String[] shardPath = this.messageShardPaths.get(i);
				builer.append("{");
				for (int j = 0; j < shardPath.length - 1; j++) {
					if (!"".equals(shardPath[j])) {
						builer.append(shardPath[j]);
						builer.append(",");
					}
				}
				builer.append(shardPath[shardPath.length - 1]);
				builer.append("}, ");
			}

			String[] shardPath = this.messageShardPaths.get(this.messageShardPaths.size() - 1);
			builer.append("{");
			for (int j = 0; j < shardPath.length - 1; j++) {
				if (!"".equals(shardPath[j])) {
					builer.append(shardPath[j]);
					builer.append(",");
				}
			}
			builer.append(shardPath[shardPath.length - 1]);
			builer.append("}}, messageType=");
			builer.append(this.messageType);
			builer.append("]");
			return builer.toString();
		}

	}

}
