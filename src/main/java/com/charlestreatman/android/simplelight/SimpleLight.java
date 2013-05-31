package com.charlestreatman.android.simplelight;

import android.content.SharedPreferences;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.os.Bundle;
import android.app.Activity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

public class SimpleLight extends Activity implements View.OnTouchListener {
    private boolean lightIsOn, stateSaved;
    private Camera camera;

    private static final String PREFERENCES_KEY = "SimpleLight";
    private static final String LIGHT_STATE_KEY = "lightIsOn";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.simplelight);
        FrameLayout layout = (FrameLayout) findViewById(R.id.lightToggle);
        layout.setOnTouchListener(this);
    }

    @Override
    protected void onDestroy() {
        saveLightState();
        camera.release();
        super.onDestroy();
    }

    @Override
    protected void onStart() {
        camera = Camera.open();
        restoreLightState();
        super.onStart();
    }

    @Override
    protected void onStop() {
        saveLightState();
        camera.release();
        super.onStop();
    }

    @Override
    protected void onPause() {
        saveLightState();
        super.onPause();
    }

    @Override
    protected void onResume() {
        restoreLightState();
        super.onResume();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (MotionEvent.ACTION_DOWN == event.getAction()) {
            if (lightIsOn) {
                turnOff();
            }
            else {
                turnOn();
            }
        }
        return true;
    }

    private void turnOn() {
        if (camera != null) {
            Parameters p = camera.getParameters();
            if (!Parameters.FLASH_MODE_TORCH.equals(p.getFlashMode())) {
                p.setFlashMode(Parameters.FLASH_MODE_TORCH);
                camera.setParameters(p);
                camera.startPreview();
            }
            lightIsOn = true;
        }
    }

    private void turnOff() {
        if (camera != null) {
            Parameters p = camera.getParameters();
            if (!Parameters.FLASH_MODE_OFF.equals(p.getFlashMode())) {
                p.setFlashMode(Parameters.FLASH_MODE_OFF);
                camera.setParameters(p);
                camera.stopPreview();
            }
            lightIsOn = false;
        }
    }

    private void restoreLightState() {
        SharedPreferences prefs = getSharedPreferences(PREFERENCES_KEY, MODE_PRIVATE);
        lightIsOn = prefs.getBoolean(LIGHT_STATE_KEY, false);
        if (lightIsOn) {
            turnOn();
        }
        stateSaved = false;
    }

    private void saveLightState() {
        if (!stateSaved) {
            SharedPreferences prefs = getSharedPreferences(PREFERENCES_KEY, MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean(LIGHT_STATE_KEY, lightIsOn);
            editor.commit();
            if (lightIsOn) {
                turnOff();
            }
            stateSaved = true;
        }
    }
}
