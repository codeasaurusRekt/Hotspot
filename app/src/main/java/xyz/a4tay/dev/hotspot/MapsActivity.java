package xyz.a4tay.dev.hotspot;

import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.*;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    private TextView mTextMessage;
    DotProtocols dotProtocols = new DotProtocols();
    EmojiHandler emojiHandler = new EmojiHandler();
    FloatingActionButton ratingWheelButton, rateButton1, rateButton2,
            rateButton3, rateButton4, rateButton5, hashButton;
    TextView hashText;
    Animation fabOpen, fabClose, rotateForward, rotateBackward;
    boolean isOpen = false;
    Dialog popup;
    private ClusterManager<ClusterHandler> mClusterManager;
    private int hashRating = 1;

    interface runLambda {
        void run();
    }

    private class Runner {
        private void run(runLambda g) {
            g.run();
        }
    }

    private GoogleMap mMap;
//    LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.button_activity);
        // Obtain the SupportMapFragment and run notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Runner runner = new Runner();

        runner.run(() -> {
            try {
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        ratingWheelButton = findViewById(R.id.btnOpenRatingWheel);
        rateButton1 = findViewById(R.id.btnRate1);
        rateButton2 = findViewById(R.id.btnRate2);
        rateButton3 = findViewById(R.id.btnRate3);
        rateButton4 = findViewById(R.id.btnRate4);
        rateButton5 = findViewById(R.id.btnRate5);
        hashButton = findViewById(R.id.btnHash);

        fabOpen = AnimationUtils.loadAnimation(this, R.anim.fab_open);
        fabClose = AnimationUtils.loadAnimation(this, R.anim.fab_close);

        rotateForward = AnimationUtils.loadAnimation(this, R.anim.rotate_forward);
        rotateBackward = AnimationUtils.loadAnimation(this, R.anim.rotate_backward);

        ratingWheelButton.setOnClickListener(v -> {
            animateFab();
        });

        popup = new Dialog(this);

        mTextMessage = findViewById(R.id.message);
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
            LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
            final String locationProvider = LocationManager.NETWORK_PROVIDER;
            Location lastKnownLocation = locationManager.getLastKnownLocation(locationProvider);
            final LatLng userLocation = new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 17.0f));
            mClusterManager = new ClusterManager<>(this, mMap);
            mMap.setOnCameraIdleListener(mClusterManager);
            List<ClusterHandler> items = new ArrayList<ClusterHandler>();


            rateButton1.setOnClickListener(v -> {
                if (isOpen) {
                    dropDot(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude(), 1, (double) 334);
                }
            });

            rateButton2.setOnClickListener(v -> {
                if (isOpen) {
                    dropDot(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude(), 2, (double) 334);
                }
            });

            rateButton3.setOnClickListener(v -> {
                if (isOpen) {
                    dropDot(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude(), 3, (double) 334);
                }
            });

            rateButton4.setOnClickListener(v -> {
                if (isOpen) {
                    dropDot(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude(), 4, (double) 334);
                }
            });

            rateButton5.setOnClickListener(v -> {
                if (isOpen) {
                    dropDot(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude(), 5, (double) 334);
                }
            });

            try {
                JSONArray dotArray = dotProtocols.getDots(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude()).getJSONArray("locations");

                Double dotColorTotal = 0.0;
                Double dotCount = 0.0;
                Double dotAverage;
                for (int i = 0; i < dotArray.length(); i++) {
                    JSONObject eachDot = null;
                    try {
                        eachDot = dotArray.getJSONObject(i);
                        Double dotLat = eachDot.getDouble("lat");
                        Double dotLng = eachDot.getDouble("lng");
                        Integer dotColor = eachDot.getInt("colorCode");
                        String dotHash = eachDot.getString("hash");
                        dotColorTotal = dotColorTotal + dotColor;
                        dotCount++;
                        dotAverage = Math.ceil(dotColorTotal / dotCount);

                        final CustomClusterRenderer renderer = new CustomClusterRenderer(this, mMap, mClusterManager, dotAverage, dotColor);
                        mClusterManager.setRenderer(renderer);

                        items.add(new ClusterHandler(dotLat, dotLng, dotHash, "snippet"));
                        mClusterManager.addItems(items);
                    } catch (JSONException e) {
                        Toast.makeText(MapsActivity.this,
                                e.toString()
                                , Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Toast.makeText(MapsActivity.this,
                                e.toString()
                                , Toast.LENGTH_LONG).show();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            mClusterManager
                    .setOnClusterClickListener(new ClusterManager.OnClusterClickListener<ClusterHandler>() {
                        @Override
                        public boolean onClusterClick(final Cluster<ClusterHandler> cluster) {
                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                                    cluster.getPosition(), (float) Math.floor(mMap
                                            .getCameraPosition().zoom + 1)), 300,
                                    null);
                            return true;
                        }
                    });
            hashButton.setOnClickListener(v -> {
                popup.setContentView(R.layout.rating_popup);
                popup.show();
                Button hashRate1 = popup.findViewById(R.id.hashBtnRate1);
                hashRate1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        hashRating = 1;
                    }
                });
                Button hashRate2 = popup.findViewById(R.id.hashBtnRate2);
                hashRate2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        hashRating = 2;
                    }
                });
                Button hashRate3 = popup.findViewById(R.id.hashBtnRate3);
                hashRate3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        hashRating = 3;
                    }
                });
                Button hashRate4 = popup.findViewById(R.id.hashBtnRate4);
                hashRate4.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        hashRating = 4;
                    }
                });
                Button hashRate5 = popup.findViewById(R.id.hashBtnRate5);
                hashRate5.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        hashRating = 5;
                    }
                });
                EditText hashEntry = popup.findViewById(R.id.hashText);
                Button submitHash = popup.findViewById(R.id.btnCommitRating);
                submitHash.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (hashEntry.getText().toString() != null && !hashEntry.getText().toString().equals("")) {
                            dropDot(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude(), hashRating, (double) 334,
                                    hashEntry.getText().toString());
                        }
                        else {
                            Toast.makeText(MapsActivity.this,
                                    "Text is required when rating with a hash..."
                                    , Toast.LENGTH_LONG).show();
                        }
                        popup.dismiss();
                    }
                });
            });

        } else {
            ActivityCompat.requestPermissions(MapsActivity.this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 200);

            mMap.setMyLocationEnabled(true);
            LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
            final String locationProvider = LocationManager.NETWORK_PROVIDER;
            Location lastKnownLocation = locationManager.getLastKnownLocation(locationProvider);
            final LatLng userLocation = new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 17.0f));
        }
    }

    private void animateFab() {
        if (isOpen) {
            ratingWheelButton.startAnimation(rotateBackward);
            rateButton1.startAnimation(fabClose);
            rateButton2.startAnimation(fabClose);
            rateButton3.startAnimation(fabClose);
            rateButton4.startAnimation(fabClose);
            rateButton5.startAnimation(fabClose);
            hashButton.startAnimation(fabOpen);
            rateButton1.setEnabled(false);
            rateButton2.setEnabled(false);
            rateButton3.setEnabled(false);
            rateButton4.setEnabled(false);
            rateButton5.setEnabled(false);
            hashButton.setEnabled(true);
            isOpen = false;
        } else {
            ratingWheelButton.startAnimation(rotateForward);
            rateButton1.startAnimation(fabOpen);
            rateButton2.startAnimation(fabOpen);
            rateButton3.startAnimation(fabOpen);
            rateButton4.startAnimation(fabOpen);
            rateButton5.startAnimation(fabOpen);
            hashButton.startAnimation(fabClose);
            rateButton1.setEnabled(true);
            rateButton2.setEnabled(true);
            rateButton3.setEnabled(true);
            rateButton4.setEnabled(true);
            rateButton5.setEnabled(true);
            hashButton.setEnabled(false);
            isOpen = true;
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_nearby:
                    mTextMessage.setText(getString(R.string.nearby));
                    return true;
                case R.id.navigation_trending:
                    mTextMessage.setText(getString(R.string.trending));
                    return true;
            }
            return false;
        }
    };

    private void dropDot(Double lat, Double lng, Integer colorCode, Double dotID) {
        try {
            switch (colorCode) {
                case 1:
                    Toast.makeText(MapsActivity.this,
                            emojiHandler.getEmojiByUnicode(0x1F525)
                            , Toast.LENGTH_LONG).show();
                    break;
                case 2:
                    Toast.makeText(MapsActivity.this,
                            emojiHandler.getEmojiByUnicode(0x1F525)
                                    + emojiHandler.getEmojiByUnicode(0x1F525)
                            , Toast.LENGTH_LONG).show();
                    break;
                case 3:
                    Toast.makeText(MapsActivity.this,
                            emojiHandler.getEmojiByUnicode(0x1F525)
                                    + emojiHandler.getEmojiByUnicode(0x1F525)
                                    + emojiHandler.getEmojiByUnicode(0x1F525)
                            , Toast.LENGTH_LONG).show();
                    break;
                case 4:
                    Toast.makeText(MapsActivity.this,
                            emojiHandler.getEmojiByUnicode(0x1F525)
                                    + emojiHandler.getEmojiByUnicode(0x1F525)
                                    + emojiHandler.getEmojiByUnicode(0x1F525)
                                    + emojiHandler.getEmojiByUnicode(0x1F525)
                            , Toast.LENGTH_LONG).show();
                    break;
                case 5:
                    Toast.makeText(MapsActivity.this,
                            emojiHandler.getEmojiByUnicode(0x1F525)
                                    + emojiHandler.getEmojiByUnicode(0x1F525)
                                    + emojiHandler.getEmojiByUnicode(0x1F525)
                                    + emojiHandler.getEmojiByUnicode(0x1F525)
                                    + emojiHandler.getEmojiByUnicode(0x1F525)
                            , Toast.LENGTH_LONG).show();
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        dotProtocols.putDot(lat, lng, colorCode, dotID);
        mClusterManager.addItem(new ClusterHandler(lat, lng, "From button", "Button time"));
        mClusterManager.cluster();
        animateFab();
    }

    private void dropDot(Double lat, Double lng, Integer colorCode, Double dotID, String hash) {
        try {
            switch (colorCode) {
                case 1:
                    Toast.makeText(MapsActivity.this,
                            emojiHandler.getEmojiByUnicode(0x1F525)
                            , Toast.LENGTH_LONG).show();
                    break;
                case 2:
                    Toast.makeText(MapsActivity.this,
                            emojiHandler.getEmojiByUnicode(0x1F525)
                                    + emojiHandler.getEmojiByUnicode(0x1F525)
                            , Toast.LENGTH_LONG).show();
                    break;
                case 3:
                    Toast.makeText(MapsActivity.this,
                            emojiHandler.getEmojiByUnicode(0x1F525)
                                    + emojiHandler.getEmojiByUnicode(0x1F525)
                                    + emojiHandler.getEmojiByUnicode(0x1F525)
                            , Toast.LENGTH_LONG).show();
                    break;
                case 4:
                    Toast.makeText(MapsActivity.this,
                            emojiHandler.getEmojiByUnicode(0x1F525)
                                    + emojiHandler.getEmojiByUnicode(0x1F525)
                                    + emojiHandler.getEmojiByUnicode(0x1F525)
                                    + emojiHandler.getEmojiByUnicode(0x1F525)
                            , Toast.LENGTH_LONG).show();
                    break;
                case 5:
                    Toast.makeText(MapsActivity.this,
                            emojiHandler.getEmojiByUnicode(0x1F525)
                                    + emojiHandler.getEmojiByUnicode(0x1F525)
                                    + emojiHandler.getEmojiByUnicode(0x1F525)
                                    + emojiHandler.getEmojiByUnicode(0x1F525)
                                    + emojiHandler.getEmojiByUnicode(0x1F525)
                            , Toast.LENGTH_LONG).show();
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        dotProtocols.putDot(lat, lng, colorCode, dotID, hash);
        mClusterManager.addItem(new ClusterHandler(lat, lng, "From button", "Button time"));
        mClusterManager.cluster();
    }
}