package com.example.besafe;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MapsActivity extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    View v;
    Dialog dialog;
    TextView name,note,type,phone;
    ImageView img;
    RatingBar ratingBar;
    public static int serid;
    public String sname;
    public static String pho;
    TextView numofrate;
    PlaceAutocompleteFragment placeAutoComplete;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.activity_maps, container, false);

//        placeAutoComplete = (PlaceAutocompleteFragment) getActivity().getFragmentManager().findFragmentById(R.id.place_autocomplete);
//        placeAutoComplete.setOnPlaceSelectedListener(new PlaceSelectionListener() {
//            @Override
//            public void onPlaceSelected(Place place) {
//
//                Log.d("Maps", "Place selected: " + place.getName());
//            }
//
//            @Override
//            public void onError(Status status) {
//                Log.d("Maps", "An error occurred: " + status);
//            }
//        });

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById (R.id.map);
        mapFragment.getMapAsync (this);


        return v;
    }
    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        GPSTracker gpsTracker = new GPSTracker(getContext());


        LatLng h = new LatLng(gpsTracker.getLatitude(), gpsTracker.getLongitude());
        mMap.addMarker(new MarkerOptions().position(h).title("Marker in Your Location").icon(BitmapDescriptorFactory.fromResource(R.drawable.cr)));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(h, 14));


        Database db = new Database();
        Connection conn = db.ConnectDB();
        if (conn == null) {
            Toast.makeText(getContext(), "تحقق من الاتصال بالانترنت ", Toast.LENGTH_SHORT).show();
        } else {
            ResultSet rs = db.RunSearch("select * from services");
            try {
                while (rs.next()) {


                    LatLng s = new LatLng(Double.valueOf(Double.valueOf(rs.getString(6))), Double.valueOf(rs.getString(7)));
                    mMap.addMarker(new MarkerOptions().position(s).title(rs.getString(3)).icon(BitmapDescriptorFactory.fromResource(R.drawable.maintenan)));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(h, 12));
                    serid=rs.getInt(1);


                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
            }
        });

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {

                String lat = String.valueOf(marker.getPosition().latitude);
                String lang = String.valueOf(marker.getPosition().longitude);

                dialog = new Dialog(getActivity());
                dialog.setContentView(R.layout.dialog_map);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                numofrate=(TextView)dialog.findViewById(R.id.txtcount);
                ratingBar=(RatingBar)dialog.findViewById(R.id.rateService);


                Database D = new Database();
                Connection con = D.ConnectDB();
                ResultSet res = D.RunSearch("select avg(value) as[avg],count(*) as[count] from rating");
                try {

                   while (res.next()) {
                        Float avgrate = res.getFloat(1);
                        Integer coun = res.getInt(2);
                        numofrate.setText("(" + String.valueOf(coun) + ")");
                        ratingBar.setRating(avgrate);
                    }

                } catch (SQLException e) {
                    e.printStackTrace();
                }

                name = (TextView) dialog.findViewById(R.id.txtSname);
                note = (TextView) dialog.findViewById(R.id.txtSnote);
                type = (TextView) dialog.findViewById(R.id.txtStype);
                phone = (TextView) dialog.findViewById(R.id.txtSpohone);
                img = (ImageView) dialog.findViewById(R.id.imgServ);

                ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                    @Override
                    public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {

                        SharedPreferences sharedPreferences=getActivity().getSharedPreferences("User", Context.MODE_PRIVATE);
                        double value=rating;
                        Database dab = new Database();
                        Connection conn = dab.ConnectDB();
                        if (conn == null) {
                            Toast.makeText(getContext(), "تحقق من الاتصال بالانترنت", Toast.LENGTH_SHORT).show();
                        } else {
                            String msg = dab.RUNDML("insert into rating values('" + value + "',N'"+sname+"','" + sharedPreferences.getString("Phone","")+"')");
                            if (msg.equals("ok")) {
                                Toast.makeText(getContext(), "تم التقيم بنجاح", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();

                            }
                        }
                    }
                });


                Button cancel = (Button) dialog.findViewById(R.id.btnScancel);
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                    }
                });
                if (marker.getTitle().equals("Marker in Your Location")) {
                    ;
                } else {

                    Database db = new Database();
                    Connection conn = db.ConnectDB();
                    ResultSet rs = db.RunSearch("select * from services where latitude='" + String.valueOf(marker.getPosition().latitude) + "'");
                    try {

                        while (rs.next()) {
                            type.setText(rs.getString(3));
                            note.setText(rs.getString(8));
                            sname=rs.getString(2);
                            name.setText(rs.getString(2));
                            PicassoClinte.downloadImage(getContext(), rs.getString(5), img);
                            pho = rs.getString(4);
                            phone.setText(pho);
                        }

                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                    dialog.show();

                    Button call = (Button) dialog.findViewById(R.id.btnScall);
                    call.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(Intent.ACTION_DIAL);
                            intent.setData(Uri.parse("tel:" + pho));
                            if (intent.resolveActivity(getContext().getPackageManager()) != null) {
                                getContext().startActivity(intent);
                            }
                        }
                    });
                }
                return false;
            }
        });
     }

    }

