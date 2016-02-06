package org.liferay.vaadin7.chat.portlet;

import org.osgi.service.component.annotations.Component;

import com.vaadin.server.VaadinPortlet;


@Component(
		immediate = true,
		property = {
			"com.liferay.portlet.display-category=category.vaadin",
			"com.liferay.portlet.instanceable=true",
			"javax.portlet.display-name=Vaadin Chat Portlet",
			"javax.portlet.init-param.UI=org.liferay.vaadin7.chat.ui.PortletUI",
			"javax.portlet.security-role-ref=power-user,user"
		},
		service = javax.portlet.Portlet.class
	)
public class Portlet extends VaadinPortlet {

}
