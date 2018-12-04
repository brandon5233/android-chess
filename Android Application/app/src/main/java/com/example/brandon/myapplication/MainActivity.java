package com.example.brandon.myapplication;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import static java.lang.Boolean.TRUE;


// Welcome to minesweeper
//Board size is 10x10 with 20 MINES and 20 FLAGS
//user WINS when he uncovers all 80 numbered blocks
public class MainActivity extends AppCompatActivity {
    boolean reset;
    Button button_reset, button_flag;
    CustomView view;
    static TextView flagcounter;
    private SensorManager sm;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        flagcounter = (TextView)findViewById(R.id.tv_flag_counter);
        view = (CustomView)findViewById(R.id.custom_view);
        sm = (SensorManager) getSystemService(SENSOR_SERVICE);

        button_reset = (Button) findViewById(R.id.button_reset);
        button_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reset = TRUE;
                view.reset();
            }
        });

        button_flag = (Button) findViewById(R.id.button_flag);
        button_flag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (view.flag) button_flag.setText(R.string.button_flag_off);
                else button_flag.setText(R.string.button_flag_on);
                view.flag = !view.flag;
            }
        });

        //uses accelerometer to reset game if shake in any direction with acceleration>1G
      /*  sm.registerListener(new SensorEventListener() {
                                public void onAccuracyChanged(Sensor sensor, int accuracy) {
                                }
                                public void onSensorChanged(SensorEvent event) {

                                    if(event.values[0]>=9.81 && event.values[1]>=9.81 || event.values[1]>=9.81 && event.values[2]>=9.81 || event.values[2]>=9.81 && event.values[0]>=9.81)
                                    {
                                        reset=TRUE;
                                        view.reset();
                                    }
                                }
                            }, sm.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION),
                SensorManager.SENSOR_DELAY_UI);*/
    }
}



