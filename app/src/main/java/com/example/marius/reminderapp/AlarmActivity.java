package com.example.marius.reminderapp;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

public class AlarmActivity extends AppCompatActivity implements SensorEventListener {

    private Ringtone ringtoneAlarm;
    private SensorManager mSensorManager;
    private Sensor mSensor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON |
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        Intent intent = getIntent();
        String text = intent.getStringExtra("Text");

        System.out.println(text);

        TextView tv = (TextView)findViewById(R.id.reminder_text);
        tv.setText(text);

        Button button = (Button)findViewById(R.id.stop_alarm);
        button.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopAlarm();
            }
        });

        Uri alarmTone = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        ringtoneAlarm = RingtoneManager.getRingtone(getApplicationContext(), alarmTone);
        ringtoneAlarm.play();

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
    }

    private void stopAlarm() {
        Intent intent = new Intent(this, ContentList.class);
        startActivity(intent);
        ringtoneAlarm.stop();
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        Sensor source = sensorEvent.sensor;
        float z = sensorEvent.values[2];

        if(source.getType()==Sensor.TYPE_GRAVITY){
            if(z<=-SensorManager.STANDARD_GRAVITY/2){
                ringtoneAlarm.stop();
                stopAlarm();
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(
                this,
                mSensor,
                SensorManager.SENSOR_DELAY_NORMAL);
    }
}
