package com.jonasSoftware.blueharvest;

import android.app.Activity;
import android.os.Bundle;

/**
 * Created by Brad on 2/14/2015.
 */
public class ReportActivity extends Activity {

    DatabaseHandler db = new DatabaseHandler(this);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
    }
}