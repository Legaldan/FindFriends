package com.assignment.friends.friends;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.assignment.friends.friends.model.Profile;
import com.assignment.friends.friends.util.HttpGetAsyncTask;

import java.io.InputStream;
import java.util.List;
import java.util.Scanner;

public class ProfileActivity extends AppCompatActivity {

    private TextView mFirstName;
    private TextView mSurname;
    private TextView mEmail;
    private TextView mBirth;
    private TextView mGender;
    private TextView mCourse;
    private TextView mStudyMode;
    private TextView mNationality;
    private TextView mNativeLanguage;
    private TextView mAddress;
    private TextView mFavoriteMovie;
    private TextView mFavoriteUnit;
    private TextView mFavoriteSport;
    private TextView mCurrentJob;
    private Profile mProfile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        initView();
    }

    private void initView(){
        //view reference
        mFirstName = (TextView)findViewById(R.id.first_name);
        mSurname = (TextView)findViewById(R.id.surname);
        mEmail = (TextView)findViewById(R.id.email);
        mBirth = (TextView)findViewById(R.id.birth);
        mGender = (TextView)findViewById(R.id.gender);
        mCourse = (TextView)findViewById(R.id.course);
        mStudyMode = (TextView)findViewById(R.id.study_mode);
        mNationality = (TextView)findViewById(R.id.nationality);
        mNativeLanguage = (TextView)findViewById(R.id.native_language);
        mAddress = (TextView)findViewById(R.id.address);
        mFavoriteMovie = (TextView)findViewById(R.id.favorite_movie);
        mFavoriteUnit = (TextView)findViewById(R.id.favorite_unit);
        mFavoriteSport = (TextView)findViewById(R.id.favorite_sport);
        mCurrentJob = (TextView)findViewById(R.id.current_job);

        Intent intent = getIntent();
        mProfile = (Profile)intent.getSerializableExtra("profile");
        if (mProfile != null) {
            this.setTitle("Information Details");
            setView();
        }
        else {
            String email = intent.getStringExtra("email");
            if (!email.equals("")) {
                String path = "http://192.168.191.1:8080/FindFriends/webresources/profile/findByEmail/" + email;
                GetProfileTask task = new GetProfileTask(this, "Loading Profile failed", path);
                task.execute();
            }
        }
    }

    protected void setView(){
        mFirstName.append(mProfile.getFirstName());
        mSurname.append(mProfile.getSurname());
        mEmail.append(mProfile.getEmail());

        int gender = mProfile.getGender();
        mGender.append(gender==0?"Male":"Female");

        String birth = mProfile.getBirth();
        mBirth.append(birth==null?getString(R.string.null_profile_details):birth.substring(0,10));

        int studyMode = mProfile.getStudyMode();
        mStudyMode.append(studyMode==0?"Part Time":"Full Time");

        String nationality = mProfile.getNationality();
        mNationality.append(nationality==null?getString(R.string.null_profile_details):nationality);

        String address = mProfile.getAddress();
        mAddress.append(address==null?getString(R.string.null_profile_details):address);

        String course = mProfile.getCourse();
        mCourse.append(course==null?getString(R.string.null_profile_details):course);

        String nativeLanguage = mProfile.getNativeLanguage();
        mNativeLanguage.append(nativeLanguage==null?getString(R.string.null_profile_details):nativeLanguage);

        String currentJob = mProfile.getCurrentJob();
        mCurrentJob.append(currentJob==null?getString(R.string.null_profile_details):currentJob);

        String favoriteMovie = mProfile.getFavMovie();
        mFavoriteMovie.append(favoriteMovie==null?getString(R.string.null_profile_details):favoriteMovie);

        String favoriteUnit = mProfile.getFavUnit();
        mFavoriteUnit.append(favoriteUnit==null?getString(R.string.null_profile_details):favoriteUnit);

        String favoriteSport = mProfile.getFavSport();
        mFavoriteSport.append(favoriteSport==null?getString(R.string.null_profile_details):favoriteSport);
    }

    class GetProfileTask extends HttpGetAsyncTask<Void,Profile>{

        public GetProfileTask(Context c, String msg, String path) {
            super(c, msg, path);
        }

        @Override
        protected void httpPostExecute(Profile profile) throws Exception {
            mProfile = profile;
            setView();
        }

        @Override
        protected Profile ownTask(InputStream result) throws Exception {
            String json = "";
            Scanner inStream = new Scanner(result);
            while(inStream.hasNextLine()){
                json += inStream.nextLine();
            }
            List<Profile> profileList = JSON.parseArray(json,Profile.class);
            Profile profile = profileList.get(0);
            return profile;
        }
    }

}
