package com.example.taskmaster;
import static androidx.test.espresso.Espresso.*;
//import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)

public class HomePageTests {
    @Rule
    public ActivityScenarioRule<MainActivity> activityRule = new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void homePageTests (){
        onView(withText("TaskMaster")).check(matches(isDisplayed()));
        onView(withId(R.id.imageView3)).check(matches(isDisplayed()));
        onView(withId(R.id.textView9)).check(matches(isDisplayed()));
        onView(withId(R.id.rcv)).check(matches(isDisplayed()));
        onView(withId(R.id.button)).check(matches(isDisplayed()));
        onView(withId(R.id.button2)).check(matches(isDisplayed()));
        onView(withId(R.id.button4)).check(matches(isDisplayed()));
    }

    @Test
    public void settingTest(){
        onView(withId(R.id.button4)).perform(click());
        onView(withId(R.id.button5)).check(matches(isDisplayed()));
        onView(withId(R.id.textView8)).check(matches(isDisplayed()));
        onView(withId(R.id.userNameInput)).perform(typeText("Tareq"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.button5)).perform(click());
//        onView(withContentDescription(R.string.abc_action_bar_up_description)).perform(click());
        Espresso.pressBack();
        onView(withId(R.id.textView9)).check(matches(isDisplayed()));
        onView(withText("Tareq's Tasks")).check(matches(isDisplayed()));
    }
    @Test
    public void addTaskTest(){
        onView(withId(R.id.button)).perform(click());
        onView(withId(R.id.textView2)).check(matches(isDisplayed()));
        onView(withId(R.id.textView3)).check(matches(isDisplayed()));
        onView(withId(R.id.textView4)).check(matches(isDisplayed()));
        onView(withId(R.id.textView5)).check(matches(isDisplayed()));
        onView(withId(R.id.taskTitle)).check(matches(isDisplayed()));
        onView(withId(R.id.taskDescription)).check(matches(isDisplayed()));
        onView(withId(R.id.taskTitle)).perform(typeText("sleep"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.taskDescription)).perform(typeText("sleep for 2 hours"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.button3)).perform(click());
        Espresso.pressBack();
        onView(withId(R.id.rcv)).perform(RecyclerViewActions.actionOnItemAtPosition(1, click()));
        onView(withId(R.id.textView7)).check(matches(withText("sleep")));
        Espresso.pressBack();
    }

}
