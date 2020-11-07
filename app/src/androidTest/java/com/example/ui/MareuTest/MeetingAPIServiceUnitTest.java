package com.example.ui.MareuTest;

import org.hamcrest.collection.IsIterableContainingInAnyOrder;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;
import java.util.List;
import java.util.Objects;

import DI.DI;
import Model.Delegate;
import Model.Meeting;
import Model.Room;
import Service.DummyMeetingGenerator;
import Service.MeetingApiService;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class MeetingAPIServiceUnitTest {

    private MeetingApiService service;

    @Before
    public void setup() {
        service = DI.getNewInstanceApiService();
    }

    @Test
    public void getMeetingWithSuccess() {
        List<Meeting> meetings = service.getMeetings();
        List<Meeting> expectedMeetings = DummyMeetingGenerator.DUMMY_MEETINGS;
        assertThat(meetings, IsIterableContainingInAnyOrder.containsInAnyOrder(expectedMeetings.toArray()));
    }

    @Test
    public void getDelegateWithSuccess() {
        List<Delegate> delegates = service.getDelegates();
        List<Delegate> expectedDelegates = DummyMeetingGenerator.DUMMY_DELEGATES;
        assertThat(delegates, IsIterableContainingInAnyOrder.containsInAnyOrder(expectedDelegates.toArray()));
    }

    @Test
    public void getRoomWithSuccess() {
        List<Room> rooms = service.getRooms();
        List<Room> expectedRooms = DummyMeetingGenerator.DUMMY_ROOMS;
        assertThat(rooms, IsIterableContainingInAnyOrder.containsInAnyOrder(expectedRooms.toArray()));
    }


    @Test
    public void deleteMeetingWithSuccess() {
        Meeting meetingToDelete = service.getMeetings().get(0);
        service.deleteMeeting(meetingToDelete);
        assertFalse(service.getMeetings().contains(meetingToDelete));
    }

    @Test
    public void createMeetingWithSuccess() {
        Meeting meetingToCreate = new Meeting(System.currentTimeMillis(),"",0xFFFFFFFF,service.getRooms().get(0), new Date(), new Date(), service.getDelegates(),"");
        service.createMeeting(meetingToCreate);
        assertTrue(service.getMeetings().contains(meetingToCreate));
    }

    @Test
    public void filterMeetingWithSuccess() {
        int testIndex = 1;
        Meeting expectedMeeting = service.getMeetings().get(testIndex);
        List<Meeting> filteredMeetings = service.meetingListFilter(true, true, expectedMeeting.getLocation().getRoom(), expectedMeeting.getBeginTime());
        int nMeetingTheSameDayInTheSameRoom = 0;
        for (int i=0; i < service.getMeetings().size(); i++) {
            if (service.getMeetings().get(i).getLocation() != expectedMeeting.getLocation())
                continue;
            nMeetingTheSameDayInTheSameRoom++;
        }
        assertTrue(filteredMeetings.contains(expectedMeeting));
        assertFalse(filteredMeetings.size() > nMeetingTheSameDayInTheSameRoom);
    }

}
