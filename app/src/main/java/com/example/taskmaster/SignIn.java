package com.example.taskmaster;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.amplifyframework.AmplifyException;
import com.amplifyframework.analytics.pinpoint.AWSPinpointAnalyticsPlugin;
import com.amplifyframework.api.aws.AWSApiPlugin;
import com.amplifyframework.auth.cognito.AWSCognitoAuthPlugin;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.AWSDataStorePlugin;
import com.amplifyframework.storage.s3.AWSS3StoragePlugin;

public class SignIn extends AppCompatActivity {
    Handler handler;

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(PushListenerService.CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        createNotificationChannel();
        try {
//            Amplify.addPlugin(new AWSDataStorePlugin());
            Amplify.addPlugin(new AWSPinpointAnalyticsPlugin(getApplication()));
            Amplify.addPlugin(new AWSApiPlugin());
            Amplify.addPlugin(new AWSCognitoAuthPlugin());
            Amplify.addPlugin(new AWSS3StoragePlugin());
            Amplify.configure(getApplicationContext());
        } catch (AmplifyException e) {
            e.printStackTrace();
        }
        Button signUpBtn = findViewById(R.id.signUpBTN);
        signUpBtn.setOnClickListener(view -> {
            Intent intent = new Intent(this,SignUp.class);
            startActivity(intent);
        });

        Toast toast = Toast.makeText(this, "Invalid Email or Password , Try Again", Toast.LENGTH_LONG);
        handler = new Handler(Looper.getMainLooper(), message -> {
            Log.i("In Handler", "In Handler");
            toast.show();
            return false;
        });
        Button signInBTN = findViewById(R.id.signInBTN);
        signInBTN.setOnClickListener(view -> {
            EditText userEmailText = findViewById(R.id.UserEmailSignIn);
            EditText passwordText = findViewById(R.id.password);

            String userEmail = userEmailText.getText().toString();
            String password = passwordText.getText().toString();
            Amplify.Auth.signIn(
                    userEmail,
                    password,
                    result -> {Log.i("AuthQuickstart ==> Sign In Page", result.isSignInComplete() ? "Sign in succeeded" : "Sign in not complete");
                        Intent intent = new Intent(this,MainActivity.class);
                        startActivity(intent);},
                    error -> {Log.e("AuthQuickstart ==> Sign In Page", error.toString());
                        handler.sendEmptyMessage(0);
                    }
            );

        });
    }
}