package org.freeplane.plugin.move_linked_file;

import org.freeplane.features.mode.ModeController;
import org.freeplane.features.mode.mindmapmode.MModeController;
import org.freeplane.main.application.CommandLineOptions;
import org.freeplane.main.osgi.IModeControllerExtensionProvider;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import java.util.Hashtable;

public class Activator implements BundleActivator {

	public void start(BundleContext bundleContext) throws Exception {

		bundleContext.registerService(

				IModeControllerExtensionProvider.class.getName(),

				new IModeControllerExtensionProvider() {
					@Override
					public void installExtension(ModeController modeController, CommandLineOptions options) {
						modeController.addAction(new MoveLinkedFile());
					}
				},

				getProperties());
	}

	private Hashtable<String, String[]> getProperties() {
		final Hashtable<String, String[]> properties = new Hashtable<String, String[]>();
		properties.put("mode", new String[] { MModeController.MODENAME });
		return properties;
	}

	public void stop(BundleContext bundleContext) throws Exception {
	}

}
