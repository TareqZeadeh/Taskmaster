package com.example.taskmaster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.amplifyframework.auth.AuthUserAttributeKey;
import com.amplifyframework.auth.options.AuthSignUpOptions;
import com.amplifyframework.core.Amplify;

public class SignUp extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);



//        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
//        SharedPreferences.Editor editor = preferences.edit();


        Button signUpKActionBtn = findViewById(R.id.sign_up_page_BTN);
        signUpKActionBtn.setOnClickListener(view -> {
            EditText userNameText = findViewById(R.id.signUpUserName);
            EditText emailText = findViewById(R.id.signUpEmail);
            EditText passwordText = findViewById(R.id.signUpPassword);

            String userName = userNameText.getText().toString();
            String email = emailText.getText().toString();
            String password = passwordText.getText().toString();
            AuthSignUpOptions options = AuthSignUpOptions.builder()
                    .userAttribute(AuthUserAttributeKey.name(),userName )
                    .build();
            Amplify.Auth.signUp(email, password, options,
                    result -> {Log.i("AuthQuickStart ==> Sign Up Page", "Result: " + result.toString());
//                        editor.putString("userEmail", email);
//                        editor.apply();
                        Intent intent = new Intent(this, ConfirmingUser.class);
                        intent.putExtra("userEmail",email);
                        startActivity(intent);},
                    error -> Log.e("AuthQuickStart ==> Sign Up Page", "Sign up failed", error)
            );
        });

    }
}