package com.example.taskmaster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class taskDetail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);
        Intent intent = getIntent();
        String title = intent.getExtras().getString("title");
        TextView text = findViewById(R.id.textView7);
        text.setText(title);
        String body = intent.getExtras().getString("body");
        TextView text2 = findViewById(R.id.textView10);
        text2.setText(body);
        String state = intent.getExtras().getString("state");
        TextView text3 = findViewById(R.id.textView11);
        text3.setText(state);

    }
}