package com.mepma.smid.Fragment;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mepma.smid.Model.SMEvent;
import com.mepma.smid.R;
import com.mepma.smid.Util.Const;
import com.mepma.smid.Util.DataHolder;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MeetingListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MeetingListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MeetingListFragment extends Fragment {

    @BindView(R.id.recycler_view) RecyclerView _recycleView;

    private Unbinder unbinder;
    private ViewAdapter mAdapter;
    private List<SMEvent> mEvents;


    private OnFragmentInteractionListener mListener;

    public MeetingListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment MeetingListFragment.
     */

    public void setEvents(List<SMEvent> events) {
        mEvents = events;
        if (_recycleView != null) {
            configureUI();
        }

    }

    public static MeetingListFragment newInstance() {
        MeetingListFragment fragment = new MeetingListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_meeting_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, getView());

        if (mEvents != null) {
            configureUI();
        }

    }

    private void configureUI() {

        mAdapter = new ViewAdapter(mEvents);

        final MeetingListFragment thisFragment = this;
        if (_recycleView.getLayoutManager() == null) {
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
            _recycleView.setLayoutManager(layoutManager);
            _recycleView.setItemAnimator(new DefaultItemAnimator());
            _recycleView.addItemDecoration(new DividerItemDecoration(this.getContext(), LinearLayoutManager.VERTICAL));

            _recycleView.addOnItemTouchListener(
                    new RecyclerItemClickListener(getActivity(), new   RecyclerItemClickListener.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {
                            Log.d("itemClick: ",""+position);
                            SMEvent event = mEvents.get(position);
                            if (Integer.parseInt(event.meetingStatus) == 0) {
                                mListener.onEventTapped(thisFragment, mEvents.get(position));
                            }
                        }
                    })
            );

        }

        _recycleView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();


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
        void onEventTapped(MeetingListFragment fragment, SMEvent event);
    }



    class ViewAdapter extends RecyclerView.Adapter<ViewAdapter.ViewHolder> {

        private List<SMEvent> events;


    /*
    @Override
    public void onClick(View v) {
        Log.d("didSelectedInfoPage",  "onPageSelected position " );
    } */

        class ViewHolder extends RecyclerView.ViewHolder {

            public TextView date, status, description;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);

                date = itemView.findViewById(R.id.tv_meeting_date);
                status = itemView.findViewById(R.id.tv_meeting_status);
                description = (TextView) itemView.findViewById(R.id.tv_meeting_description);

            }
        }


        public ViewAdapter() {

        }

        public ViewAdapter(List<SMEvent> events) {
            this.events = events;
        }


        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.event_row, viewGroup, false);

            ViewGroup.LayoutParams layoutParams = itemView.getLayoutParams();
            return new ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
            SMEvent info = events.get(i);
            long dateInMilliSec = DataHolder.getInstance().parseDateStringToTimeInterval(info.meetingDate);
            String dateStr = DateUtils.formatDateTime(getActivity(), dateInMilliSec, DateUtils.FORMAT_SHOW_YEAR | DateUtils.FORMAT_NO_MONTH_DAY);
            String statusStr = Const.MeetingStatus.stringValueFromInt(Integer.parseInt(info.meetingStatus));

            viewHolder.date.setText(dateStr);
            viewHolder.status.setText(statusStr.toUpperCase());
            viewHolder.status.setTextColor(Const.MeetingStatus.color(Integer.parseInt(info.meetingStatus)));
            viewHolder.description.setText(info.meetingDescription);


        }

        @Override
        public int getItemCount() {
            return events.size();
        }

    }
}
