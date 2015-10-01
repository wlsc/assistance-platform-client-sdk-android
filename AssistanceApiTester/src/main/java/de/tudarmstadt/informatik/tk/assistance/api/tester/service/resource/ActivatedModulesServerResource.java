/**
 * 
 */
package de.tudarmstadt.informatik.tk.assistance.api.tester.service.resource;

import java.util.ArrayList;
import java.util.List;

import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

import com.google.gson.GsonBuilder;

/**
 * Activated modules list service answerer
 * 
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 *
 */
public class ActivatedModulesServerResource extends ServerResource {

  @Get("json")
  public String represent(String json) {

    System.out.println("Input:");
    System.out.println(json);

    List<String> activatedModules = new ArrayList<>();
    activatedModules.add("de.tudarmstadt.tk.assistance.hotzones2");

    GsonBuilder builder = new GsonBuilder();
    builder.excludeFieldsWithoutExposeAnnotation();

    String output = builder.create().toJson(activatedModules);

    System.out.println("Output:");
    System.out.println(output);

    return output;
  }

}
