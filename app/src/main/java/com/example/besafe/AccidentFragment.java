package com.example.besafe;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class AccidentFragment extends Fragment implements SensorEventListener {

    LocationManager locationManager;
    private SensorManager mSensorManager;
    private Sensor mSensor;
    SmsManager mSmsManager;
    float laset_x;
    float laset_y;
    public static SharedPreferences prefs;
    float laset_z;
    long lastUpdate;
    private CountDownTimer timer;
    public static String name;
    Animation animShake;
    int SHAKE_THRESHOLD = 3000;
    MediaPlayer mediaPlayeralarm;
    List<Emergency> Data;
    GetEmergencyData getEmergencyData=new GetEmergencyData();
    Dialog dialog;
    ImageView alarm;
    String address;
    View v;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_accident, container, false);
        ActivityCompat.requestPermissions(getActivity(),
                new String[]{Manifest.permission.SEND_SMS},
                1);
        dialog = new Dialog(getActivity());
        mSmsManager = SmsManager.getDefault();
        dialog.setContentView(R.layout.dialog_accident);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        prefs = getContext().getSharedPreferences("name", Context.MODE_PRIVATE);
        name=prefs.getString("name","");

        Button cancel=(Button)dialog.findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mediaPlayeralarm.pause();
                dialog.cancel();
                timer.cancel();

            }
        });

        Geocoder geocoder;
        List<Address> addresses = null;
        geocoder = new Geocoder(getContext(), Locale.getDefault());

        GPSTracker gpsTracker=new GPSTracker(getContext());
        try {
            addresses = geocoder.getFromLocation(gpsTracker.latitude, gpsTracker.longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
        } catch (IOException e) {
            e.printStackTrace();
        }

        address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
        //String city = addresses.get(0).getLocality();
        //String state = addresses.get(0).getAdminArea();
        // String country = addresses.get(0).getCountryName();
        // String postalCode = addresses.get(0).getPostalCode();
        //String knownName = addresses.get(0).getFeatureName();


        //hospitals = mGPSHandler.getHospitalAddress();
        mSensorManager = (SensorManager) getContext().getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        return v;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];

        long curTime = System.currentTimeMillis();
        if ((curTime - lastUpdate) > 100) {

            long diffTime = (curTime - lastUpdate);
            lastUpdate = curTime;


            float speed = Math.abs(x + y + z - laset_x - laset_y - laset_z) / diffTime * 10000;

            if (speed > SHAKE_THRESHOLD) {

                Vibrator v = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);
                v.vibrate(500);
                animShake = AnimationUtils.loadAnimation(getContext(), R.anim.shake);

                alarm = (ImageView) dialog.findViewById(R.id.imageView2);
                alarm.startAnimation(animShake);

                mediaPlayeralarm = MediaPlayer.create(getContext(), R.raw.rising_swoops);
                mediaPlayeralarm.start();
                dialog.show();

                timer=new CountDownTimer(10000,1000){


                    @Override
                    public void onTick(long millisUntilFinished) {
                        TextView seconds=(TextView)dialog.findViewById(R.id.timer);
                        seconds.setText("ارسال تلقائي بعد : "+String.valueOf(millisUntilFinished/1000) );
                        mediaPlayeralarm.start();
                        alarm.startAnimation(animShake);


                    }

                    @Override
                    public void onFinish() {
                        sendSMS();
                        mediaPlayeralarm.stop();
                        dialog.cancel();
                        timer.cancel();

                    }
                }.start();
                dialog.show();
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public void onPause() {
        super.onPause();
        mSensorManager.unregisterListener((SensorEventListener) this);
    }

    @Override
    public void onResume() {
        super.onResume();
        mSensorManager.registerListener((SensorEventListener) this, mSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }
    public void sendSMS() {
        try {
            String message = constructMessage();
            ArrayList<String> dividedMessage = mSmsManager.divideMessage(message);
            SmsManager smsManager = SmsManager.getDefault();
            Data=new ArrayList<>(getEmergencyData.getdata(getContext()));

            mSmsManager.sendMultipartTextMessage(Data.get(0).getPhone(), null, dividedMessage, null, null);
            mSmsManager.sendMultipartTextMessage(Data.get(1).getPhone(), null, dividedMessage, null, null);
            mSmsManager.sendMultipartTextMessage(Data.get(2).getPhone(), null, dividedMessage, null, null);
            Toast.makeText(getActivity(), "تم الارسال", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getContext(),HomeActivity.class));
            this.onDestroy();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    private String constructMessage() {

        String message = " تحذير  "+name
                + " ربما يكون في حادث تصادم . "+name
                + " قد اختارك لتكون رقم يتصل به في حالة الطوارئ. "
                + " موقعه الحالي هوا "+address;
        return message;
    }


}

