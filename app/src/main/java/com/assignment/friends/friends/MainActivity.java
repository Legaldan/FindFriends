package com.assignment.friends.friends;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import java.text.DateFormat;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.assignment.friends.friends.service.WeatherService;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(!isLogin()){
            Intent intent = new Intent(this,LoginActivity.class);
            startActivity(intent);
            this.finish();
        }else {
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.setDrawerListener(toggle);
            toggle.syncState();

            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(this);

            initTime();
            initWeather("37.5", "122.1");

            TextView userNameView = (TextView)findViewById(R.id.username);
            userNameView.setText(getString(R.string.say_hi) + username);
            /*
            TextView barUsername = (TextView)findViewById(R.id.bar_username);
            barUsername.setText(this.username);
            */

        }
    }

    private void initTime(){
        TextView timeView = (TextView)findViewById(R.id.curr_time);
        TextView dateView = (TextView)findViewById(R.id.date);

        Date date = new Date();
        Locale locale = new Locale("en");
        DateFormat format = DateFormat.getDateInstance(DateFormat.LONG, locale);
        dateView.setText(format.format(date));

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        timeView.setText(sdf.format(date));
    }

    private void initWeather(String lat,String lon){
        //view references
        final TextView tv_temp = (TextView) findViewById(R.id.temp_cur);
        final TextView tv_range = (TextView) findViewById(R.id.temp_range);
        final TextView tv_location = (TextView) findViewById(R.id.location);
        final ImageView weather_icon = (ImageView) findViewById(R.id.weather_image);

        WeatherService weatherTask = new WeatherService(this, getString(R.string.error_weather_init), lat, lon,tv_temp, tv_range, tv_location, weather_icon);
        weatherTask.execute();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private boolean isLogin(){
        SharedPreferences preferences = this.getSharedPreferences("user", Context.MODE_PRIVATE);
        String email = preferences.getString("email","");
        String surName = preferences.getString("surName","");
        String firstName = preferences.getString("firstName","");
        if (email.isEmpty()){
            return false;
        }else{
            username = firstName + " " + surName;
            return true;
        }
    }
}