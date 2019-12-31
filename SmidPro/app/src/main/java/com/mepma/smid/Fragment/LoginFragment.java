package com.mepma.smid.Fragment;

import android.app.ProgressDialog;
import android.widget.EditText;
import android.widget.TextView;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.mepma.smid.MainActivity;
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


import static android.content.ContentValues.TAG;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LoginFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private String mMobile;

    private OnFragmentInteractionListener mListener;

    private Unbinder unbinder;

    @BindView(R.id.input_mobile) EditText _mobileText;
    @BindView(R.id.input_password) EditText _passwordText;
    @BindView(R.id.btn_login) Button _loginButton;
    @BindView(R.id.link_signup) TextView _signupLink;




    public LoginFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LoginFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LoginFragment newInstance(String param1, String param2) {
        LoginFragment fragment = new LoginFragment();
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
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
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

    public void onStart() {
        super.onStart();
        Log.d("Fragment", "onStart");

        _mobileText.addTextChangedListener(new PhoneTextFormatter(_mobileText, Const.Value.Mobile.pattern));
//        _mobileText.setText("8179402094");
//        _passwordText.setText("12345678");

        _loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                login();
            }
        });

        _signupLink.setVisibility(View.GONE);
        _signupLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

               // showSignUpScreen();

                // Start the Signup activity
                /*
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out); */
            }
        });

    }


    public void login() {
        Log.d(TAG, "Login");

        if (Util.isNetworkAvaliable(getContext()) == false) {
            Util.showOfflineMessage(getContext());
            return;
        }


        if (!validate()) {
            onLoginFailed();
            return;
        }

        _loginButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(getContext(),
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();

        String password = _passwordText.getText().toString();


        String urlStr = Const.Url.fullPath(Const.Url.login);
        RequestParams params = new RequestParams();
        params.put(Const.Params.mobileNumber,mMobile);
        params.put(Const.Params.password, password);
        Log.d("updateUI",urlStr + "\n Params : " + params.toString());

        HttpClient client = new AsyncHttpClient();
        client.setConnectionTimeout(3000);
        client.post(urlStr, params, new StringHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Map<String, List<String>> headers, String content) {
                /* Request was successful */
                progressDialog.dismiss();
                Log.d("updateUI", "onSuccess: " + statusCode + "  Response: " +headers.toString() + " Content:"+content);
                if (content.length() > 0) {
                    try {
                        JSONObject object = new JSONObject(content);

                         if (object.has(Const.Params.success) ) {
                             JSONObject successObj = object.getJSONObject(Const.Params.success);
                             if (successObj.has(Const.Params.token)) {
                                 final String token = successObj.getString(Const.Params.token);
                                 getActivity().runOnUiThread(new Runnable() {
                                     @Override
                                     public void run() {
                                         DataHolder.getInstance().token = token;
                                         progressDialog.dismiss();
                                         onLoginSuccess();
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
                } else {

                }

            }

            @Override
            public void onFailure(int statusCode, Map<String, List<String>> headers, String content) {
                /* Server responded with a status code 4xx or 5xx error */

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();
                        onLoginFailed();
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
                        progressDialog.dismiss();
                        onLoginFailed();
                    }
                });
            }
        });




       /* new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onLoginSuccess or onLoginFailed
                        onLoginSuccess();
                        // onLoginFailed();
                        progressDialog.dismiss();
                    }
                }, 3000); */
    }

    public void onLoginSuccess() {
        Log.d("Login", "onLoginSuccess: " +mListener.toString() );
        //Toast.makeText(getContext(), "Logged in Successfully", Toast.LENGTH_SHORT).show();
        //_loginButton.setEnabled(true);
        mListener.onLoginSuccessfully();


    }

    public void onLoginFailed() {
        Toast.makeText(getActivity(), "Login failed", Toast.LENGTH_SHORT).show();

        _loginButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String mobileStr = _mobileText.getText().toString();
        String password = _passwordText.getText().toString();

        mMobile = Util.extractNumberFromString(mobileStr);
        Log.d("Login", "mobileStr: "+mobileStr + "  number:"+mMobile  );


        if (mMobile.isEmpty() || ( mMobile.length() != Const.Value.Mobile.length) ) {
            _mobileText.setError("enter Valid Mobile Number");
            valid = false;
        } else {
            _mobileText.setError(null);
        }

        if (password.isEmpty() || false == Const.Value.Password.isInRange(password.length())) {
            _passwordText.setError(String.format("between %d and %d characters",Const.Value.Password.min,Const.Value.Password.max));
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }

    public void showSignUpScreen() {
        mListener.onFragmentInteraction(this);
    }






    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);

        void onLoginSuccessfully();

        void onFragmentInteraction(LoginFragment fragment);
    }
}
