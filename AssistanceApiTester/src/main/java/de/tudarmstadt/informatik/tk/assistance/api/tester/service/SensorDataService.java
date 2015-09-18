/**
 * 
 */
package de.tudarmstadt.informatik.tk.assistance.api.tester.service;

import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.routing.Router;

import de.tudarmstadt.informatik.tk.assistance.api.tester.service.resource.EmptyServerResource;

/**
 * Sensors data API routing
 * 
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 *
 */
public class SensorDataService extends Application {

	@Override
	public synchronized Restlet createInboundRoot() {
		
		Router router = new Router(getContext());
        router.attach("/upload", EmptyServerResource.class);
        return router;
	}
	
}
