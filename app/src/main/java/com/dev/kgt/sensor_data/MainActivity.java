package com.dev.kgt.sensor_data;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;


public class MainActivity extends AppCompatActivity  {

    SensorManager sensorMgr;
    List<Sensor> sensorList;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sensorMgr =(SensorManager)getSystemService(Context.SENSOR_SERVICE);
        sensorList = sensorMgr.getSensorList(Sensor.TYPE_ALL);
        displayButtons(sensorList);
    }

    /* Display the list of available sensors as buttons
     * and set each buttons event actions. Buttons are
     * displayed in grid format.
     *
     *  @param final List<Sensor> list - the list of sensors on device
     */
    public void displayButtons(final List<Sensor> list){

        GridLayout gl = (GridLayout)findViewById(R.id.grid_box);

        for(final Sensor sensor : list){

            Button button = new Button(this);
            button.setText(sensor.getName());
            button.setWidth(getWidth()/2);
            button.setGravity(Gravity.FILL_HORIZONTAL);
            gl.addView(button);

            button.setOnClickListener(new View.OnClickListener() {
                /*
                 * Display individual sensor data.
                 */
                @Override
                public void onClick(View v) {
                    TextView tx = (TextView)findViewById(R.id.textv);
                    tx.setText(" Sensor Name \t : "+ "\t" + sensor.getName()  + "\n Sensor Vendor : " + "\t"+ sensor.getVendor() );
                    TextView tx1 = (TextView)findViewById(R.id.textv1);
                    tx1.setText(" Max Range \t\t\t : "+ "\t" + sensor.getMaximumRange() + "\n Resolution\t\t\t   : " + "\t"+ sensor.getResolution());
                    TextView tx2 = (TextView)findViewById(R.id.textv2);
                    tx2.setText(" Min Delay \t\t\t   : " + "\t" + sensor.getMinDelay() + "\n Power \t\t\t\t\t     : " +"\t"+ sensor.getPower());
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
}
