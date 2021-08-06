package sg.edu.rp.c346.id19047433.c302_p12_ws;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Pattern;

import cz.msebera.android.httpclient.Header;
import sg.edu.rp.c346.id19047433.c302_p12_ws.databinding.ActivityMapsBinding;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    Double lat;
    Double lng;
    String type;
    ArrayList<Incident> al;

    // TODO: Task 1 - Declare Firebase variables
    private FirebaseFirestore db;
    private CollectionReference colRef;
    private DocumentReference docRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        lat = intent.getDoubleExtra("lat", 0.0);
        lng = intent.getDoubleExtra("lng", 0.0);
        type = intent.getStringExtra("type");

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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


        // Add a marker in Singapore and move the camera
        LatLng Singapore = new LatLng(1.3521,103.8198);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Singapore, 12));
        LatLng location = new LatLng(lat,lng);
        mMap.addMarker(new MarkerOptions().position(location).title(type));
        al = new ArrayList<>();
        AsyncHttpClient client = new AsyncHttpClient();
        client = new AsyncHttpClient();
        client.addHeader("AccountKey", "5PVVM3gaQZGOLBx1/FgUXg==");
        client.get("http://datamall2.mytransport.sg/ltaodataservice/TrafficIncidents", new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // called when response HTTP status is "200 OK"
                try {
                    JSONArray jsonArray = response.getJSONArray("value");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObj = jsonArray.getJSONObject(i);
                        String type = jsonObj.getString("Type");
                        double latitude = jsonObj.getDouble("Latitude");
                        double longitude = jsonObj.getDouble("Longitude");
                        String message = jsonObj.getString("Message");

                        Date date = null;
                        String dateFromMsg = message.substring(1);
                        String[] realDate = dateFromMsg.split(Pattern.quote(")"));
                        try {
                            SimpleDateFormat format = new SimpleDateFormat("dd/MM");
                            date = (format.parse(realDate[0]));
                        }catch (ParseException e){
                            e.printStackTrace();
                        }
                        Incident inc = new Incident(type, latitude, longitude, message, date);
                        al.add(inc);
                        for (int a = 0; a < al.size(); a++){
                            mMap.addMarker(new MarkerOptions()
                                    .position(new LatLng(al.get(a).getLatitude(), al.get(a).getLongitude()))
                                    .title(al.get(a).getType()));
                        }
                    }
                }catch(JSONException e) {
                    Log.w("tag", "JSONException: ", e);
                }
            }
        });
    }
}