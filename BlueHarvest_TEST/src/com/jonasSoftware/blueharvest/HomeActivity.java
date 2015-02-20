package com.jonasSoftware.blueharvest;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

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
    private static ImageButton _chargeSendAllBtn;
    private static ImageButton _chargeDeleteAllBtn;
    private static ImageButton _uploadSendAllBtn;
    private static ImageButton _uploadDeleteAllBtn;
    private static ImageButton _transferSendAllBtn;
    private static ImageButton _transferDeleteAllBtn;
    private static ImageButton _receiveSendAllBtn;
    private static ImageButton _receiveDeleteAllBtn;
    private static String _filename;
    private final Crypter crypter = new Crypter();
    private Boolean save = false;

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
        setTitle(" Inventory Scanner");

        //buttons
        //final Button configBtn = (Button) findViewById(R.id.configBtn);
        //final Button settingsBtn = (Button) findViewById(R.id.settingsBtn);
        _chrgBtn = (Button) findViewById(R.id.chargeBtn);
        _uploadBtn = (Button) findViewById(R.id.uploadBtn);
        _transferBtn = (Button) findViewById(R.id.transferBtn);
        _receiveBtn = (Button) findViewById(R.id.receiveBtn);
        _chargeSendAllBtn = (ImageButton) findViewById(R.id.chargeSendAllBtn);
        _uploadSendAllBtn = (ImageButton) findViewById(R.id.uploadSendAllBtn);
        _transferSendAllBtn = (ImageButton) findViewById(R.id.transferSendAllBtn);
        _receiveSendAllBtn = (ImageButton) findViewById(R.id.receiveSendAllBtn);
        _chargeDeleteAllBtn = (ImageButton) findViewById(R.id.chargeDeleteAllBtn);
        _uploadDeleteAllBtn = (ImageButton) findViewById(R.id.uploadDeleteAllBtn);
        _transferDeleteAllBtn = (ImageButton) findViewById(R.id.transferDeleteAllBtn);
        _receiveDeleteAllBtn = (ImageButton) findViewById(R.id.receiveDeleteAllBtn);

        //test
        //_chrgBtn.setBackgroundResource(R.color.DataToSendButtonColor);

        //check tables for data.  If data exists, change the button color of the corresponding module and italicise the text
        DatabaseHandler dbh = new DatabaseHandler(getApplicationContext());
        dbh.checkTables();

        if (!DatabaseHandler._dataTables.isEmpty()) {
            List<String> dataTables = DatabaseHandler._dataTables;
            moduleBtnColorChngr(dataTables);
        }

        /*configBtn.setOnClickListener(new OnClickListener() {
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
        });*/

        _chargeSendAllBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // send all
                ChargeActivity ca = new ChargeActivity();
                send(1);
            }
        });

        _uploadSendAllBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                UploadActivity ua = new UploadActivity();
                send(2);
            }
        });

        _transferSendAllBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                TransferActivity ta = new TransferActivity();
                send(3);
            }
        });

        _receiveSendAllBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ReceivePO rpo = new ReceivePO();
                send(4);
            }
        });

        _chargeDeleteAllBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ChargeActivity ca = new ChargeActivity();
                deleteAll(1);
            }
        });

        _uploadDeleteAllBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                UploadActivity ua = new UploadActivity();
                deleteAll(2);
            }
        });

        _transferDeleteAllBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                TransferActivity ta = new TransferActivity();
                deleteAll(3);
            }
        });

        _receiveDeleteAllBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ReceivePO rpo = new ReceivePO();
                deleteAll(4);
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
            case "Configure":
                //launch configActivity
                configScreen();
                break;
            case "Settings":
                //launch settingsActivity
                settingsScreen();
                break;
        }
        return true;
    }

    void about() {
        Intent aboutIntent = new Intent(getApplicationContext(), ReportActivity.class);
        aboutIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(aboutIntent);
        this.finish();
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
        this.finish();
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
        this.finish();
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
        this.finish();
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
        this.finish();
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
        this.finish();
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
        this.finish();
	}

    @SuppressWarnings("ConstantConditions")
    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    void send(final Integer table) {
        AlertDialog.Builder aDB = new AlertDialog.Builder(this);
        aDB.setTitle("Send all?");
        aDB.setMessage("Are you sure you want to send all data from this module?");
        aDB.setPositiveButton("OK", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // When user clicks OK, the db is purged and user is sent back to main activity.
                // Auto-generated method stub
                DatabaseHandler db = new DatabaseHandler(getApplicationContext());
                db.populateFields();

                setDateTime(table);

                //db.exportDb(getApplicationContext());
                db.exportDb(getApplicationContext(), _filename, table);
                //
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);

                // decode password - see Crypter class for methods
                String _password = SettingsActivity._password;
                _password = crypter.decode(_password);

                //testing
                //Toast.makeText(getApplicationContext(), getString(R.string.toast_decode_message) + _password, LENGTH_LONG).show();

                Mail m = new Mail(SettingsActivity._actName, _password);
                String[] toArr = SettingsActivity._to.split(";");
                //String[] toArr = { "brad.bass@jonassoftware.com",	"brad.bass@hotmail.ca", "baruch.bass@gmail.com", "tripleb33@hotmail.com" };
                m.setTo(toArr);
                m.setFrom(SettingsActivity._from);
                m.setSubject(SettingsActivity._subject);
                m.setBody(SettingsActivity._body);
                m.setHost(SettingsActivity._host);
                m.setPort(SettingsActivity._port);
                try {
                    m.addAttachment(Environment.getExternalStorageDirectory().getPath(),_filename);

                    if (!m.send()) {
                        makeText(HomeActivity.this, getString(R.string.toast_email_fail_message), LENGTH_LONG).show();
                    } else {
                        makeText(HomeActivity.this, getString(R.string.toast_email_success_message), LENGTH_LONG).show();
                        Boolean sent = true;
                        db.purgeData("chrgData");
                    }
                } catch (final Exception e) {
                    //Toast.makeText(MailApp.this, "There was a problem sending the email.", Toast.LENGTH_LONG).show();
                    Log.e(getString(R.string.mail_log_e_title), getString(R.string.mail_log_e_message), e);

                    e.printStackTrace();
                    String stackTrace = Log.getStackTraceString(e);

                    AlertDialog.Builder aDB = new AlertDialog.Builder(getApplicationContext());
                    aDB.setTitle("Program Exception!");
                    aDB.setMessage(stackTrace);
                    aDB.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // When user clicks OK, the db is purged and user is sent back to main activity.
                        }
                    });
                    aDB.show();
                }
                db.close();
                //change home screen module button back to original color
                HomeActivity._moduleBtnColorChngr(table);
            }
        });
        aDB.setNegativeButton("NO", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // If user clicks NO, dialog is closed.
                dialog.cancel();
            }
        });
        aDB.show();
        //if ((save == null) || !save) {
            //saveMsg();
        //} else {

        //}
    }

    @SuppressLint("SimpleDateFormat")
    void setDateTime(Integer table) {
        // add DateTime to filename
        Calendar cal = Calendar.getInstance(TimeZone.getDefault());
        Date currentLocalTime = cal.getTime();
        SimpleDateFormat date = new SimpleDateFormat(getString(R.string.filename_simple_date_format));
        date.setTimeZone(TimeZone.getDefault());
        String currentDateTime = date.format(currentLocalTime);

        setFileName(table, currentDateTime, getBaseContext());
    }

    /**
     *
     * @param currentDateTime the current date and time from setDateTime()
     * @param context   application context
     */
    void setFileName(Integer table, String currentDateTime, Context context) {
        //
        switch (table) {
            case 1:
                _filename = currentDateTime + getString(R.string.filename_extension);
                break;
            case 2:
                _filename = currentDateTime + getString(R.string.upload_filename_extension);
                break;
            case 3:
                _filename = currentDateTime + getString(R.string.whse_transfer_filename_extension);
                break;
            case 4:
                _filename = currentDateTime + getString(R.string.po_receive_filename_extension);
                break;
            default:
                break;
        }
        //_filename = currentDateTime + getString(R.string.filename_extension);

        makeText(context, getString(R.string.toast_filename_is_label)
                + _filename, LENGTH_LONG)
                .show();
    }

    void deleteAll(final Integer table) {
        final DatabaseHandler dbh = new DatabaseHandler(getApplicationContext());
        AlertDialog.Builder aDB = new AlertDialog.Builder(this);
        aDB.setTitle("Delete All Records?");
        aDB.setMessage("Are you sure you want to delete all the records from this module?");
        aDB.setPositiveButton("YES", new DialogInterface.OnClickListener() {

            @SuppressWarnings("ConstantConditions")
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // When user clicks OK, the db is purged and user is sent back to main activity.
                switch (table) {
                    case 1:
                        dbh.deleteAll("chrgData");
                        break;
                    case 2:
                        dbh.deleteAll("uploadData");
                        break;
                    case 3:
                        dbh.deleteAll("transferData");
                        break;
                    case 4:
                        dbh.deleteAll("receiveData");
                        break;
                    default:
                        break;
                }
                //dbh.deleteAll("chrgData");
                //clearVars();
                makeText(getApplicationContext(), "All records have been deleted!", LENGTH_LONG).show();
                //change home screen module button back to original color
                HomeActivity._moduleBtnColorChngr(table);
            }
        });
        aDB.setNegativeButton("NO", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // If user clicks NO, dialog is closed.
                dialog.cancel();
            }
        });
        aDB.show();
    }

    // change module button color if data exists in corresponding table
    private void moduleBtnColorChngr(List<String> dataTables) {
        for (String table : dataTables) {
            switch (table) {
                case "chrgData":
                    _chargeSendAllBtn.setBackgroundResource(R.drawable.roundbuttonother);
                    _chargeSendAllBtn.setImageResource(R.drawable.ic_action_email);
                    _chargeDeleteAllBtn.setBackgroundResource(R.drawable.roundbuttonother);
                    _chargeDeleteAllBtn.setImageResource(R.drawable.ic_action_discard);
                    _chargeSendAllBtn.setEnabled(true);
                    _chargeDeleteAllBtn.setEnabled(true);
                    break;
                case "uploadData":
                    _uploadSendAllBtn.setBackgroundResource(R.drawable.roundbuttonother);
                    _uploadSendAllBtn.setImageResource(R.drawable.ic_action_email);
                    _uploadDeleteAllBtn.setBackgroundResource(R.drawable.roundbuttonother);
                    _uploadDeleteAllBtn.setImageResource(R.drawable.ic_action_discard);
                    _uploadSendAllBtn.setEnabled(true);
                    _uploadDeleteAllBtn.setEnabled(true);
                    break;
                case "transferData":
                    _transferSendAllBtn.setBackgroundResource(R.drawable.roundbuttonother);
                    _transferSendAllBtn.setImageResource(R.drawable.ic_action_email);
                    _transferDeleteAllBtn.setBackgroundResource(R.drawable.roundbuttonother);
                    _transferDeleteAllBtn.setImageResource(R.drawable.ic_action_discard);
                    _transferSendAllBtn.setEnabled(true);
                    _transferDeleteAllBtn.setEnabled(true);
                    break;
                case "receiveData":
                    _receiveSendAllBtn.setBackgroundResource(R.drawable.roundbuttonother);
                    _receiveSendAllBtn.setImageResource(R.drawable.ic_action_email);
                    _receiveDeleteAllBtn.setBackgroundResource(R.drawable.roundbuttonother);
                    _receiveDeleteAllBtn.setImageResource(R.drawable.ic_action_discard);
                    _receiveSendAllBtn.setEnabled(true);
                    _receiveDeleteAllBtn.setEnabled(true);
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
                _chargeSendAllBtn.setBackgroundResource(R.drawable.roundbutton);
                _chargeSendAllBtn.setImageResource(R.drawable.ic_action_email_dark);
                _chargeDeleteAllBtn.setBackgroundResource(R.drawable.roundbutton);
                _chargeDeleteAllBtn.setImageResource(R.drawable.ic_action_discard_dark);
                _chargeSendAllBtn.setEnabled(false);
                _chargeDeleteAllBtn.setEnabled(false);
                break;
            case 2:
                _uploadSendAllBtn.setBackgroundResource(R.drawable.roundbutton);
                _uploadSendAllBtn.setImageResource(R.drawable.ic_action_email_dark);
                _uploadDeleteAllBtn.setBackgroundResource(R.drawable.roundbutton);
                _uploadDeleteAllBtn.setImageResource(R.drawable.ic_action_discard_dark);
                _uploadSendAllBtn.setEnabled(false);
                _uploadDeleteAllBtn.setEnabled(false);
                break;
            case 3:
                _transferSendAllBtn.setBackgroundResource(R.drawable.roundbutton);
                _transferSendAllBtn.setImageResource(R.drawable.ic_action_email_dark);
                _transferDeleteAllBtn.setBackgroundResource(R.drawable.roundbutton);
                _transferDeleteAllBtn.setImageResource(R.drawable.ic_action_discard_dark);
                _transferSendAllBtn.setEnabled(false);
                _transferDeleteAllBtn.setEnabled(false);
                break;
            case 4:
                _receiveSendAllBtn.setBackgroundResource(R.drawable.roundbutton);
                _receiveSendAllBtn.setImageResource(R.drawable.ic_action_email_dark);
                _receiveDeleteAllBtn.setBackgroundResource(R.drawable.roundbutton);
                _receiveDeleteAllBtn.setImageResource(R.drawable.ic_action_discard_dark);
                _receiveSendAllBtn.setEnabled(false);
                _receiveDeleteAllBtn.setEnabled(false);
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