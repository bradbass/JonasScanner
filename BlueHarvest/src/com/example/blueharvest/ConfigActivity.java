package com.example.blueharvest;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import android.widget.AdapterView.OnItemSelectedListener;

import java.util.List;

import static android.view.View.OnClickListener;
import static android.widget.Toast.*;

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
public class ConfigActivity extends Activity implements OnItemSelectedListener {
	
	// Spinner element
    private Spinner spinner;

    // Input text
    private EditText inputLabel;

    // Creating Tables
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);
        // Spinner element
        spinner = (Spinner) findViewById(R.id.spinner);
 
        // add button
        Button btnAdd = (Button) findViewById(R.id.btn_add);
        // del button
        Button btnDel = (Button) findViewById(R.id.btn_del);
 
        // new label input field
        inputLabel = (EditText) findViewById(R.id.input_label);
 
        // Spinner click listener
        spinner.setOnItemSelectedListener(this);
 
        // Loading spinner data from database
        loadSpinnerData();
        
        btnDel.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                // On selecting a spinner item
                String label = spinner.getSelectedItem().toString();

                // database handler
                DatabaseHandler db = new DatabaseHandler(
                        getApplicationContext());

                // remove label from database
                db.removeLabel(label);

                inputLabel.setText("");

                loadSpinnerData();
            }
        });
        
        btnAdd.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                String label = inputLabel.getText().toString();

                if (label.trim().length() > 0) {
                    // database handler
                    DatabaseHandler db = new DatabaseHandler(
                            getApplicationContext());

                    // inserting new label into database
                    db.insertLabel(label);

                    // making input field text to blank
                    inputLabel.setText("");

                    // Hiding the keyboard
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(inputLabel.getWindowToken(), 0);

                    // loading spinner with newly added data
                    loadSpinnerData();
                } else {
                    makeText(getApplicationContext(), getString(R.string.toast_label_name_message),
                            LENGTH_SHORT).show();
                }

            }
        });
    }
    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        //inflater.inflate(R.menu.game_menu, menu);
        return true;
    }
    //*/
    /*
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.new_game:
                newGame();
                return true;
            case R.id.help:
                showHelp();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    //*/
    /**
     * Function to load the spinner data from SQLite database
     * */
    private void loadSpinnerData() {
        // database handler
        DatabaseHandler db = new DatabaseHandler(getApplicationContext());

        // Spinner Drop down elements
        List<String> labels = db.getAllLabels();

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, labels);
 
        // Drop down layout style - list view with radio button
        dataAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
 
        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);
    }
 
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position,
            long id) {
        // On selecting a spinner item
        String label = parent.getItemAtPosition(position).toString();
 
        // Showing selected spinner item
        makeText(parent.getContext(), new StringBuilder()
                .append(getString(R.string.toast_you_selected_message))
                .append(label).toString(), LENGTH_LONG)
                .show();
 
    }
 
    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
 
    }
    
    /*
	@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1)
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		
		if(keyCode == KeyEvent.KEYCODE_ENTER)
		{
			this.btnAdd.callOnClick();			
		}
		return false;		
	}
	//*/
}
