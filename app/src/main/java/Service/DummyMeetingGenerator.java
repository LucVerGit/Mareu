package Service;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import Model.Delegate;
import Model.Meeting;
import Model.Room;

public abstract class DummyMeetingGenerator {

    public static List<Room> DUMMY_ROOMS = Arrays.asList(
            new Room(0, "Mario"),
            new Room(1, "Luigi"),
            new Room(2, "Peach"),
            new Room(3, "Toad"),
            new Room(4, "Bowser")
    );

    public static List<Delegate> DUMMY_DELEGATES = Arrays.asList(
            new Delegate("Caroline@lamzone.com"),
            new Delegate("Jack@lamzone.com"),
            new Delegate("Chloé@lamzone.com"),
            new Delegate( "Vincent@lamzone.com"),
            new Delegate("Elodie@lamzone.com"),
            new Delegate("Sylvain@lamzone.com"),
            new Delegate("Laetitia@lamzone.com"),
            new Delegate("Dan@lamzone.com"),
            new Delegate("Joseph@lamzone.com"),
            new Delegate("Emma@lamzone.com")
    );



    public static List<Meeting> DUMMY_MEETINGS = Arrays.asList(
            new Meeting(1, "Test", 0xFF4c4f6a, DUMMY_ROOMS.get(1), initBeginTime("10:00", "7/11/2020"), initEndTime("11:00", "7/11/2020"), getDelegatesEmail(1, 4, 6, 9),""),
            new Meeting(2, "Gestion",0xFFd63535,DUMMY_ROOMS.get(0), initBeginTime("11:00", "7/11/2020"), initEndTime("11:30", "7/11/2020"), getDelegatesEmail(1, 2, 5, 8, 9),""),
            new Meeting(3, "Cafe",0xFFffee86,DUMMY_ROOMS.get(2), initBeginTime("14:30", "7/11/2020"), initEndTime("15:30", "7/11/2020"), getDelegatesEmail(3, 7, 9),""),
            new Meeting(4, "Point",0xFF6fd446, DUMMY_ROOMS.get(4), initBeginTime("12:30", "8/11/2020"), initEndTime("14:00", "8/11/2020"), getDelegatesEmail( 1, 4, 5, 7, 9),""),
            new Meeting(5, "Dispenser",0xFF4690d4, DUMMY_ROOMS.get(3), initBeginTime("15:00", "8/11/2020"), initEndTime("16:30", "8/11/2020"), getDelegatesEmail(3, 5),"")
    );

    // // Pick up Delegates with their place n° from the list
   private static List<Delegate> getDelegatesEmail(int... args){
        List<Delegate> delegates = new ArrayList<>();
        for(int x : args ) {
            delegates.add(DUMMY_DELEGATES.get(x));
        }

        return delegates;
    }

    // Allow to set the begin AND end times with Strings
    private static Date initBeginTime(String time, String date){
        Date beginTime = null;
        String sDate = date+" "+time;
        try {
            beginTime = new SimpleDateFormat("dd/MM/yy HH:mm", Locale.getDefault()).parse(sDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return beginTime;
    }

    private static Date initEndTime(String time, String date){
        Date endTime = null;
        String sDate = date+" "+time;
        try {
            endTime = new SimpleDateFormat("dd/MM/yy HH:mm", Locale.getDefault()).parse(sDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return endTime;
    }

    // Returns lists
    static List<Meeting> generateMeeting (){
        return new ArrayList<>(DUMMY_MEETINGS);
    }

    static List<Room> generateRoom(){
        return new ArrayList<>(DUMMY_ROOMS);
    }

    static List<Delegate> generateDelegate (){
        return new ArrayList<>(DUMMY_DELEGATES);
    }
}


