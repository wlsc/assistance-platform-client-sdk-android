/**
 * 
 */
package de.tudarmstadt.informatik.tk.assistance.api.tester.service;

import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.routing.Router;

import de.tudarmstadt.informatik.tk.assistance.api.tester.service.resource.ActivatedModulesServerResource;
import de.tudarmstadt.informatik.tk.assistance.api.tester.service.resource.EmptyServerResource;
import de.tudarmstadt.informatik.tk.assistance.api.tester.service.resource.ModuleListServerResource;

/**
 * Modules API routing
 * 
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 *
 */
public class AssistanceService extends Application {

  @Override
  public synchronized Restlet createInboundRoot() {

    Router router = new Router(getContext());

    router.attach("/list", ModuleListServerResource.class);
    router.attach("/activate", EmptyServerResource.class);
    router.attach("/deactivate", EmptyServerResource.class);
    router.attach("/activations", ActivatedModulesServerResource.class);

    return router;
  }

}
