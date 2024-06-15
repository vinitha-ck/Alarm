package com.example.todo;

public class Task {
    private long id; // Change from int to long
    private String text;
    private String subtext;
    private String time;
    private boolean isChecked;

    public Task(long id, String text, String subtext, String time, boolean isChecked) {
        this.id = id;
        this.text = text;
        this.subtext = subtext;
        this.time = time;
        this.isChecked = isChecked;
    }

    public Task(String text, String subtext, String time, boolean isChecked) {
        this.text = text;
        this.subtext = subtext;
        this.time = time;
        this.isChecked = isChecked;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getSubtext() {
        return subtext;
    }

    public void setSubtext(String subtext) {
        this.subtext = subtext;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
