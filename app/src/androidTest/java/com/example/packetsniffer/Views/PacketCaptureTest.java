package com.example.packetsniffer.Views;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
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
import com.example.packetsniffer.R;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.IsInstanceOf;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class PacketCaptureTest {

    public static final String TAG = PacketCaptureTest.class.getName();

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
            copyResources(context, R.raw.ssh_server);
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
     * Tests for successful pcap import by checking for recycler view in "Packet
     * Captures" View
     */
    public void loadPacketCaptureTest() {
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

        ViewInteraction recyclerView = onView(
                allOf(withId(R.id.recyclerView),
                        withParent(withParent(withId(android.R.id.content))),
                        isDisplayed()));
        recyclerView.check(matches(isDisplayed()));
    }

    @Test
    /**
     * Tests detail view of individual packets from imported pcap file
     */
    public void packetDetailTest() {
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

        ViewInteraction recyclerView = onView(
                allOf(withId(R.id.recyclerView),
                        childAtPosition(
                                withClassName(is(
                                        "androidx.constraintlayout.widget.ConstraintLayout")),
                                0)));
        recyclerView.perform(actionOnItemAtPosition(0, click()));

        ViewInteraction textView = onView(
                allOf(withId(R.id.textViewArrivalLabel), withText("Arrival Time"),
                        withParent(withParent(IsInstanceOf
                                .<View>instanceOf(android.widget.TableLayout.class))),
                        isDisplayed()));
        textView.check(matches(isDisplayed()));

        ViewInteraction textView2 = onView(
                allOf(withId(R.id.textViewProtocolLabel), withText("Protocol"),
                        withParent(withParent(IsInstanceOf
                                .<View>instanceOf(android.widget.TableLayout.class))),
                        isDisplayed()));
        textView2.check(matches(isDisplayed()));

        ViewInteraction textView3 = onView(
                allOf(withId(R.id.textViewSrcLabel), withText("Src"),
                        withParent(withParent(IsInstanceOf
                                .<View>instanceOf(android.widget.TableLayout.class))),
                        isDisplayed()));
        textView3.check(matches(isDisplayed()));

        ViewInteraction textView4 = onView(
                allOf(withId(R.id.textViewSrcPortLabel), withText("Port"),
                        withParent(withParent(IsInstanceOf
                                .<View>instanceOf(android.widget.TableLayout.class))),
                        isDisplayed()));
        textView4.check(matches(isDisplayed()));

        ViewInteraction textView5 = onView(
                allOf(withId(R.id.textViewDstLabel), withText("Dst"),
                        withParent(withParent(IsInstanceOf
                                .<View>instanceOf(android.widget.TableLayout.class))),
                        isDisplayed()));
        textView5.check(matches(isDisplayed()));

        ViewInteraction textView6 = onView(
                allOf(withId(R.id.textViewDstPortLabel), withText("Port"),
                        withParent(withParent(IsInstanceOf
                                .<View>instanceOf(android.widget.TableLayout.class))),
                        isDisplayed()));
        textView6.check(matches(isDisplayed()));

        ViewInteraction textView7 = onView(
                allOf(withId(R.id.textViewTcpFlagsLabel), withText("Flags"),
                        withParent(allOf(withId(R.id.tcpRowFlags),
                                withParent(IsInstanceOf.<View>instanceOf(
                                        android.widget.TableLayout.class)))),
                        isDisplayed()));
        textView7.check(matches(isDisplayed()));

        ViewInteraction textView8 = onView(
                allOf(withId(R.id.textViewAckLabel), withText("Ack #"),
                        withParent(allOf(withId(R.id.tcpRowAck),
                                withParent(IsInstanceOf.<View>instanceOf(
                                        android.widget.TableLayout.class)))),
                        isDisplayed()));
        textView8.check(matches(isDisplayed()));

        ViewInteraction textView9 = onView(
                allOf(withId(R.id.textViewSeqLabel), withText("Seq #"),
                        withParent(allOf(withId(R.id.tcpRowSeq),
                                withParent(IsInstanceOf.<View>instanceOf(
                                        android.widget.TableLayout.class)))),
                        isDisplayed()));
        textView9.check(matches(isDisplayed()));

        ViewInteraction button = onView(
                allOf(withId(R.id.button2), withText("FINISH"),
                        withParent(withParent(IsInstanceOf
                                .<View>instanceOf(android.widget.TableLayout.class))),
                        isDisplayed()));
        button.check(matches(isDisplayed()));

        ViewInteraction materialButton3 = onView(
                allOf(withId(R.id.button2), withText("Finish"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is(
                                                "android.widget.TableLayout")),
                                        9),
                                0),
                        isDisplayed()));
        materialButton3.perform(click());

        ViewInteraction recyclerView2 = onView(
                allOf(withId(R.id.recyclerView),
                        withParent(withParent(withId(android.R.id.content))),
                        isDisplayed()));
        recyclerView2.check(matches(isDisplayed()));
    }

    @Test
    /**
     * Tests "src" filter from imported pcap file
     */
    public void packetCaptureTest_SrcFilter() {
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

        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.etFilter),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                2),
                        isDisplayed()));
        appCompatEditText.perform(replaceText("src 192.168.0.19"), closeSoftKeyboard());

        ViewInteraction materialButton3 = onView(
                allOf(withId(R.id.button), withText("SEARCH"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                1),
                        isDisplayed()));
        materialButton3.perform(click());

        ViewInteraction recyclerView = onView(
                allOf(withId(R.id.recyclerView),
                        childAtPosition(
                                withClassName(is(
                                        "androidx.constraintlayout.widget.ConstraintLayout")),
                                0)));
        recyclerView.perform(actionOnItemAtPosition(2, click()));

        ViewInteraction textView = onView(
                allOf(withId(R.id.textViewInfoSrc), withText("192.168.0.19"),
                        withParent(withParent(IsInstanceOf
                                .<View>instanceOf(android.widget.TableLayout.class))),
                        isDisplayed()));
        textView.check(matches(withText("192.168.0.19")));

        ViewInteraction materialButton4 = onView(
                allOf(withId(R.id.button2), withText("Finish"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is(
                                                "android.widget.TableLayout")),
                                        9),
                                0),
                        isDisplayed()));
        materialButton4.perform(click());

        ViewInteraction recyclerView2 = onView(
                allOf(withId(R.id.recyclerView),
                        withParent(withParent(withId(android.R.id.content))),
                        isDisplayed()));
        recyclerView2.check(matches(isDisplayed()));
    }

    @Test
    /**
     * Tests && logic using dst and protocol fields in imported pcap file
     */
    public void packetCaptureTest_DstAndProtocolFilter() {
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

        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.etFilter),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                2),
                        isDisplayed()));
        appCompatEditText.perform(replaceText("tcp && dst 192.168.0.23"), closeSoftKeyboard());

        ViewInteraction materialButton3 = onView(
                allOf(withId(R.id.button), withText("SEARCH"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                1),
                        isDisplayed()));
        materialButton3.perform(click());

        ViewInteraction recyclerView = onView(
                allOf(withId(R.id.recyclerView),
                        childAtPosition(
                                withClassName(is(
                                        "androidx.constraintlayout.widget.ConstraintLayout")),
                                0)));
        recyclerView.perform(actionOnItemAtPosition(0, click()));

        ViewInteraction textView = onView(
                allOf(withId(R.id.textViewInfoProtocol), withText("TCP"),
                        withParent(withParent(IsInstanceOf
                                .<View>instanceOf(android.widget.TableLayout.class))),
                        isDisplayed()));
        textView.check(matches(withText("TCP")));

        ViewInteraction textView2 = onView(
                allOf(withId(R.id.textViewInfoDest), withText("192.168.0.23"),
                        withParent(withParent(IsInstanceOf
                                .<View>instanceOf(android.widget.TableLayout.class))),
                        isDisplayed()));
        textView2.check(matches(withText("192.168.0.23")));

        ViewInteraction materialButton4 = onView(
                allOf(withId(R.id.button2), withText("Finish"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is(
                                                "android.widget.TableLayout")),
                                        9),
                                0),
                        isDisplayed()));
        materialButton4.perform(click());
    }

    @Test
    /**
     * Tests if RTT Graph loads from imported pcap file
     */
    public void graphTest() {
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

        ViewInteraction viewGroup = onView(
                allOf(withId(R.id.rttChart),
                        withParent(withParent(withId(android.R.id.content))),
                        isDisplayed()));
        viewGroup.check(matches(isDisplayed()));

        pressBack();

        ViewInteraction recyclerView = onView(
                allOf(withId(R.id.recyclerView),
                        withParent(withParent(withId(android.R.id.content))),
                        isDisplayed()));
        recyclerView.check(matches(isDisplayed()));
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
