package com.jonasSoftware.blueharvest;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import java.io.File;
//import android.webkit.WebView;

/**
 * Created by braba on 3/5/2015.
 */
public class HelpActivity extends Activity {

    //private WebView webView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        File file = new File("/sdcard/example.pdf");

        if (file.exists()) {
            Uri path = Uri.fromFile(file);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(path, "application/pdf");
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            try {
                startActivity(intent);
            }
            catch (ActivityNotFoundException e) {
                Toast.makeText(HelpActivity.this,
                        "No Application Available to View PDF",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }
}