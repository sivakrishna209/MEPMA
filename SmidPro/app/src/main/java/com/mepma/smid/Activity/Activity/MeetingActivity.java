package com.mepma.smid.Activity.Activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.mepma.smid.Fragment.CorpusUpdate;
import com.mepma.smid.Fragment.NewMeetFragment;
import com.mepma.smid.R;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class MeetingActivity extends AppCompatActivity implements NewMeetFragment.OnFragmentInteractionListener, CorpusUpdate.OnFragmentInteractionListener{



    public static final int REQUEST_IMAGE = 100;
    public static final int REQUEST_PERMISSION = 200;
    private String imageFilePath = "";
    private NewMeetFragment mNewMeetFragment;
    private String mMeetingId;
    private Uri mPhotoUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting);

        setTitle("Meeting Details");

        mMeetingId = (String) getIntent().getSerializableExtra("meeting_id");
        String meetingStatus = (String) getIntent().getSerializableExtra("meeting_status");

        NewMeetFragment fragment = NewMeetFragment.newInstance(mMeetingId, meetingStatus);
        addFragment(fragment);
        mNewMeetFragment = fragment;

        configureUIListeners();

    }


    private void configureUIListeners() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_PERMISSION);
        }


    }





    private void addFragment(Fragment fragment) {
        if (fragment != null) {

            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.fragment_container, fragment)
                    .commit();
        }
    }

    private void replaceFragment(Fragment fragment) {
        if (fragment != null) {

            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
        }
    }

    public void dismissProgressDialogue(final ProgressDialog progressDialog) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressDialog.dismiss();
            }
        });
    }


    private void openCameraIntent() {
        Intent pictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (pictureIntent.resolveActivity(getPackageManager()) != null) {

            File photoFile = null;
            try {
                photoFile = createImageFile();
            }
            catch (IOException e) {
                e.printStackTrace();
                return;
            }
            mPhotoUri = FileProvider.getUriForFile(this, getPackageName() +".provider", photoFile);
            pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mPhotoUri);
            startActivityForResult(pictureIntent, REQUEST_IMAGE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_PERMISSION && grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Toast.makeText(this, "Thanks for granting Permission", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d("onActivityResult",String.format("requestCode %d  resultCode:%d ",requestCode,resultCode));
        if (requestCode == REQUEST_IMAGE) {
            if (resultCode == RESULT_OK ) {
                //imageView.setImageURI(Uri.parse(imageFilePath));
                if (data != null) {
                    Log.d("onActivityResult", "path " + data.getData().toString());
                }
                mNewMeetFragment.onImageCaptured(imageFilePath, mPhotoUri);
            }
            else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "You cancelled the operation", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private File createImageFile() throws IOException{

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "IMG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        imageFilePath = image.getAbsolutePath();
        Log.d("imageFilePath","imageFilePath "+imageFilePath);
        return image;
    }



    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void takePhoto() {
        openCameraIntent();
    }

    @Override
    public void didNewMeetDetailsSubmitted() {

        Fragment corpusFragment =  CorpusUpdate.newInstance(mMeetingId);
        replaceFragment(corpusFragment);
        setTitle("Corpus Details");


    }


}
