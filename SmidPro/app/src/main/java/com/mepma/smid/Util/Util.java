package com.mepma.smid.Util;

import android.app.DownloadManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.FragmentManager;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.mepma.smid.R;
import com.mepma.smid.http.AsyncHttpClient;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.security.PublicKey;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Util {

    public static String optString(JSONObject json, String key) {
        // http://code.google.com/p/android/issues/detail?id=13830
        if (json.isNull(key))
            return "";
        else
            return json.optString(key, "");
    }

    static public KProgressHUD stdProgressHUD(Context context, String title, String detailText) {
        KProgressHUD hud = KProgressHUD.create(context)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel(title)
                .setDetailsLabel(detailText)
                .setCancellable(true)
                .setAnimationSpeed(1)
                .setDimAmount(0.5f);
        return hud;

    }

    static public void popToTop(FragmentManager fragmentManager){

        Log.d("Button"," count : "+fragmentManager.getBackStackEntryCount());

        for(int i = 0; i < fragmentManager.getBackStackEntryCount(); ++i) {
            fragmentManager.popBackStack();
        }
    }

    public static String extractNumberFromString(String source) {
        StringBuilder result = new StringBuilder(100);
        for (char ch : source.toCharArray()) {
            if (ch >= '0' && ch <= '9') {
                result.append(ch);
            }
        }

        return result.toString();
    }

    public static void addSMIDHeader(AsyncHttpClient httpClient) {

        String authorizationValue = Const.Value.HttpHeader.Bearer + DataHolder.getInstance().token;
        Log.d("token"," count : "+authorizationValue);

        httpClient.setHeader(Const.Value.HttpHeader.accept,Const.Value.HttpHeader.appJson);
        httpClient.setHeader(Const.Value.HttpHeader.authorization,authorizationValue);
    }

    // put the image file path into this method
    public static String getFileToByte(String filePath){
        Bitmap bmp = null;
        ByteArrayOutputStream bos = null;
        byte[] bt = null;
        String encodeString = null;
        try{
            bmp = BitmapFactory.decodeFile(filePath);
            bos = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bt = bos.toByteArray();
            encodeString = Base64.encodeToString(bt, Base64.DEFAULT);
        }catch (Exception e){
            e.printStackTrace();
        }
        return encodeString;
    }

    public static String ImageToString(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        byte [] imgBytes = outputStream.toByteArray();
        return Base64.encodeToString(imgBytes, Base64.DEFAULT);
    }

    public static String dateInStringFormat(Date date) {
        return new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(date);
    }

    public static String timeInStringFormat() {
       return new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
    }


    public static boolean isNetworkAvaliable(Context ctx) {
        ConnectivityManager connectivityManager = (ConnectivityManager) ctx
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if ((connectivityManager
                .getNetworkInfo(ConnectivityManager.TYPE_MOBILE) != null && connectivityManager
                .getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED)
                || (connectivityManager
                .getNetworkInfo(ConnectivityManager.TYPE_WIFI) != null && connectivityManager
                .getNetworkInfo(ConnectivityManager.TYPE_WIFI)
                .getState() == NetworkInfo.State.CONNECTED)) {
            return true;
        } else {
            return false;
        }
    }

    public static void showOfflineMessage(Context ctx) {

        Toast.makeText(ctx, ctx.getResources().getString(R.string.no_network), Toast.LENGTH_SHORT).show();

    }






}
