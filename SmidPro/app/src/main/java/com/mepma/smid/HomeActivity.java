package com.mepma.smid;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.EditText;
import android.widget.TextView;

import com.mepma.smid.Activity.Activity.MeetingActivity;
import com.mepma.smid.Fragment.EventsFragment;
import com.mepma.smid.Fragment.MeetingListFragment;
import com.mepma.smid.Fragment.NewMeetFragment;
import com.mepma.smid.Model.SMEvent;
import com.mepma.smid.Util.Const;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class HomeActivity extends AppCompatActivity implements
        MeetingListFragment.OnFragmentInteractionListener,
        EventsFragment.OnFragmentInteractionListener {


    private Unbinder unbinder;

    @BindView(R.id.appbar) AppBarLayout appBarLayout;
    @BindView(R.id.toolbar) Toolbar toolbar;


    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    public EventsFragment eventsFragment;
    public MeetingListFragment meetingListFragment;
    public List<SMEvent> events;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        unbinder = ButterKnife.bind(this);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), this);

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
                Log.d("home oncreate", String.format("onPageScrolled i:%d v:%f  i1:%d",i,v,i1));
            }

            @Override
            public void onPageSelected(int i) {
                Log.d("home oncreate", String.format("onPageSelected i:%d ",i));
                onTabChanged(i);

            }

            @Override
            public void onPageScrollStateChanged(int i) {
                Log.d("home oncreate", String.format("onPageScrollStateChanged i:%d ",i));

            }
        });

//        mNewMeetButton = (FloatingActionButton)findViewById(R.id.new_meeting);
//        onTabChanged(Const.TabOrder.Home.Events);
//        mNewMeetButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                launchNewMeetingForm();
////                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
////                        .setAction("Action", null).show();
//
//            }
//        });

    }


    @Override
    protected void onResume() {
        super.onResume();
        Log.d("LifeCycle", "Home Activity onResume");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }


    private void onTabChanged(int tabIndex) {
        if (Const.TabOrder.Home.Events == tabIndex) {
           // mNewMeetButton.hide();
        } else {
            //mNewMeetButton.show();
        }
    }

    private void launchMeetingForm(SMEvent event) {

        Intent intent = new Intent(this, MeetingActivity.class);
        intent.putExtra("meeting_id",event.mettingId);
        intent.putExtra("meeting_status",event.meetingStatus);
        startActivity(intent);


//        appBarLayout.setVisibility(View.INVISIBLE);
//        toolbar.setVisibility(View.INVISIBLE);
//        mNewMeetButton.hide();
//
//        NewMeetFragment fragment = NewMeetFragment.newInstance();
//        addFragment(fragment);

    }


    private void addFragment(Fragment fragment) {
        if (fragment != null) {

            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.fragment_container, fragment)
                    .addToBackStack(null)
                    .commit();
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onEventTapped(MeetingListFragment fragment, SMEvent event) {
        launchMeetingForm(event);
    }


    @Override
    public void onEventsLoaded(List<SMEvent> events) {
        this.events = events;
        if (meetingListFragment != null) {
            meetingListFragment.setEvents(events);
        }
    }

    @Override
    public void onEventTapped(EventsFragment fragment, SMEvent event) {
        Log.d("meetform","onEventTapped");
        launchMeetingForm(event);
    }


    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_home, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        private HomeActivity homeActivity;

        public SectionsPagerAdapter(FragmentManager fm, HomeActivity homeActivity) {
            super(fm);
            this.homeActivity = homeActivity;
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            if (position == Const.TabOrder.Home.Events) {
                EventsFragment fragment = EventsFragment.newInstance();
                this.homeActivity.eventsFragment = fragment;
                return fragment;
            } else if (position == Const.TabOrder.Home.Meetings) {
                MeetingListFragment fragment = MeetingListFragment.newInstance();
                this.homeActivity.meetingListFragment = fragment;
                if (this.homeActivity.events != null) {
                    fragment.setEvents(this.homeActivity.events);
                }
                return fragment;
            }

            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            return 2;
        }
    }
}
