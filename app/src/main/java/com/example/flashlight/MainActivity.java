package com.example.flashlight; // നിങ്ങളുടെ പാക്കേജ് നാമം ഇവിടെ നൽകുക

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private ImageButton flashButton;
    private TextView statusText;
    private CameraManager cameraManager;
    private String cameraId;
    private boolean isFlashOn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        flashButton = findViewById(R.id.flashButton);
        statusText = findViewById(R.id.statusText);

        cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);

        try {
            cameraId = cameraManager.getCameraIdList()[0];
        } catch (CameraAccessException e) {
            e.printStackTrace();
            Toast.makeText(this, "Camera Access Error!", Toast.LENGTH_SHORT).show();
            return;
        }

        flashButton.setOnClickListener(v -> {
            if (isFlashOn) {
                turnOffFlash();
            } else {
                turnOnFlash();
            }
        });
        
        // ഫ്ലാഷ് സപ്പോർട്ട് ഉണ്ടോ എന്ന് പരിശോധിക്കുന്നു
        if (!getPackageManager().hasSystemFeature("android.hardware.camera.flash")) {
            Toast.makeText(this, "Flashlight is not available on this device.", Toast.LENGTH_LONG).show();
            flashButton.setEnabled(false);
            statusText.setText("Device lacks flashlight.");
        }
    }

    private void turnOnFlash() {
        try {
            cameraManager.setTorchMode(cameraId, true);
            isFlashOn = true;
            // ബട്ടൺ ഐക്കൺ ഓൺ സ്റ്റേറ്റിലേക്ക് മാറ്റുന്നു.
            flashButton.setImageResource(android.R.drawable.ic_lock_power_on); 
            statusText.setText("Flashlight is ON");
        } catch (CameraAccessException e) {
            e.printStackTrace();
            Toast.makeText(this, "Failed to turn ON flash.", Toast.LENGTH_SHORT).show();
        }
    }

    private void turnOffFlash() {
        try {
            cameraManager.setTorchMode(cameraId, false);
            isFlashOn = false;
            // ബട്ടൺ ഐക്കൺ ഓഫ് സ്റ്റേറ്റിലേക്ക് മാറ്റുന്നു.
            flashButton.setImageResource(android.R.drawable.ic_lock_power_off);
            statusText.setText("Flashlight is OFF");
        } catch (CameraAccessException e) {
            e.printStackTrace();
            Toast.makeText(this, "Failed to turn OFF flash.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        // ആപ്പ് നിർത്തുമ്പോൾ ഫ്ലാഷ് ഓഫ് ചെയ്യുന്നു
        if (isFlashOn) {
            turnOffFlash();
        }
    }
}
