package org.opencv.javacv.facerecognition;
 
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.Locale;
import java.util.UUID;

import android.speech.tts.TextToSpeech;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.IntentSender.SendIntentException;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.Button;
import android.widget.Toast;
  
public class sending extends Activity  {
  public static final String TAG = "bluetooth";
    
  //Button btnOn, btnOff;
    
  public BluetoothAdapter btAdapter = null;
  public BluetoothSocket btSocket = null;
  public static OutputStream outStream = null;
  public InputStream inStream=null;
  
  // SPP UUID service
  public static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
 // String uniqueid=UUID.randomUUID().toString();
  //private final UUID MY_UUID=UUID.fromString(uniqueid);
  // MAC-address of Bluetooth module (you must edit this line)
// private static final UUID MY_UUID=UUID.randomUUID();
  
  //MAC ID
  public static String address = "00:14:01:21:25:53";
 
  TextToSpeech ttobj;
  /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  
  //  setContentView(R.layout.fragment_main);
    
  //  btnOn = (Button) findViewById(R.id.on);
  //  btnOff = (Button) findViewById(R.id.off);
    
    btAdapter = BluetoothAdapter.getDefaultAdapter();
    if(!btAdapter.isEnabled())
    	btAdapter.enable();
    checkBTState();
    //s=this;
  
    
    
    
    /*if(!btAdapter.isEnabled())
    {
    	ttobj.speak("Please turn on the bluetooth", TextToSpeech.QUEUE_FLUSH, null);
    	finish();
    }*/
 /*  btnOn.setOnLongClickListener(new OnLongClickListener() {
	
	//@Override
	public boolean onLongClick(View v) {
		// TODO Auto-generated method stub
		sendData("1");
		ttobj.speak("LED Turned ON", TextToSpeech.QUEUE_FLUSH,null);
       // Toast.makeText(getBaseContext(), "Turn on LED", Toast.LENGTH_SHORT).show();
		return true;
	}
});
    btnOn.setOnClickListener(new OnClickListener() {
      public void onClick(View v) {
      ttobj.speak("LED ON", TextToSpeech.QUEUE_FLUSH, null);
        //sendData("1");
      //  Toast.makeText(getBaseContext(), "Turn on LED", Toast.LENGTH_SHORT).show();
      }
    });
    
    btnOff.setOnLongClickListener(new OnLongClickListener() {
		
		@Override
		public boolean onLongClick(View v) {
			// TODO Auto-generated method stub
			 sendData("0");
			 ttobj.speak("LED Turned OFF", TextToSpeech.QUEUE_FLUSH,null);
		    // Toast.makeText(getBaseContext(), "Turn off LED", Toast.LENGTH_SHORT).show();
			return true;
		}
	});
  
    btnOff.setOnClickListener(new OnClickListener() {
      public void onClick(View v) {
    	ttobj.speak("LED OFF", TextToSpeech.QUEUE_FLUSH, null);
     /*   sendData("0");
        Toast.makeText(getBaseContext(), "Turn off LED", Toast.LENGTH_SHORT).show();
      }
    });*/
  }
  
 /* public static sending getInstance()
  {
	  return s;
  }*/
   
  public BluetoothSocket createBluetoothSocket(BluetoothDevice device) throws IOException {
      if(Build.VERSION.SDK_INT >= 10){
          try {
              final Method  m = device.getClass().getMethod("createInsecureRfcommSocketToServiceRecord", new Class[] { UUID.class });
              return (BluetoothSocket) m.invoke(device, MY_UUID);
          } catch (Exception e) {
              Log.e(TAG, "Could not create Insecure RFComm Connection",e);
          }
      }
      return  device.createRfcommSocketToServiceRecord(MY_UUID);
  }
    
  @Override
  public void onResume() {
	 
    super.onResume();
    ttobj=new TextToSpeech(getApplicationContext(), 
  	      new TextToSpeech.OnInitListener() {
  	      @Override
  	      public void onInit(int status) {
  	         if(status != TextToSpeech.ERROR){
  	             ttobj.setLanguage(Locale.UK);
  	            }				
  	         }
  	      });
  
    Log.d(TAG, "...onResume - try connect...");
    
    // Set up a pointer to the remote node using it's address.
    BluetoothDevice device = btAdapter.getRemoteDevice(address);
    
    // Two things are needed to make a connection:
    //   A MAC address, which we got above.
    //   A Service ID or UUID.  In this case we are using the
    //     UUID for SPP.
    
    try {
        btSocket = createBluetoothSocket(device);
    } catch (IOException e1) {
        errorExit("Fatal Error", "In onResume() and socket create failed: " + e1.getMessage() + ".");
    }
        
    // Discovery is resource intensive.  Make sure it isn't going on
    // when you attempt to connect and pass your message.
    btAdapter.cancelDiscovery();
    
    // Establish the connection.  This will block until it connects.
    Log.d(TAG, "...Connecting...");
    try {
      btSocket.connect();
      Log.d(TAG, "...Connection ok...");
    } catch (IOException e) {
      try {
        btSocket.close();
      } catch (IOException e2) {
        errorExit("Fatal Error", "In onResume() and unable to close socket during connection failure" + e2.getMessage() + ".");
      }
    }
      
    // Create a data stream so we can talk to server.
    Log.d(TAG, "...Create Socket...");
  
    try {
      outStream = btSocket.getOutputStream();
      inStream=btSocket.getInputStream();
    } catch (IOException e) {
      errorExit("Fatal Error", "In onResume() and output stream creation failed:" + e.getMessage() + ".");
    }
  }
  
@Override
public void onDestroy()
{
	super.onDestroy();
	ttobj.shutdown();
}
  
  
  @Override
  public void onPause() {
	  ttobj.speak("PAUSED", TextToSpeech.QUEUE_FLUSH,null);
	    if(ttobj !=null){
	        ttobj.stop();
	        ttobj.shutdown();
	     }
    super.onPause();
   
  
    Log.d(TAG, "...In onPause()...");
  
    if (outStream != null) {
      try {
        outStream.flush();
      } catch (IOException e) {
        errorExit("Fatal Error", "In onPause() and failed to flush output stream: " + e.getMessage() + ".");
      }
    }
  
    try     {
      btSocket.close();
    } catch (IOException e2) {
      errorExit("Fatal Error", "In onPause() and failed to close socket." + e2.getMessage() + ".");
    }
  }
    
  public void checkBTState() {
    // Check for Bluetooth support and then check to make sure it is turned on
    // Emulator doesn't support Bluetooth and will return null
    if(btAdapter==null) {
      errorExit("Fatal Error", "Bluetooth not support");
    } else {
      if (btAdapter.isEnabled()) {
        Log.d(TAG, "...Bluetooth ON...");
      } else {
    	  
        //Prompt user to turn on Bluetooth
       // Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        // startActivityForResult(enableBtIntent, 1);
      }
    }
  }
  
  public void errorExit(String title, String message){
    Toast.makeText(getBaseContext(), title + " - " + message, Toast.LENGTH_LONG).show();
    finish();
  }
  
  public void sendData(String message) {
	 final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
	   String address = "00:14:01:21:25:53";
	  Toast.makeText(getApplicationContext(), message, 0).show();
	  btAdapter = BluetoothAdapter.getDefaultAdapter();
	  BluetoothDevice device = btAdapter.getRemoteDevice(address);
	  try {
	        btSocket = createBluetoothSocket(device);
	    } catch (IOException e1) {
	        errorExit("Fatal Error", "In onResume() and socket create failed: " + e1.getMessage() + ".");
	    }
	  try {
	      outStream = btSocket.getOutputStream();
	     
	    } catch (IOException e) {
	      errorExit("Fatal Error", "In onResume() and output stream creation failed:" + e.getMessage() + ".");
	    }
    byte[] msgBuffer = message.getBytes();
  
    Log.d(TAG, "...Send data: " + message + "...");
  
    try {
    	Toast.makeText(getApplicationContext(), message, 0).show();
      outStream.write(msgBuffer);
    } catch (IOException e) {
      String msg = "In onResume() and an exception occurred during write: " + e.getMessage();
      if (address.equals("00:00:00:00:00:00"))
        msg = msg + ".\n\nUpdate your server address from 00:00:00:00:00:00 to the correct address on line 35 in the java code";
        msg = msg +  ".\n\nCheck that the SPP UUID: " + MY_UUID.toString() + " exists on server.\n\n";
        
      //  errorExit("Fatal Error", msg);      
    }
  }
}