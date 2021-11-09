package com.example.taskmaster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.core.Amplify;

public class Add_Task extends AppCompatActivity {
    private static final String TAG = "Add_Task";
    public static int c =0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        Button button = findViewById(R.id.button3);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText taskTitle = findViewById(R.id.taskTitle);
                String title = taskTitle.getText().toString();
                EditText textDescription = findViewById(R.id.taskDescription);
                String description = textDescription.getText().toString();
                Task task = new Task(title,description,State.NEW.toString());
                TaskDataBase.getInstance(getApplicationContext()).taskDAO().insertTask(task);

                TextView textView = findViewById(R.id.textView5);
                c++;

                com.amplifyframework.datastore.generated.model.Task task1 = com.amplifyframework.datastore.generated.model.Task.builder()
                        .title(title).body(description).state(State.NEW.toString()).build();
                apiTaskSave(task1);

                textView.setText("Total Tasks :"+c);
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(Add_Task.this);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putInt("c",c);
                editor.apply();
                Toast toast = Toast.makeText(getApplicationContext(),"Submitted!", Toast.LENGTH_SHORT);
                toast.show();
                Intent intent = new Intent(Add_Task.this,MainActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences Preferences = PreferenceManager.getDefaultSharedPreferences(this);
        int c = Preferences.getInt("c",0);
        TextView textView = findViewById(R.id.textView5);
        textView.setText("Total Tasks :"+c);
    }

    public void apiTaskSave(com.amplifyframework.datastore.generated.model.Task task){
        Amplify.API.mutate(ModelMutation.create(task),
                taskSaved ->{ Log.i(TAG, "Task Is Saved => " + taskSaved.getData().getTitle());},
                error->{Log.e(TAG, "Task Is Not Saved => " + error.toString());});
    }
}