package com.example.marlene.maze;import android.content.Context;import android.graphics.Rect;import android.hardware.Sensor;import android.hardware.SensorEvent;import android.hardware.SensorEventListener;import android.hardware.SensorManager;import android.support.v7.app.AppCompatActivity;import android.os.Bundle;import android.util.DisplayMetrics;import android.view.Display;import android.view.View;import android.widget.Button;import android.widget.ImageView;import android.widget.TextView;import org.w3c.dom.Text;import static com.example.marlene.maze.R.id.btnRestart;import static com.example.marlene.maze.R.id.text2;public class MainActivity extends AppCompatActivity implements SensorEventListener {    public float AccX, AccY = 0.0f;    public float PosX, PosY = 0.0f;    public float VelX, VelY = 0.0f;    public float frameTime = 0.6f;    public float xmax, ymax;    public boolean gameWin = false;    public boolean gameLost = false;    private SensorManager senSensorManager;    private Sensor senAccelerometer;    Button btnRestart;    boolean hit = false;    @Override    protected void onCreate(Bundle savedInstanceState) {        super.onCreate(savedInstanceState);        setContentView(R.layout.activity_main);        Display display = getWindowManager().getDefaultDisplay();        xmax = (float) display.getWidth() - 50;        ymax = (float) display.getHeight() - 260;        senSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);        senAccelerometer = senSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);        senSensorManager.registerListener(this, senAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);        final TextView text2 = (TextView) findViewById(R.id.textView2);        btnRestart = (Button) findViewById(R.id.btnRestart);        btnRestart.setOnClickListener(new View.OnClickListener() {            @Override            public void onClick(View v) {                text2.setText("Clicked!!");                gameWin = false;                ToggleRestart();            }        });    }    @Override    public void onSensorChanged(SensorEvent sensorEvent) {        Sensor mySensor = sensorEvent.sensor;        TextView text3 = (TextView) findViewById(R.id.textView3);        ImageView imgBall = (ImageView) findViewById(R.id.ivBall);        ImageView imgFinish = (ImageView) findViewById(R.id.ivFinish);        if (mySensor.getType() == Sensor.TYPE_ACCELEROMETER) {            //sensor values(x and y) and current time.            AccX = sensorEvent.values[0];            AccY = sensorEvent.values[1];            CheckCollision(imgBall);            updateBall();            imgBall.setX(PosX);            imgBall.setY(PosY);            if (PosY + imgBall.getHeight() > imgFinish.getTop() && PosY < imgFinish.getTop() + imgFinish.getHeight() && PosX + imgBall.getWidth() > imgFinish.getLeft() && PosX < imgFinish.getLeft() + imgFinish.getWidth()) {                gameWin = true;                    text3.setText("triggered but you won");                    imgBall.setX(0);                    imgBall.setY(0);                    AccX = 0.0f;                    AccY = 0.0f;                    PosX = 0.0f;                    PosY = 0.0f;                    VelX = 0.0f;                    VelY = 0.0f;                    ToggleRestart();            } else {                text3.setText("jkjk" + PosY);            }        }    }    public void updateBall() {        VelX += (AccX * 1.5f * frameTime);        VelY += (AccY * 1.5f * frameTime);        float xS = (VelX / 2) * frameTime;        float yS = (VelY / 2) * frameTime;        if (hit == true){            gameLost = true;            ToggleRestart();            AccX = 0.0f;            AccY = 0.0f;            PosX = 0.0f;            PosY = 0.0f;            VelX = 0.0f;            VelY = 0.0f;        }else {            PosX -= xS;            PosY += yS;        }        if (PosX > xmax) {            PosX = xmax;            VelX = 0.0f;        } else if (PosX < 0) {            PosX = 0;            VelX = 0.0f;        }        if (PosY > ymax) {            PosY = ymax;            VelY = 0.0f;        } else if (PosY < 0) {            PosY = 0;            VelY = 0.0f;        }    }    public void onAccuracyChanged(Sensor sensor, int accuracy) {    }    protected void onPause() {        super.onPause();        senSensorManager.unregisterListener(this);    }    protected void onResume() {        super.onResume();        senSensorManager.registerListener(this, senAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);    }   private void ToggleRestart() {       TextView txtMsg = (TextView) findViewById(R.id.txtMsg);       if (gameWin) {           btnRestart.setVisibility(View.VISIBLE);           txtMsg.setText("You won!");           txtMsg.setVisibility(View.VISIBLE);           onPause();           gameWin = false;       } else if (gameLost){           txtMsg.setText("You lost..");           txtMsg.setVisibility(View.VISIBLE);           btnRestart.setVisibility(View.VISIBLE);           onPause();           gameLost = false;       }       else       {           btnRestart.setVisibility(View.GONE);           txtMsg.setVisibility(View.GONE);           onResume();       }   }       void CheckCollision(ImageView img){           ImageView imgBlock = (ImageView) findViewById(R.id.ivBlock);           ImageView imgBlock2 = (ImageView) findViewById(R.id.ivBlock2);           Rect rectBall = new Rect();           Rect rectBlock = new Rect();           Rect rectBlock2 = new Rect();           getHitRect(img, rectBall);           getHitRect(imgBlock, rectBlock);           getHitRect(imgBlock2, rectBlock2);           TextView text2 = (TextView) findViewById(R.id.textView2);           if (Rect.intersects(rectBlock, rectBall) || Rect.intersects(rectBlock2, rectBall)) {               text2.setText("HIT HIT HIT HITH IH!" + hit);               hit = true;           } else {                text2.setText("not touching, sry" + hit);                hit = false;           }       }    private static void getHitRect(View v, Rect rect) {        rect.left = (int) (v.getLeft() + v.getTranslationX());        rect.top = (int) (v.getTop() + v.getTranslationY());        rect.right = rect.left + v.getWidth();        rect.bottom = rect.top + v.getHeight();    }}