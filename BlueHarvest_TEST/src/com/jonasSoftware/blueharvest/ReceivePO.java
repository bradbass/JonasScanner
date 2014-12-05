package com.jonasSoftware.blueharvest;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.DialogFragment;
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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
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
 * User: brad
 * Date: 23/09/13
 * Time: 11:48 AM
 */
public class ReceivePO extends Activity implements OnItemSelectedListener, OnDateSetListener {

    private Spinner spinnerWhse;

    private static String _label;
    private static String _upc;
    private static String _date;
    private static String _filename;
    private static String _whse;
    private static String _quantity;
    private static String _serial;
    private static String _comment;
    private static String _po;

    private Boolean sent = false;
    private Boolean exit = false;
    private Boolean save = false;
    private Boolean isValid = false;
    private String date;
    //private String comment;
    private final Crypter crypter = new Crypter();

    private static EditText _quantityField;
    private static EditText _serialField;
    private static TextView _commentField;
    private static TextView _scanField;
    private static TextView _dateField;
    private static String _currentDate;
    private static EditText _poField;

    /**
     *
     * @param savedInstanceState    savedInstanceState
     */
    @SuppressLint("CutPasteId")
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receive);
        setTitle("onas Receive P/O");

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

        _dateField = (TextView) findViewById(R.id.receiveDateField);
        SimpleDateFormat dateFormat = new SimpleDateFormat(getString(R.string.simple_date_format));
        _currentDate = dateFormat.format(new Date());
        _dateField.setText(_currentDate);

        EditText _installField = (EditText) findViewById(R.id.receiveDateField);
        _quantityField = (EditText) findViewById(R.id.quantityField);
        _quantityField.setText("1");
        _serialField = (EditText) findViewById(R.id.serialField);
        _commentField = (EditText) findViewById(R.id.commentField);
        _scanField = (EditText) findViewById(R.id.scanField);
        _poField = (EditText) findViewById(R.id.poField);

        spinnerWhse = (Spinner) findViewById(R.id.spinnerWhse);
        spinnerWhse.setOnItemSelectedListener(this);
        loadSpinnerDataWhse();

        _installField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog();
            }
        });

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
                if(_date == null) {
                    date = _currentDate.replaceAll("\\s+", "").replaceAll("/", "");
                    _date = date;
                }
                if(_upc == null) {
                    _upc = _scanField.getText().toString();
                }

                DatabaseHandler dbh = new DatabaseHandler(getApplicationContext());

                _quantity = _quantityField.getText().toString();
                _whse = spinnerWhse.getSelectedItem().toString();
                _serial = _serialField.getText().toString();
                _comment = _commentField.getText().toString();
                _po = _poField.getText().toString();

                validateFields();

                if (isValid) {
                    dbh.saveToDb(_whse, _quantity, _upc, _serial, _date, _comment, _po, getBaseContext());

                    save = true;
                    clearBottomFields();
                }
            }

            private void clearBottomFields() {
                _upc = null;
                _scanField.setText(null);
                _dateField.setText(_currentDate);
                _quantityField.setText("1");
                _serialField.setText(null);
                _commentField.setText(null);
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
                _db.moveToFirst("receiveData", 4);
                populateFields();
            }
        });

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _db.moveToNext("receiveData", getBaseContext(), 4);
                populateFields();
            }
        });

        prevBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _db.moveToPrevious("receiveData", getBaseContext(), 4);
                populateFields();
            }
        });

        lastBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _db.moveToLast("receiveData", 4);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.charge_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // go back to home screen
        endActivity();
        return true;
    }

    private boolean validateFields() {
        // validate the required fields
        String field;
        if (_date.equals("")) {
            field = "Date";
            isValid = false;
            msgBox(field);
        } else if (_whse.equals("")) {
            field = "Warehouse";
            isValid = false;
            msgBox(field);
        } else if (_po.equals("")) {
            field = "P.O.#";
            isValid = false;
            msgBox(field);
        } else if (_upc.equals("")) {
            field = "Part # UPC";
            isValid = false;
            msgBox(field);
        } else if (_quantity.equals("")) {
            field = "Quantity";
            isValid = false;
            msgBox(field);
        } else {
            isValid = true;
        }
        return isValid;
    }

    private void msgBox(String msg) {
        //
        AlertDialog.Builder aDB = new AlertDialog.Builder(this);
        aDB.setTitle("Invalid Field Found!");
        aDB.setMessage("The " + msg + " field is a required field and must be filled out.");
        aDB.setPositiveButton("OK", new DialogInterface.OnClickListener() {

            @SuppressWarnings("ConstantConditions")
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        aDB.show();
    }

    /**
     *
     */
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
                dbh.deleteAll("receiveData");
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

    /**
     *
     */
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
                dbh.deleteOne("receiveData");
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

    /**
     *
     */
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
            db.exportDb(getApplicationContext(), _filename, 4);
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
            try {
                m.addAttachment(Environment.getExternalStorageDirectory().getPath(),_filename);

                if (!m.send()) {
                    makeText(ReceivePO.this, getString(R.string.toast_email_fail_message), LENGTH_LONG).show();
                } else {
                    makeText(ReceivePO.this, getString(R.string.toast_email_success_message), LENGTH_LONG).show();
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
            db.purgeData("receiveData");
            db.close();

            //change home screen module button back to original color
            HomeActivity._moduleBtnColorChngr(4);
        }
    }

    /**
     *
     */
    void saveMsg() {
        //for (String table : DatabaseHandler._dataTables) {
        String[] tables = new String[DatabaseHandler._dataTables.size()];
        tables = DatabaseHandler._dataTables.toArray(tables);
        for (String table : tables) {
            if (table.equals("receiveData")) {
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

    /**
     *
     */
    private void clearFields() {
        _upc = null;
        _scanField.setText(null);
        _quantityField.setText(null);
        _serialField.setText(null);
        _commentField.setText(null);
        _poField.setText(null);
        _dateField.setText(_currentDate);
        spinnerWhse.setSelection(0);
    }

    /**
     *
     */
    private void populateFields() {
        //populate fields
        _scanField.setText(_upc);
        if (_quantity == null || _quantity.equals("")) {
            _quantityField.setText("1");
        } else {
            _quantityField.setText(_quantity);
        }
        _serialField.setText(_serial);
        _poField.setText(_po);
        _commentField.setText(_comment);
        _dateField.setText(_date);
        setSpinnerWhse(_whse);
    }

    /**
     *
     * @param valueWhse     the selected warehouse
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

    /**
     *
     */
    private void loadSpinnerDataWhse() {
        loadSpinnerData();
    }

    /**
     * Function to load the spinner data from SQLite database
     * */
    private void loadSpinnerData() {
        // load WHSE, COST ITEM and COST TYPE spinners from DB
        //String spinner = tableName;
        // database handler
        DatabaseHandler db = new DatabaseHandler(getApplicationContext());

        // Spinner Drop down elements
        List<String> labels = db.getAllLabels("1");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, labels);
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerWhse.setAdapter(dataAdapter);
    }

    /**
     * When user clicks on the DATE button, we call this method to show the datePicker.
     *
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    void showDatePickerDialog() {
        DialogFragment newFragment = new DatePickerFragment2();
        newFragment.show(getFragmentManager(), "datePicker");
    }

    /**
     *
     */
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
     *
     * @param currentDateTime   the current date and time from setDateTime()
     * @param context           application context
     */
    private void setFileName(String currentDateTime, Context context) {
        _filename = currentDateTime + getString(R.string.po_receive_filename_extension);

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

    /**
     *
     * @param upc       n/a
     */
    public void setUPC(String upc) {
        _upc = upc;
    }

    /**
     *
     * @param fromWhse  warehouse the part is coming from
     */
    public void setWHSE(String fromWhse) {
        _whse = fromWhse;
    }

    /**
     *
     * @param qty       the entered quantity
     */
    public void setQty(String qty) {
        _quantity = qty;
    }

    /**
     *
     * @param serial        the optional serial number
     */
    public void setSerial(String serial) {
        _serial = serial;
    }

    /**
     *
     * @param comment       the optional comment
     */
    public void setComment(String comment) {
        _comment = comment;
    }

    /**
     *
     * @param date      the selected date
     */
    public void setDate(String date) {
        _date = date;
    }

    /**
     *
     * @param po        the po number
     */
    public void setPo(String po) {
        _po = po;
    }

    /**
     *
     * @param label     the currently selected dropbox item
     */
    void setLabel(String label) {
        _label = label;
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
        // modify for new spinners
        // On selecting a spinner item
        _label = parent.getItemAtPosition(position).toString();

        setLabel(_label);
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
                EditText code =(EditText)findViewById(R.id.scanField);
                code.setText(scanResult);
                setUpc(scanResult);
            } else if (resultCode == RESULT_CANCELED) {
                // Handle cancel
                Toast.makeText(getApplicationContext(), getString(R.string.toast_failed_to_scan_message), LENGTH_LONG).show();
            }
        }
    }

    /**
     * When user selects a date from the datePicker, we first set the contents of the _dateField
     * with the date the user selected.  We then format the date string to be sent to the database.
     * we then initialise the static variable _date to the new formatted date.
     *
     * @param view      view
     * @param year		the year returned from the datePicker
     * @param month		the month returned from the datePicker
     * @param day		the day returned from the datePicker
     */
    //@SuppressLint("DefaultLocale")
    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        // Auto-generated method stub
        Log.w("DatePicker", year + " / " + (month + 1) + " / " + day);
        ((EditText) findViewById(R.id.receiveDateField)).setText(year + " / " + (month+1) + " / " + day);
        date = year + "" + (month+1) + "" + day;
        String _month = String.format("%02d", month+1);
        String _day = String.format("%02d", day);
        _date = year + "" + _month + "" + _day;
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
     * closes the ChargeActivity.
     */
    void endActivity() {
        this.finish();
    }

    /**
     *
     */
    void exitApp() {
        makeText(getBaseContext(), getString(R.string.toast_goodbye_message), LENGTH_LONG).show();
        DatabaseHandler db = new DatabaseHandler(getApplicationContext());
        Intent recAct = new Intent();
        setResult(RESULT_OK, recAct);
        db.purgeData("receiveData");
        db.close();
        finish();
    }

    /**
     *
     */
    void backToMain() {
        //Toast.makeText(getBaseContext(), "Thanks for using BlueHarvest!", Toast.LENGTH_LONG).show();
        //String TABLE_CHRG_DATA = "TABLE_CHRG_DATA";
        DatabaseHandler db = new DatabaseHandler(getApplicationContext());
        //Intent i = new Intent(getApplicationContext(), HomeActivity.class);
        db.purgeData("receiveData");
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