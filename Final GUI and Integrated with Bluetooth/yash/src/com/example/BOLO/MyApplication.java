package com.example.BOLO;

import android.app.Application;
import android.bluetooth.BluetoothAdapter;

public class MyApplication extends Application
{
	@Override
	public void onCreate()
	{
		super.onCreate();

		// Initialize the singletons so their instances
		// are bound to the application process.
	    
	    if (!BluetoothAdapter.getDefaultAdapter().isEnabled()) {
//	        Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
	      BluetoothAdapter.getDefaultAdapter().enable();
	    }
		initSingletons();
	}

	protected void initSingletons()
	{
		// Initialize the instance of MySingleton
		Bluetooth.initInstance();
	}

	public void customAppMethod()
	{
		// Custom application method
	}
	public void finalize()
	{
		
	}
}