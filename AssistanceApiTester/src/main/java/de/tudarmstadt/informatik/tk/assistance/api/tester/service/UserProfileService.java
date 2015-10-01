/**
 * 
 */
package de.tudarmstadt.informatik.tk.assistance.api.tester.service;

import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.routing.Router;

import de.tudarmstadt.informatik.tk.assistance.api.tester.service.resource.UserProfileFullServerResource;

/**
 * User profile API routing
 *
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 *
 */
public class UserProfileService extends Application {

  @Override
  public Restlet createInboundRoot() {

    Router router = new Router(getContext());

    router.attach("/short", UserProfileFullServerResource.class);
    router.attach("/long", UserProfileFullServerResource.class);

    return router;
  }

}
