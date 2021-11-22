package com.example.taskmaster;

import static android.content.Intent.ACTION_PICK;
import static android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
import static android.provider.MediaStore.Video.Media.INTERNAL_CONTENT_URI;
import br.com.onimur.handlepathoz.HandlePathOzListener;
import android.provider.Settings;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Team;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;

import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import br.com.onimur.handlepathoz.HandlePathOz;
import br.com.onimur.handlepathoz.model.PathOz;

public class Add_Task extends AppCompatActivity implements HandlePathOzListener.SingleUri{
    private static final String TAG = "Add_Task";
    public static int c = 0;
    private static final int REQUEST_PERMISSION = 123;
    private static final int REQUEST_OPEN_GALLERY = 1111;
    Handler handler;
    private Spinner teamSpin;
    private Spinner statusSpin;
    private HandlePathOz handlePathOz;
    String fileName;
    Handler fileNameHandler;
    Uri fileUri ;

    private static final int PERMISSION_ID = 44;
    private FusedLocationProviderClient mFusedLocationClient;
    private double latitude;
    private double longitude;

    private final LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location mLastLocation = locationResult.getLastLocation();
            latitude = mLastLocation.getLatitude();
            longitude = mLastLocation.getLongitude();
            Log.i(TAG, "The location is => " + mLastLocation);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        handlePathOz = new HandlePathOz(this, this);

        if (getIntent().getClipData() != null) {
            fileUri = getIntent().getClipData().getItemAt(0).getUri();
            handlePathOz.getRealPath(fileUri);
        }

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        findViewById(R.id.latlonBTN).setOnClickListener(view -> {
            getLastLocation();
        });




        MainActivity.sendAnalyticsInfo(this.toString(),MainActivity.class.toString());

        teamSpin = findViewById(R.id.teamSpin);
        statusSpin = findViewById(R.id.statusSpin);
        ArrayAdapter<CharSequence> teamAdapter = ArrayAdapter.createFromResource(this,
                R.array.teams_Names, android.R.layout.simple_spinner_item);
        teamAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        teamSpin.setAdapter(teamAdapter);


        ArrayAdapter<CharSequence> statusAdapter = ArrayAdapter.createFromResource(this,
                R.array.status, android.R.layout.simple_spinner_item);
        statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        statusSpin.setAdapter(statusAdapter);
        handler = new Handler(Looper.myLooper(), message -> {
            String title = message.getData().getString("title");
            String teamID = message.getData().getString("teamId");
            String body = message.getData().getString("body");
            String status = message.getData().getString("status");
            com.amplifyframework.datastore.generated.model.Task task = com.amplifyframework.datastore.generated.model.Task
                    .builder().title(title).body(body).state(status).teamId(teamID).lat(latitude).lon(longitude).fileKey(fileName).build();
            saveTaskHelper(task);
            Log.i(TAG, "LATLON ==============================>>>>>>" + latitude+ "=====>" + longitude);
            return false;
        });

        fileNameHandler = new Handler(Looper.getMainLooper(),message -> {
            fileName = message.getData().getString("fileName1");
            return false;
        });

//        initHandlePathOz();
        Button uploadBtn = findViewById(R.id.uploadBtn);
        uploadBtn.setOnClickListener(view -> {
            openFile();
        });

        Button button = findViewById(R.id.button3);
//        button.setOnClickListener(view -> {
//
//
//            Spinner teamSpin = findViewById(R.id.teamSpin);
//            Spinner statusSpin = findViewById(R.id.statusSpin);
//
//            ArrayAdapter<CharSequence> teamAdapter = ArrayAdapter.createFromResource(this,
//                    R.array.teams_Names, android.R.layout.simple_spinner_item);
//            teamAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//            teamSpin.setAdapter(teamAdapter);
//
//
//            ArrayAdapter<CharSequence> statusAdapter = ArrayAdapter.createFromResource(this,
//                    R.array.status, android.R.layout.simple_spinner_item);
//            statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//            statusSpin.setAdapter(statusAdapter);
//
//
//            String status = statusSpin.getSelectedItem().toString();
//            String team = teamSpin.getSelectedItem().toString();
//            Log.i(TAG, "Spinner: " + status + "    " + team);
//
//
//            EditText taskTitle = findViewById(R.id.taskTitle);
//            String title = taskTitle.getText().toString();
//
//            EditText textDescription = findViewById(R.id.taskDescription);
//            String description = textDescription.getText().toString();
//
//
//
//            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(Add_Task.this);
//            SharedPreferences.Editor editor = preferences.edit();
//            editor.putString("team", team);
//            editor.apply();
//
//            Task task = new Task(title, description, State.NEW.toString());
//            TaskDataBase.getInstance(getApplicationContext()).taskDAO().insertTask(task);
//
//            TextView textView = findViewById(R.id.textView5);
//            c++;
//
//
//            apiTaskSave(title,description,status);
//
//            textView.setText("Total Tasks :" + c);
//            SharedPreferences preferences1 = PreferenceManager.getDefaultSharedPreferences(Add_Task.this);
//            SharedPreferences.Editor editor1 = preferences1.edit();
//            editor1.putInt("c", c);
//            editor1.apply();
//            Toast toast = Toast.makeText(getApplicationContext(), "Submitted!", Toast.LENGTH_SHORT);
//            toast.show();
//            Intent intent = new Intent(Add_Task.this, MainActivity.class);
//            startActivity(intent);
////            finish();
//        });
    }


    public void addTask(View view) {


        String status = statusSpin.getSelectedItem().toString();
        String team = teamSpin.getSelectedItem().toString();
        Log.i(TAG, "Spinner: " + status + "    " + team);


        EditText taskTitle = findViewById(R.id.taskTitle);
        String title = taskTitle.getText().toString();

        EditText textDescription = findViewById(R.id.taskDescription);
        String description = textDescription.getText().toString();


        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(Add_Task.this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("team", team);
        editor.apply();

        Task task = new Task(title, description, State.NEW.toString());
        TaskDataBase.getInstance(getApplicationContext()).taskDAO().insertTask(task);

        TextView textView = findViewById(R.id.textView5);
        c++;


        apiTaskSave(title, description, status);

        textView.setText("Total Tasks :" + c);
        SharedPreferences preferences1 = PreferenceManager.getDefaultSharedPreferences(Add_Task.this);
        SharedPreferences.Editor editor1 = preferences1.edit();
        editor1.putInt("c", c);
        editor1.apply();
        Toast toast = Toast.makeText(getApplicationContext(), "Submitted!", Toast.LENGTH_SHORT);
        toast.show();
        Intent intent = new Intent(Add_Task.this, MainActivity.class);
        startActivity(intent);
//            finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences Preferences1 = PreferenceManager.getDefaultSharedPreferences(this);
        int c = Preferences1.getInt("c", 0);
        TextView textView = findViewById(R.id.textView5);
        textView.setText("Total Tasks :" + c);
        if (checkPermissions()) {
            getLastLocation();
        }
    }

    public void apiTaskSave(String title, String body, String status) {
        SharedPreferences Preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String teamName = Preferences.getString("team", null);
        List<Team> apiTeams = new ArrayList<>();


        Amplify.API.query(ModelQuery.list(Team.class, Team.NAME.contains(teamName)),
                teamReceived -> {
//                    Log.i(TAG, "Team ID  => " + teamReceived.getData() );
                    for (Team team : teamReceived.getData()) {
                        apiTeams.add(team);
                    }

                    Bundle bundle = new Bundle();
                    bundle.putString("teamId", apiTeams.get(0).getId());
                    bundle.putString("title", title);
                    bundle.putString("body", body);
                    bundle.putString("status", status);

                    Message message = new Message();
                    message.setData(bundle);

                    handler.sendMessage(message);

                },
                error -> {
                    Log.e(TAG, "Error In Retrieving Teams", error);
                });

    }

    private void saveTaskHelper(com.amplifyframework.datastore.generated.model.Task task) {
        Amplify.API.mutate(ModelMutation.create(task),
                taskSaved -> {
                    Log.i(TAG, "Team ID  => " + taskSaved.getData().getTitle());
                    Log.i(TAG, "Task Is Saved => " + taskSaved.getData().getTitle());
                },
                error -> {
                    Log.e(TAG, "Task Is Not Saved => " + error.toString());
                });
    }
    //===================================================================================
    //===================================================================================
    //===================================================================================
//    private void initHandlePathOz() {
//        //initialize library
//        handlePathOz = new HandlePathOz(this, this);
//    }

    private void openFile(){
        if (checkSelfPermission()) {
            Intent intent;
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                intent = new Intent(ACTION_PICK, EXTERNAL_CONTENT_URI);
            } else {
                intent = new Intent(ACTION_PICK, INTERNAL_CONTENT_URI);
            }

            intent.setType("*/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            intent.putExtra("return-data", true);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            startActivityForResult(intent, REQUEST_OPEN_GALLERY);
        }
    }
    private boolean checkSelfPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PERMISSION);
            return false;
        }
        return true;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_OPEN_GALLERY && resultCode == RESULT_OK) {
            Uri uri = data.getData();
            if (uri != null) {
                //set Uri to handle
                Log.i("in onActivityResult",uri.toString());
                handlePathOz.getRealPath(uri);
                //show Progress Loading
            }
        }
    }


    @Override
    public void onRequestHandlePathOz(@NonNull PathOz pathOz, @Nullable Throwable throwable) {
        if (throwable != null) {
            Toast.makeText(this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
        }
        String filename = FilenameUtils.getName(pathOz.getPath());
        File file = new File(pathOz.getPath());
        Amplify.Storage.uploadFile(
                filename,
                file,
                result -> {Log.i("MyAmplifyApp", "Successfully uploaded: " + result.getKey());
                    Amplify.Storage.getUrl(
                            result.getKey(),
                            resultUrl -> {
                                Log.i("MyAmplifyApp", "Successfully generated: " + resultUrl.getUrl());
                                Bundle bundle = new Bundle();
                                bundle.putString("fileName1",resultUrl.getUrl().toString());
                                Message message = new Message();
                                message.setData(bundle);
                                fileNameHandler.sendMessage(message);
                                showToast();
                                fileUri = null;},
                            error -> Log.e("MyAmplifyApp", "URL generation failure", error)
                    );
                },
                storageFailure -> Log.e("MyAmplifyApp", "Upload failed", storageFailure)
        );
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openFile();
            } else {
                Log.i(TAG + " ==> onRequestPermissionsResult", "Error : Permission Field");
            }
        }
        if (requestCode == PERMISSION_ID) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            }
        }
    }

    public void showToast() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast toast = Toast.makeText(getApplicationContext(), "The file has been added to the task", Toast.LENGTH_LONG);
                toast.show();
            }
        });
    }

//===================================================================================
//====================================Location=======================================
//===================================================================================

    @SuppressLint("MissingPermission")
    private void getLastLocation() {
        if (checkPermissions()) {

            if (isLocationEnabled()) {

                mFusedLocationClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull com.google.android.gms.tasks.Task<Location> task) {

                        Location location = task.getResult();

                        if (location == null) {
                            requestNewLocationData();
                        } else {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();

                        }
                    }
                });
            } else {
                Toast.makeText(this, "Please turn on your location...", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        } else {
            requestPermissions();
        }
    }

    private boolean checkPermissions() {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;

        // If we want background location
        // on Android 10.0 and higher,
        // use:
        // ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_ID);
    }

    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }
    @SuppressLint("MissingPermission")
    private void requestNewLocationData() {
        // Initializing LocationRequest
        // object with appropriate methods
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5);
        locationRequest.setFastestInterval(0);
        locationRequest.setNumUpdates(1);

        // setting LocationRequest
        // on FusedLocationClient
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this); // this may or may not be needed
        mFusedLocationClient.requestLocationUpdates(locationRequest, mLocationCallback, Looper.myLooper());
    }
}
