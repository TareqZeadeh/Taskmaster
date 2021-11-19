package com.example.taskmaster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.apache.commons.io.FilenameUtils;

public class taskDetail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MainActivity.sendAnalyticsInfo(this.toString(),MainActivity.class.toString());
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

        String fileUrl = intent.getExtras().getString("fileKey");
        TextView fileURL = findViewById(R.id.urlText);
        ImageView taskImage = findViewById(R.id.taskImage);

        fileURL.setVisibility(View.VISIBLE);
        taskImage.setVisibility(View.VISIBLE);
        fileURL.setText(fileUrl);
        fileURL.setMovementMethod(new ScrollingMovementMethod());
            fileURL.setOnClickListener(view -> {
                Intent intentUrl = new Intent(Intent.ACTION_VIEW);
                intentUrl.setData(Uri.parse(fileUrl));
                startActivity(intentUrl);

            });

        Picasso.get().load(fileUrl).into(taskImage);
//        if(fileUrl == null){
//            taskImage.setVisibility(View.INVISIBLE);
//            fileURL.setVisibility(View.INVISIBLE);
//        }
//        else if(FilenameUtils.getName(fileUrl).contains("jpg") ||
//                FilenameUtils.getName(fileUrl).contains("jpeg") ||
//                FilenameUtils.getName(fileUrl).contains("gif") ||
//                FilenameUtils.getName(fileUrl).contains("png") ||
//                FilenameUtils.getName(fileUrl).contains("svg") ||
//                FilenameUtils.getName(fileUrl).contains("webp")){
//
//            fileURL.setVisibility(View.INVISIBLE);
//            taskImage.setVisibility(View.VISIBLE);
//            Picasso.get().load(fileUrl).into(taskImage);
//
//        }else if(!(FilenameUtils.getName(fileUrl).contains("jpg") ||
//                FilenameUtils.getName(fileUrl).contains("jpeg") ||
//                FilenameUtils.getName(fileUrl).contains("gif") ||
//                FilenameUtils.getName(fileUrl).contains("png") ||
//                FilenameUtils.getName(fileUrl).contains("svg") ||
//                FilenameUtils.getName(fileUrl).contains("webp"))){
//
//            taskImage.setVisibility(View.INVISIBLE);
//            fileURL.setVisibility(View.VISIBLE);
//            fileURL.setText(fileUrl);
//            fileURL.setOnClickListener(view -> {
//                Intent intentUrl = new Intent(Intent.ACTION_VIEW);
//                intentUrl.setData(Uri.parse(fileUrl));
//                startActivity(intentUrl);
//
//            });
//        }

    }
}