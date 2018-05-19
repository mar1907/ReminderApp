package com.example.marius.reminderapp;

import android.app.IntentService;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationManagerCompat;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.util.Date;

import model.Reminder;


public class AlarmIntentService extends IntentService{
    private final int NOTIFICATION_ID = 3;

    public AlarmIntentService() {
        super("AlarmIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Reminder reminder = converToReminder(intent.getByteArrayExtra("Reminder"));

        if(reminder.get_alarm()==0)
            createNotification(reminder);
        else
            //TODO create alarm
            ;
    }

    private void createNotification(Reminder reminder) {
        Notification.Builder builder = new Notification.Builder(this)
                .setContentTitle("ReminderApp")
                .setContentText(reminder.get_text())
                .setVibrate(new long[] { 1000, 1000})
                //TODO change icon
                .setSmallIcon(R.mipmap.ic_delete);

        Intent notifyIntent = new Intent(this, ContentList.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 2, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setContentIntent(pendingIntent);
        Notification notification = builder.build();
        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(this);
        managerCompat.notify(reminder.get_id(), notification);


    }

    private Reminder converToReminder(byte[] array){
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
