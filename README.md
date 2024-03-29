android-media-provider
======================

[![](https://jitpack.io/v/lykmapipo/android-media-provider.svg)](https://jitpack.io/#lykmapipo/android-media-provider)

A pack of helpful helpers to gather images, video and audio from media source(s).

## Installation
Add [https://jitpack.io](https://jitpack.io) to your build.gradle with:
```gradle
allprojects {
    repositories {
        maven { url "https://jitpack.io" }
    }
}
```
add `android-media-provider` dependency into your project

```gradle
dependencies {
    implementation 'com.github.lykmapipo:android-media-provider:v0.3.0'
}
```

## Usage

In activity(or fragment) capture image or record video and audio

```java
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
```


## Test
```sh
./gradlew test
```

## Contribute
It will be nice, if you open an issue first so that we can know what is going on, then, fork this repo and push in your ideas.
Do not forget to add a bit of test(s) of what value you adding.

## License

(The MIT License)

Copyright (c) lykmapipo && Contributors

Permission is hereby granted, free of charge, to any person obtaining
a copy of this software and associated documentation files (the
'Software'), to deal in the Software without restriction, including
without limitation the rights to use, copy, modify, merge, publish,
distribute, sublicense, and/or sell copies of the Software, and to
permit persons to whom the Software is furnished to do so, subject to
the following conditions:

The above copyright notice and this permission notice shall be
included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED 'AS IS', WITHOUT WARRANTY OF ANY KIND,
EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY
CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
