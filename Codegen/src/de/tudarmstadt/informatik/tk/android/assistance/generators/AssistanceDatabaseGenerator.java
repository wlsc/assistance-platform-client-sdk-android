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
public class AssistanceDatabaseGenerator {

  public static void main(String[] args) throws Exception {
    new File(Config.ASSISTANCE_OUTPUT).mkdirs();
    generateSchemas();
    supressWarningsFiles();
  }

  private static void generateSchemas() throws Exception, IOException {
    Schema schema = new Schema(Config.ASSISTANCE_DB_SCHEMA_VERSION, Config.ASSISTANCE_PACKAGE);

    // ****************************************
    // ------------ ASSISTANCE STUFF ----------
    // ****************************************

    // ----- User scheme -----
    Entity user = schema.addEntity("DbUser");
    user.setTableName("user");
    user.addIdProperty().autoincrement().index();
    user.addStringProperty("token").notNull().index();
    user.addStringProperty("firstname");
    user.addStringProperty("lastname");
    user.addStringProperty("primaryEmail").notNull().index();
    user.addStringProperty("userpicFilename");
    user.addStringProperty("lastLogin");
    user.addStringProperty("joinedSince");
    user.addStringProperty("uuid");
    user.addStringProperty("created").notNull();

    // ----- Social user profile scheme -----
    Entity socialProfile = schema.addEntity("DbUserSocialProfile");
    socialProfile.setTableName("user_social_profile");
    socialProfile.addIdProperty().autoincrement().index();
    socialProfile.addStringProperty("name");
    socialProfile.addStringProperty("firstname");
    socialProfile.addStringProperty("lastname");
    socialProfile.addStringProperty("email");
    socialProfile.addStringProperty("updated");
    socialProfile.addStringProperty("created").notNull();

    Property socialProfileFKUserProperty =
        socialProfile.addLongProperty("userId").index().getProperty();
    socialProfile.addToOne(user, socialProfileFKUserProperty);
    user.addToMany(socialProfile, socialProfileFKUserProperty);

    // ----- Device scheme -----
    Entity device = schema.addEntity("DbDevice");
    device.setTableName("device");
    device.addIdProperty().autoincrement().index();
    device.addStringProperty("deviceIdentifier");
    device.addStringProperty("os");
    device.addStringProperty("osVersion");
    device.addStringProperty("brand");
    device.addStringProperty("model");
    device.addLongProperty("serverDeviceId");
    device.addStringProperty("gcmRegistrationToken");
    device.addStringProperty("userDefinedName");
    device.addStringProperty("created").notNull();

    Property deviceFKUserProperty = device.addLongProperty("userId").index().getProperty();
    device.addToOne(user, deviceFKUserProperty);
    user.addToMany(device, deviceFKUserProperty);

    // ----- Module installation scheme -----
    Entity module = schema.addEntity("DbModule");
    module.setTableName("module");
    module.addIdProperty().autoincrement().index();
    module.addStringProperty("packageName").notNull().unique().index();
    module.addStringProperty("title");
    module.addStringProperty("logoUrl");
    module.addStringProperty("descriptionShort");
    module.addStringProperty("descriptionFull");
    module.addStringProperty("copyright");
    module.addStringProperty("supportEmail");
    module.addBooleanProperty("active").notNull().index();
    module.addStringProperty("created").notNull();

    Property moduleFKUserProperty = module.addLongProperty("userId").index().getProperty();
    module.addToOne(user, moduleFKUserProperty);
    user.addToMany(module, moduleFKUserProperty);

    // ----- Module capability scheme -----
    Entity moduleCapability = schema.addEntity("DbModuleCapability");
    moduleCapability.setTableName("module_capability");
    moduleCapability.addIdProperty().autoincrement().index();
    moduleCapability.addStringProperty("type").notNull().index();
    moduleCapability.addDoubleProperty("collectionInterval");
    moduleCapability.addDoubleProperty("updateInterval");
    moduleCapability.addIntProperty("accuracy");
    moduleCapability.addStringProperty("permissions");
    moduleCapability.addBooleanProperty("required").notNull();
    moduleCapability.addBooleanProperty("active").notNull().index();
    moduleCapability.addStringProperty("created").notNull();

    Property moduleCapabilityFKModuleProperty =
        moduleCapability.addLongProperty("moduleId").index().getProperty();
    moduleCapability.addToOne(module, moduleCapabilityFKModuleProperty);
    module.addToMany(moduleCapability, moduleCapabilityFKModuleProperty);
    
    // ----- Module types permission scheme -----
    Entity moduleTypesPerm = schema.addEntity("DbModuleAllowedCapabilities");
    moduleTypesPerm.setTableName("module_allowed_capabilities");
    moduleTypesPerm.addIdProperty().autoincrement().index();
    moduleTypesPerm.addStringProperty("type").notNull().index();
    moduleTypesPerm.addBooleanProperty("isAllowed").notNull().index();
    moduleTypesPerm.addStringProperty("created").notNull();

    Property moduleTypesPermFKUserProperty =
        moduleTypesPerm.addLongProperty("userId").index().getProperty();
    moduleTypesPerm.addToOne(user, moduleTypesPermFKUserProperty);
    user.addToMany(moduleTypesPerm, moduleTypesPermFKUserProperty);

    // ----- Assistance News scheme -----
    Entity assistanceNews = schema.addEntity("DbNews");
    assistanceNews.setTableName("news");
    assistanceNews.addIdProperty().autoincrement().index();
    assistanceNews.addStringProperty("content");
    assistanceNews.addStringProperty("created").notNull();
      
    Property newsFKModuleProperty =
        assistanceNews.addLongProperty("moduleId").index().getProperty();
    Property newsFKUserProperty =
        assistanceNews.addLongProperty("userId").index().getProperty();
    assistanceNews.addToOne(module, newsFKModuleProperty);
    module.addToMany(assistanceNews, newsFKModuleProperty);
    assistanceNews.addToOne(user, newsFKUserProperty);
    user.addToMany(assistanceNews, newsFKUserProperty);
      
    // ****************************************
    // ---- LOGS AND INTERNAL MEASUREMENTS ----
    // ****************************************
    
    // ----- Logs: Sensor Upload -----
    Entity logsSensorUpload = schema.addEntity("LogsSensorUpload");
    logsSensorUpload.setTableName("logs_sensor_upload");
    logsSensorUpload.addIdProperty().autoincrement().index();
    logsSensorUpload.addLongProperty("startTime");
    logsSensorUpload.addLongProperty("processingTime");
    logsSensorUpload.addLongProperty("responseTime");
    logsSensorUpload.addStringProperty("networkType");
    logsSensorUpload.addIntProperty("eventsNumber");
    logsSensorUpload.addLongProperty("bodySize");
    
    Property logsSensorUploadfkDeviceEvent = logsSensorUpload.addLongProperty("userId").index().getProperty();
    logsSensorUpload.addToOne(user, logsSensorUploadfkDeviceEvent);
    
    // ****************************************
    // ------------ COMMON SENSORS ------------
    // ****************************************

    // ----- GPS Position -----
    // REQUIRED
    Entity positionSensor = schema.addEntity("DbPositionSensor");
    positionSensor.setTableName("position_sensor");
    positionSensor.addIdProperty().autoincrement().index();
    positionSensor.implementsInterface(Config.ASSISTANCE_PACKAGE_SENSOR);
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
    
    Property positionfkDeviceEvent = positionSensor.addLongProperty("deviceId").index().getProperty();
    positionSensor.addToOne(device, positionfkDeviceEvent);

    // ----- Gyroscope -----
    // REQUIRED
    Entity gyroscopeSensor = schema.addEntity("DbGyroscopeSensor");
    gyroscopeSensor.setTableName("gyroscope_sensor");
    gyroscopeSensor.addIdProperty().autoincrement().index();
    gyroscopeSensor.implementsInterface(Config.ASSISTANCE_PACKAGE_SENSOR);
    gyroscopeSensor.addDoubleProperty("x");
    gyroscopeSensor.addDoubleProperty("y");
    gyroscopeSensor.addDoubleProperty("z");
    gyroscopeSensor.addStringProperty("created").notNull();
    // OPTIONAL
    gyroscopeSensor.addIntProperty("accuracy");
    gyroscopeSensor.addFloatProperty("xUncalibratedNoDrift");
    gyroscopeSensor.addFloatProperty("yUncalibratedNoDrift");
    gyroscopeSensor.addFloatProperty("zUncalibratedNoDrift");
    gyroscopeSensor.addFloatProperty("xUncalibratedEstimatedDrift");
    gyroscopeSensor.addFloatProperty("yUncalibratedEstimatedDrift");
    gyroscopeSensor.addFloatProperty("zUncalibratedEstimatedDrift");
    
    Property gyroscopefkDeviceEvent = gyroscopeSensor.addLongProperty("deviceId").index().getProperty();
    gyroscopeSensor.addToOne(device, gyroscopefkDeviceEvent);

    // ----- Accelerometer -----
    // REQUIRED
    Entity accelerometerSensor = schema.addEntity("DbAccelerometerSensor");
    accelerometerSensor.setTableName("accelerometer_sensor");
    accelerometerSensor.addIdProperty().autoincrement().index();
    accelerometerSensor.implementsInterface(Config.ASSISTANCE_PACKAGE_SENSOR);
    accelerometerSensor.addDoubleProperty("x");
    accelerometerSensor.addDoubleProperty("y");
    accelerometerSensor.addDoubleProperty("z");
    accelerometerSensor.addStringProperty("created").notNull();
    // OPTIONAL
    accelerometerSensor.addIntProperty("accuracy");
    
    Property accelerometerfkDeviceEvent = accelerometerSensor.addLongProperty("deviceId").index().getProperty();
    accelerometerSensor.addToOne(device, accelerometerfkDeviceEvent);

    // ----- Magnetic Field -----
    // REQUIRED
    Entity magneticFieldSensor = schema.addEntity("DbMagneticFieldSensor");
    magneticFieldSensor.setTableName("magnetic_field_sensor");
    magneticFieldSensor.addIdProperty().autoincrement().index();
    magneticFieldSensor.implementsInterface(Config.ASSISTANCE_PACKAGE_SENSOR);
    magneticFieldSensor.addDoubleProperty("x");
    magneticFieldSensor.addDoubleProperty("y");
    magneticFieldSensor.addDoubleProperty("z");
    magneticFieldSensor.addStringProperty("created").notNull();
    // OPTIONAL
    magneticFieldSensor.addIntProperty("accuracy");
    magneticFieldSensor.addFloatProperty("xUncalibratedNoHardIron");
    magneticFieldSensor.addFloatProperty("yUncalibratedNoHardIron");
    magneticFieldSensor.addFloatProperty("zUncalibratedNoHardIron");
    magneticFieldSensor.addFloatProperty("xUncalibratedEstimatedIronBias");
    magneticFieldSensor.addFloatProperty("yUncalibratedEstimatedIronBias");
    magneticFieldSensor.addFloatProperty("zUncalibratedEstimatedIronBias");
    
    Property magneticFieldfkDeviceEvent = magneticFieldSensor.addLongProperty("deviceId").index().getProperty();
    magneticFieldSensor.addToOne(device, magneticFieldfkDeviceEvent);


    // ----- Motion Activity -----
    // REQUIRED
    Entity motionActivityEvent = schema.addEntity("DbMotionActivitySensor");
    motionActivityEvent.setTableName("motion_activity_sensor");
    motionActivityEvent.addIdProperty().autoincrement().index();
    motionActivityEvent.implementsInterface(Config.ASSISTANCE_PACKAGE_SENSOR);
    motionActivityEvent.addIntProperty("walking");
    motionActivityEvent.addIntProperty("running");
    motionActivityEvent.addIntProperty("cycling");
    motionActivityEvent.addIntProperty("driving");
    motionActivityEvent.addIntProperty("stationary");
    motionActivityEvent.addIntProperty("unknown");
    motionActivityEvent.addStringProperty("created").notNull();
    // OPTIONAL
    motionActivityEvent.addIntProperty("onFoot");
    motionActivityEvent.addIntProperty("tilting");

    Property motionActivityfkDeviceEvent = motionActivityEvent.addLongProperty("deviceId").index().getProperty();
    motionActivityEvent.addToOne(device, motionActivityfkDeviceEvent);
    
    // ----- Connection -----
    // REQUIRED
    Entity connectionEvent = schema.addEntity("DbConnectionSensor");
    connectionEvent.setTableName("connection_sensor");
    connectionEvent.addIdProperty().autoincrement().index();
    connectionEvent.implementsInterface(Config.ASSISTANCE_PACKAGE_SENSOR);
    connectionEvent.addBooleanProperty("isWifi");
    connectionEvent.addBooleanProperty("isMobile");
    connectionEvent.addStringProperty("created").notNull();
    // OPTIONAL
    // none
    
    Property connectionfkDeviceEvent = connectionEvent.addLongProperty("deviceId").index().getProperty();
    connectionEvent.addToOne(device, connectionfkDeviceEvent);

    // ----- WIFI Connection -----
    // REQUIRED
    Entity wifiConnectionEvent = schema.addEntity("DbWifiConnectionSensor");
    wifiConnectionEvent.setTableName("wifi_connection_sensor");
    wifiConnectionEvent.addIdProperty().autoincrement().index();
    wifiConnectionEvent.implementsInterface(Config.ASSISTANCE_PACKAGE_SENSOR);
    wifiConnectionEvent.addStringProperty("ssid");
    wifiConnectionEvent.addStringProperty("bssid");
    wifiConnectionEvent.addStringProperty("created").notNull();
    // OPTIONAL
    wifiConnectionEvent.addIntProperty("channel");
    wifiConnectionEvent.addIntProperty("frequency");
    wifiConnectionEvent.addIntProperty("linkSpeed");
    wifiConnectionEvent.addIntProperty("signalStrength");
    wifiConnectionEvent.addIntProperty("networkId");
    
    Property wifiConnectionfkDeviceEvent = wifiConnectionEvent.addLongProperty("deviceId").index().getProperty();
    wifiConnectionEvent.addToOne(device, wifiConnectionfkDeviceEvent);

    // ----- Mobile Connection -----
    // REQUIRED
    Entity mobileConnectionEvent = schema.addEntity("DbMobileConnectionSensor");
    mobileConnectionEvent.setTableName("mobile_connection_sensor");
    mobileConnectionEvent.addIdProperty().autoincrement().index();
    mobileConnectionEvent.implementsInterface(Config.ASSISTANCE_PACKAGE_SENSOR);
    mobileConnectionEvent.addStringProperty("carrierName");
    mobileConnectionEvent.addStringProperty("mobileCountryCode");
    mobileConnectionEvent.addStringProperty("mobileNetworkCode");
    mobileConnectionEvent.addStringProperty("created").notNull();
    // OPTIONAL
    mobileConnectionEvent.addBooleanProperty("voipAvailable");

    Property mobileConnectionfkDeviceEvent = mobileConnectionEvent.addLongProperty("deviceId").index().getProperty();
    mobileConnectionEvent.addToOne(device, mobileConnectionfkDeviceEvent);
    
    // ----- Loudness -----
    // REQUIRED
    Entity loudnessEvent = schema.addEntity("DbLoudnessSensor");
    loudnessEvent.setTableName("loudness_sensor");
    loudnessEvent.addIdProperty().autoincrement().index();
    loudnessEvent.implementsInterface(Config.ASSISTANCE_PACKAGE_SENSOR);
    loudnessEvent.addFloatProperty("loudness").notNull();
    loudnessEvent.addStringProperty("created").notNull();
    // OPTIONAL
    // none
    
    Property loudnessfkDeviceEvent = loudnessEvent.addLongProperty("deviceId").index().getProperty();
    loudnessEvent.addToOne(device, loudnessfkDeviceEvent);

    // -------------------------------------------------------------------------------------------
    // -------------------------------------------------------------------------------------------
    // -------------------------------------------------------------------------------------------

    // ******************************
    // ** ANDROID EVENTS / SENSORS **
    // ******************************

    // ----- Foreground Sensor -----
    Entity foregroundEvent = schema.addEntity("DbForegroundSensor");
    foregroundEvent.setTableName("foreground_sensor");
    foregroundEvent.addIdProperty().autoincrement().index();
    foregroundEvent.implementsInterface(Config.ASSISTANCE_PACKAGE_SENSOR);
    foregroundEvent.addStringProperty("packageName");
    foregroundEvent.addStringProperty("appName");
    foregroundEvent.addStringProperty("className");
    foregroundEvent.addStringProperty("activityLabel");
    foregroundEvent.addStringProperty("color");
    foregroundEvent.addStringProperty("url");
    foregroundEvent.addIntProperty("eventType");
    foregroundEvent.addIntProperty("keystrokes");
    foregroundEvent.addStringProperty("created").notNull();
    
    Property foregroundfkDeviceEvent = foregroundEvent.addLongProperty("deviceId").index().getProperty();
    foregroundEvent.addToOne(device, foregroundfkDeviceEvent);
    
    // ----- Light Sensor -----
    Entity lightSensor = schema.addEntity("DbLightSensor");
    lightSensor.setTableName("light_sensor");
    lightSensor.addIdProperty().autoincrement().index();
    lightSensor.implementsInterface(Config.ASSISTANCE_PACKAGE_SENSOR);
    lightSensor.addIntProperty("accuracy");
    lightSensor.addFloatProperty("value");
    lightSensor.addStringProperty("created").notNull();

    Property lightfkDeviceEvent = lightSensor.addLongProperty("deviceId").index().getProperty();
    lightSensor.addToOne(device, lightfkDeviceEvent);
    
    // ----- Ringtone Sensor -----
    Entity ringtoneEvent = schema.addEntity("DbRingtoneSensor");
    ringtoneEvent.setTableName("ringtone_sensor");
    ringtoneEvent.addIdProperty().autoincrement().index();
    ringtoneEvent.implementsInterface(Config.ASSISTANCE_PACKAGE_SENSOR);
    ringtoneEvent.addIntProperty("mode");
    ringtoneEvent.addStringProperty("created").notNull();
    
    Property ringtonefkDeviceEvent = ringtoneEvent.addLongProperty("deviceId").index().getProperty();
    ringtoneEvent.addToOne(device, ringtonefkDeviceEvent);
    
    // ----- Running Processes Sensor -----
    Entity runningProcessesEvent = schema.addEntity("DbRunningProcessesSensor");
    runningProcessesEvent.setTableName("running_processes_sensor");
    runningProcessesEvent.addIdProperty().autoincrement().index();
    runningProcessesEvent.implementsInterface(Config.ASSISTANCE_PACKAGE_SENSOR);
    runningProcessesEvent.addStringProperty("name");
    runningProcessesEvent.addStringProperty("created").notNull();

    Property runningProcessesfkDeviceEvent = runningProcessesEvent.addLongProperty("deviceId").index().getProperty();
    runningProcessesEvent.addToOne(device, runningProcessesfkDeviceEvent);
    
    // ----- Running Services Sensor -----
    Entity runningServicesEvent = schema.addEntity("DbRunningServicesSensor");
    runningServicesEvent.setTableName("running_services_sensor");
    runningServicesEvent.addIdProperty().autoincrement().index();
    runningServicesEvent.implementsInterface(Config.ASSISTANCE_PACKAGE_SENSOR);
    runningServicesEvent.addStringProperty("packageName");
    runningServicesEvent.addStringProperty("className");
    runningServicesEvent.addStringProperty("created").notNull();
    
    Property runningServicesfkDeviceEvent = runningServicesEvent.addLongProperty("deviceId").index().getProperty();
    runningServicesEvent.addToOne(device, runningServicesfkDeviceEvent);

    // ----- Running Tasks Sensor -----
    Entity runningTasksEvent = schema.addEntity("DbRunningTasksSensor");
    runningTasksEvent.setTableName("running_tasks_sensor");
    runningTasksEvent.addIdProperty().autoincrement().index();
    runningTasksEvent.implementsInterface(Config.ASSISTANCE_PACKAGE_SENSOR);
    runningTasksEvent.addStringProperty("name");
    runningTasksEvent.addIntProperty("stackPosition");
    runningTasksEvent.addStringProperty("created").notNull();

    Property runningTasksfkDeviceEvent = runningTasksEvent.addLongProperty("deviceId").index().getProperty();
    runningTasksEvent.addToOne(device, runningTasksfkDeviceEvent);
    
    // ----- Account Reader Sensor -----
    Entity accountReaderEvent = schema.addEntity("DbAccountReaderSensor");
    accountReaderEvent.setTableName("account_reader_sensor");
    accountReaderEvent.addIdProperty().autoincrement().index();
    accountReaderEvent.implementsInterface(Config.ASSISTANCE_PACKAGE_SENSOR);
    accountReaderEvent.addStringProperty("types");
    accountReaderEvent.addStringProperty("created").notNull();

    Property accountReaderfkDeviceEvent = accountReaderEvent.addLongProperty("deviceId").index().getProperty();
    accountReaderEvent.addToOne(device, accountReaderfkDeviceEvent);
    
    // ----- Network Traffic Sensor -----
    Entity networkTrafficEvent = schema.addEntity("DbNetworkTrafficSensor");
    networkTrafficEvent.setTableName("network_traffic_sensor");
    networkTrafficEvent.addIdProperty().autoincrement().index();
    networkTrafficEvent.implementsInterface(Config.ASSISTANCE_PACKAGE_SENSOR);
    networkTrafficEvent.addStringProperty("appName");
    networkTrafficEvent.addLongProperty("rxBytes");
    networkTrafficEvent.addLongProperty("txBytes");
    networkTrafficEvent.addBooleanProperty("background");
    networkTrafficEvent.addDoubleProperty("longitude");
    networkTrafficEvent.addDoubleProperty("latitude");
    networkTrafficEvent.addStringProperty("created").notNull();
      
    Property networkTrafficfkDeviceEvent = networkTrafficEvent.addLongProperty("deviceId").index().getProperty();
    networkTrafficEvent.addToOne(device, networkTrafficfkDeviceEvent);
    
    // ----- Browser History Sensor -----
    Entity browserHistoryEvent = schema.addEntity("DbBrowserHistorySensor");
    browserHistoryEvent.setTableName("browser_history_sensor");
    browserHistoryEvent.addIdProperty().autoincrement().index();
    browserHistoryEvent.implementsInterface(Config.ASSISTANCE_PACKAGE_UPDATABLE_SENSOR);
    browserHistoryEvent.addStringProperty("url");
    browserHistoryEvent.addStringProperty("title");
    browserHistoryEvent.addLongProperty("lastVisited");
    browserHistoryEvent.addIntProperty("visits");
    browserHistoryEvent.addBooleanProperty("bookmark");
    browserHistoryEvent.addBooleanProperty("isNew");
    browserHistoryEvent.addBooleanProperty("isUpdated");
    browserHistoryEvent.addBooleanProperty("isDeleted");
    browserHistoryEvent.addStringProperty("created").notNull();

    Property browserHistoryfkDeviceEvent = browserHistoryEvent.addLongProperty("deviceId").index().getProperty();
    browserHistoryEvent.addToOne(device, browserHistoryfkDeviceEvent);
    
    // ----- Call Log Sensor -----
    Entity callLogEvent = schema.addEntity("DbCallLogSensor");
    callLogEvent.setTableName("call_log_sensor");
    callLogEvent.addIdProperty().autoincrement().index();
    callLogEvent.implementsInterface(Config.ASSISTANCE_PACKAGE_UPDATABLE_SENSOR);
    callLogEvent.addLongProperty("callId");
    callLogEvent.addIntProperty("type");
    callLogEvent.addStringProperty("name");
    callLogEvent.addStringProperty("number");
    callLogEvent.addLongProperty("date");
    callLogEvent.addLongProperty("duration");
    callLogEvent.addBooleanProperty("isNew");
    callLogEvent.addBooleanProperty("isUpdated");
    callLogEvent.addBooleanProperty("isDeleted");
    callLogEvent.addStringProperty("created").notNull();

    Property callLogfkDeviceEvent = callLogEvent.addLongProperty("deviceId").index().getProperty();
    callLogEvent.addToOne(device, callLogfkDeviceEvent);
    
    // ----- Calendar Sensor -----
    Entity calendarEvent = schema.addEntity("DbCalendarSensor");
    calendarEvent.setTableName("calendar_sensor");
    calendarEvent.addIdProperty().autoincrement().index();
    calendarEvent.implementsInterface(Config.ASSISTANCE_PACKAGE_UPDATABLE_SENSOR);
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
    calendarEvent.addBooleanProperty("isNew");
    calendarEvent.addBooleanProperty("isUpdated");
    calendarEvent.addBooleanProperty("isDeleted");
    calendarEvent.addStringProperty("created").notNull();

    Property calendarfkDeviceEvent = calendarEvent.addLongProperty("deviceId").index().getProperty();
    calendarEvent.addToOne(device, calendarfkDeviceEvent);
    
    // ----- Calendar Reminder Sensor -----
    Entity calendarReminderEvent = schema.addEntity("DbCalendarReminderSensor");
    calendarReminderEvent.setTableName("calendar_reminder_sensor");
    calendarReminderEvent.addIdProperty().autoincrement().index();
    calendarReminderEvent.implementsInterface(Config.ASSISTANCE_PACKAGE_UPDATABLE_SENSOR);
    calendarReminderEvent.addLongProperty("reminderId").notNull().unique().index();
    calendarReminderEvent.addLongProperty("eventId");
    calendarReminderEvent.addIntProperty("method");
    calendarReminderEvent.addIntProperty("minutes");
    calendarReminderEvent.addBooleanProperty("isNew");
    calendarReminderEvent.addBooleanProperty("isUpdated");
    calendarReminderEvent.addBooleanProperty("isDeleted");
    calendarReminderEvent.addStringProperty("created").notNull();

    Property calendarReminderfkDeviceEvent = calendarReminderEvent.addLongProperty("deviceId").index().getProperty();
    calendarReminderEvent.addToOne(device, calendarReminderfkDeviceEvent);
    
    // ----- Contact Sensor -----
    Entity contactEvent = schema.addEntity("DbContactSensor");
    contactEvent.setTableName("contact_sensor");
    contactEvent.addIdProperty().autoincrement().index();
    contactEvent.implementsInterface(Config.ASSISTANCE_PACKAGE_UPDATABLE_SENSOR);
    contactEvent.addLongProperty("contactId");
    contactEvent.addLongProperty("globalContactId");
    contactEvent.addStringProperty("displayName");
    contactEvent.addStringProperty("givenName");
    contactEvent.addStringProperty("familyName");
    contactEvent.addIntProperty("starred");
    contactEvent.addIntProperty("lastTimeContacted");
    contactEvent.addIntProperty("timesContacted");
    contactEvent.addStringProperty("note");
    contactEvent.addBooleanProperty("isNew");
    contactEvent.addBooleanProperty("isUpdated");
    contactEvent.addBooleanProperty("isDeleted");
    contactEvent.addStringProperty("created").notNull();

    Property contactfkDeviceEvent = contactEvent.addLongProperty("deviceId").index().getProperty();
    contactEvent.addToOne(device, contactfkDeviceEvent);
    
    // ----- Contact Numbers Sensor -----
    Entity contactNumbersEvent = schema.addEntity("DbContactNumberSensor");
    contactNumbersEvent.setTableName("contact_number_sensor");
    contactNumbersEvent.addIdProperty().autoincrement().index();
    contactNumbersEvent.implementsInterface(Config.ASSISTANCE_PACKAGE_UPDATABLE_SENSOR);
    contactNumbersEvent.addLongProperty("numberId").index().notNull().unique();
    contactNumbersEvent.addStringProperty("type");
    contactNumbersEvent.addStringProperty("number");
    contactNumbersEvent.addBooleanProperty("isNew");
    contactNumbersEvent.addBooleanProperty("isUpdated");
    contactNumbersEvent.addBooleanProperty("isDeleted");
    contactNumbersEvent.addStringProperty("created").notNull();

    Property contactNumberfkContactEvent =
        contactNumbersEvent.addLongProperty("contactId").index().notNull().getProperty();
    contactNumbersEvent.addToOne(contactEvent, contactNumberfkContactEvent);
    contactEvent.addToMany(contactNumbersEvent, contactNumberfkContactEvent);
    Property contactNumbersfkDeviceEvent = contactNumbersEvent.addLongProperty("deviceId").index().getProperty();
    contactNumbersEvent.addToOne(device, contactNumbersfkDeviceEvent);

    // ----- Contact Mail Addresses Sensor -----
    Entity contactMailEvent = schema.addEntity("DbContactEmailSensor");
    contactMailEvent.setTableName("contact_email_sensor");
    contactMailEvent.addIdProperty().autoincrement().index();
    contactMailEvent.implementsInterface(Config.ASSISTANCE_PACKAGE_UPDATABLE_SENSOR);
    contactMailEvent.addLongProperty("mailId");
    contactMailEvent.addStringProperty("address");
    contactMailEvent.addStringProperty("type");
    contactMailEvent.addBooleanProperty("isNew");
    contactMailEvent.addBooleanProperty("isUpdated");
    contactMailEvent.addBooleanProperty("isDeleted");
    contactMailEvent.addStringProperty("created").notNull();

    contactNumberfkContactEvent =
        contactMailEvent.addLongProperty("contactId").index().notNull().getProperty();
    contactMailEvent.addToOne(contactEvent, contactNumberfkContactEvent);
    contactEvent.addToMany(contactMailEvent, contactNumberfkContactEvent);
    Property contactMailfkDeviceEvent = contactMailEvent.addLongProperty("deviceId").index().notNull().getProperty();
    contactMailEvent.addToOne(device, contactMailfkDeviceEvent);
    
    // ----- Power State Sensor -----
    // REQUIRED
    Entity powerStatusEvent = schema.addEntity("DbPowerStateSensor");
    powerStatusEvent.setTableName("power_state_sensor");
    powerStatusEvent.addIdProperty().autoincrement().index();
    powerStatusEvent.implementsInterface(Config.ASSISTANCE_PACKAGE_SENSOR);
    powerStatusEvent.addBooleanProperty("isCharging");
    powerStatusEvent.addFloatProperty("percent");
    powerStatusEvent.addStringProperty("created").notNull();
    // OPTIONAL
    powerStatusEvent.addIntProperty("chargingState");
    powerStatusEvent.addIntProperty("chargingMode");
    powerStatusEvent.addBooleanProperty("powerSaveMode");
    
    Property powerStatusfkDeviceEvent = powerStatusEvent.addLongProperty("deviceId").index().getProperty();
    powerStatusEvent.addToOne(device, powerStatusfkDeviceEvent);
    
    // ----- Power Level Sensor -----
    // REQUIRED
    Entity powerLevelEvent = schema.addEntity("DbPowerLevelSensor");
    powerLevelEvent.setTableName("power_level_sensor");
    powerLevelEvent.addIdProperty().autoincrement().index();
    powerLevelEvent.implementsInterface(Config.ASSISTANCE_PACKAGE_SENSOR);
    powerLevelEvent.addFloatProperty("percent");
    powerLevelEvent.addStringProperty("created").notNull();
    
    Property powerLevelfkDeviceEvent = powerLevelEvent.addLongProperty("deviceId").index().getProperty();
    powerLevelEvent.addToOne(device, powerLevelfkDeviceEvent);
    
    /**
     * SOCIAL
     */
    
    // ----- TUCaN Sensor -----
    // REQUIRED
    Entity tucanEvent = schema.addEntity("DbTucanSensor");
    tucanEvent.setTableName("tucan_sensor");
    tucanEvent.addIdProperty().autoincrement().index();
    tucanEvent.implementsInterface(Config.ASSISTANCE_PACKAGE_SENSOR);
    tucanEvent.addStringProperty("username").notNull();
    tucanEvent.addStringProperty("password").notNull();
    tucanEvent.addBooleanProperty("wasChanged").notNull();
    tucanEvent.addStringProperty("created").notNull();
    
    Property tucanfkUserEvent = tucanEvent.addLongProperty("userId").index().getProperty();
    tucanEvent.addToOne(user, tucanfkUserEvent);
    
    // ----- Facebook Sensor -----
    // REQUIRED
    Entity facebookEvent = schema.addEntity("DbFacebookSensor");
    facebookEvent.setTableName("facebook_sensor");
    facebookEvent.addIdProperty().autoincrement().index();
    facebookEvent.implementsInterface(Config.ASSISTANCE_PACKAGE_SENSOR);
    facebookEvent.addStringProperty("oauthToken");
    facebookEvent.addStringProperty("permissionsDeclined");
    facebookEvent.addStringProperty("permissions");
    facebookEvent.addBooleanProperty("wasChanged").notNull();
    facebookEvent.addStringProperty("created").notNull();
    
    Property facebookfkUserEvent = facebookEvent.addLongProperty("userId").index().getProperty();
    facebookEvent.addToOne(user, facebookfkUserEvent);
   

    // **************************
    // **** GENERATE CLASSES ****
    // **************************
    new DaoGenerator().generateAll(schema, Config.ASSISTANCE_OUTPUT);
  }

  private static void generateOLDSchema() throws Exception, IOException {
    Schema schema = new Schema(Config.ASSISTANCE_DB_SCHEMA_VERSION, Config.ASSISTANCE_PACKAGE);

    // ------------ SENSORS ------------
    Entity measurementLogger = schema.addEntity("SensorMeasurementLog");
    measurementLogger.addIdProperty();
    measurementLogger.implementsInterface(Config.ASSISTANCE_PACKAGE_SENSOR);
    measurementLogger.addBooleanProperty("started");
    measurementLogger.addLongProperty("timestamp");

    // ----- Accelerometer -----
    Entity sensorAccelerometer = schema.addEntity("SensorAccelerometer");
    sensorAccelerometer.addIdProperty();
    sensorAccelerometer.implementsInterface(Config.ASSISTANCE_PACKAGE_SENSOR);
    sensorAccelerometer.addIntProperty("accuracy");
    sensorAccelerometer.addFloatProperty("accelerationX");
    sensorAccelerometer.addFloatProperty("accelerationY");
    sensorAccelerometer.addFloatProperty("accelerationZ");
    sensorAccelerometer.addLongProperty("timestamp");

    // ----- Activity -----
    Entity sensorActivity = schema.addEntity("SensorActivity");
    sensorActivity.addIdProperty();
    sensorActivity.implementsInterface(Config.ASSISTANCE_PACKAGE_SENSOR);
    sensorActivity.addIntProperty("type");
    sensorActivity.addIntProperty("confidence");
    sensorActivity.addIntProperty("ranking");
    sensorActivity.addLongProperty("timestamp");

    // ----- Network -----
    Entity sensorNetwork = schema.addEntity("SensorConnection");
    sensorNetwork.addIdProperty();
    sensorNetwork.implementsInterface(Config.ASSISTANCE_PACKAGE_SENSOR);
    sensorNetwork.addIntProperty("activeNetwork");
    sensorNetwork.addBooleanProperty("mobileIsAvailable");
    sensorNetwork.addIntProperty("mobileState");
    sensorNetwork.addIntProperty("wlanState");
    sensorNetwork.addBooleanProperty("wlanIsAvailable");
    sensorNetwork.addLongProperty("timestamp");

    // ----- Network Traffic-----
    Entity sensorNetworkTraffic = schema.addEntity("SensorNetworkTraffic");
    sensorNetworkTraffic.addIdProperty();
    sensorNetworkTraffic.implementsInterface(Config.ASSISTANCE_PACKAGE_SENSOR);
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
    sensorLight.implementsInterface(Config.ASSISTANCE_PACKAGE_SENSOR);
    sensorLight.addIntProperty("accuracy");
    sensorLight.addFloatProperty("value");
    sensorLight.addLongProperty("timestamp");

    // ----- Location -----
    Entity sensorLocation = schema.addEntity("SensorLocation");
    sensorLocation.addIdProperty();
    sensorLocation.implementsInterface(Config.ASSISTANCE_PACKAGE_SENSOR);
    sensorLocation.addFloatProperty("accuracy");
    sensorLocation.addDoubleProperty("longitude");
    sensorLocation.addDoubleProperty("latitude");
    sensorLocation.addFloatProperty("speed");
    sensorLocation.addStringProperty("provider");
    sensorLocation.addLongProperty("timestamp");

    // ----- Ringtone -----
    Entity sensorRingtone = schema.addEntity("SensorRingtone");
    sensorRingtone.addIdProperty();
    sensorRingtone.implementsInterface(Config.ASSISTANCE_PACKAGE_SENSOR);
    sensorRingtone.addIntProperty("ringtoneMode");
    sensorRingtone.addLongProperty("timestamp");

    // ----- Loudness -----
    Entity sensorLoudness = schema.addEntity("SensorLoudness");
    sensorLoudness.addIdProperty();
    sensorLoudness.implementsInterface(Config.ASSISTANCE_PACKAGE_SENSOR);
    sensorLoudness.addFloatProperty("loudness");
    sensorLoudness.addLongProperty("startTimestamp");
    sensorLoudness.addLongProperty("timestamp");

    // ----- Accounts -----
    Entity sensorAccountsReader = schema.addEntity("SensorAccountsReader");
    sensorAccountsReader.addIdProperty();
    sensorAccountsReader.implementsInterface(Config.ASSISTANCE_PACKAGE_SENSOR);
    sensorAccountsReader.addStringProperty("accountTypes");
    sensorAccountsReader.addLongProperty("timestamp");

    // ----- Running Processes -----
    Entity sensorRunningProcesses = schema.addEntity("SensorRunningProcesses");
    sensorRunningProcesses.addIdProperty();
    sensorRunningProcesses.implementsInterface(Config.ASSISTANCE_PACKAGE_SENSOR);
    sensorRunningProcesses.addStringProperty("runningProcesses");
    sensorRunningProcesses.addLongProperty("timestamp");

    // ----- Running Services -----
    Entity sensorRunningServices = schema.addEntity("SensorRunningServices");
    sensorRunningServices.addIdProperty();
    sensorRunningServices.implementsInterface(Config.ASSISTANCE_PACKAGE_SENSOR);
    sensorRunningServices.addStringProperty("runningServices");
    sensorRunningServices.addLongProperty("timestamp");

    // ----- Running Tasks -----
    Entity sensorRunningTasks = schema.addEntity("SensorRunningTasks");
    sensorRunningTasks.addIdProperty();
    sensorRunningTasks.implementsInterface(Config.ASSISTANCE_PACKAGE_SENSOR);
    sensorRunningTasks.addStringProperty("runningTasks");
    sensorRunningTasks.addIntProperty("stackPosition");
    sensorRunningTasks.addLongProperty("timestamp");

    // ----- Contacts -----
    Entity sensorContact = schema.addEntity("SensorContact");
    sensorContact.addIdProperty();
    sensorContact.implementsInterface(Config.ASSISTANCE_PACKAGE_UPDATABLE_SENSOR);
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
    contactNumbers.implementsInterface(Config.ASSISTANCE_PACKAGE_UPDATABLE_SENSOR);
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
    contactMails.implementsInterface(Config.ASSISTANCE_PACKAGE_UPDATABLE_SENSOR);
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
    calendarEvent.implementsInterface(Config.ASSISTANCE_PACKAGE_UPDATABLE_SENSOR);
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
    calendarEventReminder.implementsInterface(Config.ASSISTANCE_PACKAGE_UPDATABLE_SENSOR);
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
    sensorCallLog.implementsInterface(Config.ASSISTANCE_PACKAGE_UPDATABLE_SENSOR);
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
    sensorBrowserHistory.implementsInterface(Config.ASSISTANCE_PACKAGE_UPDATABLE_SENSOR);
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
    sensorForegroundEvent.implementsInterface(Config.ASSISTANCE_PACKAGE_SENSOR);
    sensorForegroundEvent.addStringProperty("packageName");
    sensorForegroundEvent.addStringProperty("appName");
    sensorForegroundEvent.addStringProperty("className");
    sensorForegroundEvent.addStringProperty("activityLabel");
    sensorForegroundEvent.addStringProperty("color");
    sensorForegroundEvent.addStringProperty("url");
    sensorForegroundEvent.addIntProperty("eventType");
    sensorForegroundEvent.addIntProperty("keystrokes");
    sensorForegroundEvent.addLongProperty("timestamp");

    new DaoGenerator().generateAll(schema, Config.ASSISTANCE_OUTPUT);
  }

  private static void supressWarningsFiles() {
    System.out.println("--------------------------------------------------------");
    System.out.println(
        "Processing every generated file to add needed '@SupressWarning(\"serial\")' to avoid compile warnings.");
    long longStart = System.currentTimeMillis();

    String strPackagePath = Config.ASSISTANCE_PACKAGE.replaceAll("\\.", "/");
    File directory = new File(Config.ASSISTANCE_OUTPUT + "/" + strPackagePath);
    System.out.println(directory.getAbsolutePath());
    directory.getAbsolutePath();

    FilenameFilter filter = new FilenameFilter() {
      public boolean accept(File directory, String fileName) {
        return fileName.endsWith(".java");
      }
    };

    File[] filesJavaClasses = directory.listFiles(filter);

    Pattern replacementPattern =
        Pattern.compile("\\* Entity mapped to table (.*)." + System.lineSeparator() + " \\*/");

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
          System.out.println(
              fileJava.getName() + " implements IDbSensor, so we add the suppress annotation.");
        else {
          bProcess = stringJavaContent.contains("implements IDbUpdatableSensor");
          if (bProcess)
            System.out.println(fileJava.getName()
                + " implements IDbUpdatableSensor, so we add the suppress annotation.");
          else
            System.out.println(fileJava.getName() + " implements no relevant interface.");
        }

        i++;
        if (bProcess) {
          u++;
          Matcher matcher = replacementPattern.matcher(stringJavaContent);
          if (matcher.find()) {
            stringJavaContent = matcher.replaceAll(
                "* Entity mapped to table " + matcher.group(1) + "." + System.lineSeparator()
                    + " \\*/" + System.lineSeparator() + "@SuppressWarnings(\"serial\")");
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
    System.out.println(
        "Processed " + i + " files in " + longDuration + "ms. " + u + " files were manipulated.");
  }

  public AssistanceDatabaseGenerator() throws Exception {

  }
}
