package com.jonasSoftware.blueharvest;

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
        setTitle("onas Scanner");

        //buttons
        final Button scanBtn = (Button) findViewById(R.id.scanBtn);
        final Button configBtn = (Button) findViewById(R.id.configBtn);
        final Button settingsBtn = (Button) findViewById(R.id.settingsBtn);
        final Button uploadBtn = (Button) findViewById(R.id.uploadBtn);
        final Button transferBtn = (Button) findViewById(R.id.transferBtn);
        final Button receiveBtn = (Button) findViewById(R.id.receiveBtn);
        // testing
        //final Button testBtn = (Button) findViewById(R.id.testBtn);
/*

        testBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                testWebService();
            }
        });
*/

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

        uploadBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadParts();
            }
        });

        transferBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                transferParts();
            }
        });

        receiveBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                receivePO();
            }
        });
    }

    void receivePO() {
        //start the receivePO activity
        Intent receivePOIntent = new Intent(getApplicationContext(), ReceivePO.class);
        receivePOIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(receivePOIntent);
    }

    /**
     * calls the TransferActivity.
     *
     */
    void transferParts() {
        //start the transferParts activity
        Intent transferIntent = new Intent(getApplicationContext(), TransferActivity.class);
        transferIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(transferIntent);
    }

    /**
     * calls the UploadActivity.
     *
     */
    void uploadParts() {
        //start the uploadParts activity
        Intent uploadIntent = new Intent(getApplicationContext(), UploadActivity.class);
        uploadIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(uploadIntent);
    }
/*

    void testWebService() {
        // start the testWebService activity
        Intent testIntent = new Intent(getApplicationContext(), WebServiceDemo.class);
        testIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(testIntent);
    }
*/

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
}