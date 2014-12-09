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
import android.widget.*;
import android.widget.AdapterView.OnItemSelectedListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.TimeZone;

import static android.app.AlertDialog.Builder;
import static android.view.View.OnClickListener;
import static android.widget.Toast.LENGTH_LONG;
import static android.widget.Toast.makeText;

/**
 * @author Brad Bass
 * @version 1.0
 *
 * ChargeActivity allows user to scan UPC barcode, select a Name from database (see configActivity),
 * select a date, using a datePickerDialog, and allows them to enter a comment.  All fields are sent
 * to a database and then if/when user clicks on the SEND button, will extract database contents into
 * a .CSV file and then sends this file as an attachment.  
 *
 * @see android.os
 * @see android.app
 * @see android.content
 * @see android.view
 * @see android.widget
 */
@SuppressWarnings("unused")
public class ChargeActivity extends Activity implements OnItemSelectedListener, OnDateSetListener {

	// Spinner element
    private Spinner spinnerWhse;
    private Spinner spinnerItem;
    private Spinner spinnerType;
    private static String _upc;
    private static String _date;
    private static String _filename;
    private static String _wo;
    private static String _whse;
    private static String _item;
    private static String _type;
    private static String _quantity;
    private static String _serial;
    private static String _comment;

    private Boolean exit = false;
    private Boolean save = false;
    private Boolean isValid = false;
    // --Commented out by Inspection (5/15/13 12:43 PM):public String label;
    private String date;
    private String comment;
    private final Crypter crypter = new Crypter();
    //static ArrayAdapter<String> dataAdapter;

    private static EditText _installField;
    private static EditText _jobWoField;
    private static EditText _quantityField;
    private static EditText _serialField;
    private static EditText _commentField;
    private static EditText _scanField;
    private static TextView _dateField;
    private static String _currentDate;

    //DatabaseHandler dbh = new DatabaseHandler(getApplicationContext());

    @SuppressLint({"SimpleDateFormat", "CutPasteId"})
	@Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_charge);
        setTitle(" Charge Parts");

        final DatabaseHandler _db = new DatabaseHandler(getApplicationContext());

		//buttons
		final Button scanBtn = (Button) findViewById(R.id.scanBtn);
		//final Button dateBtn = (Button) findViewById(R.id.dateBtn);
		final Button saveBtn = (Button) findViewById(R.id.saveBtn);
		final Button sendBtn = (Button) findViewById(R.id.sendBtn);
		//final Button backBtn = (Button) findViewById(R.id.btnBack);
		//final Button exitBtn = (Button) findViewById(R.id.btnExit);
        final Button firstBtn = (Button) findViewById(R.id.firstBtn);
        final Button nextBtn = (Button) findViewById(R.id.nextBtn);
        final Button prevBtn = (Button) findViewById(R.id.previousBtn);
        final Button lastBtn = (Button) findViewById(R.id.lastBtn);

        final Button delBtn = (Button) findViewById(R.id.delOne);
        final Button delAllBtn = (Button) findViewById(R.id.delAll);

		//*
		_dateField = (TextView) findViewById(R.id.installField);
		SimpleDateFormat dateFormat = new SimpleDateFormat(getString(R.string.simple_date_format));
		_currentDate = dateFormat.format(new Date());
		_dateField.setText(_currentDate);
		//*/

		_installField = (EditText) findViewById(R.id.installField);
        _jobWoField = (EditText) findViewById(R.id.jobWoField);
        _quantityField = (EditText) findViewById(R.id.quantityField);
        _quantityField.setText("1");
        _serialField = (EditText) findViewById(R.id.serialField);
        _commentField = (EditText) findViewById(R.id.commentField);
		_scanField = (EditText) findViewById(R.id.scanField);

		//comment = _commentField.getText().toString();

        // Spinner element
        spinnerWhse = (Spinner) findViewById(R.id.spinnerWhse);
        spinnerItem = (Spinner) findViewById(R.id.spinnerCostItem);
        spinnerType = (Spinner) findViewById(R.id.spinnerCostType);

        // Spinner click listener
        spinnerWhse.setOnItemSelectedListener(this);
        spinnerItem.setOnItemSelectedListener(this);
        spinnerType.setOnItemSelectedListener(this);

        // Loading spinner data from database
        loadSpinnerDataWhse();
        loadSpinnerDataItem();
        loadSpinnerDataType();

        _installField.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog();
            }
        });

        /**
         * When user clicks the SCAN button, we start a new intent which invokes the 
         * scanner library.  We use .putExtra to send the scan mode, which we set to
         * PRODUCT_MODE, which scans UPC bar codes.
         */
        scanBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// Auto-generated method stub
				Intent chargeIntent = new Intent("com.google.zxing.client.android.SCAN");
					chargeIntent.putExtra("SCAN_MODE", "PRODUCT_MODE");
					startActivityForResult(chargeIntent, 0);

			}
		});

        /**
         * When user clicks the SAVE button, we insert all fields into the database
         */
		saveBtn.setOnClickListener(new OnClickListener() {

			@SuppressWarnings({"StringBufferReplaceableByString", "ConstantConditions"})
            @Override
			public void onClick(View v) {
				// add fields from new table
				if(_date == null) {
					date = _currentDate.replaceAll("\\s+", "").replaceAll("/", "");
                    _date = date;
				}
				if(_upc == null || _upc.equals("")) {
					_upc = _scanField.getText().toString();
				}else if (!_upc.equals(_scanField.getText().toString())) {
                    _upc = _scanField.getText().toString();
                }
				//DatabaseHandler saveToDb = new DatabaseHandler(getApplicationContext());

				comment = _commentField.getText().toString();
                _quantity = _quantityField.getText().toString();
                _wo = _jobWoField.getText().toString();
                _serial = _serialField.getText().toString();
                _whse = spinnerWhse.getSelectedItem().toString();
                _item = spinnerItem.getSelectedItem().toString();
                _type = spinnerType.getSelectedItem().toString();

                validateFields();

                if (isValid) {
                    _db.saveToDb(_whse, _wo, _item, _type, _upc, _quantity,
                            _serial, comment, _date);

                    save = true;
                    clearBottomFields();
                }
            }

            private void clearBottomFields() {
                _upc = null;
                _scanField.setText(null);
                _commentField.setText(null);
                _quantityField.setText("1");
                _serialField.setText(null);
                _dateField.setText(_currentDate);
                spinnerItem.setSelection(0);
            }
        });

		/**
		 * When user clicks the SEND button we first call db.exportDB() from
         * DatabaseHandler which exports the contents of the chrgData table
         * into a .csv file.  We then create a new Object m of Mail class to
         * create and send an email with the csv file as an attachment.
		 */
		sendBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(final View v) {

                send();
                //testService();
                clearFields();
		    }
	    });

        firstBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                // cursor.moveToFirst()
                _db.moveToFirst("chrgData", 1);
                populateFields();
            }
        });

        nextBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                // cursor.moveToNext()
                _db.moveToNext("chrgData", getBaseContext(), 1);
                populateFields();
            }
        });

        prevBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                // cursor.moveToPrevious()
                _db.moveToPrevious("chrgData", getBaseContext(), 1);
                populateFields();
            }
        });

        lastBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                // cursor.moveToLast()
                _db.moveToLast("chrgData", 1);
                populateFields();
            }
        });

        delAllBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //delete all records in db
                deleteAll();
                clearFields();
            }
        });

        delBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //delete only the current record
                deleteOne();
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
        if (_whse.equals("")) {
            field = "Warehouse";
            msgBox(field);
        } else if (_wo.equals("")) {
            field = "Job/WO #";
            msgBox(field);
        } else if (_type.equals("")) {
            field = "Cost Type";
            msgBox(field);
        } else if (_upc.equals("")) {
            field = "Part # UPC";
            msgBox(field);
        } else if (_quantity.equals("")) {
            field = "Quantity";
            msgBox(field);
        } else {
            return isValid = true;
        }
        return isValid = false;
    }

    /**
     * if any invalid fields are found, we alert the user
     *
     * @param field is the invalid field
     */
    private void msgBox(String field) {
        AlertDialog.Builder aDB = new AlertDialog.Builder(this);
        aDB.setTitle("Invalid Field Found!");
        aDB.setMessage("The " + field + " is a required field and must be filled out.");
        aDB.setPositiveButton("OK", new DialogInterface.OnClickListener() {

            @SuppressWarnings("ConstantConditions")
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        aDB.show();
    }

    private void clearFields() {
        _upc = null;
        _scanField.setText(null);
        _commentField.setText(null);
        _jobWoField.setText(null);
        _quantityField.setText(null);
        _serialField.setText(null);
        _dateField.setText(_currentDate);
        spinnerWhse.setSelection(0);
        spinnerItem.setSelection(0);
        spinnerType.setSelection(0);
    }

    private void deleteOne() {
        //
        final DatabaseHandler dbh = new DatabaseHandler(getApplicationContext());
        Builder aDB = new Builder(this);
        aDB.setTitle("Delete Current Record?");
        aDB.setMessage("Are you sure you want to delete the current record?");
        aDB.setPositiveButton("YES", new DialogInterface.OnClickListener() {

            @SuppressWarnings("ConstantConditions")
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // When user clicks OK, the db is purged and user is sent back to main activity.
                dbh.deleteOne("chrgData");
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

    private void clearVars() {
        _quantity = "";
        _wo = "";
        _type = "";
        _comment = "";
        _date = "";
        _item = "";
        _upc = "";
        _whse = "";
        _serial = "";
    }

    private void deleteAll() {
        final DatabaseHandler dbh = new DatabaseHandler(getApplicationContext());
        Builder aDB = new Builder(this);
        aDB.setTitle("Delete All Records?");
        aDB.setMessage("Are you sure you want to delete all the records you've created?");
        aDB.setPositiveButton("YES", new DialogInterface.OnClickListener() {

            @SuppressWarnings("ConstantConditions")
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // When user clicks OK, the db is purged and user is sent back to main activity.
                dbh.deleteAll("chrgData");
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

    void saveMsg() {
        //for (String table : DatabaseHandler._dataTables) {
        String[] tables = new String[DatabaseHandler._dataTables.size()];
        tables = DatabaseHandler._dataTables.toArray(tables);
        for (String table : tables) {
            if (table.equals("chrgData")) {
                save = true;
                send();
            }
        }
        if (!save) {
            Builder aDB = new Builder(this);
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

	//*
	@SuppressLint("SimpleDateFormat")
    void setDateTime() {
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
     * @param currentDateTime the current date and time from setDateTime()
     * @param context   application context
     */
    void setFileName(String currentDateTime, Context context) {
    	//
    	_filename = currentDateTime + getString(R.string.filename_extension);

    	makeText(context, getString(R.string.toast_filename_is_label)
                + _filename, LENGTH_LONG)
                .show();
    }

	/**
	 * When user clicks on the DATE button, we call this method to show the datePicker.
	 *
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    void showDatePickerDialog() {
		DialogFragment newFragment = new DatePickerFragment();
		newFragment.show(getFragmentManager(), "datePicker");
	}

    private void loadSpinnerDataWhse() {
        // calls method to load spinner
        loadSpinnerData("1");
        // load the spinner
        //spinnerWhse.setAdapter(dataAdapter);
    }

    private void loadSpinnerDataItem() {
        loadSpinnerData("2");

        //spinnerItem.setAdapter(dataAdapter);
    }

    private void loadSpinnerDataType() {
        loadSpinnerData("3");

        //spinnerType.setAdapter(dataAdapter);
    }

    /**
     * Function to load the spinner data from SQLite database
     *
     * @param tableName selected table
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
        switch (tableName) {
            case "1":
                spinnerWhse.setAdapter(dataAdapter);
                break;
            case "2":
                spinnerItem.setAdapter(dataAdapter);
                break;
            case "3":
                spinnerType.setAdapter(dataAdapter);
                break;
        }

        //return dataAdapter;
    }

    /**
     * When user selects a name from the spinner.
     *
     * @param parent    AdapterView
     * @param view      View
     * @param position  selected item position
     * @param id        selected item id
     */
    @SuppressWarnings("ConstantConditions")
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position,
            long id) {
        // modify for new spinners
        // On selecting a spinner item
        String label = parent.getItemAtPosition(position).toString();

        setLabel(label);

    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // Auto-generated method stub

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
             Toast.makeText(getApplicationContext(),getString(R.string.toast_failed_to_scan_message),LENGTH_LONG).show();
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
		Log.w("DatePicker",year + " / " + (month+1) + " / " + day);
		((EditText) findViewById(R.id.installField)).setText(year + " / " + (month+1) + " / " + day);
		date = year + "" + (month+1) + "" + day;
		String _month = String.format("%02d", month+1);
		String _day = String.format("%02d", day);
		_date = year + "" + _month + "" + _day;
    }

    /*
    public void testService() {
        WebServiceDemo wsd = new WebServiceDemo();

        setDateTime();

        if(_date == null) {
            date = _currentDate.replaceAll("\\s+", "").replaceAll("/", "");
            _date = date;
        }
        if(_upc == null) {
            _upc = _scanField.getText().toString();
        }
        //DatabaseHandler saveToDb = new DatabaseHandler(getApplicationContext());

        comment = _commentField.getText().toString();
        _quantity = _quantityField.getText().toString();
        _wo = _jobWoField.getText().toString();
        _serial = _serialField.getText().toString();
        _whse = spinnerWhse.getSelectedItem().toString();
        _item = spinnerItem.getSelectedItem().toString();
        _type = spinnerType.getSelectedItem().toString();

        wsd.testService(_whse,_wo,_item,_type,_upc,_quantity,_serial,comment,_date);
    }
    //*/

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
            db.exportDb(getApplicationContext(), _filename, 1);
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
                    makeText(ChargeActivity.this, getString(R.string.toast_email_fail_message), LENGTH_LONG).show();
                } else {
                    makeText(ChargeActivity.this, getString(R.string.toast_email_success_message), LENGTH_LONG).show();
                    Boolean sent = true;
                }
            } catch (final Exception e) {
                //Toast.makeText(MailApp.this, "There was a problem sending the email.", Toast.LENGTH_LONG).show();
                Log.e(getString(R.string.mail_log_e_title), getString(R.string.mail_log_e_message), e);

                e.printStackTrace();
                String stackTrace = Log.getStackTraceString(e);

                Builder aDB = new Builder(this);
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
            db.purgeData("chrgData");
            db.close();

            //change home screen module button back to original color
            HomeActivity._moduleBtnColorChngr(1);
        }
    }

    void populateFields() {
        //
        _scanField.setText(_upc);
        _commentField.setText(_comment);
        _installField.setText(_date);
        _jobWoField.setText(_wo);
        if (_quantity == null || _quantity.equals("")) {
            _quantityField.setText("1");
        } else {
            _quantityField.setText(_quantity);
        }
        _serialField.setText(_serial);
        setSpinnerWhse(_whse);
        setSpinnerItem(_item);
        setSpinnerType(_type);
    }

    /**
     * Initialise the static variable _label
     *
     * @param label		Name that user selected from the spinner
     */
    void setLabel(String label) {

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
     * @param upc   upc code
     */
    public void setUPC(String upc) {
        _upc = upc;
    }

    /**
     *
     * @param date  install date
     */
    public void setDate(String date) {
        _date = date;
    }

    /**
     *
     * @param wo    job/wo number
     */
    public void setWO(String wo) {
        _wo = wo;
    }

    /**
     *
     * @param whse  warehouse
     */
    public void setWHSE(String whse) {
        _whse = whse;
    }

    /**
     *
     * @param item  cost item
     */
    public void setItem(String item) {
        _item = item;
    }

    /**
     *
     * @param type  cost type
     */
    public void setType(String type) {
        _type = type;
    }

    /**
     *
     * @param qty   qty
     */
    public void setQty(String qty) {
        _quantity = qty;
    }

    /**
     *
     * @param serial optional serial number
     */
    public void setSerial(String serial) {
        _serial = serial;
    }

    /**
     *
     * @param comment optional comment
     */
    public void setComment(String comment) {
        _comment = comment;
    }

    /**
     *
     * @param valueWhse the selected warehouse
     */
    @SuppressWarnings("ConstantConditions")
    private void setSpinnerWhse(String valueWhse) {
        //ArrayAdapter spinAdapter = (ArrayAdapter) spinnerWhse.getAdapter();
        //int spinnerPos = spinAdapter.getPosition(valueWhse);
        //spinnerWhse.setSelection(spinnerPos);
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
     * @param valueItem     Cost Item
     */
    @SuppressWarnings("ConstantConditions")
    private void setSpinnerItem(String valueItem) {
        //ArrayAdapter spinAdapter = (ArrayAdapter) spinnerItem.getAdapter();
        //int spinnerPos = spinAdapter.getPosition(valueItem);
        //spinnerItem.setSelection(spinnerPos);
        int index = 0;

        for (int i=0;i<spinnerItem.getCount();i++) {
            if (spinnerItem.getItemAtPosition(i).toString().equalsIgnoreCase(valueItem)) {
                index = i;
                i = spinnerItem.getCount();
            }
        }
        spinnerItem.setSelection(index);
    }

    /**
     *
     * @param valueType     Cost Type
     */
    @SuppressWarnings("ConstantConditions")
    private void setSpinnerType(String valueType) {
        //ArrayAdapter<String> spinAdapter = (ArrayAdapter<String>) spinnerType.getAdapter();
        //int spinnerPos = spinAdapter.getPosition(valueType);
        //spinnerType.setSelection(spinnerPos);
        int index = 0;

        for (int i=0;i<spinnerType.getCount();i++) {
            if (spinnerType.getItemAtPosition(i).toString().equalsIgnoreCase(valueType)) {
                index = i;
                i = spinnerType.getCount();
            }
        }
        spinnerType.setSelection(index);
    }

	/**
	 * closes the ChargeActivity.
	 */
    void endActivity() {
		this.finish();
	}

	void exitApp() {
		makeText(getBaseContext(), getString(R.string.toast_goodbye_message), LENGTH_LONG).show();
		DatabaseHandler db = new DatabaseHandler(getApplicationContext());
		Intent chrgAct = new Intent();
		setResult(RESULT_OK, chrgAct);
		db.purgeData("chrgData");
        db.close();
		finish();
	}

	void backToMain() {
		//Toast.makeText(getBaseContext(), "Thanks for using BlueHarvest!", Toast.LENGTH_LONG).show();
		//String TABLE_CHRG_DATA = "TABLE_CHRG_DATA";
		DatabaseHandler db = new DatabaseHandler(getApplicationContext());
		//Intent i = new Intent(getApplicationContext(), HomeActivity.class);
		db.purgeData("chrgData");
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
            Builder aDB = new Builder(this);
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
        }//*/
    }

    private void isItReallyRandom() {
        System.out.println(randomString(-229985452)+' '+randomString(-147909649));
    }

    public static String randomString(int seed) {
        Random rand = new Random(seed);
        StringBuilder sb = new StringBuilder();
        for(int i=0;;i++) {
            int n = rand.nextInt(27);
            if (n == 0) break;
            sb.append((char) ('`' + n));
        }
        return sb.toString();
    }
}

