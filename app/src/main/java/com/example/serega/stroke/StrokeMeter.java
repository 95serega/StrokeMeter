package com.example.serega.stroke;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

/**
 * Created by Serega on 06.02.2017.
 */
public class StrokeMeter implements SensorEventListener {

    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private Sensor mGyroscope;

    private float[] accelerationX;
    private float[] accelerationY;
    private float[] angularVelocityZ;
    private long[] timeStamps;
    private boolean strokeIsGoing;
    private StrokeListener strokeListener;

    public static final int MAX_LIST_SIZE = 200;


    public StrokeMeter(Context context) {
        mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        mGyroscope = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
    }

    private int countAccel;
    private int countGyro;

    public void prepare() {

        countAccel = 0;
        countGyro = 0;
        accelerationX = new float[MAX_LIST_SIZE];
        accelerationY = new float[MAX_LIST_SIZE];
        angularVelocityZ = new float[MAX_LIST_SIZE];
        timeStamps = new long[MAX_LIST_SIZE];

        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_FASTEST);
        mSensorManager.registerListener(this, mGyroscope, SensorManager.SENSOR_DELAY_FASTEST);
    }

    public void start() {
        strokeIsGoing = true;
    }

    private void end() {
        strokeIsGoing = false;
        mSensorManager.unregisterListener(this);
        strokeListener.Stroke(getResult());
    }

    public void setStrokeListener(StrokeListener strokeListener){
        this.strokeListener=strokeListener;
    }

    private StrokeResult getResult() {
        StrokeCalculator calculator = new StrokeCalculator(
                accelerationX, accelerationY, angularVelocityZ, timeStamps);
        return calculator.getResult();
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (strokeIsGoing) {

            if (sensorEvent.sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION) {
                if (countAccel < MAX_LIST_SIZE) {
                    accelerationX[countAccel] = sensorEvent.values[0];
                    accelerationY[countAccel] = sensorEvent.values[1];

                    timeStamps[countAccel] = sensorEvent.timestamp;
                }
                countAccel++;

            } else if (sensorEvent.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
                if (countGyro < MAX_LIST_SIZE) {

                    angularVelocityZ[countGyro] = sensorEvent.values[2];

                }
                countGyro++;
            }
            if (countGyro == MAX_LIST_SIZE && countAccel == MAX_LIST_SIZE) {
                end();
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
    }
}