package de.tudarmstadt.informatik.tk.assistance.api.tester.service.resource;

import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;

import com.google.gson.GsonBuilder;

import de.tudarmstadt.informatik.tk.assistance.api.tester.model.LoginResponse;

/**
 * User login service answerer
 * 
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 *
 */
public class LoginServerResource extends ServerResource {

  private static final String USER_TOKEN =
      "eyJhbGciOiJIUzI1NiJ9.eyJleHBpcmF0aW9uVGltZXN0YW1wIjoxNDQyNDMxNjI0MzI3LCJpZCI6Mn0.-3F8Fex7VR6vh-ilW67en43YVyhxRK43gIkUquh-Dmg";
  private static final Long DEVICE_ID = 2L;

  @Post("json")
  public String represent(String json) {

    System.out.println("Input:");
    System.out.println(json);

    LoginResponse loginResponse = new LoginResponse();
    loginResponse.setDeviceId(DEVICE_ID);
    loginResponse.setUserToken(USER_TOKEN);

    GsonBuilder builder = new GsonBuilder();
    builder.excludeFieldsWithoutExposeAnnotation();

    String output = builder.create().toJson(loginResponse);

    System.out.println("Output:");
    System.out.println(output);

    return output;
  }

}
