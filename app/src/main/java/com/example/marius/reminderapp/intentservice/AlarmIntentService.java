package com.example.marius.reminderapp.intentservice;

import android.app.IntentService;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.app.NotificationManagerCompat;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;

import com.example.marius.reminderapp.R;
import com.example.marius.reminderapp.activity.AlarmActivity;
import com.example.marius.reminderapp.activity.ContentList;
import com.example.marius.reminderapp.model.Reminder;

public class AlarmIntentService extends IntentService{

    public AlarmIntentService() {
        super("AlarmIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Reminder reminder = convertToReminder(intent.getByteArrayExtra("Reminder"));

        if(reminder.get_alarm()==0)
            createNotification(reminder);
        else
            createAlarm(reminder);
    }

    private void createAlarm(Reminder reminders) {
        Intent intent = new Intent(this, AlarmActivity.class);
        intent.putExtra("Text", reminders.get_text());
        startActivity(intent);
    }

    private void createNotification(Reminder reminder) {
        Notification.Builder builder = new Notification.Builder(this)
                .setContentTitle("ReminderApp")
                .setContentText(reminder.get_text())
                .setVibrate(new long[] { 1000, 1000})
                .setColor(0xffc107)
                .setSmallIcon(R.mipmap.ic_access_alarm)
                .setLights(0xffc107, 500, 500);

        Intent notifyIntent = new Intent(this, ContentList.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 2, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setContentIntent(pendingIntent);
        Notification notification = builder.build();
        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(this);
        managerCompat.notify(reminder.get_id(), notification);
    }

    private Reminder convertToReminder(byte[] array){
        ByteArrayInputStream bis = new ByteArrayInputStream(array);
        try {
            ObjectInput in = new ObjectInputStream(bis);
            return (Reminder)in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }
}
