package com.example.bluetoothspp;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;


/**
 * This is the main Activity that displays the current chat session.
 */
public class MainActivity extends Activity {	
	
	public static String readMessage = "";
	public static String test = "blahpoop";
	
    private static final String TAG = "BluetoothChat";
    private static final boolean D = true;

    public static final int MESSAGE_STATE_CHANGE = 1;
    public static final int MESSAGE_READ = 2;
    public static final int MESSAGE_WRITE = 3;
    public static final int MESSAGE_DEVICE_NAME = 4;
    public static final int MESSAGE_TOAST = 5;

    public static final String DEVICE_NAME = "device_name";
    public static final String TOAST = "toast";

    private static final int REQUEST_CONNECT_DEVICE_SECURE = 1;
    private static final int REQUEST_ENABLE_BT = 3;

    private ListView mConversationView;
    private Button b1, b2, b3, b4, b5, b6;
    private Spinner mSpinner;
    
    public static int PlayerCount = 0;
    
    // Spinner Options
    private static final String[] sPlayerNumber = {"Server", "Player 1", "Player 2", "Player 3", "Player 4", "Player 5", "Player 6", "Player 7"};

    private String mConnectedDeviceName;
    private ArrayAdapter<String> mConversationArrayAdapter;
    private BluetoothAdapter mBluetoothAdapter = null;
    public static BluetoothService mChatService = null;
    
    public enum MessageType {
        BET, CHECK, FOLD, QUIT, SERVER
    }
    
    Button btnServer, btnClient;
	String Client_c1, Client_c2;
	ArrayList<String> Server_cards;
	
	/** Initializing the game object **/
	//public static PokerGame game;
	//public static ArrayList<Card> cards;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(D) Log.e(TAG, "+++ ON CREATE +++");
        setContentView(R.layout.activity_main);
        
        btnServer = (Button) findViewById(R.id.makeMeTheTable);
        btnClient = (Button) findViewById(R.id.makeMeAHand);
        
        /*game = new PokerGame(2, 10, 10);
		cards = new ArrayList<Card>();
		cards.clear();
		game.SetBlinds();
		game.DealCards(2);
		cards.clear();
		for(int i = 0; i < game.players.size(); i++)
		{
			cards.add(game.players.get(i).GetCards().get(0));
			cards.add(game.players.get(i).GetCards().get(1));
		}
		Client_c1 = (cards.get(0).suit + "" + cards.get(0).rank);
		Client_c2 = (cards.get(0).suit + "" + cards.get(1).rank);*/
		
        
		btnServer.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				if (mChatService.getPlayerNumber() == 0) {
					PlayerCount = mChatService.mConnThreads.size();
					if (PlayerCount !=0) {
						Intent startServer = new Intent(MainActivity.this, ServerActivity.class);
						startServer.putExtra("Default", "blank");
						startActivity(startServer);
						sendMessage("StartGameInit");
						sendServerMessage("CardInit ", mChatService.getPlayerNumber()-1);						
						Toast.makeText(getApplicationContext(), Integer.toString(PlayerCount), Toast.LENGTH_LONG).show();
					}
				}
				//			
			}
		});
        
        btnClient.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				Intent startClient = new Intent(MainActivity.this, ClientActivity.class);
				startClient.putExtra("Card1", "bc");
				startClient.putExtra("Card2", "bc");
				startActivity(startClient);
				//Toast.makeText(getApplicationContext(), ServerActivity.ivCard1.toString(), Toast.LENGTH_SHORT).show();
				//ServerActivity.setCardImage(ServerActivity.ivCard1, "ac");
			}
		});
        
        mSpinner = (Spinner) findViewById(R.id.spinner1);
        b6 = (Button) findViewById(R.id.button6);
        
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_spinner_item, sPlayerNumber);
        
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(spinnerAdapter);
        
        b6.setOnClickListener(new OnClickListener() {
        	@Override
            public void onClick(View v) {
            	mSpinner = (Spinner) findViewById(R.id.spinner1);
                Toast.makeText(MainActivity.this, String.valueOf(mSpinner.getSelectedItem()), Toast.LENGTH_SHORT).show();
                
                mChatService.setPlayerNumber(mSpinner.getSelectedItemPosition());
            }
        });
        
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, "Bluetooth is not available", Toast.LENGTH_LONG).show();
            finish();
            return;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
        } else {
            if (mChatService == null) setupChat();
        }
    }

    @Override
    public synchronized void onResume() {
        super.onResume();
        if (mChatService != null) {
            if (mChatService.getState() == BluetoothService.STATE_NONE) {
              mChatService.start();
            }
        }
    }

    private void setupChat() {
        mConversationArrayAdapter = new ArrayAdapter<String>(this, R.layout.message);
        mConversationView = (ListView) findViewById(R.id.in);
        mConversationView.setAdapter(mConversationArrayAdapter);
        
        //Bet 100
        b1 = (Button) findViewById(R.id.button2);
        b1.setOnClickListener(new OnClickListener() {public void onClick(View v) {sendMessage(Integer.toString(mChatService.getPlayerNumber()) + "0100");}});
        
        //Check
        b2 = (Button) findViewById(R.id.button3);
        b2.setOnClickListener(new OnClickListener() {public void onClick(View v) {sendMessage(Integer.toString(mChatService.getPlayerNumber()) + "1");}});
        
        //Fold
        b3 = (Button) findViewById(R.id.button4);
        b3.setOnClickListener(new OnClickListener() {public void onClick(View v) {sendMessage(Integer.toString(mChatService.getPlayerNumber()) + "2");}}); 
        
        //Quit
        b4 = (Button) findViewById(R.id.button5);
        b4.setOnClickListener(new OnClickListener() {public void onClick(View v) {sendMessage(Integer.toString(mChatService.getPlayerNumber()) + "3");}});         
        
        b5 = (Button) findViewById(R.id.button1);
        b5.setOnClickListener(new OnClickListener() {public void onClick(View v) {
        	final int selectedPosition = (int)mSpinner.getSelectedItemPosition();
        	//Toast.makeText(getApplicationContext(), selectedPosition, Toast.LENGTH_SHORT).show();
        	sendServerMessage("44SENDING MESSAGE TO SPECIFIC PLAYER " + selectedPosition, selectedPosition);
        	}});
        
        mChatService = new BluetoothService(this, mHandler);
    }

    @Override
    public synchronized void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mChatService != null) mChatService.stop();
    }

    private void ensureDiscoverable() {
        if (mBluetoothAdapter.getScanMode() !=
            BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
            Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
            startActivity(discoverableIntent);
        }
    }

    /**
     * Sends a message.
     * @param message  A string of text to send.
     */
    private void sendMessage(String message) {
        if (mChatService.getState() != BluetoothService.STATE_CONNECTED) {
            Toast.makeText(this, R.string.not_connected, Toast.LENGTH_SHORT).show();
            return;
        }
        if (message.length() > 0) {
            // Get the message bytes and tell the BluetoothService to write
            byte[] send = message.getBytes();
            mChatService.write(send);
        }
    }
    
    /**
     * Sends a message.
     * @param message  A string of text to send.
     */
    private void sendServerMessage(String message, int playerNumber) {
        if (mChatService.getState() != BluetoothService.STATE_CONNECTED) {
            Toast.makeText(this, R.string.not_connected, Toast.LENGTH_SHORT).show();
            return;
        }
        if (message.length() > 0) {
            // Get the message bytes and tell the BluetoothService to write
            byte[] send = message.getBytes();
            mChatService.writeToPlayerNumber(send, playerNumber);
        }
    }

    private final void setStatus(int resId) {
        final ActionBar actionBar = getActionBar();
        actionBar.setSubtitle(resId);
    }

    private final void setStatus(CharSequence subTitle) {
        final ActionBar actionBar = getActionBar();
        actionBar.setSubtitle(subTitle);
    }

    public static String waitForBluetooth(int previousPlayer) {
		//String readMessage2 = readMessage;
		while(!readMessage.startsWith("p" + (previousPlayer+1))) {
			try {
				Thread.sleep(1000);
				System.out.println("|" + readMessage + "|");
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return readMessage;
	}
    
    // The Handler that gets information back from the BluetoothService
    @SuppressLint("HandlerLeak")
	private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
            case MESSAGE_STATE_CHANGE:
                if(D) Log.i(TAG, "MESSAGE_STATE_CHANGE: " + msg.arg1);
                switch (msg.arg1) {
                case BluetoothService.STATE_CONNECTED:
                    setStatus(getString(R.string.title_connected_to, mConnectedDeviceName));
                    mConversationArrayAdapter.clear();
                    break;
                case BluetoothService.STATE_CONNECTING:
                    setStatus(R.string.title_connecting);
                    break;
                case BluetoothService.STATE_LISTEN:
                case BluetoothService.STATE_NONE:
                    setStatus(R.string.title_not_connected);
                    break;
                }
                break;
            case MESSAGE_WRITE:
                byte[] writeBuf = (byte[]) msg.obj;
                // construct a string from the buffer
                String writeMessage = new String(writeBuf);
                mConversationArrayAdapter.add("Me:  " + writeMessage);
                break;
            case MESSAGE_READ:
                byte[] readBuf = (byte[]) msg.obj;
                // construct a string from the valid bytes in the buffer
                readMessage = new String(readBuf, 0, msg.arg1);
                //Intent startServer = new Intent(MainActivity.this, ServerActivity.class);
                //startServer.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				//startServer.putExtra("rm", readMessage);
				//startActivity(startServer);
                test = readMessage;
                //System.out.println(test);
//XXXNTS:parse message here
                mConversationArrayAdapter.add(MessageParse(readMessage));
                break;
            case MESSAGE_DEVICE_NAME:
                // save the connected device's name
                mConnectedDeviceName = msg.getData().getString(DEVICE_NAME);
                Toast.makeText(getApplicationContext(), "Connected to "
                               + mConnectedDeviceName, Toast.LENGTH_SHORT).show();
                break;
            case MESSAGE_TOAST:
                Toast.makeText(getApplicationContext(), msg.getData().getString(TOAST),
                               Toast.LENGTH_SHORT).show();
                break;
            }
        }
    };

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(D) Log.d(TAG, "onActivityResult " + resultCode);
        switch (requestCode) {
        case REQUEST_CONNECT_DEVICE_SECURE:
            // When DeviceListActivity returns with a device to connect
            if (resultCode == Activity.RESULT_OK) {
                connectDevice(data);
            }
            break;
        case REQUEST_ENABLE_BT:
            // When the request to enable Bluetooth returns
            if (resultCode == Activity.RESULT_OK) {
                // Bluetooth is now enabled, so set up a chat session
                setupChat();
            } else {
                // User did not enable Bluetooth or an error occurred
                Log.d(TAG, "BT not enabled");
                Toast.makeText(this, R.string.bt_not_enabled_leaving, Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    private void connectDevice(Intent data) {
        // Get the device MAC address
        String address = data.getExtras().getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
        // Get the BluetoothDevice object
        BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
        // Attempt to connect to the device
        mChatService.connect(device, true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent serverIntent = null;
        switch (item.getItemId()) {
        case R.id.secure_connect_scan:
            // Launch the DeviceListActivity to see devices and do scan
            serverIntent = new Intent(this, DeviceListActivity.class);
            startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE_SECURE);
            return true;
        case R.id.discoverable:
            // Ensure this device is discoverable by others
            ensureDiscoverable();
            return true;
        }
        return false;
    }
    
    public String MessageParse(String message)
    {	
    	if (readMessage.equals("StartGameInit")) {
    		Intent startClient = new Intent(MainActivity.this, ClientActivity.class);
			startClient.putExtra("Card1", "bc");
			startClient.putExtra("Card2", "bc");
			startActivity(startClient);
    	}
    	if (readMessage.startsWith("sendCard")) { //send cards to all the players
    		String c1 = readMessage.substring(9,readMessage.indexOf(":"));
    		String c2 = readMessage.substring(readMessage.indexOf(":")+1, readMessage.length());
    		Intent startClient = new Intent(MainActivity.this, ClientActivity.class);
			startClient.putExtra("Card1", c1);
			startClient.putExtra("Card2", c2);
			startActivity(startClient);
    	}
    	if (readMessage.equals("setServer")) {
    		ServerActivity.setTableCards("d10", "h13", "s14", "blank", "blank");
    	}
//    	//Remove the first character to determine who sent the message
//    	String playerNumber = message.substring(0, 1);
//
//    	//Remove the second character to determine what message type was sent
//    	MessageType mType = MessageType.values()[Integer.valueOf(message.substring(1, 2))];
//    	
//    	String parsedString = null;
//    	
//    	switch(mType)
//    	{
//    		case SERVER:
//    			parsedString = message;
//    			break;
//    		//player wants to bet
//	    	case BET:
//	    		if(message.length() > 2)
//	    		{
//	    			String betAmount = message.substring(2);
//	    			parsedString = "Player " + playerNumber + " BETS " + betAmount;
//	    		}
//	    		else
//	    			parsedString = "Player " + playerNumber + " BETS 0";
//	    		break;
//	    	//player wants to fold
//	    	case FOLD:
//	    		parsedString = "Player " + playerNumber + " FOLDS";
//	    		break;
//	    	//player wants to check
//	    	case CHECK:
//	    		parsedString = "Player " + playerNumber + " CHECKS";
//	    		break;
//	    	//player wants to leave the game
//	    	case QUIT:
//	    		parsedString = "Player " + playerNumber + " QUITS";
//	    		break;
//	    	//the player somehow sent an unknown message
//	    	default:
//	    		parsedString = message;
//	    		break;
//    	}
    	
    	return message;
    }
}
