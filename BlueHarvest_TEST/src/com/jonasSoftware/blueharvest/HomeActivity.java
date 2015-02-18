package com.jonasSoftware.blueharvest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.util.List;

import static android.view.View.OnClickListener;
import static android.widget.Toast.LENGTH_LONG;
import static android.widget.Toast.makeText;

/**
 * @author Brad Bass
 * @version 1.0
 * 
 * HomeActivity allows user to either go to the ConfigActivity or the ChargeActivity.
 *
 * @see android.os
 * @see android.app
 * @see android.content
 * @see android.view
 * @see android.widget
 */

public class HomeActivity extends Activity {

    private static Button _uploadBtn;
    private static Button _chrgBtn;
    private static Button _transferBtn;
    private static Button _receiveBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
        setTitle(" Inventory Scanner");

        //buttons
        final Button configBtn = (Button) findViewById(R.id.configBtn);
        final Button settingsBtn = (Button) findViewById(R.id.settingsBtn);
        _chrgBtn = (Button) findViewById(R.id.scanBtn);
        _uploadBtn = (Button) findViewById(R.id.uploadBtn);
        _transferBtn = (Button) findViewById(R.id.transferBtn);
        _receiveBtn = (Button) findViewById(R.id.receiveBtn);

        //test
        //_chrgBtn.setBackgroundResource(R.color.DataToSendButtonColor);

        //check tables for data.  If data exists, change the button color of the corresponding module and italicise the text
        DatabaseHandler dbh = new DatabaseHandler(getApplicationContext());
        dbh.checkTables();

        if (!DatabaseHandler._dataTables.isEmpty()) {
            List<String> dataTables = DatabaseHandler._dataTables;
            moduleBtnColorChngr(dataTables);
        }

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

        _chrgBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                chargeParts();
            }
        });

        _uploadBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadParts();
            }
        });

        _transferBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                transferParts();
            }
        });

        _receiveBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                receivePO();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // go back to home screen
        String toolbarItem = item.toString();
        switch (toolbarItem) {
            case "HOME":
                this.finish();
                break;
            case "Report":
                //launch report of all records waiting to be sent
                report();
                break;
            case "Help":
                //launch help docs?
                break;
            case "About":
                //launch about Jonas activity
                about();
                break;
        }
        return true;
    }

    void about() {
        Intent aboutIntent = new Intent(getApplicationContext(), ReportActivity.class);
        aboutIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(aboutIntent);
    }

    void report() {
        Intent reportIntent = new Intent(getApplicationContext(), ReportActivity.class);
        reportIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(reportIntent);
        this.finish();
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

    // change module button color if data exists in corresponding table
    private void moduleBtnColorChngr(List<String> dataTables) {
        for (String table : dataTables) {
            switch (table) {
                case "chrgData":
                    _chrgBtn.setBackgroundResource(R.drawable.roundbuttonother);
                    break;
                case "uploadData":
                    _uploadBtn.setBackgroundResource(R.drawable.roundbuttonother);
                    break;
                case "transferData":
                    _transferBtn.setBackgroundResource(R.drawable.roundbuttonother);
                    break;
                case "receiveData":
                    _receiveBtn.setBackgroundResource(R.drawable.roundbuttonother);
                    break;
                default:
                    //default case?
                    break;
            }
        }
    }

    //after send, change module button color back to original
    public static void _moduleBtnColorChngr(Integer tableNum) {
        switch (tableNum) {
            case 1:
                _chrgBtn.setBackgroundResource(R.drawable.roundbutton);
                break;
            case 2:
                _uploadBtn.setBackgroundResource(R.drawable.roundbutton);
                break;
            case 3:
                _transferBtn.setBackgroundResource(R.drawable.roundbutton);
                break;
            case 4:
                _receiveBtn.setBackgroundResource(R.drawable.roundbutton);
                break;
            default:
                // default case?
                break;
        }
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
		makeText(getBaseContext(), getString(R.string.toast_goodbye_message), LENGTH_LONG); //.show();
		
		this.finish();
	}
}