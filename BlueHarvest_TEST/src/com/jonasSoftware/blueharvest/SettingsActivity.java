package com.jonasSoftware.blueharvest;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import static android.widget.Toast.LENGTH_LONG;
import static android.widget.Toast.makeText;

/**
 * @author Brad Bass
 * TODO add ability to change email server settings
 */
public class SettingsActivity extends Activity {
	
	public static String _actName;
	public static String _password;
	public static String _from;
	public static String _to;
	public static String _subject;
	public static String _body;
	//private MenuItem menuItem;
    private TextView actName;
	private TextView password;
	private TextView from;
	private TextView to;
	private TextView subject;
	private TextView body;
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
        setTitle("onas eMail Settings");
		
		actName = (TextView) findViewById(R.id.accountName);
		password = (TextView) findViewById(R.id.accountPassword);
		from = (TextView) findViewById(R.id.emailFrom);
		to = (TextView) findViewById(R.id.emailTo);
		subject = (TextView) findViewById(R.id.emailSubject);
		body = (TextView) findViewById(R.id.emailBody);

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
		//call saveSettings()
		saveSettings();
		
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
		
		// encrypt password - see ChargeActivity to decrypt password
        // Sending side
        _password = crypter.encode(_password);

        //test
        makeText(getApplicationContext(),getString(R.string.toast_encode_message)+_password,LENGTH_LONG).show();

		db.purgeSettings();
		db.saveToDb(_actName, _password, _from, _to, _subject, _body, getApplicationContext());
		makeText(getBaseContext(), getString(R.string.toast_saved_settings_message), LENGTH_LONG).show();
		db.close();
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
        makeText(getApplicationContext(), getString(R.string.toast_decode_message)+_password, LENGTH_LONG).show();

		actName.setText(_actName);
		password.setText(_password);
		from.setText(_from);
		to.setText(_to);
		subject.setText(_subject);
		body.setText(_body);
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
	
	/**
	 * DOES NOT WORK
	 * when user clicks the UI SPACE button, we create a Toast
	 * 
	 * @return      returns keyCode, event
	 */
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
}
