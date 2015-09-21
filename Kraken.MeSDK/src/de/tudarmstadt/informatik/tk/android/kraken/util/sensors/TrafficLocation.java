package de.tudarmstadt.informatik.tk.android.kraken.util.sensors;

/**
 * Singleton to get actual location in ForegroundTrafficSensor
 */
public class TrafficLocation {

    private static TrafficLocation instance;

    private double longitude = -1;
    private double latitude = -1;

    private TrafficLocation() {}

    public static TrafficLocation getInstance(){
        if (instance == null) {
            instance = new TrafficLocation();
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
