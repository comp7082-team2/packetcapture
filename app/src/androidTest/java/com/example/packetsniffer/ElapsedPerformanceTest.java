package com.example.packetsniffer;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

import android.Manifest;
import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.espresso.ViewInteraction;
import androidx.test.filters.LargeTest;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import androidx.test.rule.GrantPermissionRule;
import androidx.test.runner.AndroidJUnit4;

import com.example.packetsniffer.Models.PcapRepository;
import com.example.packetsniffer.Views.MainActivity;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class ElapsedPerformanceTest {

    public static final String TAG = ElapsedPerformanceTest.class.getName();

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Rule
    public GrantPermissionRule internetPermissionRule = GrantPermissionRule.grant(Manifest.permission.WRITE_EXTERNAL_STORAGE);

    @BeforeClass
    /**
     * Static method that runs before all the tests in this class. Copies a file from resources
     * that can be used for test data
     **/
    public static void setUp() {
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        try {
            copyResources(context, R.raw.performance_test);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Log.e(TAG, e.getMessage(), e);
        }
    }

    @AfterClass
    /**
     * Static method that runs after all the tests in this class. Deletes any files
     * in the Documents directory whose filename is espresso.pcap
     */
    public static void tearDown() {
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        File dir = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
        File file = new File(dir, "espresso.pcap");

        if(file.exists()){
            file.delete();
        }
    }

    @Test
    /**
     * Tests to see if the PacketListView screen loads within the specified threshold, regardless
     * of the size of the packet capture file
     */
    public void elapsedLoadPacketListView() {
        Double perfThreshold = 3000.00;

        long startTime = System.nanoTime()/1000000;

        ViewInteraction materialButton2 = onView(
                allOf(withId(R.id.btnAnalyze), withText("Packet Captures"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is(
                                                "android.widget.TableLayout")),
                                        0),
                                1),
                        isDisplayed()));
        materialButton2.perform(click());

        long endTime = System.nanoTime()/1000000;
        long elapsedTime = (endTime - startTime);
        Log.i(TAG, "elapsedLoadPacketListView elapsed time: " + elapsedTime);

        assertTrue(perfThreshold > elapsedTime);
    }

    @Test
    /**
     * Tests to see if graph loads within the specified threshold, regardless of the
     * size of the packet capture file
     */
    public void elapsedLoadGraph() {
        Double perfThreshold = 5000.00;

        long startTime = System.nanoTime()/1000000;

        ViewInteraction materialButton2 = onView(
                allOf(withId(R.id.btnAnalyze), withText("Packet Captures"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is(
                                                "android.widget.TableLayout")),
                                        0),
                                1),
                        isDisplayed()));
        materialButton2.perform(click());

        ViewInteraction floatingActionButton = onView(
                allOf(withId(R.id.fab), withContentDescription("RTT Graph"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                3),
                        isDisplayed()));
        floatingActionButton.perform(click());

        long endTime = System.nanoTime()/1000000;
        long elapsedTime = (endTime - startTime);
        Log.i(TAG, "elapsedLoadGraph elapsed time: " + elapsedTime);

        assertTrue(perfThreshold > elapsedTime);
    }

    /**
     * Copies a pcap from resources to the file system with the name espresso.pcap
     * to be used as test data
     * @param context The context the tests will be executing in
     * @param resId   The resource that will be copied to the file system
     */
    public static void copyResources(Context context, int resId) throws IOException {
        InputStream inputStream = context.getResources().openRawResource(resId);
        File dir = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
        File file = new File(dir, "espresso.pcap");
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];

        FileOutputStream outputStream = null;
        try {
            int read = 0;
            outputStream = new FileOutputStream(file);
            while((read = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer,0,read);
            }
            outputStream.close();
            PcapRepository.getInstance().readPcap(file);
        } catch (FileNotFoundException e) {
            Log.e(TAG, e.getMessage(), e);
        } catch (IOException e) {
            Log.e(TAG, e.getMessage(), e);
        }
    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
