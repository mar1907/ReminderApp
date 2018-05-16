package com.example.marius.reminderapp;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.AdapterView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import model.Reminder;
import repository.ReminderHandler;
import service.ReminderService;

public class ContentList extends AppCompatActivity {

    private ReminderService reminderService;
    private CheckLineAdapter adapter;
    private Context context = this;
    private boolean longClick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setImageResource(R.mipmap.ic_add);
        fab.setRippleColor(Color.YELLOW);

        //TODO change
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(null);
            }
        });

        reminderService = new ReminderService(new ReminderHandler(this,null,null,1));
        createList(reminderService);

        //TODO add options?
    }

    private void createList(final ReminderService reminderService){
        adapter = new CheckLineAdapter(this, R.layout.line_check, reminderService.getReminderList());
        ListView listView = (ListView)findViewById(R.id.list1);
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
        dialog.setContentView(R.layout.alarm_view_dialog_layout);//TODO create said view
        dialog.setTitle("Reminder");

        final EditText messageEditText = dialog.findViewById(R.id.messageEditText);
        final EditText timeEditText = dialog.findViewById(R.id.timeEditText);
        final CheckBox alarm = dialog.findViewById(R.id.checkBox);

        if(reminder!=null){
            messageEditText.setText(reminder.get_text());
            timeEditText.setText(reminder.get_time().toString());
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

                Date date=new Date();
                DateFormat format = new SimpleDateFormat("hh:mm:ss", Locale.GERMANY);
                try {
                    date = format.parse(timeEditText.getText().toString());
                } catch (ParseException e) {
                    //TODO something
                }

                int al = alarm.isChecked()?1:0;

                if(reminder!=null){
                    Reminder newReminder = new Reminder(date, message, al);
                    reminderService.addReminder(newReminder);
                } else {
                    Reminder reminder = new Reminder();
                    reminder.set_text(message);
                    reminder.set_time(date);
                    reminder.set_alarm(al);
                    reminderService.addReminder(reminder);
                }

                createList(reminderService);
                dialog.cancel();
            }
        });

        dialog.show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_content_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
