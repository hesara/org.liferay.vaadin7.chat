package org.liferay.vaadin7.chat.portlet;

import org.liferay.vaadin7.compatibilitypack.webresources.VaadinWebResource;
import org.osgi.service.component.annotations.Component;

import com.vaadin.server.VaadinPortlet;
import com.vaadin.shared.Version;


@Component(
		immediate = true,
		property = {
			"com.liferay.portlet.display-category=category.vaadin",
			"com.liferay.portlet.instanceable=true",
			"javax.portlet.display-name=Vaadin Chat Portlet",
			"javax.portlet.init-param.UI=org.liferay.vaadin7.chat.ui.PortletUI",
			VaadinWebResource.JAVAX_PORTLET_RESOURCES_PATH,
			"javax.portlet.security-role-ref=power-user,user"
		},
		service = javax.portlet.Portlet.class
	)
public class Portlet extends VaadinPortlet {

}
