package com.github.lykmapipo.media;


import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.MediaStore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;

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
    private static final String MEDIA_PROVIDER_AUTHORITIES_SUFFIX = "fileprovider";
    private static final String REQUEST_IMAGE_CAPTURE_TAG = ImageCaptureFragment.class.getSimpleName();
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

    public static synchronized void captureImage(@NonNull AppCompatActivity activity, @NonNull OnImageCapturedListener listener) {
        // prepare fragment manager and transaction
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // TODO find existing
//        Fragment fragment = fragmentManager.findFragmentByTag(REQUEST_IMAGE_CAPTURE_TAG);
        ImageCaptureFragment imageCaptureFragment = new ImageCaptureFragment(listener);
        fragmentTransaction.add(imageCaptureFragment, REQUEST_IMAGE_CAPTURE_TAG);
        fragmentTransaction.commitAllowingStateLoss();
        fragmentManager.executePendingTransactions();

        // launch image capture intent
        imageCaptureFragment.startForResult();
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

    /**
     * Image capture request fragment
     *
     * @since 0.1.0
     */
    static class ImageCaptureFragment extends Fragment {
        OnImageCapturedListener listener; //TODO use week references
        File imageFile;
        Uri imageFileUri;

        public ImageCaptureFragment(@NonNull OnImageCapturedListener listener) {
            this.listener = listener;
        }

        public void startForResult() {
            // try launch image capture activity
            try {
                // prepare image capture intent
                Intent captureImageIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                // ensure that there's a camera activity to handle the intent
                PackageManager packageManager = getActivity().getPackageManager();
                boolean canCapture = captureImageIntent.resolveActivity(packageManager) != null;

                // throw not camera
                if (!canCapture) {
                    throw new Exception("Camera Not Found"); //TODO use string res
                }
                // continue with capturing image
                else {
                    // obtain image temp file and uri
                    imageFile = createImageTempFile(getActivity());
                    imageFileUri = getUriFor(getActivity(), imageFile);

                    // update intent with image uri
                    captureImageIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageFileUri);
                    startActivityForResult(captureImageIntent, REQUEST_IMAGE_CAPTURE_CODE);
                }
            }
            // catch all errors and notify
            catch (Exception error) {
                listener.onError(error);
            }
        }

        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            if (requestCode == REQUEST_IMAGE_CAPTURE_CODE && resultCode == RESULT_OK) {
                if (listener != null) {
                    listener.onImage(imageFile);
                }
            }
            // TODO remove fragment
            // TODO handle other result codes
        }
    }
}
