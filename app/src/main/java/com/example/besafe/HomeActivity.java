package com.example.besafe;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.besafe.slideview.MainActivity;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Toolbar toolbar;
    SharedPreferences prefs;
    private static long back_pressed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ActivityCompat.requestPermissions(HomeActivity.this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                1);

        ActivityCompat.requestPermissions(HomeActivity.this,
                new String[]{Manifest.permission.CALL_PHONE},
                3);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        //toolbar.setLogo(R.drawable.logonew);

        Fragment fragment = null;
        fragment = new MapsActivity();
        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.homeContiner, fragment);
            ft.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerview = navigationView.getHeaderView(0);
        TextView name = (TextView) headerview.findViewById(R.id.txtnavname);
        prefs = getSharedPreferences("name", Context.MODE_PRIVATE);
        name.setText(prefs.getString("name", ""));

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
        getMenuInflater().inflate(R.menu.home, menu);
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
        Fragment fragment = null;
        if (id == R.id.profile) {
            toolbar.setTitle("الصفحة الشخصية");
            fragment = new ProfileFragment();
        } else if (id == R.id.emer_contacts) {
            toolbar.setTitle("ارقام الطوارئ");
            fragment = new EmergencyFragment();

        } else if (id == R.id.acc_accelator) {
            toolbar.setTitle("مستشعر التصادم");
            fragment = new AccidentFragment();
        } else if (id == R.id.maps) {
            toolbar.setTitle("الخدمات");
            fragment = new MapsActivity();
        } else if (id == R.id.nav_share) {
            Intent sheraintent = new Intent();
            sheraintent.setAction(Intent.ACTION_SEND);
            sheraintent.putExtra(Intent.EXTRA_TEXT, "https:/play.google/story/app/details?id=hi");
            sheraintent.setType("text/plain");
            startActivity(Intent.createChooser(sheraintent, "Share App BeSafe"));
        }

        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.homeContiner, fragment);
            ft.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(HomeActivity.this, "Permission denied to read your Location", Toast.LENGTH_SHORT).show();
                }


                return;

                // other 'case' lines to check for other
                // permissions this app might request
            }
        }
    }

}