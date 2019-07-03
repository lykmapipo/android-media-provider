package com.github.lykmapipo.media.sample.ui;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import com.github.lykmapipo.media.MediaProvider;
import com.github.lykmapipo.media.sample.R;

import java.io.File;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private ImageView ivCapturedImage;
    private VideoView vvRecordedVideo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // capture image
        ivCapturedImage = findViewById(R.id.ivCapturedImage);
        Button captureImageButton = findViewById(R.id.btnCaptureImage);
        captureImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MediaProvider.captureImage(MainActivity.this, new MediaProvider.OnImageCapturedListener() {
                    @Override
                    public void onSuccess(File file, Uri uri) {
                        ivCapturedImage.setImageURI(uri);
                        Toast.makeText(MainActivity.this, "Image Captured Success: " + file.getAbsolutePath(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Exception error) {
                        Toast.makeText(MainActivity.this, "Image Captured Failed: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        // record video
        vvRecordedVideo = findViewById(R.id.vvRecordedVideo);
        Button recordVideoButton = findViewById(R.id.btnRecordVideo);
        recordVideoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MediaProvider.recordVideo(MainActivity.this, new MediaProvider.OnVideoRecordedListener() {
                    @Override
                    public void onSuccess(File file, Uri uri) {
                        vvRecordedVideo.setVideoURI(uri);
                        vvRecordedVideo.start();
                        Toast.makeText(MainActivity.this, "Video Recorded Success: " + file.getAbsolutePath(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Exception error) {
                        Toast.makeText(MainActivity.this, "Video Recorded Failed: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        // record audio
        Button recordAudioButton = findViewById(R.id.btnRecordAudio);
        recordAudioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MediaProvider.recordAudio(MainActivity.this, new MediaProvider.OnAudioRecordedListener() {
                    @Override
                    public void onSuccess(File file, Uri uri) {
                        Toast.makeText(MainActivity.this, "Audio Record Success: " + file.getAbsolutePath(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Exception error) {
                        Toast.makeText(MainActivity.this, "Audio Record Failed: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}
