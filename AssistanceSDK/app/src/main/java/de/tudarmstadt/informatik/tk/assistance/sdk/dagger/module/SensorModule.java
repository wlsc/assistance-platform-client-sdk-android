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
@Module(includes = ContextModule.class)
public class SensorModule {

    @Singleton
    @Provides
    AccelerometerSensor provideAccelerometerSensor(Context context) {
        return new AccelerometerSensor(context);
    }

    @Singleton
    @Provides
    ConnectionSensor provideConnectionSensor(Context context) {
        return new ConnectionSensor(context);
    }


    @Singleton
    @Provides
    ForegroundSensor provideForegroundSensor(Context context) {
        return new ForegroundSensor(context);
    }


    @Singleton
    @Provides
    ForegroundTrafficSensor provideForegroundTrafficSensor(Context context) {
        final ForegroundTrafficSensor foregroundTrafficSensor = new ForegroundTrafficSensor(context);
        foregroundTrafficSensor.setOperationMode(ForegroundTrafficSensor.Mode.PERIODIC);
        return foregroundTrafficSensor;
    }


    @Singleton
    @Provides
    GyroscopeSensor provideGyroscopeSensor(Context context) {
        return new GyroscopeSensor(context);
    }

    @Singleton
    @Provides
    LightSensor provideLightSensor(Context context) {
        return new LightSensor(context);
    }

    @Singleton
    @Provides
    LocationSensor provideLocationSensor(Context context) {
        return new LocationSensor(context);
    }

    @Singleton
    @Provides
    MagneticFieldSensor provideMagneticFieldSensor(Context context) {
        return new MagneticFieldSensor(context);
    }

    @Singleton
    @Provides
    MotionActivitySensor provideMotionActivitySensor(Context context) {
        return new MotionActivitySensor(context);
    }

    @Singleton
    @Provides
    PowerLevelSensor providePowerLevelSensor(Context context) {
        return new PowerLevelSensor(context);
    }

    @Singleton
    @Provides
    BackgroundTrafficSensor provideBackgroundTrafficSensor(Context context) {
        return new BackgroundTrafficSensor(context);
    }

    @Singleton
    @Provides
    RingtoneSensor provideRingtoneSensor(Context context) {
        return new RingtoneSensor(context);
    }

    @Singleton
    @Provides
    LoudnessSensor provideLoudnessSensor(Context context) {
        return new LoudnessSensor(context);
    }

    @Singleton
    @Provides
    RunningProcessesReaderSensor provideRunningProcessesReaderSensor(Context context) {
        return new RunningProcessesReaderSensor(context);
    }

    @Singleton
    @Provides
    RunningTasksReaderSensor provideRunningTasksReaderSensor(Context context) {
        return new RunningTasksReaderSensor(context);
    }

    @Singleton
    @Provides
    RunningServicesReaderSensor provideRunningServicesReaderSensor(Context context) {
        return new RunningServicesReaderSensor(context);
    }

    @Singleton
    @Provides
    BrowserHistorySensor provideBrowserHistorySensor(Context context) {
        return new BrowserHistorySensor(context);
    }

    @Singleton
    @Provides
    CalendarSensor provideCalendarSensor(Context context) {
        return new CalendarSensor(context);
    }

    @Singleton
    @Provides
    ContactsSensor provideContactsSensor(Context context) {
        return new ContactsSensor(context);
    }

    @Singleton
    @Provides
    CallLogSensor provideCallLogSensor(Context context) {
        return new CallLogSensor(context);
    }

    @Singleton
    @Provides
    TucanSensor provideTucanSensor(Context context) {
        return new TucanSensor(context);
    }

    @Singleton
    @Provides
    FacebookSensor provideFacebookSensor(Context context) {
        return new FacebookSensor(context);
    }
}
