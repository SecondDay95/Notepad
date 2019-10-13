package com.example.notepad;

import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.view.View;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.pressImeActionButton;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

public class MainActivityTest {

    @Rule
    //Launching Main activity before executing any of the tests
    public ActivityTestRule<MainActivity> mainActivityActivityTestRule = new ActivityTestRule<>(MainActivity.class);
    private MainActivity mainActivity;

    @Before
    public void setUp() throws Exception {
        mainActivity = mainActivityActivityTestRule.getActivity();
    }

    @Test
    public void launchViews () {
        View view = mainActivity.findViewById(R.id.recycler);
        View view1 = mainActivity.findViewById(R.id.toolbar);
        View view2 = mainActivity.findViewById(R.id.fab);

        Assert.assertNotNull(view);
        Assert.assertNotNull(view1);
        Assert.assertNotNull(view2);
    }



    @Test
    public void addingNote () throws Exception {
        onView(withId(R.id.fab)).perform(click());
        onView(withId(R.id.etTitle)).perform(typeText("Title")).perform(pressImeActionButton()).perform(closeSoftKeyboard());
        onView(withId(R.id.etNote)).perform(typeText("Note"));
        onView(withId(R.id.action_save)).perform(click());
        //onData(withId(R.id.info_text)).inAdapterView(withId(R.id.card_view)).atPosition(8).check(matches(withText("Title1")));
        //onData(withId(R.id.description_text)).inAdapterView(withId(R.id.recycler)).atPosition(8).check(ViewAssertions.matches(withText("Note")));
        onView(ViewMatchers.withId(R.id.recycler))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0,
                        click()));
        onView(withText("Title")).check(matches(isDisplayed()));
    }

    @Test
    public void deleteNote() throws Exception {
        int size = mainActivity.notesList.size();
        onView(ViewMatchers.withId(R.id.recycler))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0,
                        click()));
        onView(withId(R.id.action_delete)).perform(click());
        MainActivity mainActivity2 = mainActivityActivityTestRule.getActivity();
        int size1 = mainActivity2.notesList.size();
        //System.out.println(size + "," + size1);
        //Thread.sleep(1000);
        //int size1 = mainActivity.notesList.size();
        Assert.assertNotEquals(size, size1);
    }

    @Test
    public void updateNote() {
        Note note = mainActivity.notesList.get(0);
        String titlePrevious = note.getTitle();
        onView(ViewMatchers.withId(R.id.recycler))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0,
                        click()));
        onView(withId(R.id.etTitleChange)).perform(typeText("Title")).perform(pressImeActionButton()).perform(closeSoftKeyboard());
        onView(withId(R.id.action_save)).perform(click());
        MainActivity mainActivity2 = mainActivityActivityTestRule.getActivity();
        Note note1 = mainActivity2.notesList.get(0);
        String titleNext = note1.getTitle();
        Assert.assertNotEquals(titlePrevious, titleNext);
    }

    @After
    public void tearDown() throws Exception {
    }
}