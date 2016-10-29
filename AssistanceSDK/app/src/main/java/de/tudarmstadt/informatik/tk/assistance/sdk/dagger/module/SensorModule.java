package de.tudarmstadt.informatik.tk.assistance.sdk.dagger.module;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import de.tudarmstadt.informatik.tk.assistance.sdk.sensing.impl.contentobserver.CalendarSensor;
import de.tudarmstadt.informatik.tk.assistance.sdk.sensing.impl.contentobserver.CallLogSensor;
import de.tudarmstadt.informatik.tk.assistance.sdk.sensing.impl.contentobserver.ContactsSensor;
import de.tudarmstadt.informatik.tk.assistance.sdk.sensing.impl.contentobserver.incomplete.BrowserHistorySensor;
import de.tudarmstadt.informatik.tk.assistance.sdk.sensing.impl.external.FacebookSensor;
import de.tudarmstadt.informatik.tk.assistance.sdk.sensing.impl.external.TucanSensor;
import de.tudarmstadt.informatik.tk.assistance.sdk.sensing.impl.periodic.BackgroundTrafficSensor;
import de.tudarmstadt.informatik.tk.assistance.sdk.sensing.impl.periodic.LoudnessSensor;
import de.tudarmstadt.informatik.tk.assistance.sdk.sensing.impl.periodic.PowerLevelSensor;
import de.tudarmstadt.informatik.tk.assistance.sdk.sensing.impl.periodic.RingtoneSensor;
import de.tudarmstadt.informatik.tk.assistance.sdk.sensing.impl.periodic.RunningProcessesReaderSensor;
import de.tudarmstadt.informatik.tk.assistance.sdk.sensing.impl.periodic.RunningServicesReaderSensor;
import de.tudarmstadt.informatik.tk.assistance.sdk.sensing.impl.periodic.RunningTasksReaderSensor;
import de.tudarmstadt.informatik.tk.assistance.sdk.sensing.impl.triggered.AccelerometerSensor;
import de.tudarmstadt.informatik.tk.assistance.sdk.sensing.impl.triggered.ConnectionSensor;
import de.tudarmstadt.informatik.tk.assistance.sdk.sensing.impl.triggered.ForegroundSensor;
import de.tudarmstadt.informatik.tk.assistance.sdk.sensing.impl.triggered.ForegroundTrafficSensor;
import de.tudarmstadt.informatik.tk.assistance.sdk.sensing.impl.triggered.GyroscopeSensor;
import de.tudarmstadt.informatik.tk.assistance.sdk.sensing.impl.triggered.LightSensor;
import de.tudarmstadt.informatik.tk.assistance.sdk.sensing.impl.triggered.LocationSensor;
import de.tudarmstadt.informatik.tk.assistance.sdk.sensing.impl.triggered.MagneticFieldSensor;
import de.tudarmstadt.informatik.tk.assistance.sdk.sensing.impl.triggered.MotionActivitySensor;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 27/10/2016
 */
@Module
public class SensorModule {

    private Context context;

    public SensorModule(Context context) {
        this.context = context;
    }

    @Singleton
    @Provides
    AccelerometerSensor provideAccelerometerSensor() {
        return new AccelerometerSensor(context);
    }

    @Singleton
    @Provides
    ConnectionSensor provideConnectionSensor() {
        return new ConnectionSensor(context);
    }


    @Singleton
    @Provides
    ForegroundSensor provideForegroundSensor() {
        return new ForegroundSensor(context);
    }


    @Singleton
    @Provides
    ForegroundTrafficSensor provideForegroundTrafficSensor() {
        final ForegroundTrafficSensor foregroundTrafficSensor = new ForegroundTrafficSensor(context);
        foregroundTrafficSensor.setOperationMode(ForegroundTrafficSensor.Mode.PERIODIC);
        return foregroundTrafficSensor;
    }


    @Singleton
    @Provides
    GyroscopeSensor provideGyroscopeSensor() {
        return new GyroscopeSensor(context);
    }

    @Singleton
    @Provides
    LightSensor provideLightSensor() {
        return new LightSensor(context);
    }

    @Singleton
    @Provides
    LocationSensor provideLocationSensor() {
        return new LocationSensor(context);
    }

    @Singleton
    @Provides
    MagneticFieldSensor provideMagneticFieldSensor() {
        return new MagneticFieldSensor(context);
    }

    @Singleton
    @Provides
    MotionActivitySensor provideMotionActivitySensor() {
        return new MotionActivitySensor(context);
    }

    @Singleton
    @Provides
    PowerLevelSensor providePowerLevelSensor() {
        return new PowerLevelSensor(context);
    }

    @Singleton
    @Provides
    BackgroundTrafficSensor provideBackgroundTrafficSensor() {
        return new BackgroundTrafficSensor(context);
    }

    @Singleton
    @Provides
    RingtoneSensor provideRingtoneSensor() {
        return new RingtoneSensor(context);
    }

    @Singleton
    @Provides
    LoudnessSensor provideLoudnessSensor() {
        return new LoudnessSensor(context);
    }

    @Singleton
    @Provides
    RunningProcessesReaderSensor provideRunningProcessesReaderSensor() {
        return new RunningProcessesReaderSensor(context);
    }

    @Singleton
    @Provides
    RunningTasksReaderSensor provideRunningTasksReaderSensor() {
        return new RunningTasksReaderSensor(context);
    }

    @Singleton
    @Provides
    RunningServicesReaderSensor provideRunningServicesReaderSensor() {
        return new RunningServicesReaderSensor(context);
    }

    @Singleton
    @Provides
    BrowserHistorySensor provideBrowserHistorySensor() {
        return new BrowserHistorySensor(context);
    }

    @Singleton
    @Provides
    CalendarSensor provideCalendarSensor() {
        return new CalendarSensor(context);
    }

    @Singleton
    @Provides
    ContactsSensor provideContactsSensor() {
        return new ContactsSensor(context);
    }

    @Singleton
    @Provides
    CallLogSensor provideCallLogSensor() {
        return new CallLogSensor(context);
    }

    @Singleton
    @Provides
    TucanSensor provideTucanSensor() {
        return new TucanSensor(context);
    }

    @Singleton
    @Provides
    FacebookSensor provideFacebookSensor() {
        return new FacebookSensor(context);
    }
}
