package com.example.taskmaster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.amplifyframework.core.Amplify;

public class ConfirmingUser extends AppCompatActivity {
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confarming_user);


        Toast toast = Toast.makeText(this, "Wrong Code , Try Again", Toast.LENGTH_LONG);
        handler = new Handler(Looper.getMainLooper(), message -> {
            Log.i("In Handler", "In Handler");
            toast.show();
            return false;
        });

//        SharedPreferences Preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String userEmail = getIntent().getStringExtra("userEmail");
        Button confirmBtn = findViewById(R.id.confirmeBTN);
        confirmBtn.setOnClickListener(view -> {
            EditText confirmNumText = findViewById(R.id.confirmNUM);
            String confirmNum = confirmNumText.getText().toString();
            Amplify.Auth.confirmSignUp(
                    userEmail,
                    confirmNum,
                    result -> {
                        Log.i("AuthQuickstart ==> confirming Page", result.isSignUpComplete() ? "Confirm signUp succeeded" : "Confirm sign up not complete");
                        Intent intent = new Intent(this, MainActivity.class);
                        startActivity(intent);
                    },
                    error -> {
                        Log.e("AuthQuickstart ==> confirming Page", error.toString());
                        handler.sendEmptyMessage(0);
                    }
            );

        });

    }
}