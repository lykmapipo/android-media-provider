package com.github.lykmapipo.media;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;

import androidx.test.core.app.ApplicationProvider;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.io.File;

import static org.junit.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
public class MediaProviderTest {
    Context context;

    @Before
    public void setup() {
        context = ApplicationProvider.getApplicationContext();
    }

    @Test
    public void testShouldGenerateRandomFileName() {
        String fileName = MediaProvider.getRandomFileName();
        assertTrue("Should generate random filename", !TextUtils.isEmpty(fileName));
    }

    @Test
    public void testShouldCreateImageTempFile() throws Exception {
        File file = MediaProvider.createImageTempFile(context);
        assertTrue("Should create temp file", file != null);
    }

    @Test
    public void testShouldObtainFileUri() throws Exception {
        File file = MediaProvider.createImageTempFile(context);
        Uri uri = MediaProvider.getUriFor(context, file);
        assertTrue("Should obtain uri for existing file", uri != null);
    }

    @After
    public void cleanup() {
        MediaProvider.clear();
    }
}