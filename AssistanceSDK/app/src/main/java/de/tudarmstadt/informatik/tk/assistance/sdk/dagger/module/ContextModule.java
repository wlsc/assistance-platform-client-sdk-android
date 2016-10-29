package de.tudarmstadt.informatik.tk.assistance.sdk.dagger.module;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 15/10/2016
 */
@Module
public class ContextModule {

    private Context context;

    public ContextModule(Context context) {
        this.context = context;
    }

    @Singleton
    @Provides
    Context provideContext() {
        return context;
    }
}
