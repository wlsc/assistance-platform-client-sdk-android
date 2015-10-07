package de.tudarmstadt.informatik.tk.android.kraken.ui.activities;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceCategory;
import android.preference.SwitchPreference;
import android.view.MenuItem;

import de.tudarmstadt.informatik.tk.android.kraken.PreferenceManager;
import de.tudarmstadt.informatik.tk.android.kraken.R;
import de.tudarmstadt.informatik.tk.android.kraken.ServiceManager;
import de.tudarmstadt.informatik.tk.android.kraken.Settings;
import de.tudarmstadt.informatik.tk.android.kraken.model.db.sensors.ESensorType;
import de.tudarmstadt.informatik.tk.android.kraken.model.db.sensors.SensorManager;


@Deprecated
public class SettingsActivity extends PreferenceActivity implements Preference.OnPreferenceChangeListener {


    private static final String KRAKEN_SENSOR_ENABLED_PREFIX = "KrakenSensorEnabled_";

    private ServiceManager mServiceManager;
    private PreferenceManager mPreferenceManager;
    private SensorManager mSensorManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getActionBar().setDisplayHomeAsUpEnabled(true);

        mPreferenceManager = PreferenceManager.getInstance(this);
        mServiceManager = ServiceManager.getInstance(getApplicationContext());
        mSensorManager = SensorManager.getInstance(this);

        addPreferencesFromResource(R.xml.preferences);

        boolean serviceRunning = mServiceManager.isServiceRunning();
        SwitchPreference krakenActivatedPref = (SwitchPreference) findPreference(PreferenceManager.KRAKEN_ACTIVATED);
        krakenActivatedPref.setChecked(serviceRunning);

        PreferenceCategory dataCategory = (PreferenceCategory) findPreference("pref_key_data");

        /*
        for(ESensorType sensorType : Settings.SENSORS_PROFILE_BASIC) {
            SwitchPreference pref = new SwitchPreference(this);
            pref.setKey(KRAKEN_SENSOR_ENABLED_PREFIX + sensorType.toString());
            pref.setTitle(sensorType.getSensorName());
            pref.setOnPreferenceChangeListener(this);
            dataCategory.addPreference(pref);
        }
        */

        for (ESensorType sensorType : Settings.SENSORS_PROFILE_FULL) {
            if (sensorType != ESensorType.SENSOR_BACKGROUND_TRAFFIC) {
                SwitchPreference pref = new SwitchPreference(this);
                pref.setKey(KRAKEN_SENSOR_ENABLED_PREFIX + sensorType.toString());
                pref.setTitle(sensorType.getDisplayName(this));
                pref.setOnPreferenceChangeListener(this);
                dataCategory.addPreference(pref);
            }
        }

        updateSensorPrefs(mPreferenceManager.getDataProfile());

        findPreference(PreferenceManager.KRAKEN_ACTIVATED).setOnPreferenceChangeListener(this);
        findPreference(PreferenceManager.KRAKEN_SHOW_NOTIFICATION).setOnPreferenceChangeListener(this);
        findPreference(PreferenceManager.KRAKEN_DATA_PROFILE).setOnPreferenceChangeListener(this);

    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        String key = preference.getKey();
        if (key.equals(PreferenceManager.KRAKEN_ACTIVATED)) {
            boolean activated = (boolean) newValue;
            if (activated) {
                mServiceManager.startKrakenService();
            } else {
                mServiceManager.stopKrakenService();
            }
        } else if (key.equals(PreferenceManager.KRAKEN_SHOW_NOTIFICATION)) {
            mServiceManager.showIcon((boolean) newValue);
        } else if (key.equals(PreferenceManager.KRAKEN_DATA_PROFILE)) {
            String profile = (String) newValue;
//            KrakenUtils.initDataProfile(this, profile);
            updateSensorPrefs(profile);
        } else if (key.startsWith(KRAKEN_SENSOR_ENABLED_PREFIX)) {
            boolean disabled = !(boolean) newValue;
            ESensorType sensorType = ESensorType.valueOf(key.replaceFirst(KRAKEN_SENSOR_ENABLED_PREFIX, ""));
            mSensorManager.getSensor(sensorType).setDisabledByUser(disabled);
            if (!disabled) {
                mSensorManager.getSensor(sensorType).startSensor();
                //start background traffic sensor if data usage slider go on and start location sensor, too
                if (sensorType == ESensorType.SENSOR_NETWORK_TRAFFIC) {
                    mSensorManager.getSensor(ESensorType.SENSOR_BACKGROUND_TRAFFIC).startSensor();
                    mSensorManager.getSensor(ESensorType.SENSOR_LOCATION).startSensor();
                    SwitchPreference switchPreference = (SwitchPreference) findPreference(KRAKEN_SENSOR_ENABLED_PREFIX + ESensorType.SENSOR_LOCATION.toString());
                    switchPreference.setChecked(true);
                }
            } else {
                mSensorManager.getSensor(sensorType).stopSensor();
                //stop background traffic sensor if data usage slider go off and stop location sensor, too
                if (sensorType == ESensorType.SENSOR_NETWORK_TRAFFIC) {
                    mSensorManager.getSensor(ESensorType.SENSOR_BACKGROUND_TRAFFIC).stopSensor();
                    mSensorManager.getSensor(ESensorType.SENSOR_NETWORK_TRAFFIC).stopSensor();
                    SwitchPreference switchPreference = (SwitchPreference) findPreference(KRAKEN_SENSOR_ENABLED_PREFIX + ESensorType.SENSOR_LOCATION.toString());
                    switchPreference.setChecked(false);
                }
            }
        }
        return true;
    }

    private void updateSensorPrefs(String profile) {
        boolean basic = profile.equals(PreferenceManager.KRAKEN_DATA_PROFILE_BASIC);
        for (ESensorType sensorType : Settings.SENSORS_PROFILE_FULL) {
            if (sensorType != ESensorType.SENSOR_BACKGROUND_TRAFFIC) {
                SwitchPreference preference = (SwitchPreference) findPreference(KRAKEN_SENSOR_ENABLED_PREFIX + sensorType.toString());
                preference.setEnabled(basic);
                if (!basic) {
                    preference.setChecked(true);
                }
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
