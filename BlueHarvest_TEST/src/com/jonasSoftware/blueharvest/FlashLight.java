/*
package com.jonasSoftware.blueharvest;
 */

/**
 * Created by Brad on 2014-12-02.
 */
//public class FlashLight implements SensorEventListener {

    /*private Camera camera;
    private boolean isFlashOn;
    private boolean hasFlash;
    private Camera.Parameters parameter;

    public static void main(String[] args)
    {
        //blah blah foo foo
    }

    //entry point for flash light
    public void flashLight(Context context, Boolean state)
    {
        if (state) {
            //validate
            validateFlash(context);

            if (hasFlash) {
                turnOnFlash();
            }
        } else {
            turnOffFlash();
        }
    }

    //validate camera has flash
    public boolean validateFlash(Context context)
    {
        // First check if device is supporting flashlight or not
        hasFlash = context.getApplicationContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);

        if (!hasFlash) {
            // Device doesn't support flash
            // Show alert message and close the application
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
            alertDialog.setTitle("Error");
            alertDialog.setMessage("Sorry, your device doesn't support flash light!");
            alertDialog.setNegativeButton("OK", (dialog, id) -> dialog.cancel());
            alertDialog.show();
        }

        return hasFlash;
    }

    // Turning On flash
    private void turnOnFlash() {
        if (!isFlashOn) {
            if (camera == null || parameter == null) {
                return;
            }
            parameter = camera.getParameters();
            parameter.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
            camera.setParameters(parameter);
            camera.startPreview();
            isFlashOn = true;
        }

    }

    // Turning Off flash
    private void turnOffFlash() {
        if (isFlashOn) {
            if (camera == null || parameter == null) {
                return;
            }
            parameter = camera.getParameters();
            parameter.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
            camera.setParameters(parameter);
            camera.stopPreview();
            isFlashOn = false;
        }
    }

    // called when sensor value have changed
    @Override
    public void onSensorChanged(SensorEvent event) {
        // The light sensor returns a single value.
        // Many sensors return 3 values, one for each axis.
        if(event.sensor.getType()== Sensor.TYPE_LIGHT){
            float currentLight = event.values[0];
            if(currentLight<5){
                //turn on the light
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    //clean up
}//*/
