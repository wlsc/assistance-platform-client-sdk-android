package de.tudarmstadt.informatik.tk.assistance.sdk.dagger.component;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Component;
import de.tudarmstadt.informatik.tk.assistance.sdk.dagger.module.ContextModule;
import de.tudarmstadt.informatik.tk.assistance.sdk.dagger.module.DbModule;
import de.tudarmstadt.informatik.tk.assistance.sdk.dagger.module.SensorModule;
import de.tudarmstadt.informatik.tk.assistance.sdk.provider.DaoProvider;
import de.tudarmstadt.informatik.tk.assistance.sdk.provider.SensorProvider;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 27/10/2016
 */
@Singleton
@Component(
        modules = {
                ContextModule.class,
                SensorModule.class,
                DbModule.class
        })
public interface SdkComponent {
    enum Initializer {
        INSTANCE;

        public SdkComponent init(Context context) {

            final Context applicationContext = context.getApplicationContext();

            return DaggerSdkComponent
                    .builder()
                    .contextModule(new ContextModule(applicationContext))
                    .sensorModule(new SensorModule(applicationContext))
                    .dbModule(new DbModule(applicationContext))
                    .build();
        }
    }

    void inject(DaoProvider daoProvider);

    void inject(SensorProvider sensorProvider);
}
