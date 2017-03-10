package com.example.serega.stroke;

/**
 * Created by Serega on 07.02.2017.
 */
public class Algorithmes {
    private static final float MIN_STROKE_ACCEL = 10.0f;
    private static final float START_STROKE_ACCEL = 1.0f;
    public static final float SEC_IN_NANO = 1000000000f;

    private Algorithmes() {
    }

    public static float[] fixedVelocity(float[] x_accel, float[] y_accel, float[] z_ang_vel,
                                        long[] timestamps) {
        int size = x_accel.length;
        float[] velocity = new float[size];
        velocity[0] = 0;
        final float koeff=2.97f;
        for (int i = 1; i < size; i++) {
            double dt = koeff*(timestamps[i] - timestamps[i - 1]) / SEC_IN_NANO;
            double a = 1.0 + dt * z_ang_vel[i] * dt * z_ang_vel[i];
            double b = x_accel[i] * x_accel[i] +
                    y_accel[i] * y_accel[i];
            double c = velocity[i - 1] * z_ang_vel[i];
            double d = b * a - c * c;
            if (d < 0) {
                d = 0;
            }
            dt = signum(x_accel[i]) * dt;
            velocity[i] = (float) ((velocity[i - 1] + dt * Math.sqrt(d)) / a);
        }
        return velocity;
    }

    public static int indexStrokeStart(float[] acceleration) {

        boolean find = false;
        int i_stroke_start = 0;
        int max_index = acceleration.length;

        int i = 1;
        while (!find && i < max_index) {
            if (Math.abs(acceleration[i]) > MIN_STROKE_ACCEL) {
                i_stroke_start = i;
                find = true;
            }
            i++;
        }
        if (i == max_index) return -1;

        find = false;
        while (!find && i >= 0) {
            if (Math.abs(acceleration[i]) < START_STROKE_ACCEL) {
                i_stroke_start = i;
                find = true;
            }
            i--;
        }

        return i_stroke_start;
    }

    public static int indexMaxRacingAcceleration(float[] acceleration, int start_index) {

        boolean find = false;
        int i_max_accel = start_index;
        int i = start_index;
        int max_index = acceleration.length;
        float max_accel = 0;

        while (!find && i < max_index-1) {
            if (Math.abs(acceleration[i]) > max_accel) {
                max_accel = Math.abs(acceleration[i]);
                i_max_accel = i;
            }
            if (signum(acceleration[i]) != signum(acceleration[i + 1])
                    && max_accel>MIN_STROKE_ACCEL) {
                find = true;
            }
            i++;
        }
        return i_max_accel;
    }

    public static int indexMaxBreakingAcceleration(float[] acceleration, int start_index) {

        boolean sign_change = false;
        int i_max_accel = start_index;
        int max_index = acceleration.length;
        double max_accel = 0;

        int i = start_index;
        while (!sign_change) {
            if (signum(acceleration[i]) != signum(acceleration[i + 1])) {
                sign_change = true;
            }
            i++;
        }
        sign_change = false;
        while (!sign_change && i < max_index-1) {
            if (Math.abs(acceleration[i]) > max_accel) {
                max_accel = Math.abs(acceleration[i]);
                i_max_accel = i;
            }
            if (signum(acceleration[i]) != signum(acceleration[i + 1])
                    && max_accel>MIN_STROKE_ACCEL) {
                sign_change = true;
            }
            i++;
        }
        return i_max_accel;
    }

    public static int indexStrokeEnd(float[] acceleration, int start_index) {

        boolean sign_change = false;
        int i_stroke_end = start_index;
        int max_index = acceleration.length;

        int i = start_index;
        while (!sign_change && i < max_index - 1) {
            if (signum(acceleration[i]) != signum(acceleration[i + 1])) {
                sign_change = true;
                i_stroke_end = i;
            }
            i++;
        }

        return (i_stroke_end + start_index) / 2;
    }

    public static int signum(float x) {
        if (x < 0) return -1;
        if (x > 0) return 1;
        return 0;
    }

    public static float maximum(float[] arr, int start, int end) {
        float max = 0;
        for (int i = start; i < end; i++) {
            if (Math.abs(arr[i]) > max) {
                max = Math.abs(arr[i]);
            }
        }
        return max;
    }

    public static float average(float[] arr, int start, int end) {
        float average = 0;
        for (int i = start; i < end; i++) {
            average += Math.abs(arr[i]);
        }
        return average / (end-start-1);
    }
}