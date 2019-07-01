package com.github.lykmapipo.media;


import android.content.Context;

import androidx.annotation.NonNull;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * A pack of helpful helpers to gather images, video and audio from media source(s).
 *
 * @author lally elias <lallyelias87@gmail.com>
 * @version 0.1.0
 * @since 0.1.0
 */
public class MediaProvider {
    private static final String MEDIA_IMAGE_SUFFIX = ".jpg";
    private static final String MEDIA_PROVIDER_PATH = "medias";
    private static final String MEDIA_PROVIDER_AUTHORITIES_SUFFIX = "medias";

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
     * Create temporary image file
     *
     * @param context
     * @return temporary image file
     * @throws IOException
     * @since 0.1.0
     */
    public static synchronized File createImageTempFile(@NonNull Context context) throws IOException {
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

    public static synchronized void clear() {
    }

    public static synchronized void captureImage(OnImageCapturedListener listener) {
    }

    public static synchronized void recordVideo(OnVideoRecordedListener listener) {
    }

    public static synchronized void recordAudio(OnAudioRecordedListener listener) {
    }

    public interface OnImageCapturedListener {
        void onImage(File file);

        void onError(Exception error);
    }

    public interface OnVideoRecordedListener {
        void onVideo(File file);

        void onError(Exception error);
    }

    public interface OnAudioRecordedListener {
        void onAudio(File file);

        void onError(Exception error);
    }
}
