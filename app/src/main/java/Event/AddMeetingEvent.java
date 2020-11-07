package Event;

import Model.Meeting;

public class AddMeetingEvent {

    public Meeting meeting;

    public AddMeetingEvent(Meeting meeting) {
        this.meeting = meeting;
    }
}
