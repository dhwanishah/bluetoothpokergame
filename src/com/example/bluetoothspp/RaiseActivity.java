package com.example.bluetoothspp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class RaiseActivity extends Activity {
	
	int currentBet = 0;
	ImageButton BtnChipOf1, BtnChipOf5, BtnChipOf10, BtnChipOf25, BtnChipOf100;
	TextView TxtBettingCount;
	Button BtnResetBet, BtnConfirmBet;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
	    this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_raise);
		
		init(); // Initialize all the Views
		
		BtnChipOf1.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				currentBet++;
				TxtBettingCount.setText("Current Bet: $" + currentBet);
			}
		});
		
		BtnChipOf5.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				currentBet += 5;
				TxtBettingCount.setText("Current Bet: $" + currentBet);
			}
		});
		
		BtnChipOf10.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				currentBet += 10;
				TxtBettingCount.setText("Current Bet: $" + currentBet);
			}
		});
		
		BtnChipOf25.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				currentBet += 25;
				TxtBettingCount.setText("Current Bet: $" + currentBet);
			}
		});
		
		BtnChipOf100.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				currentBet += 100;
				TxtBettingCount.setText("Current Bet: $" + currentBet);
			}
		});
		
		BtnResetBet.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				currentBet = 0;
				TxtBettingCount.setText("Current Bet: $" + currentBet);
			}
		});
		
		BtnConfirmBet.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				AlertDialog.Builder alertDialog = new AlertDialog.Builder(RaiseActivity.this);
		        //alertDialog.setTitle("Confirm");
		        alertDialog.setMessage("Confirm bet of $" + currentBet + "?");
		        //alertDialog.setIcon(R.drawable.ic_launcher);
		        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
		            public void onClick(DialogInterface dialog,int which) {
		            	TxtBettingCount.setText("Confirmed");
		            	// TODO: Send a RAISE via BT to Server
		            }
		        });
		        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
		            public void onClick(DialogInterface dialog, int which) {
		            	TxtBettingCount.setText("Not confirmed");
		            	dialog.cancel();
		            }
		        });
		        alertDialog.show();
	            
			}
		});
	}
	
	public void init() {
		BtnChipOf1 = (ImageButton) findViewById(R.id.chip1Btn_client);
		BtnChipOf5 = (ImageButton) findViewById(R.id.chip5Btn_client);
		BtnChipOf10 = (ImageButton) findViewById(R.id.chip10Btn_client);
		BtnChipOf25 = (ImageButton) findViewById(R.id.chip25Btn_client);
		BtnChipOf100 = (ImageButton) findViewById(R.id.chip100Btn_client);
		TxtBettingCount = (TextView) findViewById(R.id.bettingCount);
		BtnResetBet = (Button) findViewById(R.id.resetBet_client);
		BtnConfirmBet = (Button) findViewById(R.id.confirmBet_client);
	}

}
