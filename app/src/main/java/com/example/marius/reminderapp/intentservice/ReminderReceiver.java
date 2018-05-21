package com.example.marius.reminderapp.intentservice;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

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
