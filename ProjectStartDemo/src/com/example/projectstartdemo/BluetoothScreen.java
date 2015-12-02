package com.example.projectstartdemo;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class BluetoothScreen extends Activity {
   
   Button btnListPairedDevices, btnStartScan, btnStopScan;
   Button openButton, backButton, closeButton;
   ListView listView;
   BroadcastReceiver mReceiver;
   //BluetoothSocket mSocket;
   //BluetoothDevice mDevice;
   //OutputStream mOutputStream;
   //InputStream mInputStream;
   String deviceName, deviceAddress;
   GlobalClass globalVariable;
   
   private BluetoothAdapter myBluetoothAdapter;
   private Set<BluetoothDevice> pairedDevices;
   private ArrayAdapter<String> BTArrayAdapter;
   
   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.bluetoothscreen);
      
      globalVariable = (GlobalClass) getApplicationContext();
      
      // take an instance of BluetoothAdapter - Bluetooth radio
      myBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
      if(myBluetoothAdapter == null) {
         // TODO -To be safe from user pressing despite Toast message could use ".setEnabled(false)" on all buttons
         
         Toast.makeText(getApplicationContext(),"Your device does not support Bluetooth",
               Toast.LENGTH_LONG).show();
      } 
      openButton = (Button)findViewById(R.id.open);
      closeButton = (Button)findViewById(R.id.close);
      backButton = (Button)findViewById(R.id.back);
      btnListPairedDevices = (Button)findViewById(R.id.btnPairedDevices);
      listView             = (ListView)findViewById(R.id.listView);
      
      // Create the arrayAdapter that contains the BTDevices, and set it to the ListView.
      // It is populated when "List Paired Devices" button clicked.
      BTArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_activated_1);
      listView.setAdapter(BTArrayAdapter);
      listView.setTextFilterEnabled(true);
      listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
      listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
         
         @Override
         public void onItemClick(AdapterView<?> parent, View view,	int position, long id) {
            // TODO Auto-generated method stub
            String item = listView.getItemAtPosition(position).toString();
            String delims = "\n";
            String[] tokens = item.split(delims);
            deviceName = tokens[0];
            deviceAddress = tokens[1];
            listView.setItemChecked(position, true);
            Toast.makeText(BluetoothScreen.this, "You selected = " + deviceAddress, Toast.LENGTH_SHORT).show();
         }
      });
      //BluetoothAdapter mBluetoothAdapter;
      myBluetoothAdapter.startDiscovery(); 
      mReceiver = new BroadcastReceiver() {
         public void onReceive(Context context, Intent intent) {
             String action = intent.getAction();
   
             //Finding devices                 
             if (BluetoothDevice.ACTION_FOUND.equals(action)) 
             {
                 // Get the BluetoothDevice object from the Intent
                 BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                 // Add the name and address to an array adapter to show in a ListView
                BTArrayAdapter.add(device.getName() + "\n" + device.getAddress());
             }
           }
         };

      IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND); 
      registerReceiver(mReceiver, filter);
    //Open Button
      openButton.setOnClickListener(new View.OnClickListener()
      {
          public void onClick(View v)
          {
              try 
              {
                  if (deviceAddress == null)
                     return;
                  
                  findBT();
                  openBT();
              }
              catch (IOException ex) { }
          }
      });
      
      backButton.setOnClickListener(new View.OnClickListener()
      {
          public void onClick(View v)
          {
              
              finish();
          }
      });
      
      //Close button
      closeButton.setOnClickListener(new View.OnClickListener()
      {
          public void onClick(View v)
          {
              try 
              {
                 if (globalVariable.getBluetoothDevice() == null)
                    return;
                  closeBT();
              }
              catch (IOException ex) { }
          }
      });
   }
   
   protected void onListItemClicked(ListView l, View v, int position, long id) {
      listView.setItemChecked(position, true);
   }
   
   void findBT()
   {
       myBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
       if(myBluetoothAdapter == null)
       {
           Toast.makeText(BluetoothScreen.this, "No bluetooth adapter available", Toast.LENGTH_SHORT).show();
       }
       
       if(!myBluetoothAdapter.isEnabled())
       {
           Intent enableBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
           startActivityForResult(enableBluetooth, 0);
       }
       
       Set<BluetoothDevice> pairedDevices = myBluetoothAdapter.getBondedDevices();
       if(pairedDevices.size() > 0)
       {
           for(BluetoothDevice device : pairedDevices)
           {
               if(device.getAddress().equals(deviceAddress)) 
               {
                   globalVariable.setBluetoothDevice(device);
                   break;
               }
           }
       }
       Toast.makeText(BluetoothScreen.this, "Bluetooth Device Found", Toast.LENGTH_SHORT).show();
   }
   
   void openBT() throws IOException
   {
       UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb"); //Standard SerialPortService ID
       Toast.makeText(BluetoothScreen.this, globalVariable.getBluetoothDevice().toString(), Toast.LENGTH_SHORT).show();
       if (globalVariable.getBluetoothDevice()== null)
          return;
       globalVariable.setBluetoothSocket(globalVariable.getBluetoothDevice().createRfcommSocketToServiceRecord(uuid));
       globalVariable.getBluetoothSocket().connect();
       globalVariable.setOutputStream(globalVariable.getBluetoothSocket().getOutputStream());
       globalVariable.setInputStream(globalVariable.getBluetoothSocket().getInputStream());
      
//       Toast.makeText(BluetoothScreen.this, "Bluetooth Opened", Toast.LENGTH_SHORT).show();
   }
   
//   void sendData() throws IOException
//   {
//       String msg = "Hello Arduino!";
//       msg += "\n";
//       globalVariable.getOutputStream().write(msg.getBytes());
//       Toast.makeText(BluetoothScreen.this, "Data Sent", Toast.LENGTH_SHORT).show();
//   }
//   
   void closeBT() throws IOException
   {
       globalVariable.getOutputStream().close();
       globalVariable.getInputStream().close();
       globalVariable.getBluetoothSocket().close();
       Toast.makeText(BluetoothScreen.this, "Bluetooth Closed", Toast.LENGTH_SHORT).show();
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
