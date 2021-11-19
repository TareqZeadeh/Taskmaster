package com.example.taskmaster;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.amplifyframework.AmplifyException;
import com.amplifyframework.analytics.AnalyticsEvent;
import com.amplifyframework.api.aws.AWSApiPlugin;
import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.auth.options.AuthSignOutOptions;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.AWSDataStorePlugin;
import com.amplifyframework.datastore.generated.model.Task;
import com.amplifyframework.datastore.generated.model.Team;
import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobile.client.Callback;
import com.amazonaws.mobile.client.UserStateDetails;
import com.amazonaws.mobile.config.AWSConfiguration;
import com.amazonaws.mobileconnectors.pinpoint.PinpointConfiguration;
import com.amazonaws.mobileconnectors.pinpoint.PinpointManager;
import com.amplifyframework.core.Amplify;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.messaging.FirebaseMessaging;

import com.amazonaws.mobileconnectors.pinpoint.targeting.TargetingClient;
import com.amazonaws.mobileconnectors.pinpoint.targeting.endpointProfile.EndpointProfileUser;
import com.amazonaws.mobileconnectors.pinpoint.targeting.endpointProfile.EndpointProfile;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    List<Task> tasks;
    RecyclerView recyclerView;
    Handler handler;
    Handler handler1;
    Handler userNameHandler;
    String teamID = "";
    String username = "";
    //    int counter = 0;
    private static PinpointManager pinpointManager;

    public static PinpointManager getPinpointManager(final Context applicationContext) {
        if (pinpointManager == null) {
            final AWSConfiguration awsConfig = new AWSConfiguration(applicationContext);
            AWSMobileClient.getInstance().initialize(applicationContext, awsConfig, new Callback<UserStateDetails>() {
                @Override
                public void onResult(UserStateDetails userStateDetails) {
                    Log.i(TAG, "INIT => " + userStateDetails.getUserState().toString());
                }

                @Override
                public void onError(Exception e) {
                    Log.e("INIT", "Initialization error.", e);
                }
            });

            PinpointConfiguration pinpointConfig = new PinpointConfiguration(
                    applicationContext,
                    AWSMobileClient.getInstance(),
                    awsConfig);

            pinpointManager = new PinpointManager(pinpointConfig);

            FirebaseMessaging.getInstance().getToken()
                    .addOnCompleteListener(new OnCompleteListener<String>() {
                        @Override
                        public void onComplete(@NonNull com.google.android.gms.tasks.Task<String> task) {
                            if (!task.isSuccessful()) {
                                Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                                return;
                            }
                            final String token = task.getResult();
                            Log.d(TAG, "Registering push notifications token: " + token);
                            pinpointManager.getNotificationClient().registerDeviceToken(token);
                        }
                    });
        }
        return pinpointManager;
    }

    public void assignUserIdToEndpoint() {
        TargetingClient targetingClient = pinpointManager.getTargetingClient();
        EndpointProfile endpointProfile = targetingClient.currentEndpoint();
        EndpointProfileUser endpointProfileUser = new EndpointProfileUser();
        endpointProfileUser.setUserId("UserIdValue");
        endpointProfile.setUser(endpointProfileUser);
        targetingClient.updateEndpointProfile(endpointProfile);
        Log.d(TAG, "Assigned user ID " + endpointProfileUser.getUserId() +
                " to endpoint " + endpointProfile.getEndpointId());
    }
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        SharedPreferences preferences4 = PreferenceManager.getDefaultSharedPreferences(this);
//        SharedPreferences.Editor editor4 = preferences4.edit();
//        editor4.putInt("counter", counter);
//        SharedPreferences Preferences1 = PreferenceManager.getDefaultSharedPreferences(this);
//        counter = Preferences1.getInt("counter", counter);

//        System.out.println("counter ====>>>>>>>>>>>>>>>>>>>>>>" + counter);
//        if(counter==0){amplifyConfig();}
//        counter++;
//        editor4.putInt("counter", counter);
//        System.out.println("counter ====>>>>>>>>>>>>>>>>>>>>>>" + counter);


        //====================================================
        getPinpointManager(getApplicationContext());
        assignUserIdToEndpoint();
        //====================================================

        getAuthUserName();


        Button logOutBTN = findViewById(R.id.LogOutBTN);
        logOutBTN.setOnClickListener(view -> {
            Amplify.Auth.signOut(
                    () -> {
                        Log.i("AuthQuickstart", "Signed out successfully");
                        Intent intent = new Intent(this, SignIn.class);
                        startActivity(intent);
                    },
                    error -> Log.e("AuthQuickstart", error.toString())
            );
        });

//        tasks.add(new Task("Task1","first Task",State.NEW.toString()));
//        tasks.add(new Task("Task2","second Task",State.ASSIGNED.toString()));
//        tasks.add(new Task("Task3","third Task",State.COMPLETE.toString()));
//        tasks.add(new Task("Task4","fourth Task",State.IN_PROGRESS.toString()));
//        tasks.add(new Task("Task5","fifth Task",State.COMPLETE.toString()));
//        tasks.add(new Task("Task6","sixth Task",State.NEW.toString()));
//        tasks.add(new Task("Task7","seventh Task",State.ASSIGNED.toString()));
//        tasks.add(new Task("Task8","eighth Task",State.IN_PROGRESS.toString()));
//        tasks = TaskDataBase.getInstance(getApplicationContext()).taskDAO().getAll();


        // Calling the teams Seeding function============================
//        apiTeamSeeding();
        sendAnalyticsInfo(this.toString(),SignIn.class.toString());

        apiTeamGetter();
        recyclerView = findViewById(R.id.rcv);

        userNameHandler = new Handler(Looper.getMainLooper(), message -> {
            username = message.getData().getString("userName");
            TextView text = findViewById(R.id.textView9);
            text.setText(username + "'s Tasks");
            return false;
        });

        handler1 = new Handler(Looper.getMainLooper(), message -> {
            Log.i("In Handler", "In Handler");
            teamID = message.getData().getString("teamId");
            apiTaskGetter(teamID);
            return false;
        });
        handler = new Handler(Looper.getMainLooper(), message -> {
            Log.i("In Handler", "In Handler");
            if (tasks.size() > 0) {
                recyclerView.setLayoutManager(new LinearLayoutManager(this));
                recyclerView.setAdapter(new TaskAdapter(tasks));
                recyclerView.getAdapter().notifyDataSetChanged();
            }
            return false;
        });

        Button button1 = findViewById(R.id.button);
        button1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Add_Task.class);
                startActivity(intent);
            }
        });

        Button button2 = findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, All_Tasks.class);
                startActivity(intent);
            }
        });
        Button button3 = findViewById(R.id.button4);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Settings.class);
                startActivity(intent);
            }
        });
    }


    @SuppressLint("SetTextI18n")
    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences Preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String usernameSHP = Preferences.getString("userName", "Set a User Name");
        TextView text = findViewById(R.id.textView9);
        text.setText(usernameSHP + "'s Tasks");
        apiTaskGetter(teamID);
//        handler = new Handler(Looper.getMainLooper(),message -> {
//            Log.i("In Handler","In Handler");
//            if(tasks.size()>0){
//                recyclerView.setLayoutManager(new LinearLayoutManager(this));
//                recyclerView.setAdapter(new TaskAdapter(tasks));
//                recyclerView.getAdapter().notifyDataSetChanged();
//            }
//            return false;
//        });

    }

    public void apiTeamGetter() {
        SharedPreferences Preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String teamName = Preferences.getString("settingTeamName", "Team A");
        List<Team> apiTeams = new ArrayList<>();


        Amplify.API.query(ModelQuery.list(Team.class, Team.NAME.contains(teamName)),
                teamReceived -> {
                    for (Team team : teamReceived.getData()) {
                        apiTeams.add(team);
                    }
                    Bundle bundle = new Bundle();
                    bundle.putString("teamId", apiTeams.get(0).getId());
                    Message message = new Message();
                    message.setData(bundle);
                    handler1.sendMessage(message);
                },
                error -> {
                    Log.e(TAG, "Error In Retrieving Teams", error);
                });


    }

    public void apiTaskGetter(String teamID) {

        Amplify.API.query(ModelQuery.list(com.amplifyframework.datastore.generated.model.Task.class, Task.TEAM_ID.eq(teamID)),
                taskReceived -> {
                    tasks = new ArrayList<>();
                    for (com.amplifyframework.datastore.generated.model.Task task : taskReceived.getData()) {
                        tasks.add(task);
                    }
                    handler.sendEmptyMessage(0);
                },
                error -> {
                    Log.e(TAG, "Error In Retrieving Tasks", error);
                });
    }

    public void amplifyConfig() {
        try {
            Amplify.addPlugin(new AWSDataStorePlugin());
            Amplify.addPlugin(new AWSApiPlugin()); // stores things in DynamoDB and allows us to perform GraphQL queries
            Amplify.configure(getApplicationContext());

            Log.i(TAG, "Initialized Amplify");
        } catch (AmplifyException error) {
            Log.e(TAG, "Could not initialize Amplify", error);
        }
    }

    public void apiTeamSeeding() {
        String[] teams = getResources().getStringArray(R.array.teams_Names);

        for (int i = 0; i < teams.length; i++) {
            Team team = Team.builder().name(teams[i]).build();
            Amplify.API.mutate(ModelMutation.create(team),
//                    + teamSaved.getData().getName()
                    teamSaved -> {
                        Log.i(TAG, "Team Is Saved => ");
                    },
                    error -> {
                        Log.e(TAG, "Team Is Not Saved => " + error.toString());
                    });
        }
    }

    public void getAuthUserName() {
        Amplify.Auth.fetchUserAttributes(
                attributes -> {
                    Log.i("AuthDemo", "User attributes = " + attributes.get(2).getValue());
                    Bundle bundle = new Bundle();
                    bundle.putString("userName", attributes.get(2).getValue());
                    Message message = new Message();
                    message.setData(bundle);
                    userNameHandler.sendMessage(message);
                },
                error -> Log.e("AuthDemo", "Failed to fetch user attributes.", error)
        );
    }

    public static void sendAnalyticsInfo(String current,String previous ){
        AnalyticsEvent event = AnalyticsEvent.builder()
                .name("Activity Change ")
                .addProperty("Current Activity", current)
                .addProperty("Previous Activity", previous)
                .addProperty("Successful", true)
                .build();

        Amplify.Analytics.recordEvent(event);
    }
}


