package service;

import android.support.v4.util.Pair;

import java.text.SimpleDateFormat;
import java.util.List;

import model.Reminder;
import repository.ReminderHandler;

public class ReminderService {

    private ReminderHandler reminderHandler;

    public ReminderService(ReminderHandler reminderHandler) {
        this.reminderHandler = reminderHandler;
    }

    public Reminder[] getReminderList(){
        List<Reminder> reminderList = reminderHandler.getAll();
        return reminderList.toArray(new Reminder[reminderList.size()]);
    }

    public void addReminder(Reminder reminder){
        reminderHandler.addReminder(reminder);
    }
}
