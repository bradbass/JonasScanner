package com.jonasSoftware.blueharvest;

import android.content.Context;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: braba
 * Date: 25/09/13
 * Time: 11:24 AM
 */
public class UserFunctions {

    private JSONParser jsonParser;

    // Testing in localhost using wamp or xampp
    // use http://10.0.2.2/ to connect to your localhost ie http://localhost/
    private static String loginURL = "http://10.0.2.2/ah_login_api/";
    private static String registerURL = "http://10.0.2.2/ah_login_api/";

    private static String login_tag = "login";
    private static String register_tag = "register";

    // constructor
    public UserFunctions(){
        jsonParser = new JSONParser();
    }

    /**
     * function make Login Request
     * @param username
     * @param password
     * */
    public JSONObject loginUser(String username, String password){
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", login_tag));
        params.add(new BasicNameValuePair("username", username));
        params.add(new BasicNameValuePair("password", password));
        // return json
        // Log.e("JSON", json.toString());
        return jsonParser.getJSONFromUrl(loginURL, params);
    }

    /**
     * function make Login Request
     * @param name
     * @param username
     * @param password
     * */
    public JSONObject registerUser(String name, String username, String password){
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", register_tag));
        params.add(new BasicNameValuePair("name", name));
        params.add(new BasicNameValuePair("username", username));
        params.add(new BasicNameValuePair("password", password));

        // getting JSON Object
        // return json
        return jsonParser.getJSONFromUrl(registerURL, params);
    }

    /**
     * Function get Login status
     * */
    public boolean isUserLoggedIn(Context context){
        DatabaseHandler db = new DatabaseHandler(context);
        int count = db.getRowCount();
        /*
        if(count > 0){
            // user logged in
            return true;
        }//*/
        return count > 0;
    }

    /**
     * Function to logout user
     * Reset Database
     * */
    public boolean logoutUser(Context context){
        DatabaseHandler db = new DatabaseHandler(context);
        db.resetTables();
        return true;
    }

}