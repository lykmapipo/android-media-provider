package com.github.lykmapipo.media;

import android.content.Context;

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

    @Before
    public void setup() {
        Context context = ApplicationProvider.getApplicationContext();
    }

    @Test
    public void testShouldBeAbleToCreateTempFile() {
        File file = MediaProvider.createTempFile();
        assertTrue("File should be created", file != null);
    }

    @After
    public void cleanup() {
        MediaProvider.clear();
    }
}