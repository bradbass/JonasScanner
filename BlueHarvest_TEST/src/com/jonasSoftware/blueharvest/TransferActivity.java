package com.jonasSoftware.blueharvest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import static android.widget.Toast.LENGTH_LONG;

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

    Crypter crypter = new Crypter();

    static EditText _quantityField;
    static TextView _scanField;
    static EditText _serialField;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer);
        setTitle("onas Whse Transfer");

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
        loadSpinnerDataWhse();

        spinnerToWhse = (Spinner) findViewById(R.id.spinnerWhseTo);
        spinnerToWhse.setOnItemSelectedListener(this);
        loadSpinnerDataWhse();

        scanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO -
                Intent transferIntent = new Intent("com.google.zxing.client.android.SCAN");
                transferIntent.putExtra("SCAN_MODE", "PRODUCT_MODE");
                startActivityForResult(transferIntent, 0);
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO -
            }
        });

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO -
            }
        });

        firstBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO -
            }
        });

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO -
            }
        });

        prevBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO -
            }
        });

        lastBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO -
            }
        });

        delBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO -
            }
        });

        delAllBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO -
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
        spinnerFromWhse.setAdapter(dataAdapter);
        spinnerToWhse.setAdapter(dataAdapter);
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
    }//
}