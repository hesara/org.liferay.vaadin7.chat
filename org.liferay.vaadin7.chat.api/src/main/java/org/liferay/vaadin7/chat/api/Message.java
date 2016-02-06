package org.liferay.vaadin7.chat.api;

import java.util.Date;

public interface Message {
	public Date getTime();
	public String getFromUser();
	public String getBody();
}
