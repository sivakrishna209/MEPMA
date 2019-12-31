package com.mepma.smid.Fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.mepma.smid.Activity.Activity.MeetingActivity;
import com.mepma.smid.Util.Const;

import com.mepma.smid.R;
import com.mepma.smid.Util.DataHolder;
import com.mepma.smid.Util.Util;
import com.mepma.smid.http.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CorpusUpdate.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CorpusUpdate#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CorpusUpdate extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private Unbinder unbinder;
    private String mMeetingId;

    @BindView(R.id.input_collection) EditText _etCollection;
    @BindView(R.id.input_lending_amount) EditText _etLendingAmount;
    @BindView(R.id.input_lend_return_amount) EditText _etLendReturnAmount;
    @BindView(R.id.input_fund_recv_amount) EditText _etFundRecvAmount;
    @BindView(R.id.input_fund_recv_name) EditText _etFundRecvName;
    @BindView(R.id.input_other_expenditure) EditText _etOtherExpenditure;
    @BindView(R.id.input_balance_todate) EditText _etBalance;
    @BindView(R.id.btn_submit_corpus) Button _btnSubmit;



    public CorpusUpdate() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param meetingId Parameter 1.
     * @return A new instance of fragment CorpusUpdate.
     */
    // TODO: Rename and change types and number of parameters
    public static CorpusUpdate newInstance(String meetingId) {
        CorpusUpdate fragment = new CorpusUpdate();
        fragment.mMeetingId = meetingId;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_corpus_update, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        _btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    submitCorpusDetails();
            }
        });

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



    private boolean validateFormValues() {

        boolean valid = true;

        String collectionAmount = _etCollection.getText().toString();
        if (collectionAmount.isEmpty() == true) {
            _etCollection.setError("Please enter Collection Amount");
            valid = false;
        }

        String lendingAmount = _etLendingAmount.getText().toString();
        if (lendingAmount.isEmpty() == true) {
            _etLendingAmount.setError("Please enter Lending Amount");
            valid = false;
        }

        String lendingReturn = _etLendReturnAmount.getText().toString();
        if (lendingReturn.isEmpty() == true) {
            _etLendReturnAmount.setError("Please enter Lending return Amount");
            valid = false;
        }

        String receivedAmount = _etFundRecvAmount.getText().toString();
        if (receivedAmount.isEmpty() == true) {
            _etFundRecvAmount.setError("Please enter Fund received Amount");
            valid = false;
        }

        String receiverName = _etFundRecvName.getText().toString();
        if (receiverName.isEmpty() == true) {
            _etFundRecvName.setError("Please enter Fund receiver name");
            valid = false;
        }

        String otherExpenditure = _etOtherExpenditure.getText().toString();
        if (otherExpenditure.isEmpty() == true) {
            _etOtherExpenditure.setError("Please enter other expenditure amount");
            valid = false;
        }

        String balance = _etBalance.getText().toString();
        if (balance.isEmpty() == true) {
            _etBalance.setError("Please enter balance amount");
            valid = false;
        }

        return valid;
    }

    private void submitCorpusDetails() {
        if (Util.isNetworkAvaliable(getContext()) == false) {
            Util.showOfflineMessage(getContext());
            return;
        }

        if (validateFormValues() == false) {
            return;
        }

        final ProgressDialog progressDialog = new ProgressDialog(getContext(),
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Submitting Corpus...");
        progressDialog.show();
        String urlStr = Const.Url.fullPath(Const.Url.corpusUpdate);

        StringRequest postRequest = new StringRequest(Request.Method.POST, urlStr,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response.toString());
                        ((MeetingActivity)getActivity()).dismissProgressDialogue(progressDialog);
                        String resultResponse = new String(response);
                        try {
                            JSONObject result = new JSONObject(resultResponse);
                            Log.d("httpResponse",result.toString());

                            if (result.getBoolean(Const.Params.success) == true ) {
                               // mListener.didNewMeetDetailsSubmitted();
                                onSubmitSuccess();
                            } else {
                                String message = Util.optString(result,Const.Params.message);
                                if (message.isEmpty() == true) {
                                    message = "Unable to submit corpus details";
                                }
                                onSubmitFailed(message);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        ((MeetingActivity)getActivity()).dismissProgressDialogue(progressDialog);
                        Log.d("Error",error.getLocalizedMessage());
                        onSubmitFailed(error.getLocalizedMessage());

                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put(Const.Params.savings, _etCollection.getText().toString());
                params.put(Const.Params.lending, _etLendingAmount.getText().toString());
                params.put(Const.Params.lendingReturn, _etLendReturnAmount.getText().toString());
                params.put(Const.Params.fundAmount, _etFundRecvAmount.getText().toString());
                params.put(Const.Params.fundReceiverName, _etFundRecvName.getText().toString());
                params.put(Const.Params.expenditure, _etOtherExpenditure.getText().toString());
                params.put(Const.Params.corpusBalance, _etBalance.getText().toString());
                params.put(Const.Params.meetingId, mMeetingId);

                return params;
            }

            @Override
            public Map<String, String> getHeaders() {
                // Posting parameters to login url
                Map<String, String> headers = new HashMap<String, String>();
                String authorizationValue = Const.Value.HttpHeader.Bearer + DataHolder.getInstance().token;
                headers.put(Const.Value.HttpHeader.authorization,authorizationValue);
                headers.put("Accept", Const.Value.HttpHeader.appJson);
                Log.d("getHeaders"," getHeaders corpus update header "+headers.toString());
                return headers;
            }
        };
        VolleySingleton.getInstance(getContext()).addToRequestQueue(postRequest);

    }

    private void onSubmitFailed(String message) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getContext(),"Failed to submit corpus details", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void onSubmitSuccess() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getActivity().getApplicationContext(),"Corpus details submitted successfully", Toast.LENGTH_LONG).show();
                getActivity().finish();

            }
        });
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
    }
}
