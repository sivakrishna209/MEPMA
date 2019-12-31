package com.mepma.smid.Fragment;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;


import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;
import com.mepma.smid.Model.SMEvent;
import com.mepma.smid.Model.SMMember;
import com.mepma.smid.R;
import com.mepma.smid.Util.Const;
import com.mepma.smid.Util.DataHolder;
import com.mepma.smid.Util.Util;
import com.mepma.smid.http.AsyncHttpClient;
import com.mepma.smid.http.RequestParams;
import com.mepma.smid.http.StringHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link EventsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link EventsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EventsFragment extends Fragment {


    private OnFragmentInteractionListener mListener;
    private CompactCalendarView mCalenderView;

    @BindView(R.id.btn_previous_month) ImageButton _btn_previous_month;
    @BindView(R.id.btn_previous_year) ImageButton _btn_previous_year;
    @BindView(R.id.btn_next_year) ImageButton _btn_next_year;
    @BindView(R.id.btn_next_month) ImageButton _btn_next_month;
    @BindView(R.id.btn_today) Button _btn_today;
    @BindView(R.id.tv_focus_month) TextView _monthTitle;
    @BindView(R.id.compactcalendar_view) CompactCalendarView _calender;


    private List<SMEvent> mEvents;
    private Date mTitleDate;

    public EventsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment EventsFragment.
     */

    public static EventsFragment newInstance() {
        EventsFragment fragment = new EventsFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_events, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mTitleDate = new Date();
        assignCalenderMonthTitle();
        configureUI();
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("LifeCycle", "Events Fragment onResume");
        retrieveEvents();

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private void configureUI() {
        mCalenderView = getView().findViewById(R.id.compactcalendar_view);
        mCalenderView.shouldDrawIndicatorsBelowSelectedDays(true);
        mCalenderView.setEventIndicatorStyle(CompactCalendarView.FILL_LARGE_INDICATOR);
        mCalenderView.setCurrentDayIndicatorStyle(CompactCalendarView.NO_FILL_LARGE_INDICATOR);
        mCalenderView.setCurrentSelectedDayIndicatorStyle(CompactCalendarView.NO_FILL_LARGE_INDICATOR);
        mCalenderView.setCurrentDayBackgroundColor(Color.TRANSPARENT);
        mCalenderView.setCurrentSelectedDayBackgroundColor(Color.TRANSPARENT);
        mCalenderView.setCurrentDayTextColor(R.color.red);

        Log.d("Button", _btn_previous_month.toString());

        final EventsFragment thisFragment = this;

        mCalenderView.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                List<Event> events =  _calender.getEvents(dateClicked);
                if (events != null && events.size() > 0) {
                    Event event = events.get(0);
                    SMEvent smEvent = (SMEvent)event.getData();
                    if (Integer.parseInt(smEvent.meetingStatus) == 0) {
                        mListener.onEventTapped(thisFragment, smEvent);
                    }

                }

            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                mTitleDate = firstDayOfNewMonth;
                assignCalenderMonthTitle();
            }
        });

        _btn_previous_month.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                cal.setTime(mTitleDate);
                cal.add(Calendar.MONTH, -1);
                mTitleDate = cal.getTime();
                _calender.setCurrentDate(mTitleDate);
                assignCalenderMonthTitle();


            }
        });
        _btn_next_month.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                cal.setTime(mTitleDate);
                cal.add(Calendar.MONTH, 1);
                mTitleDate = cal.getTime();
                _calender.setCurrentDate(mTitleDate);
                assignCalenderMonthTitle();

            }
        });

        _btn_previous_year.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                cal.setTime(mTitleDate);
                cal.add(Calendar.YEAR, -1);
                mTitleDate = cal.getTime();
                _calender.setCurrentDate(mTitleDate);
                assignCalenderMonthTitle();

            }
        });

        _btn_next_year.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                cal.setTime(mTitleDate);
                cal.add(Calendar.YEAR, 1);
                mTitleDate = cal.getTime();
                _calender.setCurrentDate(mTitleDate);
                assignCalenderMonthTitle();

            }
        });

        _btn_today.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                cal.setTime(new Date());
                mTitleDate = cal.getTime();
                _calender.setCurrentDate(mTitleDate);
                assignCalenderMonthTitle();

            }
        });


    }

    private void assignCalenderMonthTitle() {
        String str = DateUtils.formatDateTime(getActivity(), mTitleDate.getTime(), DateUtils.FORMAT_SHOW_YEAR | DateUtils.FORMAT_NO_MONTH_DAY);
        _monthTitle.setText(str);

    }

    private void retrieveEvents() {

        String urlStr = Const.Url.fullPath(Const.Url.events);
        RequestParams params = new RequestParams();
        AsyncHttpClient client = new AsyncHttpClient();
        client.setConnectionTimeout(3000);
        Util.addSMIDHeader(client);
        client.get(urlStr, params, new StringHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Map<String, List<String>> headers, String content) {
                /* Request was successful */
                Log.d("updateUI", "onSuccess: " + statusCode + "  Response: " +headers.toString() + " Content:"+content);
                if (content.length() > 0) {
                    try {
                        final JSONObject object = new JSONObject(content);

                        if (object.has(Const.Params.success) ) {
                            if (object.getBoolean(Const.Params.success) == true && object.has(Const.Params.data)) {

                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        onRetrievingEvents(object);
                                    }
                                });

                            }

                        }
//                        DataHolder.getInstance().fitUser.addEntries(object);
//                        String status = object.getString(Const.Params.status);
//                        Log.d("updateUI", "onSuccess: " + object.getString(Const.Params.status) );
                        //redirectToIput(status);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onFailure(int statusCode, Map<String, List<String>> headers, String content) {
                /* Server responded with a status code 4xx or 5xx error */

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
//                        progressDialog.dismiss();
//                        onLoginFailed();
                    }
                });

                Log.d("updateUI", "onFailure: " + statusCode + "  Response: " +headers.toString() + " Content:"+content);

            }

            @Override
            public void onFailure(Throwable throwable) {
                /* An exception occurred during the request. Usually unable to connect or there was an error reading the response */
                Log.d("updateUI", "onFailure: "+throwable.getLocalizedMessage() );
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
//                        progressDialog.dismiss();
//                        onLoginFailed();
                    }
                });
            }
        });


    }

    private void onRetrievingEvents(JSONObject resObject) {

        mEvents =  SMEvent.parseList(resObject);
        mListener.onEventsLoaded(mEvents);
        Log.d("result", "mEvents count: "+ String.format("%d",mEvents.size()));

        _calender.removeAllEvents();
        for (int i=0; i< mEvents.size(); i++) {
            SMEvent event = mEvents.get(i);

            long timeInMilliSec = DataHolder.getInstance().parseDateStringToTimeInterval(event.meetingDate);
            if (timeInMilliSec != -1) {
                int color = Const.MeetingStatus.color(Integer.parseInt(event.meetingStatus));
                Event calEvent = new Event(color, timeInMilliSec, event);
                _calender.addEvent(calEvent);
            }


        }




    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);

        void onEventsLoaded(List<SMEvent> events);

        void onEventTapped(EventsFragment fragment, SMEvent event);
    }
}
