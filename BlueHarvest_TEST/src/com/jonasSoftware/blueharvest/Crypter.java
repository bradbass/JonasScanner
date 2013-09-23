package com.jonasSoftware.blueharvest;

import android.app.Activity;

import java.io.UnsupportedEncodingException;

/**
 * Created with IntelliJ IDEA.
 * User: Brad
 * Date: 5/16/13
 * Time: 8:44 PM
 */
class Crypter extends Activity {

    public String encode(String password) {
        //
        byte[] pw = new byte[0];
        try {
            pw = password.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }

        return Base64.encodeToString(pw, Base64.DEFAULT);
    }

    public String decode(String password) {
        //
        byte[] pw1 = Base64.decode(password, Base64.DEFAULT);
        String dPassword = null;
        try {
            dPassword = new String(pw1, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return dPassword;
    }
}
