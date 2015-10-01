/**
 * 
 */
package de.tudarmstadt.informatik.tk.assistance.api.tester.service;

import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.routing.Router;

import de.tudarmstadt.informatik.tk.assistance.api.tester.service.resource.DeviceListServerResource;
import de.tudarmstadt.informatik.tk.assistance.api.tester.service.resource.EmptyServerResource;

/**
 * Devices API routing
 * 
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 *
 */
public class DeviceService extends Application {

  @Override
  public Restlet createInboundRoot() {

    Router router = new Router(getContext());

    router.attach("/list", DeviceListServerResource.class);
    router.attach("/register_for_messaging", EmptyServerResource.class);
    router.attach("/set_user_defined_name", EmptyServerResource.class);

    return router;
  }

}
