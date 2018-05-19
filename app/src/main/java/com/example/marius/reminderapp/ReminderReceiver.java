package com.example.marius.reminderapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import model.Reminder;


public class ReminderReceiver extends BroadcastReceiver {

    public ReminderReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        byte[] reminder = intent.getByteArrayExtra("Reminder");
        Intent intent1 = new Intent(context, AlarmIntentService.class);
        intent1.putExtra("Reminder",reminder);
        context.startService(intent1);
    }
}
