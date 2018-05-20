package com.example.marius.reminderapp;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import model.Reminder;

class CheckLineAdapter extends ArrayAdapter {

    private boolean isLongPressed;

    public CheckLineAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull Object[] objects) {
        super(context, resource, objects);
        isLongPressed=false;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater=LayoutInflater.from(getContext());
        View customView= inflater.inflate(R.layout.line_check,parent,false);

        Reminder reminder = (Reminder)getItem(position);
        String text = reminder.get_text();
        Date date = reminder.get_time();

        TextView textView = customView.findViewById(R.id.line_text);
        textView.setText(text);


        DateFormat df = new SimpleDateFormat("dd/MM HH:mm");
        TextView textClock = customView.findViewById(R.id.line_time);
        textClock.setText(df.format(date));

        CheckBox checkBox=customView.findViewById(R.id.hidden_box);
        if(isLongPressed){
            checkBox.setVisibility(View.VISIBLE);
        } else{
            checkBox.setVisibility(View.INVISIBLE);
        }

        return customView;
    }

    /**
     * have the getView method be called again with different values for isLongPressed to show the check box
     */
    public void showCheckBox(){
        isLongPressed=true;
        notifyDataSetChanged();
    }

    /**
     * have the getView method be called again with different values for isLongPressed to hide the check box
     */
    public void hideCheckBox(){
        isLongPressed=false;
        notifyDataSetChanged();
    }
}
