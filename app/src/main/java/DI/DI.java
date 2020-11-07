package DI;

import Service.DummyMeetingApiService;
import Service.MeetingApiService;

public class DI {
    private static MeetingApiService myApiService = new DummyMeetingApiService();

    public static MeetingApiService getMeetingApiService() { return myApiService; }

    public static MeetingApiService getNewInstanceApiService(){
        return new DummyMeetingApiService();
    }
}