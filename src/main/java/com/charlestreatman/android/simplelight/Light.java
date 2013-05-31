package com.charlestreatman.android.simplelight;

import android.hardware.Camera;
import android.hardware.Camera.Parameters;

class Light {
    private Camera camera;
    private boolean isOn;

    public void startUp() {
        camera = Camera.open();
    }

    public void shutDown() {
        if (camera != null) {
            camera.release();
        }
    }

    public void turnOn() {
        if (camera != null) {
            Parameters p = camera.getParameters();
            if (!Parameters.FLASH_MODE_TORCH.equals(p.getFlashMode())) {
                p.setFlashMode(Parameters.FLASH_MODE_TORCH);
                camera.setParameters(p);
                camera.startPreview();
            }
        }
        isOn = true;
    }

    public void turnOff() {
        if (camera != null) {
            Parameters p = camera.getParameters();
            if (!Parameters.FLASH_MODE_OFF.equals(p.getFlashMode())) {
                p.setFlashMode(Parameters.FLASH_MODE_OFF);
                camera.setParameters(p);
                camera.stopPreview();
            }
        }
        isOn = false;
    }

    public boolean isOn() {
        return isOn;
    }
}
