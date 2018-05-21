package com.example.marius.reminderapp.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.example.marius.reminderapp.model.Reminder;

public class ReminderHandler extends SQLiteOpenHelper{

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "products.db";
    public static final String TABLE_REMINDER = "reminder";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_TIME = "time";
    public static final String COLUMN_TEXT = "text";
    public static final String COLUMN_ALARM = "alarm";

    public ReminderHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String query = "CREATE TABLE " + TABLE_REMINDER + "("+
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_TIME + " LONG," +
                COLUMN_TEXT + " TEXT," +
                COLUMN_ALARM + " INTEGER" +
                ");";
        sqLiteDatabase.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_REMINDER);
        onCreate(sqLiteDatabase);
    }

    public long addReminder(Reminder reminder){
        String sql = "INSERT INTO " + TABLE_REMINDER + " VALUES (null, ?, ?, ?)";
        SQLiteDatabase db = getWritableDatabase();
        SQLiteStatement statement= db.compileStatement(sql);

        statement.bindLong(1, reminder.get_time().getTime());
        statement.bindString(2, reminder.get_text());
        statement.bindLong(3, reminder.get_alarm());

        long id = statement.executeInsert();
        db.close();
        return id;
    }

    public void updateReminder(Reminder reminder){
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_TIME, reminder.get_time().getTime());
        contentValues.put(COLUMN_TEXT, reminder.get_text());
        contentValues.put(COLUMN_ALARM, reminder.get_alarm());
        SQLiteDatabase db = getWritableDatabase();
        db.update(TABLE_REMINDER, contentValues, COLUMN_ID+"="+reminder.get_id(), null);
        db.close();
    }

    public void deleteReminder(int id){
        String sql = "DELETE FROM " + TABLE_REMINDER + " WHERE " + COLUMN_ID + "=" + id;
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(sql);
        db.close();
    }

    public List<Reminder> getAll(){
        List<Reminder> reminderList = new ArrayList<>();

        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM "+TABLE_REMINDER+" WHERE 1";

        Cursor c = db.rawQuery(query,null);
        c.moveToFirst();
        while(!c.isAfterLast()){
            Reminder reminder = new Reminder();
            reminder.set_id(c.getInt(c.getColumnIndex(COLUMN_ID)));
            reminder.set_time(new Date(c.getLong(c.getColumnIndex(COLUMN_TIME))));
            reminder.set_text(c.getString(c.getColumnIndex(COLUMN_TEXT)));
            reminder.set_alarm(c.getInt(c.getColumnIndex(COLUMN_ALARM)));
            reminderList.add(reminder);
            c.moveToNext();
        }

        db.close();

        return reminderList;
    }
}
