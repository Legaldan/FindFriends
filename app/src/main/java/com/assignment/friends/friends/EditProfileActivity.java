package com.assignment.friends.friends;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.assignment.friends.friends.model.Profile;
import com.assignment.friends.friends.util.HttpGetAsyncTask;
import com.assignment.friends.friends.util.JsonHandler;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Scanner;

public class EditProfileActivity extends AppCompatActivity {

    private EditText mFirstName;
    private EditText mSurname;
    private Spinner mGender;
    private EditText mAddress;
    private TextView mBirth;
    private EditText mCourse;
    private EditText mNativeLanguage;
    private EditText mFavoriteSport;
    private EditText mFavoriteMovie;
    private EditText mFavoriteUnit;
    private EditText mCurrentJob;
    private Spinner mStudyModel;
    private Spinner mNationality;
    int mYear, mMonth, mDay;
    final int DATE_DIALOG = 1;
    private Profile mProfile;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        initView();
        setView();

        Button bt_birth = (Button)findViewById(R.id.bt_birth);
        bt_birth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DATE_DIALOG);
            }
        });
        final Calendar ca = Calendar.getInstance();
        mYear = ca.get(Calendar.YEAR);
        mMonth = ca.get(Calendar.MONTH);
        mDay = ca.get(Calendar.DAY_OF_MONTH);

        Button updateButton = (Button)findViewById(R.id.button);
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProfile.setAddress(mAddress.getText().toString());
                String birth = mBirth.getText().toString();
                if (!birth.equals(getString(R.string.null_profile_details)))
                    mProfile.setBirth(birth);
                mProfile.setCourse(mCourse.getText().toString());
                mProfile.setCurrentJob(mCurrentJob.getText().toString());
                mProfile.setFavMovie(mFavoriteMovie.getText().toString());
                mProfile.setFavSport(mFavoriteSport.getText().toString());
                mProfile.setFavUnit(mFavoriteUnit.getText().toString());
                mProfile.setFirstName(mFirstName.getText().toString());
                mProfile.setGender(mGender.getSelectedItemPosition());
                mProfile.setNationality(mNationality.getSelectedItem().toString());
                mProfile.setNativeLanguage(mNativeLanguage.getText().toString());
                mProfile.setSurname(mSurname.getText().toString());

                new UpdateProfileTask().execute(mProfile);
            }
        });
    }

    private void initView(){
        mFirstName = (EditText) findViewById(R.id.first_name);
        mSurname = (EditText)findViewById(R.id.surname);
        mGender = (Spinner)findViewById(R.id.gender);
        mAddress = (EditText)findViewById(R.id.address);
        mCourse = (EditText)findViewById(R.id.course);
        mBirth = (TextView)findViewById(R.id.tv_birth);
        mNationality = (Spinner)findViewById(R.id.nationality);
        mFavoriteSport = (EditText)findViewById(R.id.favorite_sport);
        mFavoriteMovie = (EditText)findViewById(R.id.favorite_movie);
        mFavoriteUnit = (EditText)findViewById(R.id.favorite_unit);
        mNativeLanguage = (EditText) findViewById(R.id.native_language);
        mStudyModel = (Spinner)findViewById(R.id.study_mode);
        mCurrentJob = (EditText)findViewById(R.id.current_job);
    }

    private void setView(){
        Intent intent = getIntent();
        String email = intent.getStringExtra("email");
        if (!email.equals("")) {
            String path = "http://192.168.191.1:8080/FindFriends/webresources/profile/findByEmail/" + email;
            GetProfileTask task = new GetProfileTask(this, "Loading Profile failed", path);
            task.execute();
        }
    }

    class GetProfileTask extends HttpGetAsyncTask<Void,Profile> {

        public GetProfileTask(Context c, String msg, String path) {
            super(c, msg, path);
        }

        @Override
        protected void httpPostExecute(Profile profile) throws Exception {
            if (profile != null) {
                mProfile = profile;
                mFirstName.setText(mProfile.getFirstName());
                mSurname.setText(mProfile.getSurname());

                int gender = mProfile.getGender();
                mGender.setSelection(gender==0?1:0);

                String birth = mProfile.getBirth();
                mBirth.setText(birth==null?getString(R.string.null_profile_details):birth.substring(0,10));

                int studyMode = mProfile.getStudyMode();
                mStudyModel.setSelection(studyMode==0?1:0);

                String nationality = mProfile.getNationality();
                if (nationality != null) {
                    String[] array = getResources().getStringArray(R.array.nationalities);
                    int position;
                    for (position = 0; position < array.length; position++) {
                        if (nationality.equals(array[position])) {
                            break;
                        }
                    }
                    mNationality.setSelection(position);
                }else{
                    mNationality.setSelection(0);
                }

                String address = mProfile.getAddress();
                if(address == null)
                    mAddress.setHint(getString(R.string.null_profile_details));
                else
                    mAddress.setText(address);

                String course = mProfile.getCourse();
                if(course == null)
                    mCourse.setHint(getString(R.string.null_profile_details));
                else
                    mCourse.setText(course);

                String nativeLanguage = mProfile.getNativeLanguage();
                if(nativeLanguage == null)
                    mNativeLanguage.setHint(getString(R.string.null_profile_details));
                else
                    mNativeLanguage.setText(nativeLanguage);

                String currentJob = mProfile.getCurrentJob();
                if (currentJob == null)
                    mCurrentJob.setHint(getString(R.string.null_profile_details));
                else
                    mCurrentJob.setText(currentJob);

                String favoriteMovie = mProfile.getFavMovie();
                if (favoriteMovie == null)
                    mFavoriteMovie.setHint(getString(R.string.null_profile_details));
                else
                    mFavoriteMovie.setText(favoriteMovie);

                String favoriteUnit = mProfile.getFavUnit();
                if (favoriteUnit == null)
                    mFavoriteUnit.setHint(getString(R.string.null_profile_details));
                else
                    mFavoriteUnit.setText(favoriteUnit);

                String favoriteSport = mProfile.getFavSport();
                if (favoriteSport == null)
                    mFavoriteSport.setHint(getString(R.string.null_profile_details));
                else
                    mFavoriteSport.setText(favoriteSport);
            }

        }

        @Override
        protected Profile ownTask(InputStream result) throws Exception {
            String json = JsonHandler.readInputStream(result);
            List<Profile> profileList = JSON.parseArray(json,Profile.class);
            Profile profile = profileList.get(0);
            return profile;
        }
    }


    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_DIALOG:
                return new DatePickerDialog(this, mDateListener, mYear, mMonth, mDay);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener mDateListener = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            mYear = year;
            mMonth = monthOfYear;
            mDay = dayOfMonth;

            mBirth.setText(new StringBuffer().append(mYear).append("-").append(mMonth + 1).append("-").append(mDay));
        }
    };


    class UpdateProfileTask extends AsyncTask<Profile,Void,Void>{

        private Exception e;

        @Override
        protected Void doInBackground(Profile... params) {

            HttpURLConnection conn = null;

            try {
                URL url = new URL("http://192.168.191.1:8080/FindFriends/webresources/profile/"+mProfile.getId());
                conn = (HttpURLConnection)url.openConnection();
                conn.setConnectTimeout(5000);
                conn.setRequestMethod("PUT");
                conn.setRequestProperty("Content-Type",  "application/json;charset=utf-8");
                OutputStream os = conn.getOutputStream();
                String content = JSON.toJSONString(params[0]);
                os.write(content.getBytes());
                os.close();
                int code = conn.getResponseCode();
                if (code < 300){
                    //ready to get response from server
                    finish();
                    Toast.makeText(getApplicationContext(), "Update successfully!",Toast.LENGTH_SHORT).show();

                    return null;
                }else{
                    throw new Exception();
                }
            } catch (Exception ie){
                this.e = ie;
            } finally {
                if (conn!=null)
                    conn.disconnect();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void nothing){
            if (e instanceof UnknownHostException)
                Toast.makeText(getApplicationContext(), "Please check your network",Toast.LENGTH_SHORT).show();
            else if (e != null)
                Toast.makeText(getApplicationContext(), "Update failed",Toast.LENGTH_SHORT).show();
        }
    }
}
