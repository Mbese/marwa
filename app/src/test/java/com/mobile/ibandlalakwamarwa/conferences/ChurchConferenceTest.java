package com.mobile.ibandlalakwamarwa.conferences;

import org.junit.Test;

import static org.junit.Assert.*;

public class ChurchConferenceTest {

    @Test
    public void testChurchConference(){
        ChurchConference churchConference = new ChurchConference("title", "date", "endDate",
                "startTime", "endTime", "address", "organizer", "0727889089");

        assertEquals(churchConference.getTitle(), "title");
        assertEquals(churchConference.getStartDate(), "date");
        assertEquals(churchConference.getEndDate(), "endDate");
        assertEquals(churchConference.getStartTime(), "startTime");
        assertEquals(churchConference.getEndTime(), "endTime");
        assertEquals(churchConference.getAddress(), "address");
        assertEquals(churchConference.getOrganizer(), "organizer");
        assertEquals(churchConference.getOrganizerContacts(), "0727889089");
    }

}