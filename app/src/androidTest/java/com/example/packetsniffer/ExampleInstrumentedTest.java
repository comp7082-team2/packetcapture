package com.example.packetsniffer;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import android.content.Context;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("com.example.packetsniffer", appContext.getPackageName());
        loadFile();
        openRecyclerandRRT();
        filter();
    }
    /*load the files*/
    public  void loadFile(){
        /*
        File downloadDir = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
        File f = new File(downloadDir +  "/" + server-ssh.pcap);
        PcapRepository pcap = new PcapRepository();
        pcap.readPcap(f);
        pcap.getInstance();
        protected CustomAdapter mAdapter  = new CustomAdapter(presenter.loadPcap());
        protected RecyclerView mRecyclerView.setAdapter(mAdapter);
         */
    }
    /*open rrt and go back*/
    public void openRecyclerandRRT(){
        onView(withId(R.id.btnAnalyze)).perform(click());
        onView(withId(R.id.fab)).perform(click());
        goBack();
    }

    public void justClickAnythingOnTheList(){
        /*open recycler did not test out and go back*/
        Espresso.onView(withId(R.id.recyclerView)).perform(
                RecyclerViewActions.actionOnItemAtPosition(1, ViewActions.click()));
        onView(withId(R.id.buttonClose)).perform(click());
    }

    /* bunch of testing*/
    public void filter(){
        /*filter src 192.168.0.23 && tcp and click one of them*/
        onView(withId(R.id.etFilter)).perform(typeText("src 192.168.0.23 && tcp"), closeSoftKeyboard());
        onView(withId(R.id.buttonSearch)).perform(click());
        Espresso.onView(withId(R.id.recyclerView)).perform(
                RecyclerViewActions.actionOnItemAtPosition(1, ViewActions.click()));
        /* check the text inside*/
        onView(withId(R.id.textViewInfoSrc)).check(matches(withText("192.168.0.23")));
        onView(withId(R.id.textViewProtocolLabel)).check(matches(withText("tcp")));
        onView(withId(R.id.buttonClose)).perform(click());


        /*filter src 192.168.0.23 && tcp and click one of them*/
        onView(withId(R.id.etFilter)).perform(typeText("dst 192.168.0.23 && tcp"), closeSoftKeyboard());
        onView(withId(R.id.buttonSearch)).perform(click());
        onView(withId(R.id.textViewInfoSrc)).check(matches(withText("192.168.0.23")));
        onView(withId(R.id.textViewProtocolLabel)).check(matches(withText("tcp")));
        Espresso.onView(withId(R.id.recyclerView)).perform(
                RecyclerViewActions.actionOnItemAtPosition(1, ViewActions.click()));
        onView(withId(R.id.buttonClose)).perform(click());

        /*filter src 192.168.0.23 && udp and click one of them*/
        onView(withId(R.id.etFilter)).perform(typeText("src 192.168.0.23 && udp"), closeSoftKeyboard());
        onView(withId(R.id.buttonSearch)).perform(click());
        Espresso.onView(withId(R.id.recyclerView)).perform(
                RecyclerViewActions.actionOnItemAtPosition(1, ViewActions.click()));
        onView(withId(R.id.textViewInfoSrc)).check(matches(withText("192.168.0.23")));
        onView(withId(R.id.textViewProtocolLabel)).check(matches(withText("udp")));
        onView(withId(R.id.buttonClose)).perform(click());

        /*filter src 192.168.0.23 && udp and click one of them*/
        onView(withId(R.id.etFilter)).perform(typeText("dstPort 10001 && udp"), closeSoftKeyboard());
        onView(withId(R.id.buttonSearch)).perform(click());
        Espresso.onView(withId(R.id.recyclerView)).perform(
                RecyclerViewActions.actionOnItemAtPosition(1, ViewActions.click()));
        onView(withId(R.id.textViewDstPortLabel)).check(matches(withText("1001")));
        onView(withId(R.id.textViewProtocolLabel)).check(matches(withText("udp")));
        onView(withId(R.id.buttonClose)).perform(click());
    }

    public void goBack(){
        onView(
                Matchers.allOf(
                        withContentDescription("Navigate up"),
                        isDisplayed()
                )
        ).perform(click());
    }
}