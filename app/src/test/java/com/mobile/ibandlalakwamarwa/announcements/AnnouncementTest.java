package com.mobile.ibandlalakwamarwa.announcements;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class AnnouncementTest {

   @Test
    public void testAnnouncementsWithCorrectData(){
       Announcement announcement = new Announcement("title", "message");

       assertEquals(announcement.getTitle(), "title");
       assertEquals(announcement.getMessage(), "message");
   }
}