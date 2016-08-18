package de.tudarmstadt.informatik.tk.assistance.sdk;


/**
 * Configuration file for Assistance Platform
 * Configuration of API endpoint, database name, etc
 *
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 18.09.2015
 */
public class Config {

    private Config() {
    }

    /**
     * Platform definition string
     */
    public static final String PLATFORM_NAME = "android";

    /*
    *   Assistance login server. Main API entry point
    *   !!! CAUTION: Please write this address without "/" at the end!
    */
    public static final String ASSISTANCE_ENDPOINT = "";

    /**
     * Local Android database name
     */
    public static final String DATABASE_NAME = "assistance.sqlite";

    /**
     * Android http logger custom name
     */
    public static final String HTTP_LOGGER_NAME = "HTTPS_CLIENT";

    /**
     * ASSISTANCE API ENDPOINTS
     */

    public static final String ASSISTANCE_USER_REGISTER_ENDPOINT = "/users/register";

    public static final String ASSISTANCE_USER_PASSWORD_ENDPOINT = "/users/password";

    public static final String ASSISTANCE_USER_PROFILE_SHORT_ENDPOINT = "/users/profile/short";

    public static final String ASSISTANCE_USER_PROFILE_FULL_ENDPOINT = "/users/profile/long";

    public static final String ASSISTANCE_USER_PROFILE_UPDATE_ENDPOINT = "/users/profile";

    public static final String ASSISTANCE_MODULE_LIST_ENDPOINT = "/assistance/list";

    public static final String ASSISTANCE_MODULE_ACTIVE_ENDPOINT = "/assistance/activations";

    public static final String ASSISTANCE_MODULE_ACTIVATE_ENDPOINT = "/assistance/activate";

    public static final String ASSISTANCE_MODULE_DEACTIVATE_ENDPOINT = "/assistance/deactivate";

    public static final String ASSISTANCE_MODULE_FEEDBACK_ENDPOINT = "/assistance/current/{deviceId}";

    public static final String ASSISTANCE_USER_LOGIN_ENDPOINT = "/users/login";

    public static final String ASSISTANCE_SENSOR_UPLOAD_SERVICE_ENDPOINT = "/sensordata/upload";

    public static final String ASSISTANCE_LOGS_SENSOR_UPLOAD_SERVICE_ENDPOINT = "/sensordata/log_responsetime";

    public static final String DEVICE_REGISTRATION_ENDPOINT = "/devices/register_for_messaging";

    public static final String DEVICE_LIST_ENDPOINT = "/devices/list";

    public static final String DEVICE_SET_USER_DEFINED_NAME_ENDPOINT = "/devices/set_user_defined_name";

    /**
     * GCM sender identification
     */
    public static final String GCM_SENDER_ID = "";
    // max wait time until timeout
    public static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    /**
     * Notifications information
     */
    public static final int DEFAULT_NOTIFICATION_ID = 7331;
    public static final String DEFAULT_NOTIFICATION_GROUP = "AssistanceNotificationGroup";

    /**
     * INTENT CONSTANTS
     */
    public static final String INTENT_EXTRA_SHOW_ICON = "showIcon";

    /**
     * PERMISSION IDs
     */
    public static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;
    public static final int PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION = 101;
    public static final int PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 102;
    public static final int PERM_MODULE_OPTIONAL_CAPABILITY = 240;
    public static final int PERM_MODULE_ALLOWED_CAPABILITY = 241;

}