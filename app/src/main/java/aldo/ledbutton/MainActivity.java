package aldo.ledbutton;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;

import com.google.android.things.contrib.driver.button.Button;
import com.google.android.things.contrib.driver.button.ButtonInputDriver;
import com.google.android.things.pio.Gpio;
import com.google.android.things.pio.PeripheralManagerService;

import java.io.IOException;

public class MainActivity extends Activity {
    private ButtonInputDriver mButtonInputDriver;
    private Gpio mLedGpio;
    private boolean mLedVal = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PeripheralManagerService pioService = new PeripheralManagerService();

        try {
            mLedGpio = pioService.openGpio("BCM6");
            mLedGpio.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW);
            mLedGpio.setValue(true);
        } catch (IOException e) {}

        try {
            mButtonInputDriver = new ButtonInputDriver(
                    "BCM21",
                    Button.LogicState.PRESSED_WHEN_LOW,
                    KeyEvent.KEYCODE_SPACE);
            mButtonInputDriver.register();
        } catch (IOException e) {}
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_SPACE) {
            try {
                if (mLedVal) {
                    mLedGpio.setValue(false);
                    mLedVal = false;
                } else {
                    mLedGpio.setValue(true);
                    mLedVal = true;
                }
            } catch (IOException e) {}
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }
}