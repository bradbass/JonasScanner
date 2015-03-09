package com.jonasSoftware.blueharvest;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog.OnDateSetListener;
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
import android.widget.*;
import android.widget.AdapterView.OnItemSelectedListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import static android.widget.Toast.LENGTH_LONG;
import static android.widget.Toast.makeText;
import static android.view.View.OnClickListener;

/**
 * Created with IntelliJ IDEA.
 * User: braba
 * Date: 19/09/13
 * Time: 4:22 PM
 */
public class UploadActivity extends Activity implements OnItemSelectedListener, OnDateSetListener {

    // Spinner element
    private Spinner spinnerWhse;
    //
    private static String _label;
    private static String _upc;
    private static String _filename;
    private static String _whse;
    private static String _quantity;

    //private Boolean sent = false;
    //private Boolean exit = false;
    private Boolean save = false;
    private Boolean isValid = false;
    // --Commented out by Inspection (5/15/13 12:43 PM):public String label;
    private final Crypter crypter = new Crypter();
    //static ArrayAdapter<String> dataAdapter;

    private static EditText _quantityField;
    private static EditText _scanField;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        setTitle(" Parts Upload");

        final DatabaseHandler _db = new DatabaseHandler(getApplicationContext());

        //create buttons
        final ImageButton firstBtn = (ImageButton) findViewById(R.id.firstBtn);
        final ImageButton nextBtn = (ImageButton) findViewById(R.id.nextBtn);
        final ImageButton prevBtn = (ImageButton) findViewById(R.id.previousBtn);
        final ImageButton lastBtn = (ImageButton) findViewById(R.id.lastBtn);
        final ImageButton scanUpcBtn = (ImageButton) findViewById(R.id.scanUpcBtn);

        final Button clrBtn = (Button) findViewById(R.id.clrBtn);
        final Button delBtn = (Button) findViewById(R.id.delOne);

        final TextView partUpcBtn = (TextView) findViewById(R.id.partUpcLabel);

        _scanField = (EditText) findViewById(R.id.scanField);
        _quantityField = (EditText) findViewById(R.id.quantityField);
        _quantityField.setText("1");

        // Spinner element
        spinnerWhse = (Spinner) findViewById(R.id.spinnerWhse);
        // Spinner click listener
        spinnerWhse.setOnItemSelectedListener(this);
        // Loading spinner data from database
        loadSpinnerDataWhse();

        _db.populateDefaults(2);
        populateDefaults();

        partUpcBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Scan.ACTION);
                startActivityForResult(intent, 2);
            }
        });

        scanUpcBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Scan.ACTION);
                startActivityForResult(intent, 2);
            }
        });

        firstBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                //do stuff - table uploadData
                _db.moveToFirst("uploadData", 2);
                populateFields();
            }
        });

        nextBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                //do stuff
                _db.moveToNext("uploadData", getBaseContext(), 2);
                populateFields();
            }
        });

        prevBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                //do stuff
                _db.moveToPrevious("uploadData", getBaseContext(), 2);
                populateFields();
            }
        });

        lastBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                //do stuff
                _db.moveToLast("uploadData", 2);
                populateFields();
            }
        });

        delBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // do stuff
                deleteOne();
                clearFields();
                _quantityField.setText("1");
            }
        });

        clrBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                clearFields();
                _quantityField.setText("1");
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.charge_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // go back to home screen
        String toolbarItem = item.toString();
        switch (toolbarItem) {
            case "HOME":
                endActivity();
                break;
            case "SEND":
                send();
                //testService();
                clearVars();
                clearFields();
                break;
            case "SAVE":
                save();
                break;
        }
        return true;
    }

    private boolean validateFields() {
        // validate the required fields
        if (_whse.equals("")) {
            String field = "Warehouse";
            msgBox(field);
        } else if (_upc.equals("")) {
            String field = "Part # UPC";
            msgBox(field);
        } else if (_quantity.equals("")) {
            String field = "Quantity";
            msgBox(field);
        } else {
            isValid = true;
        }
        return isValid;
    }

    /**
     * if any invalid fields are found, we alert the user
     *
     * @param field is the invalid field
     */
    private void msgBox(String field) {
        //
        AlertDialog.Builder aDB = new AlertDialog.Builder(this);
        aDB.setTitle("Invalid Field Found!");
        aDB.setMessage("The " + field + " field is a required field and must be filled out.");
        aDB.setPositiveButton("OK", new DialogInterface.OnClickListener() {

            @SuppressWarnings("ConstantConditions")
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        aDB.show();
    }

    void deleteAll() {
        final DatabaseHandler dbh = new DatabaseHandler(getApplicationContext());
        AlertDialog.Builder aDB = new AlertDialog.Builder(this);
        aDB.setTitle("Delete All Records?");
        aDB.setMessage("Are you sure you want to delete all the records you've created?");
        aDB.setPositiveButton("YES", new DialogInterface.OnClickListener() {

            @SuppressWarnings("ConstantConditions")
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // When user clicks OK, the db is purged and user is sent back to main activity.
                dbh.deleteAll("uploadData");
                clearVars();
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

    private void clearVars() {
        _quantity = "";
        _upc = "";
        _whse = "";
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
                dbh.deleteOne("uploadData");
                clearVars();
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
        spinnerWhse.setSelection(0);
    }

    private void clearBottomFields() {
        _upc = null;
        _scanField.setText(null);
        _quantityField.setText("1");
    }

    private void populateFields() {
        //populate fields
        _scanField.setText(_upc);
        if (_quantity == null || _quantity.equals("")) {
            _quantityField.setText("1");
        } else {
            _quantityField.setText(_quantity);
        }
        setSpinnerWhse(_whse);
    }

    void populateDefaults() {
        setSpinnerWhse(_whse);
    }

    /**
     *
     *
     * @param valueWhse the selected warehouse
     */
    @SuppressWarnings("ConstantConditions")
    private void setSpinnerWhse(String valueWhse) {
        int index = 0;

        for (int i=0;i<spinnerWhse.getCount();i++) {
            if (spinnerWhse.getItemAtPosition(i).toString().equalsIgnoreCase(valueWhse)) {
                index = i;
                i = spinnerWhse.getCount();
            }
        }
        spinnerWhse.setSelection(index);
    }

    private void loadSpinnerDataWhse() {
        // calls method to load spinner
        loadSpinnerData("1");
        // load the spinner
        //spinnerWhse.setAdapter(dataAdapter);
    }

    /**
     * Function to load the spinner data from SQLite database
     *
     * @param tableName the selected table
     */
    private void loadSpinnerData(String tableName) {
        // load WHSE, COST ITEM and COST TYPE spinners from DB
        //String spinner = tableName;
        // database handler
        DatabaseHandler db = new DatabaseHandler(getApplicationContext());

        // Spinner Drop down elements
        List<String> labels = db.getAllLabels(tableName);

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, labels);

        // Drop down layout style - list view with radio button
        dataAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //spinnerWhse.setAdapter(dataAdapter);
        spinnerWhse.setAdapter(dataAdapter);
        //return dataAdapter;
    }

    /**
     * <p>Callback method to be invoked when an item in this view has been
     * selected. This callback is invoked only when the newly selected
     * position is different from the previously selected position or if
     * there was no selected item.</p>
     * <p/>
     * Implementers can call getItemAtPosition(position) if they need to access the
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
        //_label = parent.getItemAtPosition(position).toString();
        //((TextView) parent.getChildAt(0)).setTextColor(0x00000000);
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

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i2, int i3) {
        //do stuff
    }

    @SuppressWarnings("ConstantConditions")
    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    void send() {
        if ((save == null) || !save) {
            saveMsg();
        } else {
            // Auto-generated method stub
            DatabaseHandler db = new DatabaseHandler(getApplicationContext());
            db.populateFields();

            setDateTime();

            //db.exportDb(getApplicationContext());
            db.exportDb(getApplicationContext(), _filename, 2);
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
                    makeText(UploadActivity.this, getString(R.string.toast_email_fail_message), LENGTH_LONG).show();
                } else {
                    makeText(UploadActivity.this, getString(R.string.toast_email_success_message), LENGTH_LONG).show();
                    //sent = true;
                    db.purgeData("uploadData");
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

            //change home screen module button back to original color
            HomeActivity._moduleBtnColorChngr(2);
        }
    }

    private void save() {
        DatabaseHandler _db = new DatabaseHandler(getApplicationContext());
        if(_upc == null || _upc.equals("")) {
            _upc = _scanField.getText().toString();
        }else if (!_upc.equals(_scanField.getText().toString())) {
            _upc = _scanField.getText().toString();
        }

        _quantity = _quantityField.getText().toString();
        _whse = spinnerWhse.getSelectedItem().toString();

        validateFields();

        if (isValid) {
            _db.saveToDb(_whse, _upc, _quantity);

            save = true;
            clearBottomFields();
        }
    }

    void saveMsg() {
        //for (String table : DatabaseHandler._dataTables) {
        String[] tables = new String[DatabaseHandler._dataTables.size()];
        tables = DatabaseHandler._dataTables.toArray(tables);
        for (String table : tables) {
            if (table.equals("uploadData")) {
                save = true;
                send();
            }
        }
        if (!save) {
            AlertDialog.Builder aDB = new AlertDialog.Builder(this);
            aDB.setTitle(getString(R.string.savemsg_dialog_title));
            aDB.setMessage(getString(R.string.savemsg_window_message));
            aDB.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            aDB.show();
        }
        //}
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

    /**
     * sets the filename to be sent
     *
     * @param currentDateTime   the current date and time from setDateTime()
     * @param context           application context(used for toast)
     */
    private void setFileName(String currentDateTime, Context context) {
        _filename = currentDateTime + getString(R.string.upload_filename_extension);

        makeText(context, getString(R.string.toast_filename_is_label)
                + _filename, LENGTH_LONG)
                .show();
    }

    /**
     *
     * @param label spinner item
     */
    void setLabel(String label) {
        _label = label;
    }

    /**
     *
     * @param upc upc
     */
    public void setUPC(String upc) {
        _upc = upc;
    }

    /**
     *
     * @param whse whse
     */
    public void setWHSE(String whse) {
        _whse = whse;
    }

    /**
     *
     * @param qty qty
     */
    public void setQty(String qty) {
        _quantity = qty;
    }

    /**
     * Initialise the static variable _upc
     *
     * @param scanResult	upc code returned from the scanner
     */
    void setUpc(String scanResult) {
        _upc = scanResult;
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
                String scanResult = intent.getStringExtra(Scan.BARCODE);
                //String format = intent.getStringExtra("SCAN_RESULT_FORMAT");
                // Handle successful scan
                //EditText code =(EditText)findViewById(R.id.scanField);
                _scanField.setText(scanResult);
                setUpc(scanResult);
            } else if (resultCode == RESULT_CANCELED) {
                // Handle cancel
                Toast.makeText(getApplicationContext(),getString(R.string.toast_failed_to_scan_message),LENGTH_LONG).show();
            }
        }
    }

    /**
     * closes the Activity.
     */
    private void endActivity() {
        Intent hi = new Intent(getApplicationContext(), HomeActivity.class);
        hi.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(hi);
        this.finish();
    }

    private void exitApp() {
        makeText(getBaseContext(), getString(R.string.toast_goodbye_message), LENGTH_LONG).show();
        DatabaseHandler db = new DatabaseHandler(getApplicationContext());
        Intent chrgAct = new Intent();
        setResult(RESULT_OK, chrgAct);
        db.purgeData("uploadData");
        db.close();
        finish();
    }

    private void backToMain() {
        //Toast.makeText(getBaseContext(), "Thanks for using BlueHarvest!", Toast.LENGTH_LONG).show();
        //String TABLE_CHRG_DATA = "TABLE_CHRG_DATA";
        DatabaseHandler db = new DatabaseHandler(getApplicationContext());
        //Intent i = new Intent(getApplicationContext(), HomeActivity.class);
        db.purgeData("uploadData");
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
        endActivity();
        /*if ((sent == null) || sent) {
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
        }*/
    }
}