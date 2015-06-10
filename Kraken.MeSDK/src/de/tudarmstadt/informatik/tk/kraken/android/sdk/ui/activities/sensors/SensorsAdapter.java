package de.tudarmstadt.informatik.tk.kraken.android.sdk.ui.activities.sensors;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ListView;

import java.util.LinkedList;
import java.util.List;

import de.tudarmstadt.informatik.tk.kraken.sdk.R;
import de.tudarmstadt.informatik.tk.kraken.android.sdk.sensors.ESensorType;
import de.tudarmstadt.informatik.tk.kraken.android.sdk.sensors.SensorManager;
import de.tudarmstadt.informatik.tk.kraken.android.sdk.sensors.interfaces.ISensor;

public class SensorsAdapter extends BaseAdapter {

	List<ISensor> m_liData = new LinkedList<ISensor>();
	private Activity m_context;
	@SuppressWarnings("unused")
	private ListView m_listView;
	private SensorManager m_sensorManager;


	public SensorsAdapter(Activity context, ListView listView, Intent intent) {
		this.m_context = context;
		this.m_listView = listView;

		m_sensorManager = SensorManager.getInstance(context);
		m_liData = m_sensorManager.getSensors();
	}

	@Override
	public int getCount() {
		return m_liData.size();
	}

	@Override
	public Object getItem(int position) {
		return m_liData.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {

		final ISensor sensor = m_liData.get(position);

		if (view == null) {
			LayoutInflater inflater = (LayoutInflater) m_context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(R.layout.sensorview, parent, false);
		}

		CheckBox chb = (CheckBox) view.findViewById(R.id.sensorview_checkbox);
		chb.setText(sensor.getSensorType().getSensorName());
		
		chb.setEnabled(!sensor.getSensorType().equals(ESensorType.MEASUREMENT_LOG));
		
		chb.setChecked(!sensor.isDisabledByUser());
		
		chb.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				sensor.setDisabledByUser(!((CheckBox) v).isChecked());
			}
		});
		
		return view;
	}

}
