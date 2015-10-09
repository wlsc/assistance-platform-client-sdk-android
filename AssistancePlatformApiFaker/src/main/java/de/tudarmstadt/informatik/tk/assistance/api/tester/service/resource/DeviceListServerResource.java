/**
 * 
 */
package de.tudarmstadt.informatik.tk.assistance.api.tester.service.resource;

import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;

import com.google.gson.GsonBuilder;

/**
 * Devices service answerer
 * 
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 *
 */
public class DeviceListServerResource extends ServerResource {

  @Post("json")
  public String represent(String json) {

    System.out.println("Input:");
    System.out.println(json);

    // TODO

    GsonBuilder builder = new GsonBuilder();
    builder.excludeFieldsWithoutExposeAnnotation();

    String output = builder.create().toJson("");

    System.out.println("Output:");
    System.out.println(output);

    return output;
  }

}
