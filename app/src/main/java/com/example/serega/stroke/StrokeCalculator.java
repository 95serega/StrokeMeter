package com.example.serega.stroke;

/**
 * Created by Serega on 01.03.2017.
 */
public class StrokeCalculator {
    private float[] accelerationX;
    private float[] accelerationY;
    private float[] velocity;
    private float[] angularVelocityZ;
    private long[] timeStamps;

    private int startIndex;
    private int endIndex;
    private int maxRacingAccelIndex;
    private int maxBrakingAccelIndex;

    private float maxRacingAccel;
    private float maxBrakingAccel;
    private float maxVelocity;
    private float averageVelocity;

    private int strokeDuration;
    private int reactionTime;

    private long NANO_IN_MILLI = 1000000;

    private boolean strokeWas;

    public StrokeCalculator(float[] accelX, float[] accelY, float[] angVelocZ, long[] timeSt) {
        accelerationX = accelX;
        accelerationY = accelY;
        angularVelocityZ = angVelocZ;
        timeStamps = timeSt;
        defineAll();
    }

    private void defineStart() {
        startIndex = Algorithmes.indexStrokeStart(accelerationX);
        if (startIndex == -1) {
            strokeWas = false;
        } else {
            strokeWas = true;

        }
    }

    private void defineMaxRacingAccel() {
        maxRacingAccelIndex = Algorithmes.indexMaxRacingAcceleration(accelerationX, startIndex);
        maxRacingAccel = Math.abs(accelerationX[maxRacingAccelIndex]);
    }

    private void defineMaxBrakingAccel() {
        maxBrakingAccelIndex = Algorithmes.indexMaxBreakingAcceleration(accelerationX, maxRacingAccelIndex);
        maxBrakingAccel = Math.abs(accelerationX[maxBrakingAccelIndex]);
    }

    private void defineEnd() {
        endIndex = Algorithmes.indexStrokeEnd(accelerationX, maxBrakingAccelIndex);
    }

    private void defineStrokeDuration() {
        strokeDuration = (int) ((timeStamps[endIndex] - timeStamps[startIndex]) / NANO_IN_MILLI);
    }

    private void defineReactionTime() {
        reactionTime = (int) ((timeStamps[startIndex] - timeStamps[0]) / NANO_IN_MILLI);
    }

    private void defineVelocity() {
         velocity = Algorithmes.fixedVelocity(accelerationX, accelerationY, angularVelocityZ,
             timeStamps);
    }

    private void defineMaxVelocity() {
        maxVelocity = Algorithmes.maximum(velocity,startIndex,endIndex);
    }

    private void defineAverageVelocity() {

        averageVelocity = Algorithmes.average(velocity, startIndex, endIndex);
    }

    private void defineAll() {
        defineStart();
        if (strokeWas) {
            defineMaxRacingAccel();
            defineMaxBrakingAccel();
            defineEnd();
            defineVelocity();
            defineMaxVelocity();
            defineAverageVelocity();
            defineReactionTime();
            defineStrokeDuration();
        } else {
            defineUnsuccess();
        }
    }

    private void defineUnsuccess() {
        maxBrakingAccel = 0;
        maxRacingAccel = 0;
        maxVelocity = 0;
        averageVelocity = 0;
        strokeDuration = 0;
        reactionTime = 0;
        defineVelocity();

    }

    public StrokeResult getResult() {
        StrokeResult result = new StrokeResult(maxRacingAccel,
                maxBrakingAccel,
                maxVelocity,
                averageVelocity,
                strokeDuration,
                reactionTime);
        return result;
    }
}
