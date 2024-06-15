package com.example.alarmclock;
public class Alarm {
    private int id;
    private int hour;
    private int minute;
    private boolean state;

    public Alarm(int id, int hour, int minute, boolean state) {
        this.id = id;
        this.hour = hour;
        this.minute = minute;
        this.state = state;
    }
    public Alarm(int hour, int minute, boolean state) {
        this(0, hour, minute, state);
    }

    public int getId() {
        return id;
    }

    public int getHour() {
        return hour;
    }

    public int getMinute() {
        return minute;
    }

    public boolean getState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }
    public void setId(int id)
    {
        this.id=id;
    }

}
