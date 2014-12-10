package com.jonasSoftware.blueharvest;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import static android.widget.Toast.LENGTH_LONG;
import static android.widget.Toast.makeText;

/**
 * @author Brad Bass
 */
public class SettingsActivity extends Activity {
	
	public static String _actName;
	public static String _password;
	public static String _from;
	public static String _to;
	public static String _subject;
	public static String _body;
    public static String _host;
    public static String _port;
	//private MenuItem menuItem;
    private TextView actName;
	private TextView password;
	private TextView from;
	private TextView to;
	private TextView subject;
	private TextView body;
    private TextView host;
    private TextView port;
    private final Crypter crypter;
    private final DatabaseHandler db;

    public SettingsActivity() {
        crypter = new Crypter();
        db = new DatabaseHandler(this);
    }

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
        setTitle(" eMail Settings");
		
		actName = (TextView) findViewById(R.id.accountName);
		password = (TextView) findViewById(R.id.accountPassword);
		from = (TextView) findViewById(R.id.emailFrom);
		to = (TextView) findViewById(R.id.emailTo);
		subject = (TextView) findViewById(R.id.emailSubject);
		body = (TextView) findViewById(R.id.emailBody);
        host = (TextView) findViewById(R.id.emailHost);
        port = (TextView) findViewById(R.id.emailPort);

		db.populateFields();

        populateFields();

    }
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu, menu);
		return true;
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
		//handle toolbar buttons
        String toolbarItem = item.toString();
        if (toolbarItem.equals("SAVE")) {
            saveSettings();
        } else if (toolbarItem.equals("TEST")) {
            sendTestEmail();
        }

        return true;
	}
	
	@SuppressWarnings("ConstantConditions")
    void saveSettings() {
		//save settings to db
		_actName = actName.getText().toString();
		_password = password.getText().toString();
		_from = from.getText().toString();
		_to = to.getText().toString();
		_subject = subject.getText().toString();
		_body = body.getText().toString();
        _host = host.getText().toString();
        _port = port.getText().toString();
		
		// encrypt password - see ChargeActivity to decrypt password
        // Sending side
        _password = crypter.encode(_password);

        //test
        //makeText(getApplicationContext(),getString(R.string.toast_encode_message)+_password,LENGTH_LONG).show();

		db.purgeSettings();
		db.saveToDb(_actName, _password, _from, _to, _subject, _body, _host, _port);
		makeText(getBaseContext(), getString(R.string.toast_saved_settings_message), LENGTH_LONG).show();
		db.close();
        endActivity();
	}
	
	@SuppressWarnings("ConstantConditions")
    void populateFields() {

        // Receiving side
        try {
            _password = crypter.decode(_password);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //test
        //makeText(getApplicationContext(), getString(R.string.toast_decode_message)+_password, LENGTH_LONG).show();

		actName.setText(_actName);
		password.setText(_password);
		from.setText(_from);
		to.setText(_to);
		subject.setText(_subject);
		body.setText(_body);
        host.setText(_host);
        port.setText(_port);
    }

    @SuppressWarnings("ConstantConditions")
    private void sendTestEmail() {
        DatabaseHandler db = new DatabaseHandler(getApplicationContext());
        //db.populateFields();

        _actName = actName.getText().toString();
        _password = password.getText().toString();
        _from = from.getText().toString();
        _to = to.getText().toString();
        _subject = subject.getText().toString();
        _body = body.getText().toString();
        _host = host.getText().toString();
        _port = port.getText().toString();

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        //testing
        //Toast.makeText(getApplicationContext(), getString(R.string.toast_decode_message) + _password, LENGTH_LONG).show();

        Mail m = new Mail(SettingsActivity._actName, _password);
        String[] toArr = SettingsActivity._to.split(";");
        //String[] toArr = { "brad.bass@jonassoftware.com",	"brad.bass@hotmail.ca", "baruch.bass@gmail.com", "tripleb33@hotmail.com" };
        m.setTo(toArr);
        m.setFrom(SettingsActivity._from);
        m.setSubject("This is a test email from Jonas Scanner");
        m.setBody("This is a test email verifying that the email settings in the Jonas Scanner application are functioning as expected.");
        m.setHost(SettingsActivity._host);
        m.setPort(SettingsActivity._port);
        try {
            if (!m.send()) {
                makeText(SettingsActivity.this, getString(R.string.toast_test_email_fail_msg), LENGTH_LONG).show();
            } else {
                makeText(SettingsActivity.this, getString(R.string.toast_test_email_success_msg), LENGTH_LONG).show();
            }
        } catch (final Exception e) {
            //Toast.makeText(MailApp.this, "There was a problem sending the email.", Toast.LENGTH_LONG).show();
            Log.e(getString(R.string.mail_log_e_title), getString(R.string.mail_log_e_message), e);

            e.printStackTrace();
            String stackTrace = Log.getStackTraceString(e);

            AlertDialog.Builder aDB = new AlertDialog.Builder(this);
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
    }

	public void setActName(String _act_name) {
		// Auto-generated method stub
		_actName = _act_name;
	}

	public void setPassword(String _password2) {
		// Auto-generated method stub
		_password = _password2;
	}

	public void setFrom(String _from2) {
		// Auto-generated method stub
		_from = _from2;
	}

	public void setTo(String _to2) {
		// Auto-generated method stub
		_to = _to2;
	}

	public void setSubject(String _subject2) {
		// Auto-generated method stub
		_subject = _subject2;
	}

	public void setBody(String _body2) {
		// Auto-generated method stub
		_body = _body2;
	}

    public void setHost(String _host2) {
        _host = _host2;
    }

    public void setPort(String _port2) {
        _port = _port2;
    }
	
	/**
	 * DOES NOT WORK
	 * when user clicks the UI SPACE button, we create a Toast
	 * 
	 * @return      returns keyCode, event
	 */
	@SuppressWarnings("NullableProblems")
    @Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		
		if(keyCode == KeyEvent.KEYCODE_SPACE)
		{
			makeText(getBaseContext(), "You hit the SPACE key!", LENGTH_LONG).show();
			//_to = _to + ";";
			//to.setText(_to);
		}
		return super.onKeyDown(keyCode, event);		
	}

    void endActivity() {
        this.finish();
    }
}
