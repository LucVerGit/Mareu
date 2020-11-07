package Service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import Model.Delegate;
import Model.Meeting;
import Model.Room;

public class DummyMeetingApiService implements MeetingApiService {

        private List<Meeting> meetings = DummyMeetingGenerator.generateMeeting();
        private List<Room> rooms = DummyMeetingGenerator.generateRoom();
        private List<Delegate> delegates = DummyMeetingGenerator.generateDelegate();

        @Override
        public List<Meeting> getMeetings() {
            return meetings;
        }

        @Override
        public void deleteMeeting(Meeting meeting) {
            meetings.remove(meeting);
        }

        @Override
        public void createMeeting(Meeting meeting) {
            meetings.add(meeting);
        }

        @Override
        public List<Room> getRooms() {
            return rooms;
        }

        @Override
        public List<Delegate> getDelegates() {
            return delegates;
        }

        @Override
        public List<Meeting> meetingListFilter (boolean isDateFiltered, boolean isLocationFiltered, String roomFilterSelected, Date dateFilterSelected) {

            List<Meeting> meetingsArrayList = new ArrayList<>();

            // Filter (if either room, date or both are filtered)
            if (isDateFiltered || isLocationFiltered){
                for (int i = 0; i < getMeetings().size(); i++) {

                    // Difference of rooms
                    String sMeetingLocation = getMeetings().get(i).getLocation().getRoom();
                    boolean boolLocation = sMeetingLocation.equals(roomFilterSelected);

                    // difference of dates (with calendars)
                    Calendar cal1 = Calendar.getInstance();
                    Calendar cal2 = Calendar.getInstance();
                    cal1.setTime(getMeetings().get(i).getBeginTime());
                    if (dateFilterSelected != null){
                    cal2.setTime(dateFilterSelected);
                    }

                    boolean boolDate = cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR) &&
                            cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH) &&
                            cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR);

                    // If both are filtered and match filter, add the meeting to the list
                    if (isLocationFiltered && isDateFiltered) {
                        if (boolLocation && boolDate) { meetingsArrayList.add(getMeetings().get(i)); }

                        // If only the room is filtered and match filter, add the meeting to the list
                    } else if (isLocationFiltered) {
                        if (boolLocation) { meetingsArrayList.add(getMeetings().get(i)); }

                        // If only the date is filtered and match filter, add the meeting to the list
                    } else {
                        if (boolDate) { meetingsArrayList.add(getMeetings().get(i)); }
                    }
                }
                // Without filter just show all meetings
            } else {
                meetingsArrayList = getMeetings();
            }
            return meetingsArrayList;
        }
    }