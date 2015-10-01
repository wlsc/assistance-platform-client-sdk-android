package de.tudarmstadt.informatik.tk.assistance.api.tester.service.resource;

import java.util.ArrayList;
import java.util.List;

import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

import com.google.gson.GsonBuilder;

import de.tudarmstadt.informatik.tk.assistance.api.tester.model.AvailableModuleResponse;
import de.tudarmstadt.informatik.tk.assistance.api.tester.model.ModuleCapabilityResponse;

/**
 * Module list service answerer
 * 
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 *
 */
public class ModuleListServerResource extends ServerResource {


  @Get("json")
  public String represent(String json) {

    System.out.println("Input:");
    System.out.println(json);

    List<AvailableModuleResponse> modules = new ArrayList<>();

    ModuleCapabilityResponse moduleCapability1 = new ModuleCapabilityResponse();

    moduleCapability1.setType("position");
    moduleCapability1.setCollectionFrequency(1.0);
    moduleCapability1.setRequiredUpdateFrequency(2.0);
    moduleCapability1.setMinRequiredReadingsOnUpdate(1);

    ModuleCapabilityResponse moduleCapability2 = new ModuleCapabilityResponse();

    moduleCapability2.setType("internet");
    moduleCapability2.setCollectionFrequency(1.0);
    moduleCapability2.setRequiredUpdateFrequency(2.0);
    moduleCapability2.setMinRequiredReadingsOnUpdate(1);

    ModuleCapabilityResponse moduleCapability3 = new ModuleCapabilityResponse();

    moduleCapability3.setType("motionactivity");
    moduleCapability3.setCollectionFrequency(1.0);
    moduleCapability3.setRequiredUpdateFrequency(2.0);
    moduleCapability3.setMinRequiredReadingsOnUpdate(1);

    ModuleCapabilityResponse moduleCapability4 = new ModuleCapabilityResponse();

    moduleCapability4.setType("magneticfield");
    moduleCapability4.setCollectionFrequency(1.0);
    moduleCapability4.setRequiredUpdateFrequency(2.0);
    moduleCapability4.setMinRequiredReadingsOnUpdate(1);

    AvailableModuleResponse module1 = new AvailableModuleResponse();

    module1.setModulePackage("de.tudarmstadt.tk.assistance.hotzones");
    module1.setTitle("Hotzones");
    module1.setDescriptionShort("Short Description (255 chars)");
    module1.setDescriptionFull("Long (2048 chars)");
    module1.setLogo("http://urlToLogo");
    module1.setCopyright("TK Informtik TU Darmstadt");
    module1.setSupportEmail("wlsc.dev.@gmail.com");

    AvailableModuleResponse module2 = new AvailableModuleResponse();

    module2.setModulePackage("de.tudarmstadt.tk.assistance.quantifiedself");
    module2.setTitle("Quantified self");
    module2.setDescriptionShort("Short Description (255 chars)");
    module2.setDescriptionFull("Long (2048 chars)");
    module2.setLogo("http://urlToLogo");
    module2.setCopyright("TK Informtik TU Darmstadt");
    module2.setSupportEmail("wlsc.dev.@gmail.com");

    AvailableModuleResponse module3 = new AvailableModuleResponse();

    module3.setModulePackage("de.tudarmstadt.tk.assistance.hotzones2");
    module3.setTitle("Hotzones 2");
    module3.setDescriptionShort("This is second edition of very famous Hotzones module!");
    module3.setDescriptionFull(
        "This is second edition of very famous Hotzones module! There are a lot of new feature avaiable for you!");
    module3.setLogo(
        "https://cdn1.iconfinder.com/data/icons/basic-ui-elements-round/700/09_location_pin-2-512.png");
    module3.setCopyright("TK Informtik TU Darmstadt");
    module3.setSupportEmail("wlsc.dev.@gmail.com");

    List<ModuleCapabilityResponse> req3 = new ArrayList<>();
    List<ModuleCapabilityResponse> opt3 = new ArrayList<>();

    req3.add(moduleCapability1);
    req3.add(moduleCapability2);

    opt3.add(moduleCapability3);
    opt3.add(moduleCapability4);

    module3.setSensorsRequired(req3);
    module3.setSensorsOptional(opt3);

    modules.add(module1);
    modules.add(module2);
    modules.add(module3);

    GsonBuilder builder = new GsonBuilder();
    builder.excludeFieldsWithoutExposeAnnotation();

    String output = builder.create().toJson(modules);

    System.out.println("Output:");
    System.out.println(output);

    return output;
  }

}
