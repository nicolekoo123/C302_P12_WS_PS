package sg.edu.rp.c346.id19047433.c302_p12_ws;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.loopj.android.http.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Pattern;

import cz.msebera.android.httpclient.Header;


public class MainActivity extends AppCompatActivity {

    ListView lv;
    ArrayAdapter<Incident> aa;
    ArrayList<Incident> al;

    // TODO: Task 1 - Declare Firebase variables
    private FirebaseFirestore db;
    private CollectionReference colRef;
    private DocumentReference docRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lv = findViewById(R.id.lv);
        al = new ArrayList<>();
        aa = new IncidentAdapter(MainActivity.this, R.layout.row, al);
        lv.setAdapter(aa);
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
                    }
                    aa.notifyDataSetChanged();
                }catch(JSONException e) {
                    Log.w("tag", "JSONException: ", e);
                }
            }
        });


        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Double lat = al.get(position).getLatitude();
                Double lng = al.get(position).getLongitude();
                String type = al.get(position).getType();
                String msg = al.get(position).getMessage();
                Log.d("LAT/LNG/TYPE", "onItemSelected: " + lat + ", " + lng + "/" + type);
                Intent intent = new Intent(MainActivity.this, MapsActivity.class);
                intent.putExtra("lat" , lat);
                intent.putExtra("lng", lng);
                intent.putExtra("type", type);
                startActivity(intent);
            }
        });


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        // Handle action bar item clicks here.
        // TODO: Task 2: Get FirebaseFirestore instance and collection reference to "students"
        db = FirebaseFirestore.getInstance();
        colRef = db.collection("incidents");

        int id = item.getItemId();

        if (id == R.id.viewMap) {

            Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
            startActivity(intent);

            return true;
        }
        if (id == R.id.addIncident) {
            AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity.this);
            builder1.setTitle("Upload to Firestore");
            builder1.setMessage("proceed to upload to firestore? ");
            builder1.setCancelable(true);

            builder1.setPositiveButton(
                    "Yes",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            //TODO: Task 3: Retrieve name and age from EditText and instantiate a new Student object
                            for (int i = 0; i < al.size(); i++) {
                                Date date = al.get(i).getDate();
                                Double lat = al.get(i).getLatitude();
                                Double lng = al.get(i).getLongitude();
                                String type = al.get(i).getType();
                                String msg = al.get(i).getMessage();
                                Incident inc2 = new Incident(type, lat, lng, msg, date);
                                colRef.add(inc2);
                            }
                            dialog.cancel();
                        }
                    });

            builder1.setNegativeButton(
                    "Cancel",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });

            AlertDialog alert11 = builder1.create();
            alert11.show();
        }
        if (id == R.id.viewAllIncident) {

            Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
            startActivity(intent);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}