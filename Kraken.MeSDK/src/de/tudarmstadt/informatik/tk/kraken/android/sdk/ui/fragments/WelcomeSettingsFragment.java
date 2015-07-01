package de.tudarmstadt.informatik.tk.kraken.android.sdk.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.tudarmstadt.informatik.tk.kraken.sdk.R;
import de.tudarmstadt.informatik.tk.kraken.android.sdk.preference.PreferenceManager;
import de.tudarmstadt.informatik.tk.kraken.android.sdk.sensors.ESensorType;

/**
 * @author Karsten Planz
 */
@Deprecated
public class WelcomeSettingsFragment extends Fragment implements AdapterView.OnItemClickListener {

    private PreferenceManager mPrefs;
    private ListView mListView;

    private int checkedOption = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_welcome_settings, container, false);

        mPrefs = PreferenceManager.getInstance(getActivity());

        mListView = (ListView) root.findViewById(R.id.listview);

        final String[] options = getResources().getStringArray(R.array.data_profile_titles);
        final String[] descriptions = getResources().getStringArray(R.array.data_profile_descriptions);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                getActivity(),
                R.layout.data_profile_options_item,
                R.id.option_title,
                options) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                CheckedTextView title = (CheckedTextView) view.findViewById(R.id.option_title);
                TextView description = (TextView) view.findViewById(R.id.option_description);

                title.setChecked(position == checkedOption);

                /*
                String descriptionText = "";
                if(position == 0) {
                    descriptionText = getDataProfileDescription(Settings.SENSORS_PROFILE_BASIC);
                }
                else if(position == 1) {
                    descriptionText = "Basis + " + getDataProfileDescription(Settings.SENSORS_PROFILE_FULL);
                }
                */
                title.setText(options[position]);
                description.setText(descriptions[position]);
                return view;
            }
        };
        mListView.setAdapter(adapter);
        mListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        mListView.setOnItemClickListener(this);

        return root;
    }

    private String getDataProfileDescription(ESensorType[] sensors) {
        if(sensors.length == 0) {
            return "";
        }
        StringBuilder description = new StringBuilder();
        List<ESensorType> sensorsList = new ArrayList<>(Arrays.asList(sensors));
        description.append(sensorsList.remove(0).getSensorName());
        for (ESensorType sensor : sensorsList) {
            description.append(", ");
            description.append(sensor.getSensorName());
        }
        return description.toString();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        checkedOption = position;

        ArrayAdapter<String> arrayAdapter = (ArrayAdapter<String>) mListView.getAdapter();
        arrayAdapter.notifyDataSetChanged();

        if(position == 0) {
            mPrefs.setDataProfile(PreferenceManager.KRAKEN_DATA_PROFILE_BASIC);
        }
        else if(position == 1) {
            mPrefs.setDataProfile(PreferenceManager.KRAKEN_DATA_PROFILE_FULL);
        }
    }

}