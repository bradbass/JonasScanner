package com.jonasSoftware.blueharvest;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import android.widget.AdapterView.OnItemSelectedListener;

import java.util.List;

import static android.view.View.OnClickListener;
import static android.widget.Toast.LENGTH_SHORT;
import static android.widget.Toast.makeText;

/**
 * @author Brad Bass
 * @version 1.0
 * 
 * ConfigActivity allows user to add additional names to the database.
 *
 * @see android.os
 * @see android.app
 * @see android.content
 * @see android.view
 * @see android.widget
 */
@SuppressWarnings("ConstantConditions")
public class ConfigActivity extends Activity implements OnItemSelectedListener {
	
	// Spinner element
    private Spinner spinner;
    private Spinner spinnerTable;
    private Spinner whseSpinner;
    private Spinner costTypeSpinner;
    private Spinner costItemSpinner;
    private static int tableNum;
    private static String table;
    private static String _label;
    private static String _whse;
    private static String _type;
    private static String _item;
    private static String _jobWo;

    // Input text
    private EditText inputLabel;
    private EditText jobWoField;

    // Creating Tables
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);
        setTitle(" Configuration");
        // Spinner element
        spinner = (Spinner) findViewById(R.id.spinner);
        spinnerTable = (Spinner) findViewById(R.id.spinnerTable);
        whseSpinner = (Spinner) findViewById(R.id.whseSpinner);
        costTypeSpinner = (Spinner) findViewById(R.id.costTypeSpinner);
        costItemSpinner = (Spinner) findViewById(R.id.costItemSpinner);
 
        // add button
        Button btnAdd = (Button) findViewById(R.id.btn_add);
        // del button
        Button btnDel = (Button) findViewById(R.id.btn_del);
 
        // new label input field
        inputLabel = (EditText) findViewById(R.id.input_label);
        jobWoField = (EditText) findViewById(R.id.jobWoFieldConfig);

        whseSpinner.setOnItemSelectedListener(this);
        costTypeSpinner.setOnItemSelectedListener(this);
        costItemSpinner.setOnItemSelectedListener(this);

        // Loading spinner data from database
        loadSpinnerTableData();
        loadSpinnerDataWhse();
        loadSpinnerDataItem();
        loadSpinnerDataType();

        populateFields();
        
        btnDel.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                // On selecting a spinner item
                delLabel();
            }
        });
        
        btnAdd.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                addLabel();
            }
        });

        spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
                _label = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //
            }
        });

        spinnerTable.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
                String label = parent.getItemAtPosition(position).toString();

                switch (label) {
                    case "WHSE":
                        tableNum = 1;

                        break;
                    case "ITEM":
                        tableNum = 2;

                        break;
                    case "TYPE":
                        tableNum = 3;

                        break;
                }

                loadSpinnerData(tableNum);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.config_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // go back to home screen
        String toolbarItem = item.toString();
        if (toolbarItem.equals("HOME")) {
            Intent hi = new Intent(getApplicationContext(), HomeActivity.class);
            hi.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(hi);
            endActivity();
        } else if (toolbarItem.equals("SAVE")) {
            saveToDb();
        }
        return true;
    }

    private void saveToDb() {
        //grab defaults and save to db
        DatabaseHandler db = new DatabaseHandler(getApplicationContext());

        _whse = whseSpinner.getSelectedItem().toString();
        _type = costTypeSpinner.getSelectedItem().toString();
        _item = costItemSpinner.getSelectedItem().toString();
        _jobWo = jobWoField.getText().toString();

        db.saveToDb(_whse, _jobWo, _type, _item, this);
    }

    void delLabel() {
        //String label = spinner.getSelectedItem().toString();
        String table = spinnerTable.getSelectedItem().toString();

        // database handler
        DatabaseHandler db = new DatabaseHandler(
                getApplicationContext());

        switch (table) {
            case "WHSE":
                tableNum = 1;

                break;
            case "ITEM":
                tableNum = 2;

                break;
            case "TYPE":
                tableNum = 3;

                break;
        }

        // remove label from database
        db.removeLabel(tableNum, _label);

        inputLabel.setText("");

        loadSpinnerData(tableNum);
    }

    void addLabel() {
        String label = inputLabel.getText().toString();
        String table = spinnerTable.getSelectedItem().toString();

        if (label.trim().length() > 0) {
            // database handler
            DatabaseHandler db = new DatabaseHandler(
                    getApplicationContext());

            switch (table) {
                case "WHSE":
                    tableNum = 1;

                    break;
                case "ITEM":
                    tableNum = 2;

                    break;
                case "TYPE":
                    tableNum = 3;

                    break;
            }

            // inserting new label into database
            db.insertLabel(tableNum, label);

            // making input field text to blank
            inputLabel.setText("");

            // Hiding the keyboard
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(inputLabel.getWindowToken(), 0);

            // loading spinner with newly added data
            loadSpinnerData(tableNum);
        } else {
            makeText(getApplicationContext(), getString(R.string.toast_label_name_message),
                    LENGTH_SHORT).show();
        }
    }

    private void loadSpinnerTableData() {
        DatabaseHandler db = new DatabaseHandler(getApplicationContext());

        // Spinner Drop down elements
        List<String> labels = db.getAllLabels("0");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, labels);

        // Drop down layout style - list view with radio button
        dataAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinnerTable.setAdapter(dataAdapter);
    }
    /**
     * Function to load the spinner data from SQLite database
     * */
    private void loadSpinnerData(int tableNum) {
        // database handler
        DatabaseHandler db = new DatabaseHandler(getApplicationContext());

        switch (tableNum) {
            case 1:
                table = "1";
                break;
            case 2:
                table = "2";
                break;
            case 3:
                table = "3";
                break;
            default:
                break;
        }

        // Spinner Drop down elements
        List<String> labels = db.getAllLabels(table);

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, labels);
 
        // Drop down layout style - list view with radio button
        dataAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
 
        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);
    }

    //defaults
    void populateFields() {
        //
        DatabaseHandler db = new DatabaseHandler(getApplicationContext());
        db.populateDefaults(5);
        try {
            jobWoField.setText(_jobWo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        setSpinnerWhse(_whse);
        setSpinnerItem(_item);
        setSpinnerType(_type);
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
     *
     * @param valueWhse the selected warehouse
     */
    @SuppressWarnings("ConstantConditions")
    private void setSpinnerWhse(String valueWhse) {
        //ArrayAdapter spinAdapter = (ArrayAdapter) spinnerWhse.getAdapter();
        //int spinnerPos = spinAdapter.getPosition(valueWhse);
        //spinnerWhse.setSelection(spinnerPos);
        int index = 0;

        for (int i=0;i<whseSpinner.getCount();i++) {
            if (whseSpinner.getItemAtPosition(i).toString().equalsIgnoreCase(valueWhse)) {
                index = i;
                i = whseSpinner.getCount();
            }
        }
        whseSpinner.setSelection(index);
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

        for (int i=0;i<costItemSpinner.getCount();i++) {
            if (costItemSpinner.getItemAtPosition(i).toString().equalsIgnoreCase(valueItem)) {
                index = i;
                i = costItemSpinner.getCount();
            }
        }
        costItemSpinner.setSelection(index);
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

        for (int i=0;i<costTypeSpinner.getCount();i++) {
            if (costTypeSpinner.getItemAtPosition(i).toString().equalsIgnoreCase(valueType)) {
                index = i;
                i = costTypeSpinner.getCount();
            }
        }
        costTypeSpinner.setSelection(index);
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
                whseSpinner.setAdapter(dataAdapter);
                break;
            case "2":
                costItemSpinner.setAdapter(dataAdapter);
                break;
            case "3":
                costTypeSpinner.setAdapter(dataAdapter);
                break;
        }

        //return dataAdapter;
    }

    /**
     *
     * @param wo    job/wo number
     */
    public void setWO(String wo) {
        _jobWo = wo;
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

    void endActivity() {
        this.finish();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
