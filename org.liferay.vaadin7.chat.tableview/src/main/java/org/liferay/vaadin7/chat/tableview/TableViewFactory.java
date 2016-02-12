package org.liferay.vaadin7.chat.tableview;

import java.util.Date;

import org.liferay.vaadin7.chat.api.ChatService;
import org.liferay.vaadin7.chat.api.Message;
import org.liferay.vaadin7.chat.view.ViewFactory;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.model.User;
import com.liferay.portal.service.UserLocalService;
import com.liferay.portal.util.Portal;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.server.VaadinPortletService;
import com.vaadin.ui.Button;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

@Component(service = ViewFactory.class)
public class TableViewFactory implements ViewFactory {
	private class WrappedMessage implements Message {
		public WrappedMessage(Message message) {
			_message = message;
		}
		
		@Override
		public Date getTime() {
			return _message.getTime();
		}

		@Override
		public String getFromUser() {
			return _message.getFromUser();
		}

		@Override
		public String getBody() {
			return _message.getBody().replaceAll("<[^>]*>", "");
		}
		Message _message;
	};
	@Override
	public com.vaadin.ui.Component createView() {
		_log.info("createView()");
		VerticalLayout verticalLayout = new VerticalLayout();

		BeanItemContainer<Message> beanItemContainer = new BeanItemContainer<Message>(Message.class);

		beanItemContainer.addAll(_chatService.findAllMessages());

		Table table = new Table("Tableview Chat", beanItemContainer);
		//table.setVisibleColumns(new Object[] { "time","fromUser", "body" });
		table.setVisibleColumns(new Object[] { "fromUser", "body" });
		table.setColumnReorderingAllowed(false);
		table.setPageLength(7);
		table.setBuffered(false);
		table.setSelectable(true);
		table.setWidth(100, Unit.PERCENTAGE);

		MessageHandler messageHandler = (message) -> {

			beanItemContainer.addBean(message);
		};
		
		_chatService.register(messageHandler);
		
		verticalLayout.addDetachListener((event) -> {
			_chatService.unRegister(messageHandler);
		});
		
		Button button = new Button("Send");
		TextField textField = new TextField();
		textField.setWidth(100, Unit.PERCENTAGE);
		
		button.addClickListener((event) -> {
			try {
				User user = _portal.getUser(VaadinPortletService.getCurrentPortletRequest());
								
				if (!user.isDefaultUser()) {
					_chatService.addMessage(user.getFullName(), textField.getValue());
				}
			} 
			catch (PortalException pe) {
				// Ignore
			}
			
		});

		verticalLayout.addComponent(table);
		verticalLayout.addComponent(textField);
		verticalLayout.addComponent(button);
		return verticalLayout;
	}

	@Reference(unbind = "unsetsetUserLocalService", cardinality = ReferenceCardinality.MANDATORY)
	public void setUserLocalService(UserLocalService userLocalService) {
		_log.error("setUserLocalService : " + (userLocalService != null));
		_userLocalService = userLocalService;
	}

	public void unsetsetUserLocalService(UserLocalService userLocalService) {
		_log.error("unsetsetUserLocalService : " + (userLocalService != null));
		_userLocalService = null;
	}	
	
	@Reference(unbind = "unsetChatService", cardinality = ReferenceCardinality.MANDATORY)
	public void setChatService(ChatService chatService) {
		_log.error("setChatService : " + (chatService != null));
		_chatService = chatService;
	}

	public void unsetChatService(ChatService chatService) {
		_log.error("unsetChatService : " + (chatService != null));
		_chatService = null;
	}
	
	@Reference(unbind = "unsetPortal", cardinality = ReferenceCardinality.MANDATORY)
	public void setPortal(Portal portal) {
		_log.error("setPortal : " + (portal != null));
		_portal = portal;
	}
	
	public void unsetPortal(Portal portal) {
		_portal = null;
	}
	
	private UserLocalService _userLocalService;
	private Portal _portal;
	private ChatService _chatService;

	private Log _log = LogFactoryUtil.getLog(TableViewFactory.class);
}
