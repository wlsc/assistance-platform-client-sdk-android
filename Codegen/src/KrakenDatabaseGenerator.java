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

public class KrakenDatabaseGenerator {

	private static final String FULL_QUALIFIED_PATH_IDBUPDATABLESENSOR = "de.tudarmstadt.informatik.tk.kraken.android.sdk.interfaces.IDbUpdatableSensor";
	private static final String FULL_QUALIFIED_PATH_IDBSENSOR = "de.tudarmstadt.informatik.tk.kraken.android.sdk.interfaces.IDbSensor";
	private static final String OUTPUT_PATH = "../Kraken.MeSDK/src-gen";
	private static final String PACKAGE = "de.tudarmstadt.informatik.tk.kraken.android.sdk.db";

    private static final int SCHEMA_VERSION = 5;

    public static void main(String[] args) throws Exception {
    	new File(OUTPUT_PATH).mkdirs();
		generateSchema();
		supressWarningsFiles();
	}

	private static void supressWarningsFiles() {
		System.out.println("--------------------------------------------------------");
		System.out.println("Processing every generated file to add needed '@SupressWarning(\"serial\")' to avoid compile warnings.");
		long longStart = System.currentTimeMillis();

		String strPackagePath = PACKAGE.replaceAll("\\.", "/");
		File directory = new File(OUTPUT_PATH + "/" + strPackagePath);
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

	private static void generateSchema() throws Exception, IOException {
		Schema schema = new Schema(SCHEMA_VERSION, PACKAGE);

		// ------------ SENSORS ------------
		Entity measurementLogger = schema.addEntity("SensorMeasurementLog");
		measurementLogger.addIdProperty();
		measurementLogger.implementsInterface(FULL_QUALIFIED_PATH_IDBSENSOR);
		measurementLogger.addBooleanProperty("started");
		measurementLogger.addLongProperty("timestamp");

		// ----- Accelerometer -----
		Entity sensorAccelerometer = schema.addEntity("SensorAccelerometer");
		sensorAccelerometer.addIdProperty();
		sensorAccelerometer.implementsInterface(FULL_QUALIFIED_PATH_IDBSENSOR);
		sensorAccelerometer.addIntProperty("accuracy");
		sensorAccelerometer.addFloatProperty("accelerationX");
		sensorAccelerometer.addFloatProperty("accelerationY");
		sensorAccelerometer.addFloatProperty("accelerationZ");
		sensorAccelerometer.addLongProperty("timestamp");

		// ----- Activity -----
		Entity sensorActivity = schema.addEntity("SensorActivity");
		sensorActivity.addIdProperty();
		sensorActivity.implementsInterface(FULL_QUALIFIED_PATH_IDBSENSOR);
		sensorActivity.addIntProperty("type");
		sensorActivity.addIntProperty("confidence");
		sensorActivity.addIntProperty("ranking");
		sensorActivity.addLongProperty("timestamp");

		// ----- Network -----
		Entity sensorNetwork = schema.addEntity("SensorConnection");
		sensorNetwork.addIdProperty();
		sensorNetwork.implementsInterface(FULL_QUALIFIED_PATH_IDBSENSOR);
		sensorNetwork.addIntProperty("activeNetwork");
		sensorNetwork.addBooleanProperty("mobileIsAvailable");
		sensorNetwork.addIntProperty("mobileState");
		sensorNetwork.addIntProperty("wlanState");
		sensorNetwork.addBooleanProperty("wlanIsAvailable");
		sensorNetwork.addLongProperty("timestamp");

        // ----- Network Traffic-----
        Entity sensorNetworkTraffic = schema.addEntity("SensorNetworkTraffic");
        sensorNetworkTraffic.addIdProperty();
        sensorNetworkTraffic.implementsInterface(FULL_QUALIFIED_PATH_IDBSENSOR);
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
		sensorLight.implementsInterface(FULL_QUALIFIED_PATH_IDBSENSOR);
		sensorLight.addIntProperty("accuracy");
		sensorLight.addFloatProperty("value");
		sensorLight.addLongProperty("timestamp");

		// ----- Location -----
		Entity sensorLocation = schema.addEntity("SensorLocation");
		sensorLocation.addIdProperty();
		sensorLocation.implementsInterface(FULL_QUALIFIED_PATH_IDBSENSOR);
		sensorLocation.addFloatProperty("accuracy");
		sensorLocation.addDoubleProperty("longitude");
		sensorLocation.addDoubleProperty("latitude");
		sensorLocation.addFloatProperty("speed");
		sensorLocation.addStringProperty("provider");
		sensorLocation.addLongProperty("timestamp");

		// ----- Ringtone -----
		Entity sensorRingtone = schema.addEntity("SensorRingtone");
		sensorRingtone.addIdProperty();
		sensorRingtone.implementsInterface(FULL_QUALIFIED_PATH_IDBSENSOR);
		sensorRingtone.addIntProperty("ringtoneMode");
		sensorRingtone.addLongProperty("timestamp");

		// ----- Loudness -----
		Entity sensorLoudness = schema.addEntity("SensorLoudness");
		sensorLoudness.addIdProperty();
		sensorLoudness.implementsInterface(FULL_QUALIFIED_PATH_IDBSENSOR);
		sensorLoudness.addFloatProperty("loudness");
		sensorLoudness.addLongProperty("startTimestamp");
		sensorLoudness.addLongProperty("timestamp");

		// ----- Accounts -----
		Entity sensorAccountsReader = schema.addEntity("SensorAccountsReader");
		sensorAccountsReader.addIdProperty();
		sensorAccountsReader.implementsInterface(FULL_QUALIFIED_PATH_IDBSENSOR);
		sensorAccountsReader.addStringProperty("accountTypes");
		sensorAccountsReader.addLongProperty("timestamp");

		// ----- Running Processes -----
		Entity sensorRunningProcesses = schema.addEntity("SensorRunningProcesses");
		sensorRunningProcesses.addIdProperty();
		sensorRunningProcesses.implementsInterface(FULL_QUALIFIED_PATH_IDBSENSOR);
		sensorRunningProcesses.addStringProperty("runningProcesses");
		sensorRunningProcesses.addLongProperty("timestamp");

		// ----- Running Services -----
		Entity sensorRunningServices = schema.addEntity("SensorRunningServices");
		sensorRunningServices.addIdProperty();
		sensorRunningServices.implementsInterface(FULL_QUALIFIED_PATH_IDBSENSOR);
		sensorRunningServices.addStringProperty("runningServices");
		sensorRunningServices.addLongProperty("timestamp");

		// ----- Running Tasks -----
		Entity sensorRunningTasks = schema.addEntity("SensorRunningTasks");
		sensorRunningTasks.addIdProperty();
		sensorRunningTasks.implementsInterface(FULL_QUALIFIED_PATH_IDBSENSOR);
		sensorRunningTasks.addStringProperty("runningTasks");
		sensorRunningTasks.addIntProperty("stackPosition");
		sensorRunningTasks.addLongProperty("timestamp");

		// ----- Contacts -----
		Entity sensorContact = schema.addEntity("SensorContact");
		sensorContact.addIdProperty();
		sensorContact.implementsInterface(FULL_QUALIFIED_PATH_IDBUPDATABLESENSOR);
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
		contactNumbers.implementsInterface(FULL_QUALIFIED_PATH_IDBUPDATABLESENSOR);
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
		contactMails.implementsInterface(FULL_QUALIFIED_PATH_IDBUPDATABLESENSOR);
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
		calendarEvent.implementsInterface(FULL_QUALIFIED_PATH_IDBUPDATABLESENSOR);
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
		calendarEventReminder.implementsInterface(FULL_QUALIFIED_PATH_IDBUPDATABLESENSOR);
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
		sensorCallLog.implementsInterface(FULL_QUALIFIED_PATH_IDBUPDATABLESENSOR);
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
        sensorBrowserHistory.implementsInterface(FULL_QUALIFIED_PATH_IDBUPDATABLESENSOR);
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
        sensorForegroundEvent.implementsInterface(FULL_QUALIFIED_PATH_IDBSENSOR);
        sensorForegroundEvent.addStringProperty("packageName");
        sensorForegroundEvent.addStringProperty("appName");
        sensorForegroundEvent.addStringProperty("className");
        sensorForegroundEvent.addStringProperty("activityLabel");
        sensorForegroundEvent.addStringProperty("color");
        sensorForegroundEvent.addStringProperty("url");
        sensorForegroundEvent.addIntProperty("eventType");
        sensorForegroundEvent.addIntProperty("keystrokes");
        sensorForegroundEvent.addLongProperty("timestamp");

		new DaoGenerator().generateAll(schema, OUTPUT_PATH);
	}

	public KrakenDatabaseGenerator() throws Exception {

	}
}
