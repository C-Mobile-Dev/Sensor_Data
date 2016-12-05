package com.dev.kgt.sensor_data;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;

import java.util.List;


public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorMgr;
    private List<Sensor> sensorList;
    private String currentSensor_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sensorMgr =(SensorManager)getSystemService(Context.SENSOR_SERVICE);
        sensorList = sensorMgr.getSensorList(Sensor.TYPE_ALL);
        displayButtons(sensorList);

    }

    protected void onPause(){
        super.onResume();
        //Unregister all active sensors listener
        for(final Sensor sensor : sensorList){
            sensorMgr.unregisterListener(this,sensor);
        }
    }

    protected void onResume(){
        super.onPause();
        //re-register the sensor listeners
        for(final Sensor sensor : sensorList) {
            sensorMgr.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    protected void onStop(){
        super.onStop();
        //Unregister all active sensors listener
        for(final Sensor sensor : sensorList){
            sensorMgr.unregisterListener(this,sensor);
        }
    }


    /* Display the list of available sensors as buttons
     * and set each buttons event actions. Buttons are
     * displayed in grid format.
     *
     *  @param final List<Sensor> list - the list of sensors on device
     */
    public void displayButtons(final List<Sensor> list){

        GridLayout gl = (GridLayout)findViewById(R.id.grid_box);

        for(final Sensor sensors : list){

            Button button = new Button(this);
            button.setText(sensors.getName());
            button.setWidth(getWidth()/2);
            button.setGravity(Gravity.FILL_HORIZONTAL);
            gl.addView(button);

            //register the event listener for all sensors on the device
            sensorMgr.registerListener(this,sensors,SensorManager.SENSOR_DELAY_NORMAL);
            button.setOnClickListener(new View.OnClickListener() {

                /*
                 * Display individual sensor data.
                 */
                @Override
                public void onClick(View v) {

                    //tracks which sensor the user is viewing
                    currentSensor_name = sensors.getName();

                    TextView tx = (TextView)findViewById(R.id.textv);
                    tx.setText(" Sensor Name \t : "+ "\t" + sensors.getName()  + "\n Sensor Vendor : " + "\t"+ sensors.getVendor() );
                    TextView tx1 = (TextView)findViewById(R.id.textv1);
                    tx1.setText(" Max Range \t\t\t : "+ "\t" + sensors.getMaximumRange() + "\n Resolution\t\t\t   : " + "\t"+ sensors.getResolution());
                    TextView tx2 = (TextView)findViewById(R.id.textv2);
                    tx2.setText(" Min Delay \t\t\t   : " + "\t" + sensors.getMinDelay() + "\n Power \t\t\t\t\t     : " +"\t"+ sensors.getPower());
                }
            });
        }

    }
    /* Get the device screen width to help handle
     * dynamic button layout and dimensions.
     *
     * @return - device native screen width in Pixels.
    */
    public int getWidth(){
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager windowMgr = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        windowMgr.getDefaultDisplay().getMetrics(displayMetrics);
        int deviceWidth = displayMetrics.widthPixels;

        return deviceWidth;
    }
    // Call the event listener.
    @Override
    public void onSensorChanged(SensorEvent event) {
        Sensor activeSensor = event.sensor;
        String activeSensor_name = activeSensor.getName();

        TextView tx3 = (TextView)findViewById(R.id.textv3);

        /*if the sensor name is the same as the sensor button clicked
         *display the sensor data.
         */
        if(activeSensor_name == currentSensor_name){
            if(event.values.length == 1){
                TextView tv = new TextView(this);
                tv.setText("Value x: " + String.valueOf(event.values[0]));
                //tx3.setText("Values : " + String.valueOf(event.values[0])+ " " );
            }
            else if(event.values.length == 2){
                tx3.setText("Value x: " + String.valueOf(event.values[1]) + " \nValue y: " + String.valueOf(event.values[1]));
            }
            else if(event.values.length  >= 3){
                tx3.setText("Value x: " + String.valueOf(event.values[0]) + " \nValue y: " + String.valueOf(event.values[1]) + " \nValue z: " + String.valueOf(event.values[2]));
            }

        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        //Adjust sensor accuracy here
    }
}