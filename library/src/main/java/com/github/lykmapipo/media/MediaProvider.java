package com.github.lykmapipo.media;


import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.MediaStore;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.github.florent37.inlineactivityresult.Result;
import com.github.florent37.inlineactivityresult.callbacks.ActivityResultListener;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static com.github.florent37.inlineactivityresult.InlineActivityResult.startForResult;

/**
 * A pack of helpful helpers to gather images, video and audio from media source(s).
 *
 * @author lally elias <lallyelias87@gmail.com>
 * @version 0.1.0
 * @since 0.1.0
 */
public class MediaProvider {
    private static final String MEDIA_IMAGE_SUFFIX = ".jpg";
    private static final String MEDIA_VIDEO_SUFFIX = ".mp4";
    private static final String MEDIA_PROVIDER_PATH = "medias";
    private static final String MEDIA_PROVIDER_AUTHORITIES_SUFFIX = "fileprovider";
    private static final int REQUEST_IMAGE_CAPTURE_CODE = 1;

    /**
     * Generate random file name
     *
     * @return random file name
     * @since 0.1.0
     */
    public static synchronized String getRandomFileName() {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String fileName = timeStamp + "_";
        return fileName;
    }

    /**
     * Create temporary video file
     *
     * @param context
     * @return temporary video file
     * @throws IOException
     * @since 0.1.0
     */
    public static synchronized File createVideoTempFile(@NonNull Context context) throws IOException {
        File mediaDir = getMediaDir(context);

        // generate random filename
        String fileName = getRandomFileName();

        // create temporary video file
        File imageTempFile = File.createTempFile(
                fileName,
                MEDIA_VIDEO_SUFFIX,
                mediaDir
        );

        // return created video temp file
        return imageTempFile;
    }

    /**
     * Create temporary image file
     *
     * @param context
     * @return temporary image file
     * @throws IOException
     * @since 0.1.0
     */
    public static synchronized File createImageTempFile(@NonNull Context context) throws IOException {
        File mediaDir = getMediaDir(context);

        // generate random filename
        String fileName = getRandomFileName();

        // create temporary image file
        File imageTempFile = File.createTempFile(
                fileName,
                MEDIA_IMAGE_SUFFIX,
                mediaDir
        );

        // return created image temp file
        return imageTempFile;
    }

    /**
     * Obtain media root directory relative to {@link Context#getCacheDir()}
     *
     * @param context
     * @return
     */
    public static File getMediaDir(@NonNull Context context) {
        // obtain cache directory
        File cacheDir = context.getCacheDir();
        if (!cacheDir.exists()) {
            cacheDir.mkdirs();
        }

        // prepare media storage directory
        File mediaDir = new File(cacheDir, MEDIA_PROVIDER_PATH);
        if (!mediaDir.exists()) {
            mediaDir.mkdirs();
        }
        return mediaDir;
    }

    /**
     * Obtain {@link Uri} for a given {@link File} instance
     *
     * @param context
     * @param file
     * @return valid {@link Uri} for the given {@link File} instance
     * @since 0.1.0
     */
    public static synchronized Uri getUriFor(Context context, File file) throws Exception {
        // obtain authority
        String packageName = context.getPackageName();
        String authority = packageName + "." + MEDIA_PROVIDER_AUTHORITIES_SUFFIX;

        // obtain file uri
        Uri uri = FileProvider.getUriForFile(context, authority, file);
        return uri;
    }

    public static synchronized void clear() {
    }

    /**
     * Capture image
     *
     * @param fragment
     * @param listener
     * @since 0.1.0
     */
    public static synchronized void captureImage(
            @NonNull Fragment fragment,
            @NonNull OnImageCapturedListener listener
    ) {
        captureImage(fragment.getActivity(), listener);
    }

    /**
     * Capture image
     *
     * @param activity
     * @param listener
     * @since 0.1.0
     */
    public static synchronized void captureImage(
            @NonNull FragmentActivity activity,
            @NonNull OnImageCapturedListener listener
    ) {

        // try capture image
        try {
            // TODO refactor to us background task
            // prepare image file & uri
            File imageFile = createImageTempFile(activity);
            Uri imageFileUri = getUriFor(activity, imageFile);

            // create intent
            Intent imageCaptureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            imageCaptureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageFileUri);

            // ensure that there's a camera activity to handle the intent
            PackageManager packageManager = activity.getPackageManager();
            boolean canCapture = imageCaptureIntent.resolveActivity(packageManager) != null;
            if (!canCapture) {
                throw new Exception("Camera Not Found");
            }

            // request image capture
            startForResult(activity, imageCaptureIntent, new ActivityResultListener() {
                @Override
                public void onSuccess(Result result) {
                    listener.onSuccess(imageFile, imageFileUri);
                }

                @Override
                public void onFailed(Result result) {
                    Exception error = new Exception("Image Capture Failed");
                    listener.onFailure(error);
                }
            });
        }

        // handle image capture errors
        catch (Exception error) {
            listener.onFailure(error);
        }
    }

    /**
     * Record video
     *
     * @param fragment
     * @param listener
     * @since 0.1.0
     */
    public static synchronized void recordVideo(
            @NonNull Fragment fragment,
            @NonNull OnVideoRecordedListener listener
    ) {
        recordVideo(fragment.getActivity(), listener);
    }

    /**
     * Record video
     *
     * @param activity
     * @param listener
     * @since 0.1.0
     */
    public static synchronized void recordVideo(
            @NonNull FragmentActivity activity,
            @NonNull OnVideoRecordedListener listener
    ) {

        // try record video
        try {
            // TODO refactor to us background task
            // prepare video file & uri
            File videoFile = createVideoTempFile(activity);
            Uri videoFileUri = getUriFor(activity, videoFile);

            // create intent
            Intent videoRecordIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
            videoRecordIntent.putExtra(MediaStore.EXTRA_OUTPUT, videoFileUri);

            // ensure that there's a camera activity to handle the intent
            PackageManager packageManager = activity.getPackageManager();
            boolean canCapture = videoRecordIntent.resolveActivity(packageManager) != null;
            if (!canCapture) {
                throw new Exception("Camera Not Found");
            }

            // request image capture
            startForResult(activity, videoRecordIntent, new ActivityResultListener() {
                @Override
                public void onSuccess(Result result) {
                    listener.onSuccess(videoFile, videoFileUri);
                }

                @Override
                public void onFailed(Result result) {
                    Exception error = new Exception("Video Capture Failed");
                    listener.onFailure(error);
                }
            });
        }

        // handle video capture errors
        catch (Exception error) {
            listener.onFailure(error);
        }
    }

    /**
     * Record an audio
     *
     * @param fragment
     * @param listener
     * @since 0.1.0
     */
    public static synchronized void recordAudio(
            @NonNull Fragment fragment,
            @NonNull OnAudioRecordedListener listener
    ) {
        recordAudio(fragment.getActivity(), listener);
    }

    /**
     * Record an audio
     *
     * @param activity
     * @param listener
     * @since 0.1.0
     */
    public static synchronized void recordAudio(
            @NonNull FragmentActivity activity,
            @NonNull OnAudioRecordedListener listener
    ) {

        // try record audio
        // TODO use https://github.com/3llomi/RecordView
        try {
            // TODO refactor to us background task
            // prepare audio file & uri
            File audioFile = createImageTempFile(activity);
            Uri imageFileUri = getUriFor(activity, audioFile);

            // create intent
            Intent audioRecordIntent = new Intent(MediaStore.Audio.Media.RECORD_SOUND_ACTION);

            // ensure that there's a audio recorder activity to handle the intent
            PackageManager packageManager = activity.getPackageManager();
            boolean canRecord = audioRecordIntent.resolveActivity(packageManager) != null;
            if (!canRecord) {
                throw new Exception("Audio Recorder Not Found");
            }

            // request audio recording
            startForResult(activity, audioRecordIntent, new ActivityResultListener() {
                @Override
                public void onSuccess(Result result) {
                    Intent data = result.getData();
                    Uri uri = data.getData();
                    // TODO construct file from uri
                    listener.onSuccess(audioFile, uri);
                }

                @Override
                public void onFailed(Result result) {
                    Exception error = new Exception("Audio Record Failed");
                    listener.onFailure(error);
                }
            });
        }

        // handle audio record errors
        catch (Exception error) {
            listener.onFailure(error);
        }
    }

    public interface OnImageCapturedListener {
        void onSuccess(File file, Uri uri);

        void onFailure(Exception error);
    }

    public interface OnVideoRecordedListener {
        void onSuccess(File file, Uri uri);

        void onFailure(Exception error);
    }

    public interface OnAudioRecordedListener {
        void onSuccess(File file, Uri uri);

        void onFailure(Exception error);
    }

}
