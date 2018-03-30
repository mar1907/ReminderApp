package service;

import android.support.v4.util.Pair;

import java.text.SimpleDateFormat;
import java.util.List;

import model.Reminder;
import repository.ReminderHandler;

public class ReminderService {

    private ReminderHandler reminderHandler;
    private List<Reminder> reminderList;

    public ReminderService(ReminderHandler reminderHandler) {
        this.reminderHandler = reminderHandler;
        reminderList = reminderHandler.getAll();
    }

    public Pair<String,String>[] getTextTime(){
        Pair<String,String>[] pairs = new Pair[reminderList.size()];
        SimpleDateFormat format = new SimpleDateFormat("EEE, dd MMM  HH:mm");
        for(int i = 0; i < reminderList.size(); i++){
            Reminder reminder = reminderList.get(i);
            pairs[i] = new Pair<>(reminder.get_text(), format.format(reminder.get_time()));
        }

        return pairs;
    }
}
