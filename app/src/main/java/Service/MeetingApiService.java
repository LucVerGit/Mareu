package Service;

import java.util.Date;
import java.util.List;

import Model.Delegate;
import Model.Meeting;
import Model.Room;

public interface MeetingApiService {

    List<Meeting> getMeetings();

    void deleteMeeting(Meeting meeting);

    void createMeeting(Meeting meeting);

    List<Room> getRooms();

    List<Delegate> getDelegates();

    List<Meeting> meetingListFilter (boolean isDateFiltered, boolean isLocationFiltered, String roomFilterSelected, Date dateFilterSelected);

}

