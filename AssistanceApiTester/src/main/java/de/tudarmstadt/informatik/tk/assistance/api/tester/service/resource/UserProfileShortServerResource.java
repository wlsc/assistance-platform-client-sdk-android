/**
 * 
 */
package de.tudarmstadt.informatik.tk.assistance.api.tester.service.resource;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;

import com.google.gson.GsonBuilder;

import de.tudarmstadt.informatik.tk.assistance.api.tester.model.ProfileResponse;
import de.tudarmstadt.informatik.tk.assistance.api.tester.model.UserSocialService;

/**
 * User short profile service answerer
 * 
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 *
 */
public class UserProfileShortServerResource extends ServerResource {
	
	@Post("json")
    public String represent(String json){
		
		System.out.println("Input:");
		System.out.println(json);
		
		List<UserSocialService> socialServices = new ArrayList<>();
		
		ProfileResponse profileResponse = new ProfileResponse();
		
		profileResponse.setFirstname("Max");
		profileResponse.setLastname("Mustermann");
		profileResponse.setPrimaryEmail("test@test.com");
		profileResponse.setJoinedSince(new Date().getTime()-12342343);
		profileResponse.setSocialServices(socialServices);
		
		GsonBuilder builder = new GsonBuilder();
		builder.excludeFieldsWithoutExposeAnnotation();
		
		String output = builder.create().toJson(profileResponse);
		
		System.out.println("Output:");
		System.out.println(output);
		
        return output;
    }
	
}
