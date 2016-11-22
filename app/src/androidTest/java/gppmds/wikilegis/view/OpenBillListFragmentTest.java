package gppmds.wikilegis.view;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.wifi.WifiManager;
import android.preference.PreferenceManager;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.NoActivityResumedException;
import android.support.test.espresso.action.ViewActions;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import android.view.WindowManager;

import org.junit.Before;

import gppmds.wikilegis.R;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.Espresso.closeSoftKeyboard;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withSpinnerText;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

/**
 * Created by izabela on 06/10/16.
 */
public class OpenBillListFragmentTest extends ActivityInstrumentationTestCase2<LoginActivity>{

    public OpenBillListFragmentTest() {
        super(LoginActivity.class);
    }

    @Before
    public void setUp() throws Exception {
        super.setUp();
        final Activity activityOnTest = getActivity();
        Runnable wakeUpDevice = new Runnable() {
            public void run() {
                activityOnTest.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON |
                        WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                        WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            }
        };
        activityOnTest.runOnUiThread(wakeUpDevice);

        WifiManager wifiManager = (WifiManager)getActivity().getSystemService(Context.WIFI_SERVICE);

        final boolean STATUS = true;

        wifiManager.setWifiEnabled(STATUS);

        try {
            Thread.sleep(400);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        SharedPreferences session = PreferenceManager.
                getDefaultSharedPreferences(getActivity());

        if (session.getBoolean("IsLoggedIn", false)){
            onView(withId(R.id.action_profile_logged)).perform(click());
            onView(withText("Sair")).perform(click());
        }
        closeSoftKeyboard();
        onView(withText("Visitante")).perform(ViewActions.scrollTo()).perform(click());
    }

    public void tearDown() throws Exception {
        Log.d("TAG", "TEARDOWN");

        goBackN();

        super.tearDown();
    }

    private void goBackN() {
        final int N = 10; // how many times to hit back button
        try {
            for (int i = 0; i < N; i++)
                Espresso.pressBack();
        } catch (NoActivityResumedException e) {
            Log.e("TAG", "Closed all activities", e);
        }
    }

    public void testDefaultFilteringOption(){
        onView(withId(R.id.spinner_open)).check(matches(withText("Relevantes")));
    }

    public void testChangFilteringOptionToRecent(){
        onView(withId(R.id.spinner_open)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("Recentes"))).perform(click());
        onView(withId(R.id.spinner_open)).check(matches(withText("Recentes")));
    }

    public void testChangFilteringOptionToRelevant() throws InterruptedException{
        onView(withId(R.id.spinner_open)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("Recentes"))).perform(click());
        onView(withId(R.id.spinner_open)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("Relevantes"))).perform(click());
        onView(withId(R.id.spinner_open)).check(matches(withText("Relevantes")));
    }
}
