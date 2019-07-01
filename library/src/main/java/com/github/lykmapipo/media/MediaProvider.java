package com.github.lykmapipo.media;


import java.io.File;

/**
 * A pack of helpful helpers to gather images, video and audio from media source(s).
 *
 * @author lally elias <lallyelias87@gmail.com>
 * @version 0.1.0
 * @since 0.1.0
 */
public class MediaProvider {

    public static synchronized File createTempFile() {
        return new File("./todo.txt");
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
