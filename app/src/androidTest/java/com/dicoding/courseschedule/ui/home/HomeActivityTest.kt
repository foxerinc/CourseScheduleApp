package com.dicoding.courseschedule.ui.home

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import org.junit.Rule
import org.junit.Test
import com.dicoding.courseschedule.R
import com.dicoding.courseschedule.ui.add.AddCourseActivity

class HomeActivityTest{
    @get:Rule
    var scenarioRule = ActivityScenarioRule(HomeActivity::class.java)

    @Test
    fun validateAddTaskActivity(){
        Intents.init()
        onView(withId(R.id.action_add)).perform(click())
        Intents.intended(hasComponent(AddCourseActivity::class.java.name))
        onView(withId(R.id.ed_course_name)).check(matches(isDisplayed()))
        onView(withId(R.id.ed_lecturer)).check(matches(isDisplayed()))
        onView(withId(R.id.spinner_day)).check(matches(isDisplayed()))
        onView(withId(R.id.ed_note)).check(matches(isDisplayed()))
        onView(withId(R.id.tv_start_time)).check(matches(isDisplayed()))
        onView(withId(R.id.tv_end_time)).check(matches(isDisplayed()))
        onView(withId(R.id.ib_start_time)).check(matches(isDisplayed()))
        onView(withId(R.id.ib_end_time)).check(matches(isDisplayed()))
        Intents.release()

    }
}