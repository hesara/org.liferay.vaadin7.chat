package org.liferay.vaadin7.chat.api;

import java.util.List;

public interface ChatService {
	public void addMessage(String fromUsername, String body);
	
	public List<Message> findAllMessages();
	
	public void register(MessageHandler messageHandler);
	
	public void unRegister(MessageHandler messageHandler);
	
	public interface MessageHandler {
		public void event(Message message);
	}
}
