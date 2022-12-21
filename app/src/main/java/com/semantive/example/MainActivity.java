package com.semantive.example;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


import java.util.Arrays;
import java.util.List;

import pl.mkazik.waveformandroid.ui.Segment;
import pl.mkazik.waveformandroid.ui.WaveformFragment;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_READ_STORAGE = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkPermission(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                REQUEST_READ_STORAGE,
                () -> launchWaveFormFromFile(savedInstanceState)
        );
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_READ_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                launchWaveFormFromFile(null);
            }
        }
    }

    private void checkPermission(final String permission, int requestCode, final Runnable action) {
        if (ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]{permission}, requestCode);
        } else {
            action.run();
        }
    }

    private void launchWaveFormFromFile(@Nullable Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new CustomWaveformFragment())
                    .commit();
        }
    }

    public static class CustomWaveformFragment extends WaveformFragment {

        /**
         * Provide path to your audio file.
         *
         * @return
         */
        @Override
        protected String getFileName() {
            return "path to your audio file";
        }

        /**
         * Optional - provide list of segments (start and stop values in seconds) and their corresponding colors
         *
         * @return
         */
        @Override
        protected List<Segment> getSegments() {
            return Arrays.asList(
                    new Segment(55.2, 55.8, Color.rgb(238, 23, 104)),
                    new Segment(56.2, 56.6, Color.rgb(238, 23, 104)),
                    new Segment(58.4, 59.9, Color.rgb(184, 92, 184))
            );
        }
    }
}
