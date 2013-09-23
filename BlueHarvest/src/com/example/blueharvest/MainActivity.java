package com.example.blueharvest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import static android.view.View.OnClickListener;
import static android.widget.Toast.LENGTH_LONG;
import static android.widget.Toast.makeText;

/**
 * @author Brad Bass
 * @version 1.0
 * 
 * MainActivity allows user to either go to the ConfigActivity or the ChargeActivity.
 *
 * @see android.os
 * @see android.app
 * @see android.content
 * @see android.view
 * @see android.widget
 */

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

        //buttons
        final Button scanBtn = (Button) findViewById(R.id.scanBtn);
        final Button configBtn = (Button) findViewById(R.id.configBtn);
        final Button settingsBtn = (Button) findViewById(R.id.settingsBtn);

        scanBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                chargeParts();
            }
        });

        configBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                configScreen();
            }
        });

        settingsBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                settingsScreen();
            }
        });
    }
	/*
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	*/
	/*
	@Override
	protected void onResume() {
		if(getIntent().getBooleanExtra("finishApplication", true)) {
			finish();
		}
	}
	//*/
	/**
	 * calls the ChargeActivity.
	 *
     */
    void chargeParts() {
		// start the chargeParts activity
		Intent chargeIntent = new Intent(getApplicationContext(), ChargeActivity.class);
		chargeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivityForResult(chargeIntent, 1);
	}
	
	/**
	 * calls the ConfigActivity.
	 *
     */
    void configScreen() {
		// start the upload parts activity
		Intent configIntent = new Intent(getApplicationContext(), ConfigActivity.class);
		configIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(configIntent);
	}
	
	/**
	 * calls the SettingsActivity
	 *
     */
    void settingsScreen() {
		// start the settings activity
		Intent settingsIntent = new Intent(getApplicationContext(), SettingsActivity.class);
		settingsIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(settingsIntent);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		
		if (requestCode == 1) {
			if (resultCode == RESULT_OK) {
				this.finish();
			}
		}
	}
	
	/**
	 * When user clicks the UI back button, we create a Toast.
	 */
	@Override
	public void onBackPressed() {
		makeText(getBaseContext(), getString(R.string.toast_goodbye_message), LENGTH_LONG).show();
		
		this.finish();
	}
		
	/**
	 * DOES NOT WORK
	 * when user clicks the UI HOME button, we create a Toast
	 * 
	 * @return
	 */
	/*
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		
		if(keyCode == KeyEvent.KEYCODE_HOME)
		{
			Toast.makeText(getBaseContext(), "Thanks for using BlueHarvest!", Toast.LENGTH_LONG).show();
			
			this.finish();
		}
		return false;		
	}//*/
}