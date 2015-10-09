/**
 * 
 */
package de.tudarmstadt.informatik.tk.assistance.api.tester.service.resource;

import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;

/**
 * Empty service answerer
 * 
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 *
 */
public class EmptyServerResource extends ServerResource {

  @Post("json")
  public String represent(String json) {

    System.out.println("Input:");
    System.out.println(json);

    String output = "";

    System.out.println("Output:");
    System.out.println(output);

    return output;
  }
}
