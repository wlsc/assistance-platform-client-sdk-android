package de.tudarmstadt.informatik.tk.assistance.api.tester.service;

import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.routing.Router;

import de.tudarmstadt.informatik.tk.assistance.api.tester.service.resource.EmptyServerResource;
import de.tudarmstadt.informatik.tk.assistance.api.tester.service.resource.LoginServerResource;
import de.tudarmstadt.informatik.tk.assistance.api.tester.service.resource.UserProfileFullServerResource;
import de.tudarmstadt.informatik.tk.assistance.api.tester.service.resource.UserProfileShortServerResource;

/**
 * Users API routing
 *
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 *
 */
public class UserService extends Application {
		
	@Override
	public synchronized Restlet createInboundRoot() {
		
        Router router = new Router(getContext());
        
        router.attach("/login", LoginServerResource.class);
        router.attach("/password", EmptyServerResource.class);
        router.attach("/profile/short", UserProfileShortServerResource.class);
        router.attach("/profile/long", UserProfileFullServerResource.class);
        
        return router;
	}
	
}
