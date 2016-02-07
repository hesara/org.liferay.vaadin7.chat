package org.liferay.vaadin7.chat.labelview;

import java.util.List;

import org.liferay.vaadin7.chat.api.ChatService;
import org.liferay.vaadin7.chat.api.Message;
import org.liferay.vaadin7.chat.view.ViewFactory;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.model.User;
import com.liferay.portal.service.UserLocalService;
import com.liferay.portal.util.Portal;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.server.VaadinPortletService;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.RichTextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

@org.osgi.service.component.annotations.Component(service = ViewFactory.class)
public class LabelViewFactory implements ViewFactory {

	@Override
	public com.vaadin.ui.Component createView() {
		_log.info("createView()");
		VerticalLayout verticalLayout = new VerticalLayout();
		
		Label chatLabel = new Label("",ContentMode.HTML);
		chatLabel.setCaptionAsHtml(true);
		RichTextArea richTextArea = new RichTextArea();
		richTextArea.setWidth(100, Unit.PERCENTAGE);
		
		List<Message> messages = _chatService.findAllMessages();
		
		StringBundler sb = new StringBundler(messages.size());
		
		messages.forEach((message) -> {
			sb.append(
				String.format("<fieldset><legend>(%s) %s</legend><div>%s</div></fieldset>", 
						message.getTime(), message.getFromUser(), message.getBody()));
		});
		
		chatLabel.setWidth(100, Unit.PERCENTAGE);
		chatLabel.setValue(sb.toString());
		
		_chatService.register((message) -> {
			chatLabel.setValue(
					String.format("%s<fieldset><legend>(%s) %s</legend><div>%s</div></fieldset>", 
						chatLabel.getValue(), message.getTime(), message.getFromUser(), message.getBody()));
		});

		Button button = new Button("Send");
		TextField textField = new TextField();
		textField.setWidth(100, Unit.PERCENTAGE);
		
		button.addClickListener((event) -> {
			try {
				User user = _portal.getUser(VaadinPortletService.getCurrentPortletRequest());
								
				if (!user.isDefaultUser()) {
					_chatService.addMessage(user.getFullName(), richTextArea.getValue());
				}
			} 
			catch (PortalException pe) {
				// Ignore
			}
			
		});

		verticalLayout.addComponent(chatLabel);
		verticalLayout.addComponent(richTextArea);
		verticalLayout.addComponent(button);
		return verticalLayout;
	}

	@Reference(unbind = "unsetsetUserLocalService", cardinality = ReferenceCardinality.MANDATORY)
	public void setUserLocalService(UserLocalService userLocalService) {
		_log.info("setUserLocalService : " + (userLocalService != null));
		_userLocalService = userLocalService;
	}

	public void unsetsetUserLocalService(UserLocalService userLocalService) {
		_log.info("unsetsetUserLocalService : " + (userLocalService != null));
		_userLocalService = null;
	}	
	
	@Reference(unbind = "unsetChatService", cardinality = ReferenceCardinality.MANDATORY)
	public void setChatService(ChatService chatService) {
		_log.info("setChatService : " + (chatService != null));
		_chatService = chatService;
	}

	public void unsetChatService(ChatService chatService) {
		_log.info("unsetChatService : " + (chatService != null));
		_chatService = null;
	}
	
	@Reference(unbind = "unsetPortal", cardinality = ReferenceCardinality.MANDATORY)
	public void setPortal(Portal portal) {
		_log.info("setPortal : " + (portal != null));
		_portal = portal;
	}
	
	public void unsetPortal(Portal portal) {
		_portal = null;
	}
	
	private UserLocalService _userLocalService;
	private Portal _portal;
	private ChatService _chatService;

	private Log _log = LogFactoryUtil.getLog(LabelViewFactory.class);
}
