package de.tudarmstadt.informatik.tk.android.kraken.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import de.tudarmstadt.informatik.tk.android.kraken.PreferenceManager;
import de.tudarmstadt.informatik.tk.android.kraken.ui.activities.accounts.AccountsActivity;
import de.tudarmstadt.informatik.tk.android.kraken.ui.fragments.WelcomeDisclaimerFragment;
import de.tudarmstadt.informatik.tk.android.kraken.ui.fragments.WelcomeFinishFragment;
import de.tudarmstadt.informatik.tk.android.kraken.ui.fragments.WelcomeFragment;
import de.tudarmstadt.informatik.tk.android.kraken.ui.fragments.WelcomeSettingsFragment;
import de.tudarmstadt.informatik.tk.android.kraken.ui.views.NonSwipeableViewPager;
import de.tudarmstadt.informatik.tk.android.kraken.util.AccessibilityUtils;
import de.tudarmstadt.informatik.tk.android.kraken.KrakenServiceManager;
import de.tudarmstadt.informatik.tk.android.kraken.util.KrakenUtils;
import de.tudarmstadt.informatik.tk.android.kraken.R;

/**
 * @author Karsten Planz
 */
@Deprecated
public class WelcomeActivity extends FragmentActivity {

    /**
     * The number of pages (wizard steps) to show.
     */
    private static final int NUM_PAGES = 4;

    /**
     * The pager widget, which handles animation and allows swiping horizontally to access previous
     * and next wizard steps.
     */
    private NonSwipeableViewPager mPager;

    /**
     * The pager adapter, which provides the pages to the view pager widget.
     */
    private PagerAdapter mPagerAdapter;

    private PreferenceManager mPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        mPrefs = PreferenceManager.getInstance(this);

        // Instantiate a ViewPager and a PagerAdapter.
        mPager = (NonSwipeableViewPager) findViewById(R.id.pager);
        mPager.setPagingEnabled(false);
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);

        ViewPager indicator = (ViewPager) findViewById(R.id.indicator);
        indicator.setFadingEdgeLength(0);
        indicator.setAdapter(mPagerAdapter);
    }

    @Override
    public void onBackPressed() {
        if (mPager.getCurrentItem() == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();
        } else {
            // Otherwise, select the previous step.
            mPager.setCurrentItem(mPager.getCurrentItem() - 1);
        }
    }

    public void acceptDisclaimer(View v) {
        mPrefs.setAcceptDisclaimer(true);
        nextPage(v);
    }

    public void declineDisclaimer(View v) {
        /*mPrefs.setAcceptDisclaimer(false);
        Intent main = new Intent(this, MainActivity.class);
        main.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        main.putExtra("EXIT", true);
        startActivity(main);*/
        // FIXME This is disabled for a short time, due to ongoing brainstorming on design
    }

    public void nextPage(View v) {
        if (mPager.getCurrentItem() == NUM_PAGES - 1) {
            releaseTheKraken();
            finish();
        } else {
            mPager.setCurrentItem(mPager.getCurrentItem() + 1);
        }
    }

    public void openAccessibiliySettings(View v) {
        AccessibilityUtils.openAccessibiliySettings(this);
    }

    public void openLogin(View v) {
        Intent intent = new Intent(this, AccountsActivity.class);
        startActivity(intent);
    }

    private void releaseTheKraken() {
        mPrefs.setActivated(true);
        KrakenUtils.initDataProfile(this, mPrefs.getDataProfile());
        KrakenServiceManager.getInstance(this.getApplicationContext())
                .startKrakenService();
    }

    /**
     * A simple pager adapter.
     */
    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new WelcomeDisclaimerFragment();
                case 1:
                    return new WelcomeFragment();
                case 2:
                    return new WelcomeSettingsFragment();
                case 3:
                    return new WelcomeFinishFragment();
            }
            return new WelcomeDisclaimerFragment();
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }
}