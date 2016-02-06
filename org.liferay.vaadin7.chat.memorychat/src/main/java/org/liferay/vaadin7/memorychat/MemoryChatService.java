package org.liferay.vaadin7.memorychat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.liferay.vaadin7.chat.api.ChatService;
import org.liferay.vaadin7.chat.api.Message;
import org.osgi.service.component.annotations.Component;

@Component(service = ChatService.class, immediate = true)
public class MemoryChatService implements ChatService {
	
	private class MessageImpl implements Message {
		
		public MessageImpl(String fromUsername, String body) {
			_time = new Date();
			_fromUser = fromUsername;
			_body = body;
		}

		@Override
		public Date getTime() {
			return _time;
		}

		@Override
		public String getFromUser() {
			return _fromUser;
		}

		@Override
		public String getBody() {
			return _body;
		}
		
		private String _fromUser;
		private String _body;
		private Date _time;
	}
	
	@Override
	public void addMessage(String fromUsername, String body) {
		Message message = new MessageImpl(fromUsername, body);
		_messages.add(message);

		_messageHandlers.forEach((messageHandler) -> {
			messageHandler.event(message);
		});
	}

	@Override
	public List<Message> findAllMessages() {
		return _messages;
	}

	@Override
	public void register(MessageHandler messageHandler) {
		_messageHandlers.add(messageHandler);
	}

	@Override
	public void unRegister(MessageHandler messageHandler) {
		_messageHandlers.remove(messageHandler);
	}

	private List<Message> _messages = Collections.synchronizedList(new ArrayList());
	private Set<MessageHandler> _messageHandlers = new HashSet<>();
}
