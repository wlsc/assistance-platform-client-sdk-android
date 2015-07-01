package de.tudarmstadt.informatik.tk.kraken.android.sdk.ui.activities.sensors;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;
import de.tudarmstadt.informatik.tk.kraken.sdk.R;

@Deprecated
public class SensorsActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sensors_activity);

		final ListView listview = (ListView) findViewById(R.id.sensors_listview);

		SensorsAdapter adapter = new SensorsAdapter(this, listview, getIntent());
		listview.setAdapter(adapter);

	}

}
