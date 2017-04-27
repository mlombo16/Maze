package com.example.marlene.maze;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.widget.ImageView;
import android.widget.TextView;

import static com.example.marlene.maze.R.id.imageView1;
import static com.example.marlene.maze.R.id.text;

public class MainActivity extends AppCompatActivity implements SensorEventListener{

    private SensorManager senSensorManager;
    private Sensor senAccelerometer;

    public float AccX, AccY = 0.0f;
    public float PosX, PosY = 0.0f;
    public float VelX, VelY = 0.0f;

    public float frameTime = 0.7f;
    public float xmax,ymax;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Display display = getWindowManager().getDefaultDisplay();
        xmax = (float) display.getWidth() - 50;
        ymax = (float) display.getHeight() - 260;

        senSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        senAccelerometer = senSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        senSensorManager.registerListener(this, senAccelerometer , SensorManager.SENSOR_DELAY_NORMAL);



    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        Sensor mySensor = sensorEvent.sensor;

        TextView text2 = (TextView)findViewById(R.id.textView2);
        TextView text3 = (TextView)findViewById(R.id.textView3);
        ImageView img = (ImageView) findViewById(R.id.imageView1);


        if (mySensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            //sensor values(x and y) and current time.
            AccX = sensorEvent.values[0];
            AccY = sensorEvent.values[1];

            updateBall();



            img.setX(PosX);
            img.setY(PosY);

        }
    }

    public void updateBall(){

        VelX += (AccX*1.5f * frameTime);
        VelY += (AccY*1.5f * frameTime);


        float xS = (VelX/ 1.5f)*frameTime;
        float yS = (VelY/ 1.5f)*frameTime;

        PosX -= xS;
        PosY += yS;

        if (PosX > xmax) {
            PosX = xmax;
            VelX = 0.0f;

        } else if (PosX < 0) {
            PosX = 0;
            VelX = 0.0f;
        }
        if (PosY > ymax) {
            PosY = ymax;
            VelY = 0.0f;
        } else if (PosY < 0) {
            PosY = 0;
            VelY = 0.0f;
        }



    }


    public void invalidate() {
        invalidate(true);
    }

    @Override


    public boolean onKeyDown(int keyCode) {

        if(moved) {
            //the ball was moved so we'll redraw the view
            invalidate();
            if(maze.isGameComplete()) {
                //game completed
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle(context.getText(R.string.finished_title));
                LayoutInflater inflater = context.getLayoutInflater();
                View view = inflater.inflate(R.layout.finish, null);
                builder.setView(view);
                View closeButton =view.findViewById(R.id.closeGame);
                closeButton.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View clicked) {
                        if(clicked.getId() == R.id.closeGame) {
                            context.finish();
                        }
                    }
                });
                AlertDialog finishDialog = builder.create();
                finishDialog.show();
            }
        }

    }

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
