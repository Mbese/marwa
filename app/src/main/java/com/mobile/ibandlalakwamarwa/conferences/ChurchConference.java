package com.mobile.ibandlalakwamarwa.conferences;

public class ChurchConference {

    private String title;
    private String startDate;
    private String endDate;
    private String startTime;
    private String endTime;
    private String address;
    private String organizer;
    private String organizerContacts;

    public ChurchConference(){}

    public ChurchConference(String title, String startDate, String endDate, String startTime, String endTime, String address, String organizer, String organizerContacts) {
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.address = address;
        this.organizer = organizer;
        this.organizerContacts = organizerContacts;
    }

    public String getTitle() {
        return title;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public String getAddress() {
        return address;
    }

    public String getOrganizer() {
        return organizer;
    }

    public String getOrganizerContacts() {
        return organizerContacts;
    }
}
