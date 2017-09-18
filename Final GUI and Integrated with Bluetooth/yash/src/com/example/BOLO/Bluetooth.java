package com.example.BOLO;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.net.SocketTimeoutException;
import java.util.UUID;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;


public class Bluetooth {
    private static final String BTAG = "Destroy";
	Thread bThread;
	 private static Bluetooth instance;

	BluetoothSocket bsocket;
    InputStream bis = null; //Bluetooth input stream
    OutputStream bos = null; //Bluetooth output stream
    private String MACAddress = "20:13:01:21:03:53";
    private BluetoothAdapter mBluetoothAdapter = null;
    private BluetoothDevice btdevice = null;
    Activity parent;
    String TAG="Bluetooth";
    public static void initInstance()
      {
        if (instance == null)
        {
          // Create the instance
          instance = new Bluetooth();
        }
      }
    public static Bluetooth getInstance()
      {
        // Return the instance
        return instance;
      }

    private Bluetooth()
    {
    mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    if (!mBluetoothAdapter.isEnabled()) {
        
    }
    else {

        Log.i(TAG, "Connecting to Device: " + MACAddress);
        btdevice = mBluetoothAdapter.getRemoteDevice(MACAddress);
        Log.i(TAG, "Device: " + btdevice.getName());
        Log.i(TAG, "Trying to Connect...");
        Log.i(TAG, "Starting Thread");
        try {
        	
            bThread = new Thread(new BluetoothClient(btdevice, true));
            bThread.start();
        } catch (IOException e) {
            Log.e(TAG, "Could not create thread for bluetooth: " + e);
        }
    }


    }
    public class BluetoothClient implements Runnable {
        
    	public BluetoothClient(BluetoothDevice device, boolean IsAnHTCDevice) throws IOException {
        	if (IsAnHTCDevice) {
                //This is a workaround for HTC devices, but it likes to throw an IOException "Connection timed out"
                try {
                    Method m = device.getClass().getMethod("createRfcommSocket", new Class[] {int.class});
                    bsocket = (BluetoothSocket) m.invoke(device, Integer.valueOf(1));
                } catch (Exception e) {
                    Log.e(TAG, "Error at HTC/createRfcommSocket: " + e);
                    e.printStackTrace();
                         }
            } else {
                //This is the normal method, but on a Nexus One it almost always throws an IOException "Service discovery failed" message
                try {
                    UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
                    bsocket = device.createRfcommSocketToServiceRecord(MY_UUID);
                } catch (Exception e) {
                    Log.e(TAG, "Error at createRfcommSocketToServiceRecord: " + e);
                    e.printStackTrace();
                      }
            }
        }

        public void run() {
            try {
                Log.i(TAG, "Cancelling Discovery");
                mBluetoothAdapter.cancelDiscovery();
                
                Log.i(TAG, "Connecting to Socket");
                
                bsocket.connect();
                Log.i(TAG, "Connected");
				//  Toast.makeText(parent,"Connected..",
			//				Toast.LENGTH_SHORT).show();
                bis = bsocket.getInputStream();
                bos = bsocket.getOutputStream();
                Log.i(TAG, "Socket created, streams assigned");
                Log.i(TAG, "Waiting for data...");
                
                /*byte[] buffer = new byte[4096];
                int read = bis.read(buffer, 0, 4096); // This is blocking
                Log.i(TAG, "Getting data...");
                while (read != -1) {
                    byte[] tempdata = new byte[read];
                    System.arraycopy(buffer, 0, tempdata, 0, read);
                    read = bis.read(buffer, 0, 4096); // This is blocking
                }*/
            } catch (SocketTimeoutException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                Log.i(TAG, "Finished");
                }
        }
    }

public void SendDataToBluetooth(int cmd) { // You run this from the main thread.
    	
    	try {
    	    if (bsocket != null) {
                bos.write((byte)cmd);
            }
        } catch (Exception e) {
            Log.e("SendDataToBluetooth", "Message send failed. Caught an exception: " + e);
        }
    }
public void SendDataToBluetooth(String cmd) { // You run this from the main thread.
	
	try {
        if (bsocket != null) {
        	for(int i=0;i<cmd.length();i++)
        		if(cmd.charAt(i)>='0' && cmd.charAt(i)<='9')
        			bos.write(cmd.charAt(i)-'0');
        		else
        			bos.write(cmd.charAt(i));
        }
    } catch (Exception e) {
        Log.e("SendDataToBluetooth", "Message send failed. Caught an exception: " + e);
    }
}
public void finalize()
{
	onDestroy();
}
protected void sendDestroy()
{
	SendDataToBluetooth(2);	
}
    protected void onDestroy() {
        
        if (bThread != null) { // If the thread is currently running, close the socket and interrupt it.
            Log.i(BTAG, "Killing BT Thread");
            try {
            	sendDestroy();
                bis.close();
                bos.close();
                
                bsocket.close();
                
                bsocket = null;
            } catch (IOException e) {
                Log.e(BTAG, "IOException");
                e.printStackTrace();
            } catch (Exception e) {
                Log.e(BTAG, "Exception");
                e.printStackTrace();
            }
            try {
                Thread moribund = bThread;
                bThread = null;
                moribund.interrupt();
            } catch (Exception e) {}
            Log.i(BTAG, "BT Thread Killed");
        }
    }        

}
