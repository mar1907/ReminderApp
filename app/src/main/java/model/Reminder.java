package model;


import android.os.Parcelable;

import java.io.Serializable;
import java.util.Date;

public class Reminder implements Serializable{

    private int _id;
    private Date _time;
    private String _text;
    private int _alarm;

    public Reminder() {
    }

    public Reminder(Date _time, String _text, int _alarm) {
        this._time = _time;
        this._text = _text;
        this._alarm = _alarm;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public Date get_time() {
        return _time;
    }

    public void set_time(Date _time) {
        this._time = _time;
    }

    public String get_text() {
        return _text;
    }

    public void set_text(String _text) {
        this._text = _text;
    }

    public int get_alarm() {
        return _alarm;
    }

    public void set_alarm(int _alarm) {
        this._alarm = _alarm;
    }
}
