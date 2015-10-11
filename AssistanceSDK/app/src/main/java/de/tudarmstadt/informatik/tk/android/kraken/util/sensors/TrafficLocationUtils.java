package de.tudarmstadt.informatik.tk.android.kraken.util.sensors;

/**
 * Singleton to get actual location in ForegroundTrafficSensor
 */
public class TrafficLocationUtils {

    private static TrafficLocationUtils instance;

    private double longitude = -1;
    private double latitude = -1;

    private TrafficLocationUtils() {}

    public static TrafficLocationUtils getInstance(){
        if (instance == null) {
            instance = new TrafficLocationUtils();
        }
        return instance;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
}
