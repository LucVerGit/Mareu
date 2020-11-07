package Event;

import Model.Meeting;

public class DetailMeetingEvent {
    public Meeting meeting;

    public DetailMeetingEvent(Meeting meeting) {
        this.meeting = meeting;
    }
}