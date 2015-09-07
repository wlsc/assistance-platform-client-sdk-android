package de.tudarmstadt.informatik.tk.android.assistance.generators;
/**
 * 
 */

import java.io.File;
import java.io.IOException;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Property;
import de.greenrobot.daogenerator.Schema;
import de.tudarmstadt.informatik.tk.android.assistance.Config;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 07.09.2015
 *
 */
public class AssistanceDatabaseGenerator {

	public static void main(String[] args) {
		new File(Config.ASSISTANCE_OUTPUT).mkdirs();
		
		try {
			generateSchemas();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Generates db schema 
	 * 
	 * @throws Exception
	 * @throws IOException
	 */
	private static void generateSchemas() throws Exception, IOException {
		
		Schema schema = new Schema(Config.ASSISTANCE_SCHEMA_VERSION, Config.ASSISTANCE_PACKAGE);
		
		
		// ----- User scheme -----
		Entity user = schema.addEntity("User");
		user.setTableName("user");
		user.addIdProperty().notNull().primaryKey().autoincrement().index();
		user.addStringProperty("firstname");
		user.addStringProperty("lastname");
		user.addStringProperty("primaryEmail").notNull();
		user.addStringProperty("lastLogin");
		user.addStringProperty("joinedSince");
		user.addStringProperty("created");
		
		// ----- Social user profile scheme -----
		Entity socialProfile = schema.addEntity("UserSocialProfile");
		socialProfile.setTableName("user_social_profile");
		socialProfile.addIdProperty().notNull().primaryKey().autoincrement().index();
		socialProfile.addStringProperty("name");
		socialProfile.addStringProperty("firstname");
		socialProfile.addStringProperty("lastname");
		socialProfile.addStringProperty("email");
		socialProfile.addStringProperty("updated");
		socialProfile.addStringProperty("created");
		
		Property socialProfileFKUserProperty = socialProfile.addLongProperty("user_id").notNull().getProperty();
		socialProfile.addToOne(user, socialProfileFKUserProperty);
		user.addToMany(socialProfile, socialProfileFKUserProperty);
		
		// GENERATE CLASSES
		new DaoGenerator().generateAll(schema, Config.ASSISTANCE_OUTPUT);
	}

}
