package com.example.marlene.accelerometertest;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.ImageView;
import android.widget.TextView;

import static com.example.marlene.accelerometertest.R.id.imageView1;
import static com.example.marlene.accelerometertest.R.id.text;

public class MainActivity extends AppCompatActivity implements SensorEventListener{

    private SensorManager senSensorManager;
    private Sensor senAccelerometer;

    private long lastUpdate = 0;
    private float last_x, last_y, last_z;
    private static final int SHAKE_THRESHOLD = 600;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        senSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        senAccelerometer = senSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        senSensorManager.registerListener(this, senAccelerometer , SensorManager.SENSOR_DELAY_NORMAL);

    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        Sensor mySensor = sensorEvent.sensor;
        TextView text = (TextView)findViewById(R.id.textView);
        TextView text2 = (TextView)findViewById(R.id.textView2);
        TextView text3 = (TextView)findViewById(R.id.textView3);
        ImageView img = (ImageView) findViewById(R.id.imageView1);

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        if (mySensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float x = sensorEvent.values[0];
            float y = sensorEvent.values[1];
            float z = sensorEvent.values[2];
            float tempX;
            float tempY;
            float tempz;


            text.setText("Z:" + Float.toString(z));
            text2.setText("X:" + Float.toString(x));
            text3.setText("Y:" + Float.toString(y));

            tempX = img.getTranslationX();
            tempY = img.getTranslationY();


            //Continue from the getTranslationX and Y above. We need to move the ball from it's existing location using values from -10 to 10. THen we need to Set an obstacle and make colliders. A victory state is needed as well.

            last_x = x;
            last_y = y;
            last_z = z;


            img.setX((metrics.widthPixels / 2 ) + Math.round((last_x))* -10);
            img.setY((metrics.heightPixels / 2 ) + Math.round((last_y)) * 10);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    protected void onPause() {
        super.onPause();
        senSensorManager.unregisterListener(this);
    }

    protected void onResume() {
        super.onResume();
        senSensorManager.registerListener(this, senAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }
}
