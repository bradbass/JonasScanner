package com.jonasSoftware.blueharvest;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

/**
 * Created by Brad on 2/14/2015.
 */
public class AboutActivity extends Activity {

    private static ListView _listView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);


    }
}