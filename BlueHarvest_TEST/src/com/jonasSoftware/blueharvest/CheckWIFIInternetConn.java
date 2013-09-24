package com.jonasSoftware.blueharvest;

/**
 * Created with IntelliJ IDEA.
 * User: Brad Bass
 * Date: 6/10/13
 * Time: 4:39 PM
 */
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

public class CheckWIFIInternetConn extends Activity {

    //Internet status flag
    Boolean isConnectionExist = false;

    // Connection detector class
    WIFIInternetConnectionDetector cd;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //Button btnStatus = (Button) findViewById(R.id.btn_check);

        // creating connection detector class instance
        cd = new WIFIInternetConnectionDetector(getApplicationContext());

        //Button click listener to check internet status
        //btnStatus.setOnClickListener(new View.OnClickListener() {

            //@Override
            //public void onClick(View v) {

                // get Internet status
                isConnectionExist = cd.checkMobileInternetConn();

                // check for Internet status
                if (isConnectionExist) {
                    // Internet Connection exists
                    showAlertDialog(CheckWIFIInternetConn.this, "Internet Connection",
                            "Your device has WIFI internet access", true);
                } else {
                    // Internet connection doesn't exist
                    showAlertDialog(CheckWIFIInternetConn.this, "No Internet Connection",
                            "Your device doesn't have WIFI internet access", false);
                }
            //}

        //});

    }

    /**
     * Function to display simple Alert Dialog
     * @param context - application context
     * @param title - alert dialog title
     * @param message - alert message
     * @param status - success/failure (used to set icon)
     * */
    public void showAlertDialog(Context context, String title, String message, Boolean status) {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();

        // Setting Dialog Title
        alertDialog.setTitle(title);

        // Setting Dialog Message
        alertDialog.setMessage(message);

        // Setting alert dialog icon
        //alertDialog.setIcon((status) ? R.drawable.success : R.drawable.fail);

        // Setting OK Button
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }
}