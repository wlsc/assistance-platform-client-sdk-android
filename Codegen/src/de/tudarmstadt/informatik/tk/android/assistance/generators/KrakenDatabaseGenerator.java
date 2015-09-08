package de.tudarmstadt.informatik.tk.android.assistance.generators;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Property;
import de.greenrobot.daogenerator.Schema;
import de.tudarmstadt.informatik.tk.android.assistance.Config;

/**
 * @author unknown, Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 07.09.2015
 *
 */
public class KrakenDatabaseGenerator {

	public static void main(String[] args) throws Exception {
    	new File(Config.KRAKEN_OUTPUT).mkdirs();
		generateSchemas();
		supressWarningsFiles();
	}

	private static void generateSchemas() throws Exception, IOException {
		Schema schema = new Schema(Config.KRAKEN_SCHEMA_VERSION, Config.KRAKEN_PACKAGE);
		
		// ****************************************
		// ------------ ASSISTANCE STUFF ----------
		// ****************************************
		
		// ----- User scheme -----
		Entity user = schema.addEntity("User");
		user.setTableName("user");
		user.addIdProperty().notNull().primaryKey().autoincrement().index();
		user.addStringProperty("firstname");
		user.addStringProperty("lastname");
		user.addStringProperty("primaryEmail").notNull();
		user.addStringProperty("user_pic_filename");
		user.addStringProperty("lastLogin");
		user.addStringProperty("joinedSince");
		user.addStringProperty("created").notNull();
		
		// ----- Social user profile scheme -----
		Entity socialProfile = schema.addEntity("UserSocialProfile");
		socialProfile.setTableName("user_social_profile");
		socialProfile.addIdProperty().notNull().primaryKey().autoincrement().index();
		socialProfile.addStringProperty("name");
		socialProfile.addStringProperty("firstname");
		socialProfile.addStringProperty("lastname");
		socialProfile.addStringProperty("email");
		socialProfile.addStringProperty("updated");
		socialProfile.addStringProperty("created").notNull();
		
		Property socialProfileFKUserProperty = socialProfile.addLongProperty("user_id").notNull().index().getProperty();
		socialProfile.addToOne(user, socialProfileFKUserProperty);
		user.addToMany(socialProfile, socialProfileFKUserProperty);
		
		// ----- Login information scheme -----
		Entity login = schema.addEntity("Login");
		login.setTableName("login");
		login.addIdProperty().notNull().primaryKey().autoincrement().index();
		login.addStringProperty("token").notNull().index();
		login.addLongProperty("server_device_id");
		login.addStringProperty("last_email").notNull();
		login.addStringProperty("created").notNull();
		
		// ----- Device scheme -----
		Entity device = schema.addEntity("Device");
		device.setTableName("device");
		device.addIdProperty().notNull().primaryKey().autoincrement().index();
		device.addStringProperty("device_identifier");
		device.addStringProperty("os");
		device.addStringProperty("os_version");
		device.addStringProperty("brand");
		device.addStringProperty("model");
		device.addStringProperty("created").notNull();
		
		Property deviceFKLoginProperty = device.addLongProperty("login_id").notNull().index().getProperty();
		device.addToOne(login, deviceFKLoginProperty);
		login.addToMany(device, deviceFKLoginProperty);
		
		// ----- Module availability scheme -----
		Entity module = schema.addEntity("Module");
		module.setTableName("module");
		module.addIdProperty().notNull().primaryKey().autoincrement().index();
		module.addStringProperty("package_name").notNull().index();
		module.addStringProperty("title");
		module.addStringProperty("logo_url");
		module.addStringProperty("description_short");
		module.addStringProperty("description_full");
		module.addStringProperty("copyright");
		module.addStringProperty("support_email");
		module.addStringProperty("created").notNull();
		
		Property moduleFKUserProperty = module.addLongProperty("user_id").notNull().index().getProperty();
		module.addToOne(user, moduleFKUserProperty);
		user.addToMany(module, moduleFKUserProperty);
		
		// ----- Module capability scheme -----
		Entity moduleCapability = schema.addEntity("ModuleCapability");
		moduleCapability.setTableName("module_capability");
		moduleCapability.addIdProperty().notNull().primaryKey().autoincrement().index();
		moduleCapability.addStringProperty("type").notNull().index();
		moduleCapability.addDoubleProperty("frequency").notNull();
		moduleCapability.addStringProperty("created").notNull();
		
		Property moduleCapabilityFKModuleProperty = moduleCapability.addLongProperty("module_id").notNull().index().getProperty();
		moduleCapability.addToOne(module, moduleCapabilityFKModuleProperty);
		module.addToMany(moduleCapability, moduleCapabilityFKModuleProperty);
		
		// ----- Module installation scheme -----
		Entity moduleInstallation = schema.addEntity("ModuleInstallation");
		moduleInstallation.setTableName("module_installation");
		moduleInstallation.addIdProperty().notNull().primaryKey().autoincrement().index();
		moduleInstallation.addStringProperty("created").notNull();
		
		Property moduleInstallationFKModuleProperty = moduleInstallation.addLongProperty("module_id").notNull().index().getProperty();
		Property moduleInstallationFKUserProperty = moduleInstallation.addLongProperty("user_id").notNull().index().getProperty();
		moduleInstallation.addToOne(module, moduleInstallationFKModuleProperty);
		module.addToMany(moduleInstallation, moduleInstallationFKModuleProperty);
		moduleInstallation.addToOne(user, moduleInstallationFKUserProperty);
		user.addToMany(moduleInstallation, moduleInstallationFKUserProperty);
		
		
		// ****************************************
		// ------------ COMMON SENSORS ------------
		// ****************************************
		
		// ----- GPS Position -----
		// REQUIRED
		Entity positionSensor = schema.addEntity("PositionSensor");
		positionSensor.setTableName("position_sensor");
		positionSensor.addIdProperty().notNull().primaryKey().autoincrement().index();
		positionSensor.implementsInterface(Config.KRAKEN_PACKAGE_SENSOR);
		positionSensor.addDoubleProperty("latitude");
		positionSensor.addDoubleProperty("longitude");
		positionSensor.addDoubleProperty("accuracyHorizontal");
		positionSensor.addFloatProperty("speed");
		positionSensor.addStringProperty("created").notNull();
		// OPTIONAL
		positionSensor.addDoubleProperty("altitude");
		positionSensor.addDoubleProperty("accuracyVertical");
		positionSensor.addIntProperty("course");
		positionSensor.addIntProperty("floor");
		
		// ----- Gyroscope -----
		// REQUIRED
		Entity gyroscopeSensor = schema.addEntity("GyroscopeSensor");
		gyroscopeSensor.setTableName("gyroscope_sensor");
		gyroscopeSensor.addIdProperty().notNull().primaryKey().autoincrement().index();
		gyroscopeSensor.implementsInterface(Config.KRAKEN_PACKAGE_SENSOR);
		gyroscopeSensor.addDoubleProperty("x");
		gyroscopeSensor.addDoubleProperty("y");
		gyroscopeSensor.addDoubleProperty("z");
		gyroscopeSensor.addStringProperty("created").notNull();
		// OPTIONAL
		// none
		
		// ----- Accelerometer -----
		// REQUIRED
		Entity accelerometerSensor = schema.addEntity("AccelerometerSensor");
		accelerometerSensor.setTableName("accelerometer_sensor");
		accelerometerSensor.addIdProperty().notNull().primaryKey().autoincrement().index();
		accelerometerSensor.implementsInterface(Config.KRAKEN_PACKAGE_SENSOR);
		accelerometerSensor.addDoubleProperty("x");
		accelerometerSensor.addDoubleProperty("y");
		accelerometerSensor.addDoubleProperty("z");
		accelerometerSensor.addStringProperty("created").notNull();
		// OPTIONAL
		accelerometerSensor.addIntProperty("accuracy");
		
		// ----- Magnetic Field -----
		// REQUIRED
		Entity magneticFieldSensor = schema.addEntity("MagneticFieldSensor");
		magneticFieldSensor.setTableName("magnetic_field_sensor");
		magneticFieldSensor.addIdProperty().notNull().primaryKey().autoincrement().index();
		magneticFieldSensor.implementsInterface(Config.KRAKEN_PACKAGE_SENSOR);
		magneticFieldSensor.addDoubleProperty("x");
		magneticFieldSensor.addDoubleProperty("y");
		magneticFieldSensor.addDoubleProperty("z");
		magneticFieldSensor.addStringProperty("created").notNull();
		// OPTIONAL
		magneticFieldSensor.addIntProperty("accuracy");
		
		// ********************************
		// ------------ EVENTS ------------
		// ********************************
		
		// ----- Motion Activity -----
		// REQUIRED
		Entity motionActivitySensor = schema.addEntity("MotionActivityEvent");
		motionActivitySensor.setTableName("motion_activity_event");
		motionActivitySensor.addIdProperty().notNull().primaryKey().autoincrement().index();
		motionActivitySensor.implementsInterface(Config.KRAKEN_PACKAGE_SENSOR);
		motionActivitySensor.addBooleanProperty("walking");
		motionActivitySensor.addBooleanProperty("running");
		motionActivitySensor.addBooleanProperty("cycling");
		motionActivitySensor.addBooleanProperty("driving");
		motionActivitySensor.addBooleanProperty("stationary");
		motionActivitySensor.addBooleanProperty("unknown");
		motionActivitySensor.addIntProperty("accuracy");
		motionActivitySensor.addStringProperty("created").notNull();
		// OPTIONAL
		motionActivitySensor.addBooleanProperty("onFoot");
		motionActivitySensor.addBooleanProperty("tilting");

		// ----- Connection -----
		// REQUIRED
		Entity connectionEvent = schema.addEntity("ConnectionEvent");
		connectionEvent.setTableName("connection_event");
		connectionEvent.addIdProperty().notNull().primaryKey().autoincrement().index();
		connectionEvent.implementsInterface(Config.KRAKEN_PACKAGE_SENSOR);
		connectionEvent.addBooleanProperty("isWifi");
		connectionEvent.addBooleanProperty("isMobile");
		connectionEvent.addStringProperty("created").notNull();
		// OPTIONAL
		// none

		// ----- Connection -----
		// REQUIRED
		Entity wifiConnectionEvent = schema.addEntity("WifiConnectionEvent");
		wifiConnectionEvent.setTableName("wifi_connection_event");
		wifiConnectionEvent.addIdProperty().notNull().primaryKey().autoincrement().index();
		wifiConnectionEvent.implementsInterface(Config.KRAKEN_PACKAGE_SENSOR);
		wifiConnectionEvent.addStringProperty("ssid");
		wifiConnectionEvent.addStringProperty("bssid");
		wifiConnectionEvent.addStringProperty("created").notNull();
		// OPTIONAL
		wifiConnectionEvent.addIntProperty("channel");
		wifiConnectionEvent.addIntProperty("frequency");
		wifiConnectionEvent.addIntProperty("linkSpeed");
		wifiConnectionEvent.addIntProperty("signalStrength");
		wifiConnectionEvent.addIntProperty("networkId");
		
		// ----- Mobile Connection -----
		// REQUIRED
		Entity mobileConnectionEvent = schema.addEntity("MobileConnectionEvent");
		mobileConnectionEvent.setTableName("mobile_connection_event");
		mobileConnectionEvent.addIdProperty().notNull().primaryKey().autoincrement().index();
		mobileConnectionEvent.implementsInterface(Config.KRAKEN_PACKAGE_SENSOR);
		mobileConnectionEvent.addStringProperty("carrierName");
		mobileConnectionEvent.addStringProperty("mobileCarrierCode");
		mobileConnectionEvent.addStringProperty("mobileNetworkCode");
		mobileConnectionEvent.addStringProperty("created").notNull();
		// OPTIONAL
		mobileConnectionEvent.addBooleanProperty("voipAvailable");
		
		// ----- Loudness -----
		// REQUIRED
		Entity loudnessEvent = schema.addEntity("LoudnessEvent");
		loudnessEvent.setTableName("loudness_event");
		loudnessEvent.addIdProperty().notNull().primaryKey().autoincrement().index();
		loudnessEvent.implementsInterface(Config.KRAKEN_PACKAGE_SENSOR);
		loudnessEvent.addFloatProperty("loudness").notNull();
		loudnessEvent.addStringProperty("created").notNull();
		// OPTIONAL
		// none
		
		// -------------------------------------------------------------------------------------------
		// -------------------------------------------------------------------------------------------
		// -------------------------------------------------------------------------------------------
		
		// *********************
		// ** ANDROID SENSORS **
		// *********************
		
		// ----- MAGNETIC FIELD (UNCALIBRATED) -----
		// REQUIRED
		Entity magneticFieldUncalibrated = schema.addEntity("MagneticFieldUncalibratedSensor");
		magneticFieldUncalibrated.setTableName("magnetic_field_uncalibrated_sensor");
		magneticFieldUncalibrated.addIdProperty().notNull().primaryKey().autoincrement().index();
		magneticFieldUncalibrated.implementsInterface(Config.KRAKEN_PACKAGE_SENSOR);
		magneticFieldUncalibrated.addFloatProperty("xNoHardIron").notNull();
		magneticFieldUncalibrated.addFloatProperty("yNoHardIron").notNull();
		magneticFieldUncalibrated.addFloatProperty("zNoHardIron").notNull();
		magneticFieldUncalibrated.addFloatProperty("xEstimatedIronBias").notNull();
		magneticFieldUncalibrated.addFloatProperty("yEstimatedIronBias").notNull();
		magneticFieldUncalibrated.addFloatProperty("zEstimatedIronBias").notNull();
		magneticFieldUncalibrated.addStringProperty("created").notNull();
		// OPTIONAL
		// none
		
		// ----- GYROSCOPE (UNCALIBRATED) -----
		// REQUIRED
		Entity gyroscopeUncalibrated = schema.addEntity("GyroscopeUncalibratedSensor");
		gyroscopeUncalibrated.setTableName("gyroscope_uncalibrated_sensor");
		gyroscopeUncalibrated.addIdProperty().notNull().primaryKey().autoincrement().index();
		gyroscopeUncalibrated.implementsInterface(Config.KRAKEN_PACKAGE_SENSOR);
		gyroscopeUncalibrated.addFloatProperty("xNoDrift").notNull();
		gyroscopeUncalibrated.addFloatProperty("yNoDrift").notNull();
		gyroscopeUncalibrated.addFloatProperty("zNoDrift").notNull();
		gyroscopeUncalibrated.addFloatProperty("xEstimatedDrift").notNull();
		gyroscopeUncalibrated.addFloatProperty("yEstimatedDrift").notNull();
		gyroscopeUncalibrated.addFloatProperty("zEstimatedDrift").notNull();
		gyroscopeUncalibrated.addStringProperty("created").notNull();
		// OPTIONAL
		// none

		
		// GENERATE CLASSES
		new DaoGenerator().generateAll(schema, Config.KRAKEN_OUTPUT);
	}
	
	private static void generateOLDSchema() throws Exception, IOException {
		Schema schema = new Schema(Config.KRAKEN_SCHEMA_VERSION, Config.KRAKEN_PACKAGE);

		// ------------ SENSORS ------------
		Entity measurementLogger = schema.addEntity("SensorMeasurementLog");
		measurementLogger.addIdProperty();
		measurementLogger.implementsInterface(Config.KRAKEN_PACKAGE_SENSOR);
		measurementLogger.addBooleanProperty("started");
		measurementLogger.addLongProperty("timestamp");

		// ----- Accelerometer -----
		Entity sensorAccelerometer = schema.addEntity("SensorAccelerometer");
		sensorAccelerometer.addIdProperty();
		sensorAccelerometer.implementsInterface(Config.KRAKEN_PACKAGE_SENSOR);
		sensorAccelerometer.addIntProperty("accuracy");
		sensorAccelerometer.addFloatProperty("accelerationX");
		sensorAccelerometer.addFloatProperty("accelerationY");
		sensorAccelerometer.addFloatProperty("accelerationZ");
		sensorAccelerometer.addLongProperty("timestamp");

		// ----- Activity -----
		Entity sensorActivity = schema.addEntity("SensorActivity");
		sensorActivity.addIdProperty();
		sensorActivity.implementsInterface(Config.KRAKEN_PACKAGE_SENSOR);
		sensorActivity.addIntProperty("type");
		sensorActivity.addIntProperty("confidence");
		sensorActivity.addIntProperty("ranking");
		sensorActivity.addLongProperty("timestamp");

		// ----- Network -----
		Entity sensorNetwork = schema.addEntity("SensorConnection");
		sensorNetwork.addIdProperty();
		sensorNetwork.implementsInterface(Config.KRAKEN_PACKAGE_SENSOR);
		sensorNetwork.addIntProperty("activeNetwork");
		sensorNetwork.addBooleanProperty("mobileIsAvailable");
		sensorNetwork.addIntProperty("mobileState");
		sensorNetwork.addIntProperty("wlanState");
		sensorNetwork.addBooleanProperty("wlanIsAvailable");
		sensorNetwork.addLongProperty("timestamp");

        // ----- Network Traffic-----
        Entity sensorNetworkTraffic = schema.addEntity("SensorNetworkTraffic");
        sensorNetworkTraffic.addIdProperty();
        sensorNetworkTraffic.implementsInterface(Config.KRAKEN_PACKAGE_SENSOR);
        sensorNetworkTraffic.addStringProperty("appName");
        sensorNetworkTraffic.addLongProperty("rxBytes");
        sensorNetworkTraffic.addLongProperty("txBytes");
        sensorNetworkTraffic.addLongProperty("timestamp");
        sensorNetworkTraffic.addBooleanProperty("background");
        sensorNetworkTraffic.addDoubleProperty("longitude");
        sensorNetworkTraffic.addDoubleProperty("latitude");
        
		// ----- Light -----
		Entity sensorLight = schema.addEntity("SensorLight");
		sensorLight.addIdProperty();
		sensorLight.implementsInterface(Config.KRAKEN_PACKAGE_SENSOR);
		sensorLight.addIntProperty("accuracy");
		sensorLight.addFloatProperty("value");
		sensorLight.addLongProperty("timestamp");

		// ----- Location -----
		Entity sensorLocation = schema.addEntity("SensorLocation");
		sensorLocation.addIdProperty();
		sensorLocation.implementsInterface(Config.KRAKEN_PACKAGE_SENSOR);
		sensorLocation.addFloatProperty("accuracy");
		sensorLocation.addDoubleProperty("longitude");
		sensorLocation.addDoubleProperty("latitude");
		sensorLocation.addFloatProperty("speed");
		sensorLocation.addStringProperty("provider");
		sensorLocation.addLongProperty("timestamp");

		// ----- Ringtone -----
		Entity sensorRingtone = schema.addEntity("SensorRingtone");
		sensorRingtone.addIdProperty();
		sensorRingtone.implementsInterface(Config.KRAKEN_PACKAGE_SENSOR);
		sensorRingtone.addIntProperty("ringtoneMode");
		sensorRingtone.addLongProperty("timestamp");

		// ----- Loudness -----
		Entity sensorLoudness = schema.addEntity("SensorLoudness");
		sensorLoudness.addIdProperty();
		sensorLoudness.implementsInterface(Config.KRAKEN_PACKAGE_SENSOR);
		sensorLoudness.addFloatProperty("loudness");
		sensorLoudness.addLongProperty("startTimestamp");
		sensorLoudness.addLongProperty("timestamp");

		// ----- Accounts -----
		Entity sensorAccountsReader = schema.addEntity("SensorAccountsReader");
		sensorAccountsReader.addIdProperty();
		sensorAccountsReader.implementsInterface(Config.KRAKEN_PACKAGE_SENSOR);
		sensorAccountsReader.addStringProperty("accountTypes");
		sensorAccountsReader.addLongProperty("timestamp");

		// ----- Running Processes -----
		Entity sensorRunningProcesses = schema.addEntity("SensorRunningProcesses");
		sensorRunningProcesses.addIdProperty();
		sensorRunningProcesses.implementsInterface(Config.KRAKEN_PACKAGE_SENSOR);
		sensorRunningProcesses.addStringProperty("runningProcesses");
		sensorRunningProcesses.addLongProperty("timestamp");

		// ----- Running Services -----
		Entity sensorRunningServices = schema.addEntity("SensorRunningServices");
		sensorRunningServices.addIdProperty();
		sensorRunningServices.implementsInterface(Config.KRAKEN_PACKAGE_SENSOR);
		sensorRunningServices.addStringProperty("runningServices");
		sensorRunningServices.addLongProperty("timestamp");

		// ----- Running Tasks -----
		Entity sensorRunningTasks = schema.addEntity("SensorRunningTasks");
		sensorRunningTasks.addIdProperty();
		sensorRunningTasks.implementsInterface(Config.KRAKEN_PACKAGE_SENSOR);
		sensorRunningTasks.addStringProperty("runningTasks");
		sensorRunningTasks.addIntProperty("stackPosition");
		sensorRunningTasks.addLongProperty("timestamp");

		// ----- Contacts -----
		Entity sensorContact = schema.addEntity("SensorContact");
		sensorContact.addIdProperty();
		sensorContact.implementsInterface(Config.KRAKEN_PACKAGE_UPDATABLE_SENSOR);
		sensorContact.addLongProperty("contactId");
		sensorContact.addLongProperty("globalContactId");
		sensorContact.addStringProperty("displayName");
		sensorContact.addStringProperty("givenName");
		sensorContact.addStringProperty("familyName");
		sensorContact.addIntProperty("starred");
		sensorContact.addIntProperty("lastTimeContacted");
		sensorContact.addIntProperty("timesContacted");
		sensorContact.addStringProperty("note");
		sensorContact.addLongProperty("timestamp");
		sensorContact.addBooleanProperty("isNew");
		sensorContact.addBooleanProperty("isUpdated");
		sensorContact.addBooleanProperty("isDeleted");

		// ----- Contact Numbers -----
		Entity contactNumbers = schema.addEntity("SensorContactNumber");
		contactNumbers.addIdProperty();
		contactNumbers.implementsInterface(Config.KRAKEN_PACKAGE_UPDATABLE_SENSOR);
		Property fkContact = contactNumbers.addLongProperty("fkContact").notNull().getProperty();
		sensorContact.addToMany(contactNumbers, fkContact);
		contactNumbers.addLongProperty("numberId");
		contactNumbers.addLongProperty("contactId");
		contactNumbers.addStringProperty("type");
		contactNumbers.addStringProperty("number");
		contactNumbers.addLongProperty("timestamp");
		contactNumbers.addBooleanProperty("isNew");
		contactNumbers.addBooleanProperty("isUpdated");
		contactNumbers.addBooleanProperty("isDeleted");

		// ----- Contact Mail Addresses -----
		Entity contactMails = schema.addEntity("SensorContactMail");
		contactMails.addIdProperty();
		contactMails.implementsInterface(Config.KRAKEN_PACKAGE_UPDATABLE_SENSOR);
		fkContact = contactMails.addLongProperty("fkContact").notNull().getProperty();
		sensorContact.addToMany(contactMails, fkContact);
		contactMails.addLongProperty("mailId");
		contactMails.addLongProperty("contactId");
		contactMails.addStringProperty("address");
		contactMails.addStringProperty("type");
		contactMails.addLongProperty("timestamp");
		contactMails.addBooleanProperty("isNew");
		contactMails.addBooleanProperty("isUpdated");
		contactMails.addBooleanProperty("isDeleted");

		// ----- Calendar Events -----
		Entity calendarEvent = schema.addEntity("SensorCalendarEvent");
		calendarEvent.addIdProperty();
		calendarEvent.implementsInterface(Config.KRAKEN_PACKAGE_UPDATABLE_SENSOR);
		calendarEvent.addLongProperty("eventId");
		calendarEvent.addLongProperty("calendarId");
		calendarEvent.addBooleanProperty("allDay");
		calendarEvent.addIntProperty("availability");
		calendarEvent.addStringProperty("description");
		calendarEvent.addLongProperty("timestampStart");
		calendarEvent.addLongProperty("timestampEnd");
		calendarEvent.addStringProperty("duration");
		calendarEvent.addStringProperty("location");
		calendarEvent.addStringProperty("timezoneStart");
		calendarEvent.addStringProperty("timezoneEnd");
		calendarEvent.addStringProperty("recurrenceExceptionDate");
		calendarEvent.addStringProperty("recurrenceExceptionRule");
		calendarEvent.addBooleanProperty("hasAlarm");
		calendarEvent.addLongProperty("lastDate");
		calendarEvent.addBooleanProperty("originalAllDay");
		calendarEvent.addStringProperty("originalId");
		calendarEvent.addLongProperty("originalInstanceTime");
		calendarEvent.addStringProperty("recurrenceDate");
		calendarEvent.addStringProperty("recurrenceRule");
		calendarEvent.addIntProperty("status");
		calendarEvent.addStringProperty("title");
		calendarEvent.addLongProperty("timestamp");
		calendarEvent.addBooleanProperty("isNew");
		calendarEvent.addBooleanProperty("isUpdated");
		calendarEvent.addBooleanProperty("isDeleted");

		// ----- Calendar Reminders -----
		Entity calendarEventReminder = schema.addEntity("SensorCalendarEventReminder");
		calendarEventReminder.addIdProperty();
		calendarEventReminder.implementsInterface(Config.KRAKEN_PACKAGE_UPDATABLE_SENSOR);
		calendarEventReminder.addLongProperty("reminderId");
		calendarEventReminder.addLongProperty("eventId");
		calendarEventReminder.addIntProperty("method");
		calendarEventReminder.addIntProperty("minutes");
		calendarEventReminder.addLongProperty("timestamp");
		calendarEventReminder.addBooleanProperty("isNew");
		calendarEventReminder.addBooleanProperty("isUpdated");
		calendarEventReminder.addBooleanProperty("isDeleted");

		// ----- Contacts -----
		Entity sensorCallLog = schema.addEntity("SensorCallLog");
		sensorCallLog.addIdProperty();
		sensorCallLog.implementsInterface(Config.KRAKEN_PACKAGE_UPDATABLE_SENSOR);
		sensorCallLog.addLongProperty("callId");
		sensorCallLog.addIntProperty("type");
		sensorCallLog.addStringProperty("name");
		sensorCallLog.addStringProperty("number");
		sensorCallLog.addLongProperty("date");
		sensorCallLog.addLongProperty("duration");
		sensorCallLog.addLongProperty("timestamp");
		sensorCallLog.addBooleanProperty("isNew");
		sensorCallLog.addBooleanProperty("isUpdated");
		sensorCallLog.addBooleanProperty("isDeleted");

        // ----- Browser History -----
        Entity sensorBrowserHistory = schema.addEntity("SensorBrowserHistory");
        sensorBrowserHistory.addIdProperty();
        sensorBrowserHistory.implementsInterface(Config.KRAKEN_PACKAGE_UPDATABLE_SENSOR);
        sensorBrowserHistory.addLongProperty("historyId");
        sensorBrowserHistory.addStringProperty("url");
        sensorBrowserHistory.addStringProperty("title");
        sensorBrowserHistory.addLongProperty("lastVisited");
        sensorBrowserHistory.addIntProperty("visits");
        sensorBrowserHistory.addBooleanProperty("bookmark");
        sensorBrowserHistory.addLongProperty("timestamp");
        sensorBrowserHistory.addBooleanProperty("isNew");
        sensorBrowserHistory.addBooleanProperty("isUpdated");
        sensorBrowserHistory.addBooleanProperty("isDeleted");

        // ----- Foreground Events -----
        Entity sensorForegroundEvent = schema.addEntity("SensorForegroundEvent");
        sensorForegroundEvent.addIdProperty();
        sensorForegroundEvent.implementsInterface(Config.KRAKEN_PACKAGE_SENSOR);
        sensorForegroundEvent.addStringProperty("packageName");
        sensorForegroundEvent.addStringProperty("appName");
        sensorForegroundEvent.addStringProperty("className");
        sensorForegroundEvent.addStringProperty("activityLabel");
        sensorForegroundEvent.addStringProperty("color");
        sensorForegroundEvent.addStringProperty("url");
        sensorForegroundEvent.addIntProperty("eventType");
        sensorForegroundEvent.addIntProperty("keystrokes");
        sensorForegroundEvent.addLongProperty("timestamp");

		new DaoGenerator().generateAll(schema, Config.KRAKEN_OUTPUT);
	}
	
	private static void supressWarningsFiles() {
		System.out.println("--------------------------------------------------------");
		System.out.println("Processing every generated file to add needed '@SupressWarning(\"serial\")' to avoid compile warnings.");
		long longStart = System.currentTimeMillis();

		String strPackagePath = Config.KRAKEN_PACKAGE.replaceAll("\\.", "/");
		File directory = new File(Config.KRAKEN_OUTPUT + "/" + strPackagePath);
		System.out.println(directory.getAbsolutePath());
		directory.getAbsolutePath();

		FilenameFilter filter = new FilenameFilter() {
			public boolean accept(File directory, String fileName) {
				return fileName.endsWith(".java");
			}
		};

		File[] filesJavaClasses = directory.listFiles(filter);

		Pattern replacementPattern = Pattern.compile("\\* Entity mapped to table (.*)." + System.lineSeparator() + " \\*/");

		int i = 0, u = 0;
		for (File fileJava : filesJavaClasses) {
			try (BufferedReader br = new BufferedReader(new FileReader(fileJava))) {
				StringBuilder sb = new StringBuilder();
				String line = br.readLine();

				while (line != null) {
					sb.append(line);
					sb.append(System.lineSeparator());
					line = br.readLine();
				}
				br.close();

				String stringJavaContent = sb.toString();

				boolean bProcess = stringJavaContent.contains("implements IDbSensor");
				if (bProcess)
					System.out.println(fileJava.getName() + " implements IDbSensor, so we add the suppress annotation.");
				else {
					bProcess = stringJavaContent.contains("implements IDbUpdatableSensor");
					if (bProcess)
						System.out.println(fileJava.getName() + " implements IDbUpdatableSensor, so we add the suppress annotation.");
					else
						System.out.println(fileJava.getName() + " implements no relevant interface.");
				}

				i++;
				if (bProcess) {
					u++;
					Matcher matcher = replacementPattern.matcher(stringJavaContent);
					if (matcher.find()) {
						stringJavaContent = matcher.replaceAll("* Entity mapped to table " + matcher.group(1) + "." + System.lineSeparator() + " \\*/"
								+ System.lineSeparator() + "@SuppressWarnings(\"serial\")");
						FileOutputStream fileOutput = new FileOutputStream(fileJava);
						fileOutput.write(stringJavaContent.getBytes());
						fileOutput.close();
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		long longEnd = System.currentTimeMillis();
		long longDuration = longEnd - longStart;
		System.out.println("Processed " + i + " files in " + longDuration + "ms. " + u + " files were manipulated.");
	}
	
	public KrakenDatabaseGenerator() throws Exception {

	}
}
