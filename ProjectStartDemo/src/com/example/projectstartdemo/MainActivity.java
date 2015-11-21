package com.example.projectstartdemo;

import java.util.ArrayList;
import java.util.Set;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends Activity {

	Button btnListPairedDevices;
	Button btnStartScan;
	Button btnStopScan;
	ListView listView;
	
	private BluetoothAdapter myBluetoothAdapter;
	private Set<BluetoothDevice> pairedDevices;
	private ArrayAdapter<String> BTArrayAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		// take an instance of BluetoothAdapter - Bluetooth radio
	      myBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
	      if(myBluetoothAdapter == null) {
	    	  // TODO -To be safe from user pressing despite Toast message could use ".setEnabled(false)" on all buttons
	    	  
	    	  Toast.makeText(getApplicationContext(),"Your device does not support Bluetooth",
	         		 Toast.LENGTH_LONG).show();
	      } 
		
		btnListPairedDevices = (Button)findViewById(R.id.btnPairedDevices);
		listView             = (ListView)findViewById(R.id.listView);
		
		// Create the arrayAdapter that contains the BTDevices, and set it to the ListView.
		// It is populated when "List Paired Devices" button clicked.
		BTArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
	    listView.setAdapter(BTArrayAdapter);
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,	int position, long id) {
				// TODO Auto-generated method stub
				String item = listView.getItemAtPosition(position).toString();
				Toast.makeText(MainActivity.this, "Your clicked " + item, Toast.LENGTH_SHORT).show();
			}
		});
	}
	
	public void onListPairedDevicesClicked(View view) {
		// get paired devices
		pairedDevices = myBluetoothAdapter.getBondedDevices();

		// put it's one to the adapter
		for(BluetoothDevice device : pairedDevices)
			BTArrayAdapter.add(device.getName()+ "\n" + device.getAddress());

		Toast.makeText(getApplicationContext(),"Show Paired Devices",
				Toast.LENGTH_SHORT).show();	      
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
