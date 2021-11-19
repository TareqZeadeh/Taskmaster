package com.example.taskmaster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.amplifyframework.AmplifyException;
import com.amplifyframework.api.aws.AWSApiPlugin;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.AWSDataStorePlugin;

public class Settings extends AppCompatActivity {

    private static final String TAG = "Settings";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        MainActivity.sendAnalyticsInfo(this.toString(),MainActivity.class.toString());

//        amplifyConfig();
        Spinner teamSpin = findViewById(R.id.settingTeamSpin);

        ArrayAdapter<CharSequence> teamAdapter = ArrayAdapter.createFromResource(this,
                R.array.teams_Names, android.R.layout.simple_spinner_item);
        teamAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        teamSpin.setAdapter(teamAdapter);
        Button button = findViewById(R.id.button5);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences preferences2 = PreferenceManager.getDefaultSharedPreferences(Settings.this);
                SharedPreferences.Editor editor2 = preferences2.edit();
                TextView text = findViewById(R.id.userNameInput);
                String team = teamSpin.getSelectedItem().toString();
                String username = text.getText().toString();
                editor2.putString("userName",username);
                editor2.putString("settingTeamName",team);
                editor2.apply();
                Intent intent = new Intent(Settings.this,MainActivity.class);
                startActivity(intent);
//                finish();
            }
        });


//        findViewById(R.id.button5).setOnClickListener(view ->{
//
//        });
    }
//    public void amplifyConfig() {
//        try {
//            Amplify.addPlugin(new AWSDataStorePlugin()); // stores records locally
//            Amplify.addPlugin(new AWSApiPlugin()); // stores things in DynamoDB and allows us to perform GraphQL queries
//            Amplify.configure(getApplicationContext());
//
//            Log.i(TAG, "Initialized Amplify");
//        } catch (AmplifyException error) {
//            Log.e(TAG, "Could not initialize Amplify", error);
//        }
//    }

}