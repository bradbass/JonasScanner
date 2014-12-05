package com.jonasSoftware.blueharvest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created with IntelliJ IDEA.
 * User: braba
 * Date: 24/09/13
 * Time: 6:18 PM
 */
public class LoginActivity extends Activity {

    // JSON Response node names
    private static String KEY_SUCCESS = "success";
    private static String KEY_ERROR = "error";
    private static String KEY_ERROR_MSG = "error_msg";
    private static String KEY_UID = "uid";
    private static String KEY_NAME = "name";
    private static String KEY_USERNAME = "username";
    private static String KEY_CREATED_AT = "created_at";

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setTitle("onas Scanner Login");

        final Button loginBtn = (Button) findViewById(R.id.btnLogin);
        final EditText loginUsername = (EditText) findViewById(R.id.loginUsername);
        final EditText loginPassword = (EditText) findViewById(R.id.loginPassword);
        final TextView loginErrorMsg = (TextView) findViewById(R.id.login_error);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //do stuff
                String username = loginUsername.getText().toString();
                String password = loginPassword.getText().toString();

                Intent homeActivity = new Intent(getApplicationContext(), HomeActivity.class);
                homeActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(homeActivity);

                /*
                // if GJSYSTEM bypass login check
                if (username == "GJSYSTEM" && password == "W!NE") {
                    // log in
                    Intent homeActivity = new Intent(getApplicationContext(), HomeActivity.class);
                    homeActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(homeActivity);
                } else {
                    UserFunctions userFunction = new UserFunctions();
                    JSONObject json = userFunction.loginUser(username, password);

                    // check for login response
                    try {
                        if (json.getString(KEY_SUCCESS) != null) {
                            loginErrorMsg.setText("");
                            String res = json.getString(KEY_SUCCESS);
                            if(Integer.parseInt(res) == 1){
                                // user successfully logged in
                                // Store user details in SQLite Database
                                DatabaseHandler db = new DatabaseHandler(getApplicationContext());
                                JSONObject json_user = json.getJSONObject("user");

                                // Clear all previous data in database
                                userFunction.logoutUser(getApplicationContext());
                                db.addUser(json_user.getString(KEY_NAME), json_user.getString(KEY_USERNAME), json.getString(KEY_UID), json_user.getString(KEY_CREATED_AT));

                                // Launch Dashboard Screen
                                Intent homeActivity = new Intent(getApplicationContext(), HomeActivity.class);

                                // Close all views before launching Dashboard
                                homeActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(homeActivity);

                                // Close Login Screen
                                finish();
                            }else{
                                // Error in login
                                loginErrorMsg.setText("Incorrect username/password");
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }//*/
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
        this.finish();
        return true;
    }
}