package org.openspeed;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private LocationManager locManager;
    private LocationListener locListener;

    private float currentDistanceInM = 0.0f;

    private float meterPerSecondInKilometersPerHour(float m){
        return 3.6f*m;
    }

    private String toStringKM(float km){
        return String.format("%.1f", km);
    }

    @Override
    protected void onPause() {
        try{
            locManager.removeUpdates(locListener);
        }
        catch(SecurityException ex){
            //ignore
        }
        catch(Exception ex){
            //ignore
        }
        super.onPause();
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {

        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            // permission was granted, yay! Do the
            // contacts-related task you need to do.
            initializeGPSSpeed();

        } else {
            // permission denied, boo! Disable the
            // functionality that depends on this permission.
            requestPermission();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        requestPermission();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*
        fragmentManager = getSupportFragmentManager();
        SupportMapFragment sMapFrag = (SupportMapFragment)fragmentManager.findFragmentById(R.id.maps_frag);
        sMapFrag.getMapAsync(this);
    */

        this.locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        myToolbar.setTitle("OpenSpeed");
        myToolbar.inflateMenu(R.menu.menu);

        // Set an OnMenuItemClickListener to handle menu item clicks
        myToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                // Handle the menu item
                if(item.getItemId() == R.id.action_gps){

                }
                if(item.getItemId() == R.id.action_reset){
                    currentDistanceInM = 0.0f;
                    setPerformedDistance(currentDistanceInM);
                }
                return true;
            }
        });



        //setSupportActionBar(myToolbar);
    }


    private void requestPermission(){
        boolean finePermissionGranted = PermissionUtil.isPermissionGranted(this, PermissionUtil.ACCESS_FINE_LOCATION);
        if(!finePermissionGranted){
            PermissionUtil.requestPermission(this, this, PermissionUtil.ACCESS_FINE_LOCATION);
        } else {
            initializeGPSSpeed();
        }
    }


    private Location lastLocation;
    
    private void initializeGPSSpeed(){


        LocationListener locListener = new LocationListener() {

            public void onLocationChanged(Location location) {

                float speedInKmh = meterPerSecondInKilometersPerHour(location.getSpeed());
                TextView tv = (TextView) findViewById(R.id.main_speed_view);
                tv.setText(toStringKM(speedInKmh));

                if(lastLocation != null) {
                    float distance = location.distanceTo(lastLocation);


                    currentDistanceInM = currentDistanceInM + distance;

                    setPerformedDistance(currentDistanceInM);
                }

                lastLocation = location;
            }

            public void onProviderDisabled(String provider){

                //updateWithNewLocation(null);
            }

            public void onProviderEnabled(String provider) {}
            public void onStatusChanged(String provider, int status, Bundle extras) {}
        };

        this.locListener = locListener;

        try{
            locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 750, 0, locListener);
        } catch(SecurityException e){
            e.printStackTrace();
        }

    }


    private void setPerformedDistance(float distanceInM){
        TextView dv = (TextView) findViewById(R.id.main_distance_view);
        dv.setText(toStringKM(distanceInM/1000));
    }


}
