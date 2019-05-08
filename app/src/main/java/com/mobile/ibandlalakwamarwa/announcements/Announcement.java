package com.mobile.ibandlalakwamarwa.announcements;

public class Announcement {

    private String title;
    private String message;

    public Announcement(){}

    public Announcement(String title, String message) {
        this.title = title;
        this.message = message;
    }

    public String getTitle() {
        return this.title;
    }

    public String getMessage() {
        return this.message;
    }
}
