package com.example.taskmaster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Add_Task extends AppCompatActivity {
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
                textView.setText("Total Tasks :"+c);
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(Add_Task.this);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putInt("c",c);
                editor.apply();
                Toast toast = Toast.makeText(getApplicationContext(),"Submitted!", Toast.LENGTH_SHORT);
                toast.show();
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
}