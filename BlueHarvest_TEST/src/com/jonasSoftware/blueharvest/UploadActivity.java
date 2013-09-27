package com.jonasSoftware.blueharvest;

import android.app.Activity;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import android.widget.AdapterView.OnItemSelectedListener;

import java.util.List;

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
    static String _label;
    private static String _upc;
    private static String _date;
    private static String _filename;
    private static String _whse;
    private static String _quantity;

    private Boolean sent = false;
    private Boolean exit = false;
    private Boolean save = false;
    // --Commented out by Inspection (5/15/13 12:43 PM):public String label;
    private String date;
    private String comment;
    Crypter crypter = new Crypter();
    //static ArrayAdapter<String> dataAdapter;

    static EditText installField;
    static EditText quantityField;
    static TextView scanField;
    static String currentDate;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        setTitle("onas Parts Upload");

        //create buttons
        final Button scanBtn = (Button) findViewById(R.id.scanBtn);
        final Button saveBtn = (Button) findViewById(R.id.saveBtn);
        final Button sendBtn = (Button) findViewById(R.id.sendBtn);
        final Button firstBtn = (Button) findViewById(R.id.firstBtn);
        final Button nextBtn = (Button) findViewById(R.id.nextBtn);
        final Button prevBtn = (Button) findViewById(R.id.previousBtn);
        final Button lastBtn = (Button) findViewById(R.id.lastBtn);

        // Spinner element
        spinnerWhse = (Spinner) findViewById(R.id.spinnerWhse);
        // Spinner click listener
        spinnerWhse.setOnItemSelectedListener(this);
        // Loading spinner data from database
        loadSpinnerDataWhse();

        scanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //do stuff
                Intent uploadIntent = new Intent("com.google.zxing.client.android.SCAN");
                uploadIntent.putExtra("SCAN_MODE", "PRODUCT_MODE");
                startActivityForResult(uploadIntent, 0);
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //do stuff
                if(_upc == null) {
                    _upc = scanField.getText().toString();
                }
                DatabaseHandler saveToDb = new DatabaseHandler(getApplicationContext());

                _quantity = quantityField.getText().toString();
                 _whse = spinnerWhse.getSelectedItem().toString();

                saveToDb.saveToDb(_whse, _upc, _quantity, getBaseContext());

                save = true;
                _upc = null;
                scanField.setText(null);
                quantityField.setText(null);
                spinnerWhse.setSelection(0);
            }
        });

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //do stuff
            }
        });

        firstBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //do stuff
            }
        });

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //do stuff
            }
        });

        prevBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //do stuff
            }
        });

        lastBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //do stuff
            }
        });
    }

    private void loadSpinnerDataWhse() {
        // calls method to load spinner
        loadSpinnerData("1");
        // load the spinner
        //spinnerWhse.setAdapter(dataAdapter);
    }

    /**
     * Function to load the spinner data from SQLite database
     * */
    private void loadSpinnerData(String tableName) {
        // load WHSE, COST ITEM and COST TYPE spinners from DB
        //String spinner = tableName;
        // database handler
        DatabaseHandler db = new DatabaseHandler(getApplicationContext());

        // Spinner Drop down elements
        List<String> labels = db.getAllLabels(tableName);

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
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
     * Impelmenters can call getItemAtPosition(position) if they need to access the
     * data associated with the selected item.
     *
     * @param parent   The AdapterView where the selection happened
     * @param view     The view within the AdapterView that was clicked
     * @param position The position of the view in the adapter
     * @param id       The row id of the item that is selected
     */
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        //do stuff
        _label = parent.getItemAtPosition(position).toString();
        ((TextView) parent.getChildAt(0)).setTextColor(0x00000000);
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
}