package de.tudarmstadt.informatik.tk.android.assistance;

/**
 * General configuration file for content generation
 * 
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 07.09.2015
 *
 */
public class Config {

  /**
   * Assistance
   */
  public static final String ASSISTANCE_OUTPUT = "../AssistanceSDK/app/src-gen/main/java";
  public static final String ASSISTANCE_PACKAGE = "de.tudarmstadt.informatik.tk.assistance.sdk.db";
  public static final String ASSISTANCE_PACKAGE_SENSOR =
      "de.tudarmstadt.informatik.tk.assistance.sdk.interfaces.IDbSensor";
  public static final String ASSISTANCE_PACKAGE_UPDATABLE_SENSOR =
      "de.tudarmstadt.informatik.tk.assistance.sdk.interfaces.IDbUpdatableSensor";
  public static final int ASSISTANCE_DB_SCHEMA_VERSION = 22;

  /**
   * Battery Watcher
   */
  public static final String BATTERY_WATCHER_OUTPUT = "../Profiler/app/src-gen";
  public static final String BATTERY_WATCHER_PACKAGE =
      "de.tudarmstadt.informatik.tk.assistance.profiler.db";
  public static final int BATTERY_WATCHER_DB_SCHEMA_VERSION = 1;


  private Config() {}
}
