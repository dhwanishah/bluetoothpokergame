package com.example.bluetoothspp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ServerActivity extends Activity {

	public static String test = "bloop";
	private static final int STEP_ONE_COMPLETE = 0;
	private static final int STEP_TWO_COMPLETE = 1;
	private static final int STEP_THREE_COMPLETE = 2;
	private static final int STEP_FOUR_COMPLETE = 3;
	private static final int STEP_FIVE_COMPLETE = 4;
	private static final int STEP_SIX_COMPLETE = 5;
	private static final int STEP_SEVEN_COMPLETE = 6;
	
	public static Map <String, Integer> cardImages = new HashMap <String, Integer>();
	public ArrayList<LinearLayout> playerLayoutInfo = new ArrayList<LinearLayout>();
	ArrayList<String> Cards;
	public static String Default, Card1, Card2, Card3, Card4, Card5;
	public static ImageView ivCard1, ivCard2, ivCard3, ivCard4, ivCard5;
	TextView tvCurrentPot, p1Info, p2Info, p3Info, p4Info, p5Info, p6Info, p7Info;
	LinearLayout p1, p2, p3, p4, p5, p6, p7;
	//PokerGame game;
	BluetoothService mChatService = MainActivity.mChatService;
	PokerGame game;
	
	int PlayerCount = MainActivity.PlayerCount;
	public static String readMessage = "";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
	    this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_server);		
		initCardsAndViews();		
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			Default = extras.getString("Default");
			//readMessage = extras.getString("rm");
			System.out.println(readMessage);
		}

		game = new PokerGame(PlayerCount, 10, 10);
		ArrayList<Card> cards = new ArrayList<Card>();
		cards.clear();
		game.SetBlinds();
		game.DealCards(2);
		cards.clear();
		for(int i = 0; i < game.players.size(); i++)
		{
			cards.add(game.players.get(i).GetCards().get(0));
			cards.add(game.players.get(i).GetCards().get(1));
		}
		for (int i = 0, j = 0; j < PlayerCount; i+=2, j++) {
			String Client_c1 = (cards.get(i).suit + "" + cards.get(i).rank);
			String Client_c2 = (cards.get(i+1).suit + "" + cards.get(i+1).rank);
			sendServerMessage("sendCard="+ Client_c1 + ":" + Client_c2, j);
			//Toast.makeText(getApplicationContext(), "p" + ((int) j + 1) + "="+ Client_c1 + ":" + Client_c2, Toast.LENGTH_SHORT).show();
		}
		doBackgroundUpdate1();
		/*final Thread gameBet = new Thread(new Runnable() {
			@Override
			public void run() {
				if (!game.Bet()) {
					setTableCards("d10", "h13", "s14", "blank", "blank");
					return;
				}
			}
		});
		gameBet.start();*/
		/*Thread sec = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					gameBet.join();
					setTableCards("d10", "h13", "s14", "blank", "blank");
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});
		sec.start();*/
		
		
		/*String Client_c1 = (cards.get(0).suit + "" + cards.get(0).rank);
		String Client_c2 = (cards.get(1).suit + "" + cards.get(1).rank);
		String Client2_c1 = (cards.get(2).suit + "" + cards.get(2).rank);
		String Client2_c2 = (cards.get(3).suit + "" + cards.get(3).rank);
		sendServerMessage("p1="+Client_c1+":"+Client_c2, 0);
		sendServerMessage("p2="+Client2_c1+":"+Client2_c2, 1);*/
		//Toast.makeText(getApplicationContext(), Client_c1+Client_c2, Toast.LENGTH_SHORT).show();
		//mChatService = MainActivity.mChatService;
		//sendMessage("hello");
		//sendServerMessage("p1", 0);
		setCardImage(ivCard1, Default);
		setCardImage(ivCard2, Default);
		setCardImage(ivCard3, Default);
		setCardImage(ivCard4, Default);
		setCardImage(ivCard5, Default);
		
		Toast.makeText(getApplicationContext(), readMessage, Toast.LENGTH_LONG).show();
	}
	
	private void doBackgroundUpdate1(){
	    Thread backgroundThread = new Thread() {
	        @Override
	        public void run() {
	        	runOnUiThread(new Runnable() {
	   		     @Override
	   		     public void run() {
	   		    	 UpdateCurrentPotValue();
	   		    	 UpdateAllPlayerInfo();
		   		    }
		   		});
	        	if (!game.Bet()) {
					return;
				}
	            // finished first step
	            Message msg = Message.obtain();
	            msg.what = STEP_ONE_COMPLETE;
	            handler.sendMessage(msg);
	        }
	    };
	    backgroundThread.start();
	}
	private void doBackgroundUpdate2(){		
		runOnUiThread(new Runnable() {
		     @Override
		     public void run() {
		    	 setTableCards("d10", "h13", "s14", "blank", "blank");
		    	 UpdateCurrentPotValue();
		    	 UpdateAllPlayerInfo();
		    	//   finished first step
		            Message msg = Message.obtain();
		            msg.what = STEP_TWO_COMPLETE;
		            handler.sendMessage(msg);
		    }
		});
	}
	private void doBackgroundUpdate3(){
	    Thread backgroundThread = new Thread() {
	        @Override
	        public void run() {
	        	runOnUiThread(new Runnable() {
		   		     @Override
		   		     public void run() {
		   		    	 UpdateCurrentPotValue();
		   		    	 UpdateAllPlayerInfo();
			   		    }
			   		});
	        	if (!game.Bet()) {
					return;
				}
	            // finished first step
	            Message msg = Message.obtain();
	            msg.what = STEP_THREE_COMPLETE;
	            handler.sendMessage(msg);
	        }
	    };
	    backgroundThread.start();
	}
	
	private void doBackgroundUpdate4(){		
		runOnUiThread(new Runnable() {
		     @Override
		     public void run() {
		    	 setTableCards("d10", "h13", "s14", "d2", "blank");
		    	 UpdateCurrentPotValue();
		    	 UpdateAllPlayerInfo();
		    	// finished first step
		            Message msg = Message.obtain();
		            msg.what = STEP_FOUR_COMPLETE;
		            handler.sendMessage(msg);
		    }
		});
	}
	private Handler handler = new Handler(){
	    @Override
	    public void handleMessage(Message msg) {
	        switch(msg.what){
	        case STEP_ONE_COMPLETE:
	            doBackgroundUpdate2();
	            break;
	        case STEP_TWO_COMPLETE:
	        	doBackgroundUpdate3();
	            break;
	        case STEP_THREE_COMPLETE:
	        	doBackgroundUpdate4();
	            break;
	        case STEP_FOUR_COMPLETE:
	        	//doBackgroundUpdate5();
	            break;
	        }
	    }
	};
	
	public void UpdateCurrentPotValue() {
		String currentPot = Integer.toString(PokerGame.pots.get(PokerGame.currentPotIndex));
		tvCurrentPot.setText("$" + currentPot);
	}
	
	public void UpdateAllPlayerInfo() {
		p1Info.setText("Player Fund: $"+ game.players.get(0).getTotalFunds()+"\nPlayer: 1");
	    p2Info.setText("Player Fund: $"+ game.players.get(1).getTotalFunds()+"\nPlayer: 2");
	}
	
	private void sendMessage(String message) {
        if (message.length() > 0) {
            // Get the message bytes and tell the BluetoothService to write
            byte[] send = message.getBytes();
            mChatService.write(send);
        }
    }
	
	private void sendServerMessage(String message, int playerNumber) {
        if (message.length() > 0) {
            // Get the message bytes and tell the BluetoothService to write
            byte[] send = message.getBytes();
            mChatService.writeToPlayerNumber(send, playerNumber);
        }
    }
	
	@Override
	public void onBackPressed() {
	}
	
	public static void setTableCards(String c1, String c2, String c3, String c4, String c5) {
		setCardImage(ivCard1, c1);
		setCardImage(ivCard2, c2);
		setCardImage(ivCard3, c3);
		setCardImage(ivCard4, c4);
		setCardImage(ivCard5, c5);
	}
	
	public static void setCardImage(ImageView ivCardName, String nameOfCardToChangeTo) {
		ivCardName.setImageResource(cardImages.get(nameOfCardToChangeTo));
	}
	
	public void initCardsAndViews() {
		fillCardImageMap();
		ivCard1 = (ImageView) findViewById(R.id.cardView1_server);
		ivCard2 = (ImageView) findViewById(R.id.cardView2_server);
		ivCard3 = (ImageView) findViewById(R.id.cardView3_server);
		ivCard4 = (ImageView) findViewById(R.id.cardView4_server);
		ivCard5 = (ImageView) findViewById(R.id.cardView5_server);
		tvCurrentPot = (TextView) findViewById(R.id.currentPot);
		p1 = (LinearLayout) findViewById(R.id.p1_info);
		p2 = (LinearLayout) findViewById(R.id.p2_info);
		p3 = (LinearLayout) findViewById(R.id.p3_info);
		p4 = (LinearLayout) findViewById(R.id.p4_info);
		p5 = (LinearLayout) findViewById(R.id.p5_info);
		p6 = (LinearLayout) findViewById(R.id.p6_info);
		p7 = (LinearLayout) findViewById(R.id.p7_info);
		p1Info = (TextView) findViewById(R.id.p1DetailInfo);
		p2Info = (TextView) findViewById(R.id.p2DetailInfo);
		p3Info = (TextView) findViewById(R.id.p3DetailInfo);
		p4Info = (TextView) findViewById(R.id.p4DetailInfo);
		p5Info = (TextView) findViewById(R.id.p5DetailInfo);
		p6Info = (TextView) findViewById(R.id.p6DetailInfo);
		p7Info = (TextView) findViewById(R.id.p7DetailInfo);
		playerLayoutInfo.add(p1);
		playerLayoutInfo.add(p2);
		playerLayoutInfo.add(p3);
		playerLayoutInfo.add(p4);
		playerLayoutInfo.add(p5);
		playerLayoutInfo.add(p6);
		playerLayoutInfo.add(p7);
		
		for (int i = 0; i < 7; i++) {
			if (i >= PlayerCount) {
				playerLayoutInfo.get(i).removeAllViewsInLayout();
			}
		}
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
	
	@Override
	protected void onPause() {
		//super.onPause();
	}

}
