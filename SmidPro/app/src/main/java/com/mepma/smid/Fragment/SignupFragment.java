package com.mepma.smid.Fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.mepma.smid.R;
import com.mepma.smid.Util.Const;
import com.mepma.smid.Util.DataHolder;
import com.mepma.smid.Util.PhoneTextFormatter;
import com.mepma.smid.Util.Util;
import com.mepma.smid.http.AsyncHttpClient;
import com.mepma.smid.http.HttpClient;
import com.mepma.smid.http.RequestParams;
import com.mepma.smid.http.StringHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SignupFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SignupFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SignupFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    private static final String TAG = "SignupActivity";

    @BindView(R.id.input_name) EditText _nameText;
//    @BindView(R.id.input_address) EditText _addressText;
    @BindView(R.id.input_email) EditText _emailText;
    @BindView(R.id.input_mobile) EditText _mobileText;
    @BindView(R.id.input_password) EditText _passwordText;
    @BindView(R.id.input_reEnterPassword) EditText _reEnterPasswordText;
    @BindView(R.id.btn_signup) Button _signupButton;
    @BindView(R.id.link_login) TextView _loginLink;

    private Unbinder unbinder;


    private OnFragmentInteractionListener mListener;

    public SignupFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SignupFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SignupFragment newInstance(String param1, String param2) {
        SignupFragment fragment = new SignupFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
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
        View view =  inflater.inflate(R.layout.fragment_signup, container, false);
        unbinder = ButterKnife.bind(this, view);

        return view;
    }


    public void onStart() {
        super.onStart();
        Log.d("Fragment", "onStart");

        _mobileText.addTextChangedListener(new PhoneTextFormatter(_mobileText, Const.Value.Mobile.pattern));

        _signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });

        _loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the Login activity
                /*
                Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                */
            }
        });
    }



    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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



    public void signup() {
        Log.d(TAG, "Signup");

        if (!validate()) {
            onSignupFailed();
            return;
        }

        _signupButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(getContext(),
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();

        String name = _nameText.getText().toString();
//        String address = _addressText.getText().toString();
        String email = _emailText.getText().toString();
        String mobile = Util.extractNumberFromString(_mobileText.getText().toString() );
        String password = _passwordText.getText().toString();
        String confirmPassword = _reEnterPasswordText.getText().toString();

        // TODO: Implement your own signup logic here.
        String urlStr = Const.Url.fullPath(Const.Url.register);
        RequestParams params = new RequestParams();
        params.put(Const.Params.name,name);
        params.put(Const.Params.email,email);
        params.put(Const.Params.mobileNumber,mobile);
        params.put(Const.Params.password, password);
        params.put(Const.Params.confirmPassword, confirmPassword);
        Log.d("updateUI",urlStr + "\n Params : " + params.toString());

        HttpClient client = new AsyncHttpClient();
        client.post(urlStr, params, new StringHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Map<String, List<String>> headers, String content) {
                /* Request was successful */
                progressDialog.dismiss();
                Log.d("updateUI", "onSuccess: " + statusCode + "  Response: " +headers.toString() + " Content:"+content);
                if (content.length() > 0) {
                    try {
                        JSONObject object = new JSONObject(content);

                        if (object.has(Const.Params.success) && (true == object.getBoolean(Const.Params.success) )) {
                            JSONObject dataObj = object.getJSONObject(Const.Params.data);
                            if (dataObj.has(Const.Params.token)) {
                                String token = dataObj.getString(Const.Params.token);
                                if (token != null) {
                                    DataHolder.getInstance().token = token;
                                    onSignupSuccess();
                                    progressDialog.dismiss();
                                } else {
                                    progressDialog.dismiss();
                                }
                            } else {
                                progressDialog.dismiss();
                            }
                        } else {
                            progressDialog.dismiss();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        progressDialog.dismiss();
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Map<String, List<String>> headers, String content) {
                /* Server responded with a status code 4xx or 5xx error */
                progressDialog.dismiss();
                Log.d("updateUI", "onFailure: " + statusCode + "  Response: " +headers.toString() + " Content:"+content);

            }

            @Override
            public void onFailure(Throwable throwable) {
                /* An exception occurred during the request. Usually unable to connect or there was an error reading the response */
                Log.d("updateUI", "onFailure: "+throwable.getLocalizedMessage() );
                progressDialog.dismiss();

            }
        });


    }


    public void onSignupSuccess() {
        Log.d("Login", "onSignupSuccess: " );
        //_signupButton.setEnabled(true);
        //Toast.makeText(getContext(), "Account created Successfully", Toast.LENGTH_SHORT).show();

        //setResult(RESULT_OK, null);
        //finish();
    }

    public void onSignupFailed() {
        Toast.makeText(getContext(), "Login failed", Toast.LENGTH_LONG).show();
        _signupButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String name = _nameText.getText().toString();
//        String address = _addressText.getText().toString();
        String email = _emailText.getText().toString();
        String mobile = Util.extractNumberFromString(_mobileText.getText().toString() );
        String password = _passwordText.getText().toString();
        String reEnterPassword = _reEnterPasswordText.getText().toString();

        if (name.isEmpty() || false == Const.Value.Username.isInRange(name.length()) ) {
            _nameText.setError(
                    String.format("between %d and %d characters",Const.Value.Username.min,Const.Value.Username.max)
            );
            valid = false;
        } else {
            _nameText.setError(null);
        }

        /*if (address.isEmpty()) {
            _addressText.setError("Enter Valid Address");
            valid = false;
        } else {
            _addressText.setError(null);
        } */


        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (mobile.isEmpty() || mobile.length()!=10) {
            _mobileText.setError("enter valid Mobile number");
            valid = false;
        } else {
            _mobileText.setError(null);
        }

        if (password.isEmpty() ||  false == Const.Value.Password.isInRange(password.length() ) ) {
            _passwordText.setError(String.format("between %d and %d alphanumeric characters",Const.Value.Password.min,Const.Value.Password.max));
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        if (reEnterPassword.isEmpty() || false == password.equals(reEnterPassword) ) {
            _reEnterPasswordText.setError("password do not match");
            valid = false;
        } else {
            _reEnterPasswordText.setError(null);
        }

        return valid;
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
    }
}
