package com.example.projectstartdemo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

public class ArduinoScreen extends Activity
{
   ImageView greenCheck, redX;
   ListView mCommands;
   EditText mNewCommand;
   Button mAdd, mConnect;
   List<String> mCommandList = new ArrayList<String>();
   GlobalClass globalVariable;
   
   @Override
   protected void onCreate(Bundle savedInstanceState) {
      
      super.onCreate(savedInstanceState);
      greenCheck = (ImageView)findViewById(R.id.greenCheck);
      redX = (ImageView)findViewById(R.id.redX);
      
      setContentView(R.layout.arduinoscreen);
      globalVariable = (GlobalClass) getApplicationContext();
      
      mCommandList.add("Hello Arduino!");
      
      mCommands = (ListView) findViewById(R.id.commandList);
      mNewCommand = (EditText) findViewById(R.id.etNewCommand);
      mAdd = (Button) findViewById(R.id.btnAddCommand);
      mConnect = (Button) findViewById(R.id.btnConnect);
      
      mCommands.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mCommandList));
      
      mAdd.setOnClickListener(new OnClickListener(){
         
         public void onClick(View view)
         {
            String msgText = new String(mNewCommand.getText().toString());
            if(msgText.isEmpty())
            {
               Toast.makeText(ArduinoScreen.this, "Please enter text to send to Arduino, then press Add button", Toast.LENGTH_SHORT).show();
            }
            else
            {
               mCommandList.add(msgText);
            }
            mNewCommand.setText(msgText);
         }
      });
      
      mCommands.setOnItemClickListener(new AdapterView.OnItemClickListener() {
         
         public void onItemClick(AdapterView<?> parent, View view,   int position, long id) {
            String command = mCommands.getItemAtPosition(position).toString();
            if (globalVariable.getOutputStream()==null)
            {
               return;
            }
            try
            {
               sendData(command);
            } catch (IOException e)
            {
               // TODO Auto-generated catch block
               e.printStackTrace();
            }
            Toast.makeText(ArduinoScreen.this, "Command Sent : " + command, Toast.LENGTH_SHORT).show();
         }
      });
   }
   
   void sendData(String command) throws IOException
   {
      
      command += "\n";
      globalVariable.getOutputStream().write(command.getBytes());
      Toast.makeText(ArduinoScreen.this, "Data Sent", Toast.LENGTH_SHORT).show();
   }
   
   public void openConnect(View view)
   {
      Intent intent = new Intent(ArduinoScreen.this, BluetoothScreen.class);
      startActivity(intent);
   }
   
   //method tocheck if connected or not
   @Override
   protected void onActivityResult(int requestcode, int resultCode, Intent data)
   {
      Bundle extra = getIntent().getExtras();
      if(extra.getBoolean("connected"))//get the connectedFlag boolean
      {//if true
         greenCheck.setVisibility(View.VISIBLE);//set greencheck to visible
         redX.setVisibility(View.INVISIBLE);//red x to invisible
         
         //String resultStr = data.getStringExtra("result");//get the string
         //tvDisplay.setText(resultStr + "! thank you for playing the game.");//set the string!
      }
      else 
      {
         greenCheck.setVisibility(View.INVISIBLE);//set green to invisible
         redX.setVisibility(View.VISIBLE);//set redX to visible
      }
         //tvDisplay.setText("");
      
   }
}
