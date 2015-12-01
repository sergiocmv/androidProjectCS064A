package com.example.projectstartdemo;

import java.io.InputStream;
import java.io.OutputStream;

import android.app.Application;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;

public class GlobalClass extends Application
{
   //BroadcastReceiver mReceiver;
   BluetoothSocket mSocket;
   BluetoothDevice mDevice;
   OutputStream mOutputStream;
   InputStream mInputStream;
   
   //public BroadcastReceiver getBroadcastReceiver(){
   //   return mReceiver;
   //}
   
   public BluetoothSocket getBluetoothSocket(){
      return mSocket;
   }

   public BluetoothDevice getBluetoothDevice(){
      return mDevice;
   }
   
   public OutputStream getOutputStream(){
      return mOutputStream;
   }
   
   public InputStream getInputStream(){
      return mInputStream;
   }
   
//   public void setBroadcastReceiver(BroadcastReceiver aReceiver){
//      mReceiver = aReceiver;
//   }
   
   public void setBluetoothSocket(BluetoothSocket aSocket){
      mSocket = aSocket;
   }
   
   public void setBluetoothDevice(BluetoothDevice aDevice){
      mDevice = aDevice;
   }
   
   public void setOutputStream(OutputStream aOutputStream){
      mOutputStream = aOutputStream;
   }
   
   public void setInputStream(InputStream aInputStream){
      mInputStream = aInputStream;
   }
}
