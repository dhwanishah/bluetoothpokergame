package com.example.bluetoothspp;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class ClientActivity extends Activity {
	
	Map <String, Integer> cardImages = new HashMap <String, Integer>();
	
	Button btnCall, btnRaise, btnFold;
	String Card1, Card2;
	ImageView ivCard1, ivCard2;
	BluetoothService mChatService = MainActivity.mChatService;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
	    this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_client);
		
		initCardsAndViews();
		
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
		    Card1 = extras.getString("Card1");
		    Card2 = extras.getString("Card2");
		}
		
		setCardImage(ivCard1, Card1);
		setCardImage(ivCard2, Card2);
		//ArrayList<Card> cards = MainActivity.cards;
		//Toast.makeText(getApplicationContext(), MainActivity.readMessage, Toast.LENGTH_SHORT).show();
		//Toast.makeText(getApplicationContext(), cards.get(0).suit + "" + cards.get(0).rank, Toast.LENGTH_SHORT).show();
		
		btnCall.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				AlertDialog.Builder alertDialog = new AlertDialog.Builder(ClientActivity.this);
		        alertDialog.setMessage("Are you sure you want to call?");
		        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
		            public void onClick(DialogInterface dialog,int which) {
						sendMessage("p" + (mChatService.getPlayerNumber()) + ":CALL");
		            	Toast.makeText(getApplicationContext(), "You called!", Toast.LENGTH_SHORT).show();
		            }
		        });
		        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
		            public void onClick(DialogInterface dialog, int which) {
		            	Toast.makeText(getApplicationContext(), "What? Change your mind?", Toast.LENGTH_SHORT).show();
		            	dialog.cancel();
		            }
		        });
		        alertDialog.show();
			}
		});
		
		btnRaise.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				sendMessage("p" + (mChatService.getPlayerNumber()) + ":RAISE=100");
				/*Intent startRaise = new Intent(ClientActivity.this, RaiseActivity.class);
				startActivity(startRaise);*/
			}
		});

		btnFold.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				AlertDialog.Builder alertDialog = new AlertDialog.Builder(ClientActivity.this);
		        alertDialog.setMessage("Are you sure you want to fold?");
		        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
		            public void onClick(DialogInterface dialog,int which) {
		            	// TODO: Send a message to server about fold
		            	sendMessage("p" + (mChatService.getPlayerNumber()) + ":FOLD");
						Intent startFold = new Intent(ClientActivity.this, FoldActivity.class);
						startActivity(startFold);
		            	Toast.makeText(getApplicationContext(), "You folded!", Toast.LENGTH_SHORT).show();
		            }
		        });
		        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
		            public void onClick(DialogInterface dialog, int which) {
		            	Toast.makeText(getApplicationContext(), "What? Change your mind?", Toast.LENGTH_SHORT).show();
		            	dialog.cancel();
		            }
		        });
		        alertDialog.show();
			}
		});
	}
	
	/*@SuppressLint("HandlerLeak")
	private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
            case MainActivity.MESSAGE_WRITE:
                byte[] writeBuf = (byte[]) msg.obj;
                // construct a string from the buffer
                String writeMessage = new String(writeBuf);
                Toast.makeText(getApplicationContext(), writeMessage, Toast.LENGTH_SHORT).show();
                break;
            case MainActivity.MESSAGE_READ:
                byte[] readBuf = (byte[]) msg.obj;
                // construct a string from the valid bytes in the buffer
                String readMessage = new String(readBuf, 0, msg.arg1);
                Toast.makeText(getApplicationContext(), readMessage, Toast.LENGTH_SHORT).show();
                break;
            }
        }
    };*/
	
	private void sendMessage(String message) {
        if (message.length() > 0) {
            // Get the message bytes and tell the BluetoothService to write
            byte[] send = message.getBytes();
            mChatService.write(send);
        }
    }
	
	@Override
	public void onBackPressed() {
	}
	
	public void setCardImage(ImageView ivCardName, String nameOfCardToChange) {
		ivCardName.setImageResource(cardImages.get(nameOfCardToChange));
	}
	
	public void initCardsAndViews() {
		fillCardImageMap();
		btnCall = (Button) findViewById(R.id.callBtn_client);
		btnRaise = (Button) findViewById(R.id.raiseBtn_client);
		btnFold = (Button) findViewById(R.id.foldBtn_client);
		ivCard1 = (ImageView) findViewById(R.id.cardView1_client);
		ivCard2 = (ImageView) findViewById(R.id.cardView2_client);
	}
	
	public void fillCardImageMap() {
		cardImages.put("", R.drawable.bc);
		cardImages.put(" ", R.drawable.bc);
		cardImages.put("bc", R.drawable.bc);
		cardImages.put("blank", R.drawable.blankcard);
		cardImages.put("ac", R.drawable.ac);
		cardImages.put("ad", R.drawable.ad);
		cardImages.put("ah", R.drawable.ah);
		cardImages.put("as", R.drawable.as);
		cardImages.put("c2", R.drawable.c2);
		cardImages.put("c3", R.drawable.c3);
		cardImages.put("c4", R.drawable.c4);
		cardImages.put("c5", R.drawable.c5);
		cardImages.put("c6", R.drawable.c6);
		cardImages.put("c7", R.drawable.c7);
		cardImages.put("c8", R.drawable.c8);
		cardImages.put("c9", R.drawable.c9);
		cardImages.put("c10", R.drawable.c10);
		cardImages.put("c11", R.drawable.c11);
		cardImages.put("c12", R.drawable.c12);
		cardImages.put("c13", R.drawable.c13);
		cardImages.put("c14", R.drawable.ac);
		cardImages.put("d2", R.drawable.d2);
		cardImages.put("d3", R.drawable.d3);
		cardImages.put("d4", R.drawable.d4);
		cardImages.put("d5", R.drawable.d5);
		cardImages.put("d6", R.drawable.d6);
		cardImages.put("d7", R.drawable.d7);
		cardImages.put("d8", R.drawable.d8);
		cardImages.put("d9", R.drawable.d9);
		cardImages.put("d10", R.drawable.d10);
		cardImages.put("d11", R.drawable.d11);
		cardImages.put("d12", R.drawable.d12);
		cardImages.put("d13", R.drawable.d13);
		cardImages.put("d14", R.drawable.ad);
		cardImages.put("h2", R.drawable.h2);
		cardImages.put("h3", R.drawable.h3);
		cardImages.put("h4", R.drawable.h4);
		cardImages.put("h5", R.drawable.h5);
		cardImages.put("h6", R.drawable.h6);
		cardImages.put("h7", R.drawable.h7);
		cardImages.put("h8", R.drawable.h8);
		cardImages.put("h9", R.drawable.h9);
		cardImages.put("h10", R.drawable.h10);
		cardImages.put("h11", R.drawable.h11);
		cardImages.put("h12", R.drawable.h12);
		cardImages.put("h13", R.drawable.h13);
		cardImages.put("h14", R.drawable.ah);
		cardImages.put("s2", R.drawable.s2);
		cardImages.put("s3", R.drawable.s3);
		cardImages.put("s4", R.drawable.s4);
		cardImages.put("s5", R.drawable.s5);
		cardImages.put("s6", R.drawable.s6);
		cardImages.put("s7", R.drawable.s7);
		cardImages.put("s8", R.drawable.s8);
		cardImages.put("s9", R.drawable.s9);
		cardImages.put("s10", R.drawable.s10);
		cardImages.put("s11", R.drawable.s11);
		cardImages.put("s12", R.drawable.s12);
		cardImages.put("s13", R.drawable.s13);
		cardImages.put("s14", R.drawable.as);
	}

}
