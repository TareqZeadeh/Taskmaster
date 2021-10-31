package com.example.taskmaster;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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
                TextView textView = findViewById(R.id.textView5);
                c++;
                textView.setText("Total Tasks :"+c);
                Toast toast = Toast.makeText(getApplicationContext(),"Submitted!", Toast.LENGTH_SHORT);
                toast.show();
            }
        });
    }
}