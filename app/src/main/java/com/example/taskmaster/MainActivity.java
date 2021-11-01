package com.example.taskmaster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
                Intent intent = new Intent (MainActivity.this,All_Tasks.class);
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

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences Preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String username = Preferences.getString("userName","Set a User Name");
        TextView text =findViewById(R.id.textView9);
        text.setText(username+"'s Tasks");
    }


    public void taskDetail(View view) {
    int id = view.getId();
    TextView text = findViewById(id);
    String task = text.getText().toString();
    Intent intent = new Intent(MainActivity.this,taskDetail.class);
    intent.putExtra("task",task);
    startActivity(intent);
    }
}