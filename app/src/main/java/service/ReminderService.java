package service;

import android.support.v4.util.Pair;
import android.widget.Toast;

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

    public long addReminder(Reminder reminder){
        return reminderHandler.addReminder(reminder);
    }

    public void updateReminder(Reminder reminder){
        reminderHandler.updateReminder(reminder);
    }

    public void delete(Integer index) {
        reminderHandler.deleteReminder(getReminderList()[index].get_id());
    }
}
