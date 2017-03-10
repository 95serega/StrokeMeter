package com.example.serega.stroke;

/**
 * Created by Serega on 12.02.2017.
 */
public class StrokeResult {
    public final float maxRacingAccel;
    public final float maxBrakingAccel;
    public final float maxVelocity;
    public final float averageVelocity;
    public final int strokeDuration;
    public final int reactionTime;

    public StrokeResult(float maxRacingAccel,
                        float maxBrakingAccel,
                        float maxVelocity,
                        float averageVelocity,
                        int strokeDuration,
                        int reactionTime) {
        this.maxRacingAccel = maxRacingAccel;
        this.maxBrakingAccel = maxBrakingAccel;
        this.maxVelocity = maxVelocity;
        this.averageVelocity = averageVelocity;
        this.strokeDuration = strokeDuration;
        this.reactionTime = reactionTime;
    }
}
