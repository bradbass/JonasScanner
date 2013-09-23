package com.jonasSoftware.blueharvest;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
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
public class ConfigActivity extends Activity {
	
	// Spinner element
    private Spinner spinner;
    private Spinner spinnerTable;
    public static int tableNum;
    public static String table;
    public static String _label;

    // Input text
    private EditText inputLabel;

    // Creating Tables
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);
        setTitle("onas Config");
        // Spinner element
        spinner = (Spinner) findViewById(R.id.spinner);
        spinnerTable = (Spinner) findViewById(R.id.spinnerTable);
 
        // add button
        Button btnAdd = (Button) findViewById(R.id.btn_add);
        // del button
        Button btnDel = (Button) findViewById(R.id.btn_del);
 
        // new label input field
        inputLabel = (EditText) findViewById(R.id.input_label);

        // Loading spinner data from database
        loadSpinnerTableData();
        
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

                if (label.equals("WHSE")) {
                    tableNum = 1;

                } else if (label.equals("ITEM")) {
                    tableNum = 2;

                } else if (label.equals("TYPE")) {
                    tableNum = 3;

                }

                loadSpinnerData(tableNum);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //
            }
        });
    }

    public void delLabel() {
        //String label = spinner.getSelectedItem().toString();
        String table = spinnerTable.getSelectedItem().toString();

        // database handler
        DatabaseHandler db = new DatabaseHandler(
                getApplicationContext());

        if (table.equals("WHSE")) {
            tableNum = 1;

        } else if (table.equals("ITEM")) {
            tableNum = 2;

        } else if (table.equals("TYPE")) {
            tableNum = 3;

        }

        // remove label from database
        db.removeLabel(tableNum, _label);

        inputLabel.setText("");

        loadSpinnerData(tableNum);
    }

    public void addLabel() {
        String label = inputLabel.getText().toString();
        String table = spinnerTable.getSelectedItem().toString();

        if (label.trim().length() > 0) {
            // database handler
            DatabaseHandler db = new DatabaseHandler(
                    getApplicationContext());

            if (table.equals("WHSE")) {
                tableNum = 1;

            } else if (table.equals("ITEM")) {
                tableNum = 2;

            } else if (table.equals("TYPE")) {
                tableNum = 3;

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
}
