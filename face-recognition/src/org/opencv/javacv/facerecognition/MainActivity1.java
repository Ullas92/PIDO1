package org.opencv.javacv.facerecognition;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.util.UUID;

import android.provider.MediaStore;  


import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
  
public class MainActivity1 extends Activity {
  private static final String TAG = "bluetooth2";
    
  Button btnOn, btnOff;
  TextView txtArduino;
  Handler h;
  TextView ms;
  private static OutputStream outStream = null;
  final int RECIEVE_MESSAGE = 1;        // Status  for Handler
  private BluetoothAdapter btAdapter = null;
  private BluetoothSocket btSocket = null;
  private StringBuilder sb = new StringBuilder();
  
  private ConnectedThread mConnectedThread;
    
  // SPP UUID service
  private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
  
  // MAC-address of Bluetooth module (you must edit this line)
  private static String address = "00:14:01:21:25:53";
    int k=1;
    String kk="1111";
    int t=0;
    int count=0;
  /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  
    setContentView(R.layout.fragment_main1);
   // ms=(TextView)findViewById(R.id.msg);
  /*  Intent t=new Intent(MainActivity1.this,MainActivity.class);
    startActivityForResult(t, 1);*/
                  // button LED OFF
  //  txtArduino = (TextView) findViewById(R.id.txtArduino);      // for display the received data from the Arduino
   
    
   
    
    
    
    
    h = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
            case RECIEVE_MESSAGE: 
            	
                byte[] readBuf = (byte[]) msg.obj;
               // String sbprint=new String(readBuf);
                String strIncom = new String(readBuf, 0, msg.arg1);                 // create string from bytes array
                sb.append(strIncom);                                                // append string
                int endOfLineIndex = sb.indexOf("\n");
                
                // determine the end-of-line
                if (endOfLineIndex > 0) {                                            // if end-of-line,
                    String sbprint = sb.substring(0, endOfLineIndex);               // extract string
                   // sb.delete(0, sb.length()); 
                    //Toast.makeText(getApplicationContext(), "Run",0).show();
                    if(sbprint.length()>0)
                    {
                    //ms.setText(sbprint);
                    //Toast.makeText(getApplicationContext(), sbprint,0).show();
                    	if(count==0)
                    	{
                    		count=2;
                    	//	h.removeCallbacksAndMessages(null);
                    	Intent i=new Intent(MainActivity1.this,FdActivity.class);
                    	startActivityForResult(i, 1);
                    	//startActivity(i);
                    	}
                    	
                   /* Intent t=new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
    				startActivity(t);*/
    				/*Intent i =new Intent();
    			
    				i.setClassName("com.android.calculator2","com.android.calculator.Calculator");
    				startActivity(i);*/
                    
                    }
                    else
                    {//ms.setText(sbprint);
                   // Toast.makeText(getApplicationContext(), sbprint+"WWW", 1).show();
                    }
                    //	Toast.makeText(getApplicationContext(), "hi",0).show();
                    // and clear
                 //   txtArduino.setText("Data from Arduino: " + sbprint);            // update TextView
                  //  btnOff.setEnabled(true);
                  //  btnOn.setEnabled(true);
                
                //Log.d(TAG, "...String:"+ sb.toString() +  "Byte:" + msg.arg1 + "...");
                break;
            }
            }
        };
    };

    btAdapter = BluetoothAdapter.getDefaultAdapter();       // get Bluetooth adapter
    checkBTState();
  
   /* btnOn.setOnClickListener(new OnClickListener() {
      public void onClick(View v) {
        btnOn.setEnabled(false);
        mConnectedThread.write("1");    // Send "1" via Bluetooth
        //Toast.makeText(getBaseContext(), "Turn on LED", Toast.LENGTH_SHORT).show();
      }
    });*/
  
    /*btnOff.setOnClickListener(new OnClickListener() {
      public void onClick(View v) {
        btnOff.setEnabled(false); 
        mConnectedThread.write("0");    // Send "0" via Bluetooth
        //Toast.makeText(getBaseContext(), "Turn off LED", Toast.LENGTH_SHORT).show();
      }
    });*/
  }
  
  @Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
	 // Toast.makeText(getApplicationContext(), "Hi", 0).show();
	  count=0;
	  String message="1";
	 // String message1="0";
		if(requestCode==1&&resultCode==RESULT_OK)
		{
			
			kk=data.getStringExtra("n");
			
			
	//	Toast.makeText(getApplicationContext(), kk, 0).show();
		
		if(kk.equalsIgnoreCase("f_au"))
		{
			byte[] msgBuffer = message.getBytes();
						
			try {
				
			      outStream.write(msgBuffer);
			     
			      t=1;
			   
			    } catch (IOException e) {
			      String msg = "In onResume() and an exception occurred during write: " + e.getMessage();
			      if (address.equals("00:00:00:00:00:00"))
			        msg = msg + ".\n\nUpdate your server address from 00:00:00:00:00:00 to the correct address on line 35 in the java code";
			        msg = msg +  ".\n\nCheck that the SPP UUID: " + MY_UUID.toString() + " exists on server.\n\n";
				}
			
			
			//Toast.makeText(getApplicationContext(), "Close", 0).show();
			try {
				outStream.close();
				//btSocket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
				
			
		}
		else if(kk.equalsIgnoreCase("p_au"))
		{
			try {
				outStream.close();
				//btSocket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		final Handler handler = new Handler();
	    handler.postDelayed(new Runnable() {
	        @Override
	        public void run() {
	        	
	        	   BluetoothDevice device = btAdapter.getRemoteDevice(address);
	        	    
	        	    // Two things are needed to make a connection:
	        	    //   A MAC address, which we got above.
	        	    //   A Service ID or UUID.  In this case we are using the
	        	    //     UUID for SPP.
	        	     
	        	    try {
	        	        btSocket = createBluetoothSocket(device);
	        	    } catch (IOException e) {
	        	        errorExit("Fatal Error", "In onResume() and socket create failed: " + e.getMessage() + ".");
	        	    }
	        	    
	        	    // Discovery is resource intensive.  Make sure it isn't going on
	        	    // when you attempt to connect and pass your message.
	        	    btAdapter.cancelDiscovery();
	        	    
	        	    // Establish the connection.  This will block until it connects.
	        	    Log.d(TAG, "...Connecting...");
	        	    try {
	        	      btSocket.connect();
	        	      Log.d(TAG, "....Connection ok...");
	        	    } catch (IOException e) {
	        	      try {
	        	        btSocket.close();
	        	      } catch (IOException e2) {
	        	        errorExit("Fatal Error", "In onResume() and unable to close socket during connection failure" + e2.getMessage() + ".");
	        	      }
	        	    }
	        	
	        	
	        	
	           
	        	   try {
	        		 //  Toast.makeText(getApplicationContext(), "INside", 0).show();
	        		   outStream = btSocket.getOutputStream();
	        		// kk="111";
					
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
	        	   mConnectedThread = new ConnectedThread(btSocket);
	        	   mConnectedThread.start();
	        }
	        
	    }, 20000);
	    
			//Toast.makeText(getApplicationContext(), "sds", 0).show();
			
			/*Bundle bt=data.getExtras();
			String k= bt.getString("n");
			Toast.makeText(getApplicationContext(), k, 0).show();*/
			
		/*	 Toast.makeText(getApplicationContext(), "Send", 0).show();
			byte[] msgBuffer = message.getBytes();
			final byte[] msgBuffer1 = message1.getBytes();
			  
		  
		  
		    try {
	
		      outStream.write(msgBuffer);
		     
		      t=1;
		   
		    } catch (IOException e) {
		      String msg = "In onResume() and an exception occurred during write: " + e.getMessage();
		      if (address.equals("00:00:00:00:00:00"))
		        msg = msg + ".\n\nUpdate your server address from 00:00:00:00:00:00 to the correct address on line 35 in the java code";
		        msg = msg +  ".\n\nCheck that the SPP UUID: " + MY_UUID.toString() + " exists on server.\n\n";
			}
		   
		    
		    
		    final Handler handler = new Handler();
		    handler.postDelayed(new Runnable() {
		        @Override
		        public void run() {
		            // Do something after 5s = 5000ms
		        	   try {
		        		   Toast.makeText(getApplicationContext(), "2sec", 0).show();
		        		   outStream.flush();
		        		   outStream.write(msgBuffer1);
						
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		        }
		    }, 2000);
		    t=0;*/
		   
		
		}
		
  }

  private BluetoothSocket createBluetoothSocket(BluetoothDevice device) throws IOException {
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
  
    Log.d(TAG, "...onResume - try connect...");
    
    // Set up a pointer to the remote node using it's address.
    BluetoothDevice device = btAdapter.getRemoteDevice(address);
    
    // Two things are needed to make a connection:
    //   A MAC address, which we got above.
    //   A Service ID or UUID.  In this case we are using the
    //     UUID for SPP.
     
    try {
        btSocket = createBluetoothSocket(device);
    } catch (IOException e) {
        errorExit("Fatal Error", "In onResume() and socket create failed: " + e.getMessage() + ".");
    }
    
    // Discovery is resource intensive.  Make sure it isn't going on
    // when you attempt to connect and pass your message.
    btAdapter.cancelDiscovery();
   // Toast.makeText(getApplicationContext(), kk, 0).show();
    // Establish the connection.  This will block until it connects.
    Log.d(TAG, "...Connecting...");
    try {
      btSocket.connect();
      Log.d(TAG, "....Connection ok...");
    } catch (IOException e) {
      try {
        btSocket.close();
      } catch (IOException e2) {
        errorExit("Fatal Error", "In onResume() and unable to close socket during connection failure" + e2.getMessage() + ".");
      }
    }
    try {
    	if(kk.contentEquals("f_au")||kk.contentEquals("p_au"))
    	{
    		//Toast.makeText(getApplicationContext(), "null",0).show();
    		outStream.close();
    	}
    	else
    	{
    		//Toast.makeText(getApplicationContext(), "not null", 0).show();
    		outStream = btSocket.getOutputStream();
    	}
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
    
    
    
    
    // Create a data stream so we can talk to server.
    Log.d(TAG, "...Create Socket...");
    
    mConnectedThread = new ConnectedThread(btSocket);
   mConnectedThread.start();
  }

  
  /*@Override
  public void onPause() {
    super.onPause();
  
    Log.d(TAG, "...In onPause()...");
    
    count=0;
   try     {
	   
      btSocket.close();
    } catch (IOException e2) {
      errorExit("Fatal Error", "In onPause() and failed to close socket." + e2.getMessage() + ".");
    }
  }*/
    
  private void checkBTState() {
    // Check for Bluetooth support and then check to make sure it is turned on
    // Emulator doesn't support Bluetooth and will return null
    if(btAdapter==null) {
      errorExit("Fatal Error", "Bluetooth not support");
    } else {
      if (btAdapter.isEnabled()) {
        Log.d(TAG, "...Bluetooth ON...");
      } else {
        //Prompt user to turn on Bluetooth
        Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        startActivityForResult(enableBtIntent, 1);
        
      }
    }
  }
  
  private void errorExit(String title, String message){
    Toast.makeText(getBaseContext(), title + " - " + message, Toast.LENGTH_LONG).show();
    finish();
  }
  
  private class ConnectedThread extends Thread {
        private final InputStream mmInStream;
     // private final OutputStream mmOutStream;
      
        public ConnectedThread(BluetoothSocket socket) {
            InputStream tmpIn = null;
          //  OutputStream tmpOut = null;
      
            // Get the input and output streams, using temp objects because
            // member streams are final
            try {
                tmpIn = socket.getInputStream();
                
             //   tmpOut = socket.getOutputStream();
            } catch (IOException e) { }
      
            mmInStream = tmpIn;
           // mmOutStream = tmpOut;
        }
      
        public void run() {
            byte[] buffer = new byte[256];  // buffer store for the stream
            int bytes; // bytes returned from read()
 
            // Keep listening to the InputStream until an exception occurs
            while (true) {
                try {
                	 
                    // Read from the InputStream
                    bytes = mmInStream.read(buffer);
                  
                    // Get number of bytes and message in "buffer"
                    h.obtainMessage(RECIEVE_MESSAGE, bytes, -1, buffer).sendToTarget();
                  
                	// Send to message queue Handler
                } catch (IOException e) {
                    break;
                }
            }
        }
      
        /* Call this from the main activity to send data to the remote device */
   /*     public void write(String message) {
            Log.d(TAG, "...Data to send: " + message + "...");
            byte[] msgBuffer = message.getBytes();
            try {
                mmOutStream.write(msgBuffer);
            } catch (IOException e) {
                Log.d(TAG, "...Error data send: " + e.getMessage() + "...");    
              }
        }*/
    }
}