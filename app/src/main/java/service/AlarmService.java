package service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.example.marius.reminderapp.ReminderReceiver;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import model.Reminder;

public class AlarmService {

    private Context context;

    public AlarmService(Context context) {
        this.context = context;
    }

    public void deleteReminder(Reminder reminder){
        Intent notifyIntent = new Intent().setClass(context, ReminderReceiver.class);
        notifyIntent.putExtra("Reminder",convertToByteArray(reminder));
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, reminder.get_id(), notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
    }

    public void createReminder(Reminder reminder) {
        Intent notifyIntent = new Intent().setClass(context, ReminderReceiver.class);
        notifyIntent.putExtra("Reminder",convertToByteArray(reminder));
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, reminder.get_id(), notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, reminder.get_time().getTime(), pendingIntent);
    }

    private byte[] convertToByteArray(Serializable s){
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            ObjectOutputStream out = new ObjectOutputStream(bos);
            out.writeObject(s);
            out.flush();
            return bos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
