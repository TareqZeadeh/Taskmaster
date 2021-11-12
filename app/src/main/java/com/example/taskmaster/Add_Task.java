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
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.amplifyframework.AmplifyException;
import com.amplifyframework.api.aws.AWSApiPlugin;
import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.AWSDataStorePlugin;
import com.amplifyframework.datastore.generated.model.Team;

import java.util.ArrayList;
import java.util.List;

public class Add_Task extends AppCompatActivity {
    private static final String TAG = "Add_Task";
    public static int c = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        Button button = findViewById(R.id.button3);
        button.setOnClickListener(view -> {


            Spinner teamSpin = findViewById(R.id.teamSpin);

            ArrayAdapter<CharSequence> teamAdapter = ArrayAdapter.createFromResource(this,
                    R.array.teams_Names, android.R.layout.simple_spinner_item);
            teamAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            teamSpin.setAdapter(teamAdapter);


            Spinner statusSpin = findViewById(R.id.statusSpin);

            ArrayAdapter<CharSequence> statusAdapter = ArrayAdapter.createFromResource(this,
                    R.array.status, android.R.layout.simple_spinner_item);
            statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            statusSpin.setAdapter(statusAdapter);


            EditText taskTitle = findViewById(R.id.taskTitle);
            String title = taskTitle.getText().toString();

            EditText textDescription = findViewById(R.id.taskDescription);
            String description = textDescription.getText().toString();

            String status = statusSpin.getSelectedItem().toString();
            String team = teamSpin.getSelectedItem().toString();

            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(Add_Task.this);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("team", team);
            editor.apply();

            Task task = new Task(title, description, State.NEW.toString());
            TaskDataBase.getInstance(getApplicationContext()).taskDAO().insertTask(task);

            TextView textView = findViewById(R.id.textView5);
            c++;


            apiTaskSave(title,description,status);

            textView.setText("Total Tasks :" + c);
            SharedPreferences preferences1 = PreferenceManager.getDefaultSharedPreferences(Add_Task.this);
            SharedPreferences.Editor editor1 = preferences1.edit();
            editor1.putInt("c", c);
            editor1.apply();
            Toast toast = Toast.makeText(getApplicationContext(), "Submitted!", Toast.LENGTH_SHORT);
            toast.show();
//            Intent intent = new Intent(Add_Task.this, MainActivity.class);
//            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences Preferences1 = PreferenceManager.getDefaultSharedPreferences(this);
        int c = Preferences1.getInt("c", 0);
        TextView textView = findViewById(R.id.textView5);
        textView.setText("Total Tasks :" + c);
    }

    public void apiTaskSave(String title,String body,String status) {
        SharedPreferences Preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String teamName = Preferences.getString("team", null);
        List<Team> apiTeams = new ArrayList<>();
        String teamID = "";


        Amplify.API.query(ModelQuery.list(Team.class),
                teamReceived -> {
                    for (Team team : teamReceived.getData()) {
                        apiTeams.add(team);
                    }
                },
                error -> {Log.e(TAG, "Error In Retrieving Teams", error);});


        for(Team team : apiTeams){
            if(team.getName().equals(teamName)){
                teamID = team.getId();
            }
        }


        com.amplifyframework.datastore.generated.model.Task task = com.amplifyframework.datastore.generated.model.Task
                .builder().title(title).body(body).state(status).teamId(teamID).build();
        saveTaskHelper(task);
    }
    private void saveTaskHelper(com.amplifyframework.datastore.generated.model.Task task){
        Amplify.API.mutate(ModelMutation.create(task),
                taskSaved -> {

                    Log.i(TAG, "Task Is Saved => " + taskSaved.getData().getTitle() );
                },
                error -> {
                    Log.e(TAG, "Task Is Not Saved => " + error.toString());
                });
    }



    }