package org.liferay.vaadin7.chat.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.liferay.vaadin7.chat.view.ViewFactory;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ServiceScope;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.UI;
import com.vaadin.ui.UIDetachedException;
import com.vaadin.ui.VerticalLayout;

@Component(
		scope = ServiceScope.PROTOTYPE,
		property = {
			"com.liferay.portlet.display-category=category.vaadin",
			"javax.portlet.display-name=Vaadin Chat Portlet"
		},
		service = UI.class
	)
public class VaadinChatUI extends UI implements ServiceTrackerCustomizer<ViewFactory, ViewFactory>{

	@Override
	protected void init(VaadinRequest request) {
		_log.error("init");
		setPollInterval(1000);
		VerticalLayout verticalLayout = new VerticalLayout();
		verticalLayout.setWidth(100, Unit.PERCENTAGE);
		setContent(verticalLayout);
		Bundle bundle = FrameworkUtil.getBundle(getClass());
		BundleContext bundleContext = bundle.getBundleContext();
		
		ServiceTracker<ViewFactory, ViewFactory> serviceTracker = new ServiceTracker<ViewFactory, ViewFactory>(
				bundleContext, ViewFactory.class, this);
		
		serviceTracker.open();

		addDetachListener((event) -> {
			_log.error("serviceTracker.close()");
			serviceTracker.close();
		});
	}

	@Override
	public ViewFactory addingService(ServiceReference<ViewFactory> serviceReference) {
		_log.error("addingService");

		Bundle bundle = FrameworkUtil.getBundle(getClass());
		BundleContext bundleContext = bundle.getBundleContext();

		ViewFactory viewFactory = bundleContext.getService(serviceReference);
		try {
			access(() -> {
				com.vaadin.ui.Component component = viewFactory.createView();
				List<com.vaadin.ui.Component> components = _serviceRegistry.get(serviceReference);
				if (components==null) {
					components = new ArrayList<>();
				}
				components.add(component);
				
				_serviceRegistry.put(serviceReference, components);

				VerticalLayout verticalLayout = (VerticalLayout) getContent();
				verticalLayout.addComponent(component);
			});
		} catch (UIDetachedException ui) {
			_log.error(ui);
		}

		return viewFactory;
	}

	@Override
	public void modifiedService(ServiceReference<ViewFactory> reference, ViewFactory service) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removedService(ServiceReference<ViewFactory> reference, ViewFactory service) {
		_log.error("removedService " + _serviceRegistry.size());
		try {
			accessSynchronously(() -> {
				VerticalLayout verticalLayout = (VerticalLayout) getContent();
				_serviceRegistry.get(reference).forEach(component -> {
					verticalLayout.removeComponent(component);
				});
				_serviceRegistry.remove(reference);
			});
		} catch (UIDetachedException ui) {
			_log.error(ui);
		}
		
	}
	private Map<ServiceReference<ViewFactory>, List<com.vaadin.ui.Component>> _serviceRegistry = new HashMap<>();

	private Log _log = LogFactoryUtil.getLog(VaadinChatUI.class);
}
