package com.example.marius.reminderapp.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.AdapterView;
import android.widget.TimePicker;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.example.marius.reminderapp.R;
import com.example.marius.reminderapp.model.Reminder;
import com.example.marius.reminderapp.repository.ReminderHandler;
import com.example.marius.reminderapp.service.AlarmService;
import com.example.marius.reminderapp.service.ReminderService;

public class ContentList extends AppCompatActivity {

    private ReminderService reminderService;
    private CheckLineAdapter adapter;
    private Context context = this;
    private boolean longClick;
    private ListView listView;
    private AlarmService alarmService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setImageResource(R.mipmap.ic_add);
        fab.setRippleColor(Color.YELLOW);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(null);
            }
        });

        reminderService = new ReminderService(new ReminderHandler(this,null,null,1));
        createList(reminderService);

        alarmService = new AlarmService(this);

        //TODO add options?
    }

    private void createList(final ReminderService reminderService){
        adapter = new CheckLineAdapter(this, R.layout.line_check, reminderService.getReminderList());
        listView = (ListView)findViewById(R.id.list1);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                showDialog(reminderService.getReminderList()[i]);
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                longClick = true;
                adapter.showCheckBox();
                invalidateOptionsMenu();
                return true;
            }
        });
    }


    private void showDialog(final Reminder reminder){
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.alarm_view_dialog_layout);
        dialog.setTitle("Reminder");

        final EditText messageEditText = dialog.findViewById(R.id.messageEditText);

        final TimePicker timePicker = dialog.findViewById(R.id.timePick);
        timePicker.setIs24HourView(true);

        final DatePicker datePicker = dialog.findViewById(R.id.datePick);

        final CheckBox alarm = dialog.findViewById(R.id.checkBox);

        if(reminder!=null){
            messageEditText.setText(reminder.get_text());
            alarm.setChecked(reminder.get_alarm()==1);
        }

        Button cancelButton=dialog.findViewById(R.id.cancel_button);
        Button okButton=dialog.findViewById(R.id.ok_button);

        cancelButton.setOnClickListener(
                new Button.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.cancel();
                    }
                }
        );

        okButton.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view) {
                String message = messageEditText.getText().toString();

                int day = datePicker.getDayOfMonth();
                int month = datePicker.getMonth();
                int year = datePicker.getYear();

                int hour = timePicker.getCurrentHour();
                int minute = timePicker.getCurrentMinute();

                Calendar calendar = Calendar.getInstance();
                calendar.set(year,month,day,hour,minute,0);

                Date date = calendar.getTime();

                int al = alarm.isChecked()?1:0;

                if(reminder!=null){
                    reminder.set_text(message);
                    reminder.set_time(date);
                    reminder.set_alarm(al);
                    reminderService.updateReminder(reminder);
                    alarmService.createReminder(reminder);
                } else {
                    Reminder newReminder = new Reminder();
                    newReminder.set_text(message);
                    newReminder.set_time(date);
                    newReminder.set_alarm(al);
                    newReminder.set_id((int)reminderService.addReminder(newReminder));
                    alarmService.createReminder(newReminder);
                }

                createList(reminderService);
                dialog.cancel();
            }
        });

        dialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_content_list, menu);
        MenuItem menuItem = menu.findItem(R.id.action_delete);
        menuItem.setVisible(false);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem menuItem=menu.findItem(R.id.action_delete);
        if(longClick){
            menuItem.setVisible(true);
        }
        else {
            menuItem.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_delete:
                deleteSelectedItems();
                break;
            case R.id.action_settings:
                Intent i=new Intent(this,SettingsActivity.class);
                startActivity(i);
                break;
        }
        return true;
    }

    private void deleteSelectedItems() {
        List<Integer> selectedIndices = new ArrayList<>();
        for(int i=0; i<adapter.getCount(); i++){
            View view=getViewByPosition(i,listView);

            CheckBox cb=view.findViewById(R.id.hidden_box);
            if(cb.isChecked()){
                selectedIndices.add(i);
            }
        }

        for(int i=selectedIndices.size()-1;i>=0;i--){
            Reminder reminder = new Reminder();
            reminder.set_id(reminderService.getReminderList()[selectedIndices.get(i)].get_id());
            alarmService.deleteReminder(reminder);
            reminderService.delete(selectedIndices.get(i));
        }

        longClick=false;
        adapter.hideCheckBox();
        invalidateOptionsMenu();
        createList(reminderService);
    }

    @Override
    public void onBackPressed() {
        if(longClick){
            longClick=false;
            adapter.hideCheckBox();
            invalidateOptionsMenu();
        }
        else {
            super.onBackPressed();
        }
    }

    private View getViewByPosition(int pos, ListView listView){
        final int firstListItemPosition = listView.getFirstVisiblePosition();
        final int lastListItemPosition = firstListItemPosition + listView.getChildCount() - 1;

        if (pos < firstListItemPosition || pos > lastListItemPosition ) {
            return listView.getAdapter().getView(pos, null, listView);
        } else {
            final int childIndex = pos - firstListItemPosition;
            return listView.getChildAt(childIndex);
        }
    }

}
