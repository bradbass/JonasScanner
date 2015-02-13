package com.jonasSoftware.blueharvest;

import android.content.Context;
import android.widget.Toast;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

/**
 * Created by braba on 12/2/2014.
 */
public class WebserviceCall {
    /**
     *
     */
    private final String NAMESPACE = "https://constqa124.jonasportal.com/";
    private final String URL = "https://constqa124.jonasportal.com/emobile/AjaxWebSvc.asmx";
    private final String SOAP_ACTION = "https://constqa124.jonasportal.com/emobile/Authenticate";
    private final String METHOD_NAME = "Authenticate";
    private static String _username;
    private static String _password;
    static Context _context;

    public static void set_username(String username) {
        _username = username;
    }

    public static void set_password(String password) {
        _password = password;
    }

    public static void set_context(Context context) {
        _context = context;
    }

    /**
     *
     * @param context       application context
     * @param username      the username
     * @param password      the password
     * @return              the return value, either valid login or not
     */
    public String authLogin(Context context, String username, String password)
    {
        checkHasNetConn();
        set_context(context);
        set_username(username);
        set_password(password);

        return null;
    }

    private void checkHasNetConn() {
        // check if either wifi or mobile internet is available
        //Internet status flag

        // Connection detector class
        MobileInternetConnectionDetector cd;
        // get Internet status
        // creating connection detector class instance
        cd = new MobileInternetConnectionDetector(_context.getApplicationContext());

        Boolean isConnectionExist = cd.checkMobileInternetConn();
        Boolean isWifiExist = cd.checkWifiInternetConn();

        // check for Internet status
        if (isConnectionExist || isWifiExist) {
            // Internet Connection exists
            authenticate();
        } else {
            // Internet connection doesn't exist
            Toast.makeText(_context.getApplicationContext(),
                    "Your device doesn't have an internet connection", Toast.LENGTH_LONG).show();
        }
    }

    private void authenticate() {
        //
        //Initialize soap request + add parameters
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

        //Use this to add parameters
        request.addProperty("Username", _username);
        request.addProperty("Password", _password);

        //Declare the version of the SOAP request
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

        envelope.setOutputSoapObject(request);
        envelope.dotNet = true;

        try {
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

            //this is the actual part that will call the webservice
            androidHttpTransport.call(SOAP_ACTION, envelope);

            // Get the SoapResult from the envelope body.
            SoapObject result = (SoapObject)envelope.bodyIn;

            if(result != null)
            {
                //Get the first property and change the label text
                result.getProperty(0).toString();
            }
            else
            {
                Toast.makeText(_context.getApplicationContext(), "No Response", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
