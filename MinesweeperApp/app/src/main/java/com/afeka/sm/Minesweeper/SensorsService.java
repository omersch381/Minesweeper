package com.afeka.sm.Minesweeper;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Binder;
import android.os.IBinder;

import java.util.Timer;
import java.util.TimerTask;

interface SensorServiceListener {

    enum ALARM_STATE {
        NOT_ON_POSITION, ON_POSITION
    }

    void alarmStateChanged(ALARM_STATE state, int timeSinceLastPositionChanged);
}

public class SensorsService extends Service implements SensorEventListener, Finals {

    private SensorServiceListener.ALARM_STATE currentAlarmState = SensorServiceListener.ALARM_STATE.ON_POSITION;
    private final IBinder mBinder = new SensorServiceBinder();
    private float initialX;
    private float initialY;
    private float initialZ;
    SensorServiceListener mListener;
    SensorManager mSensorManager;
    Sensor mAccel;
    boolean isFirstEvent;
    int timeSinceLastPositionChanged;
    MineSweeperTimerTaskExtended myTimerTask;

    public class SensorServiceBinder extends Binder {

        void registerListener(SensorServiceListener listener) {
            mListener = listener;
        }

        void unregisterListener(SensorServiceListener listener) {
            mListener = null;
        }

        void startSensors(boolean isFirstTImeFromApp) {
            isFirstEvent = isFirstTImeFromApp;
            mSensorManager.registerListener(SensorsService.this, mAccel, SensorManager.SENSOR_DELAY_NORMAL);
        }

        void stopSensors() {
            mSensorManager.unregisterListener(SensorsService.this);
        }
    }

    public SensorsService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if (mSensorManager == null)
            System.exit(1);

        mAccel = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if (mAccel == null)
            System.exit(1);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        setInitialPosition(event);

        double absDiffX = Math.abs(initialX - event.values[0]);
        double absDiffY = Math.abs(initialY - event.values[1]);
        double absDiffZ = Math.abs(initialZ - event.values[2]);

        SensorServiceListener.ALARM_STATE previousState = currentAlarmState;
        if (absDiffX > SENSORS_THRESHOLD || absDiffY > SENSORS_THRESHOLD || absDiffZ > SENSORS_THRESHOLD)
            this.currentAlarmState = SensorServiceListener.ALARM_STATE.NOT_ON_POSITION;
        else
            this.currentAlarmState = SensorServiceListener.ALARM_STATE.ON_POSITION;

        if (previousState != currentAlarmState)
            handleTimer(currentAlarmState);
    }

    private void handleTimer(SensorServiceListener.ALARM_STATE currentAlarmState) {
        if (currentAlarmState == SensorServiceListener.ALARM_STATE.NOT_ON_POSITION)
            runTimer();
    }

    private void runTimer() {
        Timer timer = new Timer();
        if (myTimerTask != null)
            myTimerTask.cancel();
        myTimerTask = new MineSweeperTimerTaskExtended();
        timer.schedule(myTimerTask, 0, 1000);
    }

    class MineSweeperTimerTaskExtended extends TimerTask {
        // It's not that similar to the other class, hence we don't extend it, and we prefer to extend TimerTask
        private long firstClickTime = System.currentTimeMillis();

        @Override
        public void run() {
            new Thread(new Runnable() {

                @Override
                public void run() {
                    timeSinceLastPositionChanged = (int) ((System.currentTimeMillis() - firstClickTime) / 1000);
                    mListener.alarmStateChanged(currentAlarmState, timeSinceLastPositionChanged);
                    if (timeSinceLastPositionChanged >= INSERT_A_MINE_THRESHOLD)
                        timeSinceLastPositionChanged %= INSERT_A_MINE_THRESHOLD;
                    if (currentAlarmState == SensorServiceListener.ALARM_STATE.ON_POSITION)
                        myTimerTask.cancel();
                }
            }).start();
        }
    }

    private void setInitialPosition(SensorEvent event) {
        if (isFirstEvent) {
            initialX = event.values[0];
            initialY = event.values[1];
            initialZ = event.values[2];
            isFirstEvent = false;
        }
    }
}
