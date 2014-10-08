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
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import static android.widget.Toast.LENGTH_LONG;
import static android.widget.Toast.makeText;

/**
 * Created with IntelliJ IDEA.
 * User: Brad
 * Date: 9/19/13
 * Time: 9:25 PM
 */
public class TransferActivity extends Activity implements OnItemSelectedListener {

    // Spinner element
    private Spinner spinnerFromWhse;
    private Spinner spinnerToWhse;
    //
    static String _label;
    private static String _upc;
    private static String _filename;
    private static String _fromWhse;
    private static String _toWhse;
    private static String _quantity;
    private static String _serial;

    private Boolean sent = false;
    private Boolean exit = false;
    private Boolean save = false;

    private final Crypter crypter = new Crypter();

    private static EditText _quantityField;
    private static TextView _scanField;
    private static EditText _serialField;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer);
        setTitle("onas Whse Transfer");

        final DatabaseHandler _db = new DatabaseHandler(getApplicationContext());

        //create buttons
        final Button scanBtn = (Button) findViewById(R.id.scanBtn);
        final Button saveBtn = (Button) findViewById(R.id.saveBtn);
        final Button sendBtn = (Button) findViewById(R.id.sendBtn);
        final Button firstBtn = (Button) findViewById(R.id.firstBtn);
        final Button nextBtn = (Button) findViewById(R.id.nextBtn);
        final Button prevBtn = (Button) findViewById(R.id.previousBtn);
        final Button lastBtn = (Button) findViewById(R.id.lastBtn);
        final Button delBtn = (Button) findViewById(R.id.delBtn);
        final Button delAllBtn = (Button) findViewById(R.id.delAllBtn);

        _scanField = (TextView) findViewById(R.id.partUpcField);
        _quantityField = (EditText) findViewById(R.id.quantityField);
        _serialField = (EditText) findViewById(R.id.serialField);

        spinnerFromWhse = (Spinner) findViewById(R.id.spinnerWhseFrom);
        spinnerFromWhse.setOnItemSelectedListener(this);

        spinnerToWhse = (Spinner) findViewById(R.id.spinnerWhseTo);
        spinnerToWhse.setOnItemSelectedListener(this);

        loadSpinnerDataFromWhse();
        loadSpinnerDataToWhse();

        scanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent transferIntent = new Intent("com.google.zxing.client.android.SCAN");
                transferIntent.putExtra("SCAN_MODE", "PRODUCT_MODE");
                startActivityForResult(transferIntent, 0);
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @SuppressWarnings("ConstantConditions")
            @Override
            public void onClick(View v) {
                if(_upc == null) {
                    _upc = _scanField.getText().toString();
                }
                DatabaseHandler dbh = new DatabaseHandler(getApplicationContext());

                _quantity = _quantityField.getText().toString();
                _toWhse = spinnerToWhse.getSelectedItem().toString();
                _fromWhse = spinnerFromWhse.getSelectedItem().toString();
                _serial = _serialField.getText().toString();

                dbh.saveToDb(_fromWhse, _toWhse, _quantity, _upc, _serial, getBaseContext());

                save = true;
                clearFields();
            }
        });

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                send();
                clearFields();
            }
        });

        firstBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _db.moveToFirst("transferData", 3);
                populateFields();
            }
        });

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _db.moveToNext("transferData", getBaseContext(), 3);
                populateFields();
            }
        });

        prevBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _db.moveToPrevious("transferData", getBaseContext(), 3);
                populateFields();
            }
        });

        lastBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _db.moveToLast("transferData", 3);
                populateFields();
            }
        });

        delBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //
                deleteOne();
                clearFields();
            }
        });

        delAllBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //
                deleteAll();
                clearFields();
            }
        });
    }

    private void deleteAll() {
        final DatabaseHandler dbh = new DatabaseHandler(getApplicationContext());
        AlertDialog.Builder aDB = new AlertDialog.Builder(this);
        aDB.setTitle("Delete All Records?");
        aDB.setMessage("Are you sure you want to delete all the records you've created?");
        aDB.setPositiveButton("YES", new DialogInterface.OnClickListener() {

            @SuppressWarnings("ConstantConditions")
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // When user clicks OK, the db is purged and user is sent back to main activity.
                dbh.deleteAll("transferData");
                makeText(getApplicationContext(), "All records have been deleted!", LENGTH_LONG).show();
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

    private void deleteOne() {
        final DatabaseHandler dbh = new DatabaseHandler(getApplicationContext());
        AlertDialog.Builder aDB = new AlertDialog.Builder(this);
        aDB.setTitle("Delete Current Record?");
        aDB.setMessage("Are you sure you want to delete the current record?");
        aDB.setPositiveButton("YES", new DialogInterface.OnClickListener() {

            @SuppressWarnings("ConstantConditions")
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // When user clicks OK, the db is purged and user is sent back to main activity.
                dbh.deleteOne("transferData");
                makeText(getApplicationContext(), "This record has been deleted!", LENGTH_LONG).show();
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

    private void clearFields() {
        _upc = null;
        _scanField.setText(null);
        _quantityField.setText(null);
        _serialField.setText(null);
        spinnerFromWhse.setSelection(0);
        spinnerToWhse.setSelection(0);
    }

    private void populateFields() {
        //populate fields
        _scanField.setText(_upc);
        _quantityField.setText(_quantity);
        _serialField.setText(_serial);
        setSpinnerFromWhse(_fromWhse);
        setSpinnerToWhse(_toWhse);
    }

    @SuppressWarnings("ConstantConditions")
    private void setSpinnerFromWhse(String valueWhse) {
        int index = 0;

        for (int i=0;i<spinnerFromWhse.getCount();i++) {
            if (spinnerFromWhse.getItemAtPosition(i).toString().equalsIgnoreCase(valueWhse)) {
                index = i;
                i = spinnerFromWhse.getCount();
            }
        }
        spinnerFromWhse.setSelection(index);
    }

    @SuppressWarnings("ConstantConditions")
    private void setSpinnerToWhse(String valueWhse) {
        int index = 0;

        for (int i=0;i<spinnerToWhse.getCount();i++) {
            if (spinnerToWhse.getItemAtPosition(i).toString().equalsIgnoreCase(valueWhse)) {
                index = i;
                i = spinnerToWhse.getCount();
            }
        }
        spinnerToWhse.setSelection(index);
    }

    private void loadSpinnerDataToWhse() {
        // calls method to load spinner
        loadSpinnerData(2);
        // load the spinner
        //spinnerWhse.setAdapter(dataAdapter);
    }

    private void loadSpinnerDataFromWhse() {
        loadSpinnerData(1);
    }

    /**
     * Function to load the spinner data from SQLite database
     * */
    private void loadSpinnerData(Integer column) {
        // load WHSE, COST ITEM and COST TYPE spinners from DB
        //String spinner = tableName;
        // database handler
        DatabaseHandler db = new DatabaseHandler(getApplicationContext());

        // Spinner Drop down elements
        List<String> labels = db.getAllLabels("1");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, labels);
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        if (column == 1) {
            spinnerFromWhse.setAdapter(dataAdapter);
        } else if (column == 2){
            spinnerToWhse.setAdapter(dataAdapter);
        }
    }

    @SuppressWarnings("ConstantConditions")
    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    private void send() {
        if ((save == null) || !save) {
            saveMsg();
        } else {
            // Auto-generated method stub
            DatabaseHandler db = new DatabaseHandler(getApplicationContext());
            db.populateFields();

            setDateTime();

            //db.exportDb(getApplicationContext());
            db.exportDb(getApplicationContext(), _filename, 3);
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
                    makeText(TransferActivity.this, getString(R.string.toast_email_fail_message), LENGTH_LONG).show();
                } else {
                    makeText(TransferActivity.this, getString(R.string.toast_email_success_message), LENGTH_LONG).show();
                    sent = true;
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
            db.purgeData("transferData");
            db.close();
        }
    }

    private void saveMsg() {
        AlertDialog.Builder aDB = new AlertDialog.Builder(this);
        aDB.setTitle(getString(R.string.savemsg_dialog_title));
        aDB.setMessage(getString(R.string.savemsg_window_message));
        aDB.setPositiveButton("OK", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // If user clicks NO, dialog is closed.
                dialog.cancel();
            }
        });
        aDB.show();
    }

    @SuppressLint("SimpleDateFormat")
    private void setDateTime() {
        // add DateTime to filename
        Calendar cal = Calendar.getInstance(TimeZone.getDefault());
        Date currentLocalTime = cal.getTime();
        SimpleDateFormat date = new SimpleDateFormat(getString(R.string.filename_simple_date_format));
        date.setTimeZone(TimeZone.getDefault());
        String currentDateTime = date.format(currentLocalTime);

        setFileName(currentDateTime, getBaseContext());
    }

    private void setFileName(String currentDateTime, Context context) {
        _filename = currentDateTime + getString(R.string.whse_transfer_filename_extension);

        makeText(context, getString(R.string.toast_filename_is_label)
                + _filename, LENGTH_LONG)
                .show();
    }

    /**
     * Initialise the static variable _upc
     *
     * @param scanResult	upc code returned from the scanner
     */
    void setUpc(String scanResult) {
        _upc = scanResult;
    }

    public void setUPC(String upc) {
        _upc = upc;
    }

    public void setFromWHSE(String fromWhse) {
        _fromWhse = fromWhse;
    }

    public void  setToWhse(String toWhse){
        _toWhse = toWhse;
    }

    public void setQty(String qty) {
        _quantity = qty;
    }

    public void setSerial(String serial) {
        _serial = serial;
    }

    void setLabel(String label) {
        _label = label;
    }

    /**
     * When scanner returns a result, we verify ok then send to the text box.
     *
     * @param requestCode   requestCode
     * @param resultCode    resultCode
     * @param intent        intent
     */
    @SuppressWarnings("ConstantConditions")
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                String scanResult = intent.getStringExtra("SCAN_RESULT");
                //String format = intent.getStringExtra("SCAN_RESULT_FORMAT");
                // Handle successful scan
                EditText code =(EditText)findViewById(R.id.partUpcField);
                code.setText(scanResult);
                setUpc(scanResult);
            } else if (resultCode == RESULT_CANCELED) {
                // Handle cancel
                Toast.makeText(getApplicationContext(), getString(R.string.toast_failed_to_scan_message), LENGTH_LONG).show();
            }
        }
    }//

    /**
     * <p>Callback method to be invoked when an item in this view has been
     * selected. This callback is invoked only when the newly selected
     * position is different from the previously selected position or if
     * there was no selected item.</p>
     * <p/>
     * Impelmenters can call getItemAtPosition(position) if they need to access the
     * data associated with the selected item.
     *
     * @param parent   The AdapterView where the selection happened
     * @param view     The view within the AdapterView that was clicked
     * @param position The position of the view in the adapter
     * @param id       The row id of the item that is selected
     */
    @SuppressWarnings("ConstantConditions")
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        //do stuff
        String label = parent.getItemAtPosition(position).toString();
        setLabel(label);
    }

    /**
     * Callback method to be invoked when the selection disappears from this
     * view. The selection can disappear for instance when touch is activated
     * or when the adapter becomes empty.
     *
     * @param parent The AdapterView that now contains no selected item.
     */
    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        //do stuff
    }

    /**
     * closes the Activity.
     */
    private void endActivity() {
        this.finish();
    }

    private void exitApp() {
        makeText(getBaseContext(), getString(R.string.toast_goodbye_message), LENGTH_LONG).show();
        DatabaseHandler db = new DatabaseHandler(getApplicationContext());
        Intent transAct = new Intent();
        setResult(RESULT_OK, transAct);
        db.purgeData("transferData");
        db.close();
        finish();
    }

    private void backToMain() {
        DatabaseHandler db = new DatabaseHandler(getApplicationContext());
        //Intent i = new Intent(getApplicationContext(), HomeActivity.class);
        db.purgeData("transferData");
        db.close();
        endActivity();
    }

    /**
     * When called, will pop an AlertDialog asking user if they are sure they want
     * to exit the screen.  This is attached to UI back button, BACK button and EXIT
     * button.  If user selects YES, we purge the database and send them back to the
     * main activity.
     */
    @Override
    public void onBackPressed() {
        if ((sent == null) || sent) {
            if ((sent == null) || (exit == null) || !exit) {
                backToMain();
            } else {
                exitApp();
            }
        } else {
            AlertDialog.Builder aDB = new AlertDialog.Builder(this);
            aDB.setTitle(getString(R.string.onbackpress_dialog_title));
            aDB.setMessage(getString(R.string.onbackpress_dialog_message));
            aDB.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // When user clicks OK, the db is purged and user is sent back to main activity.
                    if((exit != null) && exit) {
                        exitApp();
                    } else {
                        backToMain();
                    }
                }
            });
            aDB.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // If user clicks NO, dialog is closed.
                    exit = false;
                    dialog.cancel();
                }
            });
            aDB.show();
        }
    }
}