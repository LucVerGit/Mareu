package com.example.ui.MeetingListTest;

import android.text.format.DateFormat;
import android.widget.DatePicker;
import android.widget.TimePicker;

import androidx.test.espresso.action.GeneralLocation;
import androidx.test.espresso.action.GeneralSwipeAction;
import androidx.test.espresso.action.Press;
import androidx.test.espresso.action.Swipe;
import androidx.test.espresso.contrib.PickerActions;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.matcher.RootMatchers;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.rule.ActivityTestRule;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.ui.MainActivity;
import com.example.ui.R;
import com.example.ui.UiTest.DeleteViewAction;
import com.example.ui.UiTest.DetailMeetingViewAction;


import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;


import DI.DI;
import Event.DeleteMeetingEvent;
import Service.MeetingApiService;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.swipeUp;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasMinimumChildCount;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.example.ui.UiTest.RecyclerViewItem.withItemCount;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class MainListTestinstrumented {

    // This is fixed
    private int positionTest = 0;
    private String locationName = "Mario"; // Can be "Mario", "Luigi", "Peach", "Toad", "Bowser"
    private MeetingApiService service = DI.getNewInstanceApiService();

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<>(MainActivity.class);

    @Before
    public void setUp() {
        service = DI.getNewInstanceApiService();
        MainActivity activity = mActivityRule.getActivity();
        assertThat(activity, notNullValue());
    }

    /**
     * We ensure that our recyclerview is displaying at least on item
     */
    @Test
    public void MeetingList_shouldNotBeEmpty() {
        onView(ViewMatchers.withId(R.id.list_meetings))
                .check(matches(hasMinimumChildCount(1)));
    }

    /**
     * When we delete an item, the item is no more shown
     */
    @Test
    public void MeetingList_deleteAction_shouldRemoveItem() {
        // Given : We remove the element at position 2
        onView(withId(R.id.list_meetings)).check(withItemCount(5));
        // When perform a click on a delete icon
        onView(withId(R.id.list_meetings))
                .perform(RecyclerViewActions.actionOnItemAtPosition(1, new DeleteViewAction()));
        // Then : the number of element is 4
        onView(withId(R.id.list_meetings)).check(withItemCount(4));
}

    /**
     * When we click "location" filter, only show meetings with filter
     */
    @Test
    public void MeetingList_onLocationFilterClick_shouldFilterList() {
        // When perform a click on the menu -> then the location filter ->
        // then on the location we wanna test -> then OK button
        onView(withId(R.id.menu)).perform(click());
        onView(withText("Filter by location")).perform(click());
        onView(withText(locationName)).perform(click());
        onView(withText("OK")).perform(click());
        // Then : their is only the same number of meeting as those who have the location filter
        onView(withId(R.id.list_meetings)).check(withItemCount(0));
    }

    /**
     * When we click "date" filter, only show meetings with filter
     */
    @Test
    public void MeetingList_onDateFilterClick_shouldFilterList() {
        // When perform a click on the menu -> then the date filter ->
        // then set the date we wanna test -> then OK button
        onView(withId(R.id.menu)).perform(click());
        onView(withText("Filter by date")).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2020, 10, 31));
        onView(withText("OK")).perform(click());
        // Count the number of meeting with the same date
        int filterCount = 0;
        for (int i = 0; i< service.getMeetings().size(); i++ ){
            String meetingDate = (String) DateFormat.format("yyyy/MM/dd", service.getMeetings().get(i).getBeginTime());
            if (meetingDate.equals("2020/10/31")) filterCount++;
        }
        // Then : their is only the same number of meeting as those who have the date filter
        onView(withId(R.id.list_meetings)).check(withItemCount(filterCount));
    }

    /**
     * When we click "clear" filter, remove all filters
     */
    @Test
    public void MeetingList_onClearFilterClick_shouldFilterList() {
        // When perform a click on the menu -> then the location filter ->
        // then on the location we wanna test -> then OK button
        onView(withId(R.id.menu)).perform(click());
        onView(withText("Filter by location")).perform(click());
        onView(withText(locationName)).perform(click());
        onView(withText("OK")).perform(click());
        // When perform a click on the menu -> then the date filter ->
        // then set the date we wanna test -> then OK button
        onView(withId(R.id.menu)).perform(click());
        onView(withText("Filter by date")).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2020, 10, 31));
        onView(withText("OK")).perform(click());
        // When perform a click on the menu -> then clear filter
        onView(withId(R.id.menu)).perform(click());
        onView(withText("Clear filter")).perform(click());
        // Then : their is only the same number of meeting as those who have the date filter
        onView(withId(R.id.list_meetings)).check(withItemCount(4));
    }

    /**
     * When we click + icon, the AddMeetingFragment is launch
     */
    @Test
    public void MeetingList_onAddButtonClick_shouldOpenAddMeetingFragment() {
        // When perform a click on + button
        onView(withId(R.id.add_meetings)).perform(click());
        // Then : the view went to AddMeetingFragment
        onView(withId(R.id.add_meeting_layout)).check(matches(isDisplayed()));
    }

    /**
     * In AddMeetingFragment, should fill all info before being able to create
     */
    @Test

    public void MeetingList_inAddActivity_shouldFillAllBeforeCreation() {
        // When perform a click on + button
        onView(withId(R.id.add_meetings)).perform(click());
        //Filling everything (actually give error when create button is clicked)
        onView(withId(R.id.name)).perform(clearText(), typeText("Meeting Name"));
        onView(withId(R.id.add_date)).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2020, 11, 7));
        onView(withText("OK")).perform(click());
        onView(withId(R.id.add_room)).perform(click());
        onView(withText("Mario")).perform(click());
        onView(withText("OK")).perform(click());
        onView(withId(R.id.add_time)).perform(click());
        onView(withClassName(Matchers.equalTo(TimePicker.class.getName()))).perform(PickerActions.setTime(14, 00));
        onView(withText("OK")).perform(click());
        onView(withId(R.id.add_duration)).perform(click());
        onView(withId(R.id.picker_hour)).perform(new GeneralSwipeAction(Swipe.SLOW, GeneralLocation.BOTTOM_CENTER, GeneralLocation.TOP_CENTER, Press.FINGER));
        onView(withText("OK")).perform(click());
        onView(withId(R.id.add_delegates)).perform(click());
        onView(withId(R.id.add_delegates)).perform(clearText(), typeText("car"));
        onView(withText("Caroline")).inRoot(RootMatchers.isPlatformPopup()).perform(click());
        onView(withId(R.id.add_meeting_layout)).perform(swipeUp());
        onView(withId(R.id.add_button)).perform(click());
        // Then : the button was clickable and there is 1 more meeting in the list
        onView(withId(R.id.list_meetings)).check(withItemCount(5));
    }

    /**
     * On button create click, should check if other meeting overlap the one we create
     */
    @Test

    public void MeetingList_onClickCreate_shouldNotOverlapOtherMeeting() {
        onView(withId(R.id.add_meetings)).perform(click());
        onView(withId(R.id.name)).perform(clearText(), typeText("Meeting Name"));
        onView(withId(R.id.add_date)).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2020, 11, 7));
        onView(withText("OK")).perform(click());
        onView(withId(R.id.add_room)).perform(click());
        onView(withText("Bowser")).perform(click());
        onView(withText("OK")).perform(click());
        onView(withId(R.id.add_time)).perform(click());
        onView(withClassName(Matchers.equalTo(TimePicker.class.getName()))).perform(PickerActions.setTime(13, 0));
        onView(withText("OK")).perform(click());
        onView(withId(R.id.add_duration)).perform(click());
        onView(withId(R.id.picker_hour)).perform(new GeneralSwipeAction(Swipe.SLOW, GeneralLocation.BOTTOM_CENTER, GeneralLocation.TOP_CENTER, Press.FINGER));
        onView(withText("OK")).perform(click());
        onView(withId(R.id.add_delegates)).perform(click());
        onView(withId(R.id.add_delegates)).perform(clearText(), typeText("car"));
        onView(withText("Caroline")).inRoot(RootMatchers.isPlatformPopup()).perform(click());
        onView(withId(R.id.add_meeting_layout)).perform(swipeUp());
        onView(withId(R.id.add_button)).perform(click());
        // Then : the button was clickable and an error message is display
        onView(withText("Room unavailable")).check(matches(isDisplayed()));
    }


    /**
     * When we click an item, the detailActivity is launch
     */
    @Test
    public void MeetingList_onMeetingClick_shouldOpenDetailActivity() {
        //        // When perform a click on a meeting
        onView(allOf (withId(R.id.list_meetings), isDisplayed()))
                .perform(RecyclerViewActions.actionOnItemAtPosition(positionTest, new DetailMeetingViewAction()));
        // Then : the view went to DetailActivity
        onView(withId(R.id.detail_meeting)).check(matches(isDisplayed()));
    }

    /**
     * When the detailActivity is launch, the item detail name is shown
     */
    @Test
    public void MeetingList_onDetailOpen_shouldDisplayMeetingSubject() {
        // When perform a click on a meeting
        onView(allOf (withId(R.id.list_meetings), isDisplayed()))
                .perform(RecyclerViewActions.actionOnItemAtPosition(positionTest, new DetailMeetingViewAction()));
        String meetingSubject = service.getMeetings().get(positionTest).getName();
         // Then : the subject field is the subject of the meeting
        onView(allOf(withId(R.id.detail_subject), withText(meetingSubject))).check(matches(withText(meetingSubject)));
    }
}