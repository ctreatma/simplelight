package com.charlestreatman.android.simplelight;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Activity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

public class MainActivity extends Activity implements View.OnTouchListener {
    private Light light;

    private static final String PREFERENCES_KEY = "SimpleLight";
    private static final String LIGHT_STATE_KEY = "lightIsOn";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.simplelight);
        FrameLayout layout = (FrameLayout) findViewById(R.id.lightToggle);
        layout.setOnTouchListener(this);
        light = new Light();
    }

    @Override
    protected void onPause() {
        saveLightState();
        turnLightOff();
        light.shutDown();
        super.onPause();
    }

    @Override
    protected void onResume() {
        light.startUp();
        restoreLightState();
        super.onResume();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (MotionEvent.ACTION_DOWN == event.getAction()) {
            switchLight(!light.isOn());
        }
        return true;
    }

    private void switchLight(boolean on) {
        if (on) {
            turnLightOn();
        }
        else {
            turnLightOff();
        }
    }

    private void turnLightOn() {
        light.turnOn();
        TextView instructions = (TextView) findViewById(R.id.instructions);
        instructions.setText("Tap to turn off");
    }

    private void turnLightOff() {
        light.turnOff();
        TextView instructions = (TextView) findViewById(R.id.instructions);
        instructions.setText("Tap to turn on");
    }

    private void restoreLightState() {
        SharedPreferences prefs = getSharedPreferences(PREFERENCES_KEY, MODE_PRIVATE);
        boolean lightWasOn = prefs.getBoolean(LIGHT_STATE_KEY, false);
        switchLight(lightWasOn);
    }

    private void saveLightState() {
        SharedPreferences prefs = getSharedPreferences(PREFERENCES_KEY, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(LIGHT_STATE_KEY, light.isOn());
        editor.commit();
    }
}
