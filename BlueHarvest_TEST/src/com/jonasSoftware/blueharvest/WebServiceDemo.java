package com.jonasSoftware.blueharvest;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

public class WebServiceDemo extends Activity
{
    /* Called when the activity is first created. */
    private static String SOAP_ACTION1 = "http://tempuri.org/FahrenheitToCelsius";
    private static String SOAP_ACTION2 = "http://tempuri.org/CelsiusToFahrenheit";
    private static String SOAP_ACTION3 = "http://10.0.2.2:58953/GetInventory";
    private static String NAMESPACE = "http://tempuri.org/";
    private static String NAMESPACE1 = "http://10.0.2.2:58953/";
    private static String METHOD_NAME1 = "FahrenheitToCelsius";
    private static String METHOD_NAME2 = "CelsiusToFahrenheit";
    private static String METHOD_NAME3 = "GetInventory";
    //private static String URL = "http://www.w3schools.com/webservices/tempconvert.asmx?WSDL";
    private static String URL = "http://10.0.2.2:58953/Service1.asmx?WSDL";

    Button btnFar,btnCel,btnClear;
    EditText txtFar,txtCel;

    ConvertFar convertFar = new ConvertFar();

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webservice);

        btnFar = (Button)findViewById(R.id.btnFar);
        btnCel = (Button)findViewById(R.id.btnCel);
        btnClear = (Button)findViewById(R.id.btnClear);
        txtFar = (EditText)findViewById(R.id.txtFar);
        txtCel = (EditText)findViewById(R.id.txtCel);

        btnFar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        //*
                        //Initialize soap request + add parameters
                        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME1);

                        //Use this to add parameters
                        request.addProperty("Fahrenheit",txtFar.getText().toString());

                        //Declare the version of the SOAP request
                        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

                        envelope.setOutputSoapObject(request);
                        envelope.dotNet = true;

                        try {
                            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

                            //this is the actual part that will call the webservice
                            androidHttpTransport.call(SOAP_ACTION1, envelope);

                            // Get the SoapResult from the envelope body.
                            final SoapObject result = (SoapObject)envelope.bodyIn;

                            if(result != null)
                            {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        //Get the first property and change the label text
                                        txtCel.setText(result.getProperty(0).toString());
                                    }
                                });
                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(), "No Response",Toast.LENGTH_LONG).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }//*/
                    }
                }).start();
            }
        });

        btnCel.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        //*
                        //Initialize soap request + add parameters
                        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME2);

                        //Use this to add parameters
                        request.addProperty("Celsius",txtCel.getText().toString());

                        //Declare the version of the SOAP request
                        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

                        envelope.setOutputSoapObject(request);
                        envelope.dotNet = true;

                        try {
                            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

                            //this is the actual part that will call the webservice
                            androidHttpTransport.call(SOAP_ACTION2, envelope);

                            // Get the SoapResult from the envelope body.
                            final SoapObject result = (SoapObject)envelope.bodyIn;

                            if(result != null)
                            {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        //Get the first property and change the label text
                                        txtFar.setText(result.getProperty(0).toString());
                                    }
                                });
                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(), "No Response",Toast.LENGTH_LONG).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }//*/
                    }
                }).start();
            }
        });

        btnClear.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                txtCel.setText("");
                txtFar.setText("");
            }
        });
    }

    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    public void testService(final String whse, final String wo, final String item, final String type,
                            final String upc, final String quantity, final String serial, final String comment,
                            final String date) {
        // test the web service
        /*
        new Thread(new Runnable() {
            @Override
            public void run() {
        //*/
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
                //ChargeActivity ca = new ChargeActivity();
                //*
                //Initialize soap request + add parameters
                SoapObject request = new SoapObject(NAMESPACE1, METHOD_NAME3);

                //Use this to add parameters
                request.addProperty("whse",whse);
                request.addProperty("wo",wo);
                request.addProperty("item",item);
                request.addProperty("type",type);
                request.addProperty("upc",upc);
                request.addProperty("quantity",quantity);
                request.addProperty("serial",serial);
                request.addProperty("comment",comment);
                request.addProperty("date",date);

                //Declare the version of the SOAP request
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

                envelope.setOutputSoapObject(request);
                envelope.dotNet = true;

                try {
                    HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

                    //this is the actual part that will call the webservice
                    androidHttpTransport.call(SOAP_ACTION3, envelope);

                    // Get the SoapResult from the envelope body.
                    SoapObject result = null;
                    if (envelope.bodyIn instanceof SoapFault) {
                        String str = ((SoapFault)envelope.bodyIn).faultstring;
                        //result = (SoapObject)envelope.bodyIn;
                        Log.i("", str);
                    } else {
                        result = (SoapObject)envelope.bodyIn;
                    }

                    if(result != null)
                    {
                        //final SoapObject finalResult = result;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //Get the first property and change the label text
                                //txtFar.setText(finalResult.getProperty(0).toString());
                            }
                        });
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "No Response",Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }//*/
           /*
            }
        }).start();
        //*/
    }

    private class ConvertCel extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... strings) {
            //Initialize soap request + add parameters
            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME2);

            //Use this to add parameters
            request.addProperty("Celsius",txtCel.getText().toString());

            //Declare the version of the SOAP request
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

            envelope.setOutputSoapObject(request);
            envelope.dotNet = true;

            try {
                HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

                //this is the actual part that will call the webservice
                androidHttpTransport.call(SOAP_ACTION2, envelope);

                // Get the SoapResult from the envelope body.
                SoapObject result = (SoapObject)envelope.bodyIn;

                if(result != null)
                {
                    //Get the first property and change the label text
                    txtFar.setText(result.getProperty(0).toString());
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "No Response",Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
        public void convertCel() {
            doInBackground();
        }
    }

    private class ConvertFar extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... strings) {
            //Initialize soap request + add parameters
            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME1);

            //Use this to add parameters
            request.addProperty("Fahrenheit",txtFar.getText().toString());

            //Declare the version of the SOAP request
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

            envelope.setOutputSoapObject(request);
            envelope.dotNet = true;

            try {
                HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

                //this is the actual part that will call the webservice
                androidHttpTransport.call(SOAP_ACTION1, envelope);

                // Get the SoapResult from the envelope body.
                SoapObject result = (SoapObject)envelope.bodyIn;

                if(result != null)
                {
                    //Get the first property and change the label text
                    txtCel.setText(result.getProperty(0).toString());
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "No Response",Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        public void convertFar() {
            doInBackground();
        }
    }
}
