package com.jonasSoftware.blueharvest;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Environment;
import android.os.StrictMode;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import java.util.List;

import static android.widget.Toast.LENGTH_LONG;
import static android.widget.Toast.makeText;

/**
 * Created with IntelliJ IDEA.
 * User: Brad
 * Date: 9/19/13
 * Time: 9:35 PM
 *
 */
public class GeneralHelperClass extends ChargeActivity {

    private static DatabaseHandler _db;
    ChargeActivity ch = new ChargeActivity();
    DatabaseHandler db = new DatabaseHandler(getApplicationContext());
    Crypter crypter = new Crypter();
    static String _label;
    static String _upc;
    //static Boolean _sent = false;

    public void loadSpinnerDataWhse() {
        // calls method to load spinner
        loadSpinnerData("1");
    }

    public void loadSpinnerDataItem() {
        loadSpinnerData("2");
    }

    public void loadSpinnerDataType() {
        loadSpinnerData("3");
    }

    /**
     * Initialise the static variable _label
     *
     * @param label		Name that user selected from the spinner
     */
    public void setLabel(String label) {
        _label = label;
    }

    /**
     * Initialise the static variable _upc
     *
     * @param scanResult	upc code returned from the scanner
     */
    public void setUpc(String scanResult) {
        _upc = scanResult;
    }

    /**
     * Function to load the spinner data from SQLite database
     * */
    public void loadSpinnerData(String tableName) {
        // load WHSE, COST ITEM and COST TYPE spinners from DB
        //String spinner = tableName;
        // database handler
        //DatabaseHandler db = new DatabaseHandler(getApplicationContext
        //GeneralHelperClass ghc = new GeneralHelperClass();());

        // Spinner Drop down elements
        List<String> labels = db.getAllLabels(tableName);

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, labels);

        // Drop down layout style - list view with radio button
        dataAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //_spinnerWhse.setAdapter(dataAdapter);
        if (tableName.equals("1")) {
            //ChargeActivity._spinnerWhse.setAdapter(dataAdapter);
        } else if (tableName.equals("2")) {
            //ChargeActivity._spinnerItem.setAdapter(dataAdapter);
        } else if (tableName.equals("3")) {
            //ChargeActivity._spinnerType.setAdapter(dataAdapter);
        }

        //return dataAdapter;
    }

    Boolean send(String _filename, Boolean _save, Boolean _sent) {
        if ((_save == null) || !_save) {
            ch.saveMsg();
        } else {
            // Auto-generated method stub
            //DatabaseHandler db = new DatabaseHandler(getApplicationContext());
            db.populateFields();

            ch.setDateTime();

            //db.exportDb(getApplicationContext());
            db.exportDb(getApplicationContext(), _filename);
            //
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            // decode password - see Crypter class for methods
            String _password = SettingsActivity._password;
            _password = crypter.decode(_password);

            //testing
            Toast.makeText(getApplicationContext(), getString(R.string.toast_decode_message) + _password, LENGTH_LONG).show();

            Mail m = new Mail(SettingsActivity._actName, _password);
            String[] toArr = SettingsActivity._to.split(";");
            //String[] toArr = { "brad.bass@jonassoftware.com",	"brad.bass@hotmail.ca", "baruch.bass@gmail.com", "tripleb33@hotmail.com" };
            m.setTo(toArr);
            m.setFrom(SettingsActivity._from);
            m.setSubject(SettingsActivity._subject);
            m.setBody(SettingsActivity._body);
            try {
                m.addAttachment(Environment.getExternalStorageDirectory().getPath(),_filename);

                if (!m.send()) {
                    makeText(this, getString(R.string.toast_email_fail_message), LENGTH_LONG).show();
                } else {
                    makeText(this, getString(R.string.toast_email_success_message), LENGTH_LONG).show();
                    _sent = true;
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
        }
        return _sent;
    }
}
