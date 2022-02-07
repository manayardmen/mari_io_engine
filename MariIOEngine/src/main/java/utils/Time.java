package utils;

public class Time {
    // Application startup time
    public static final float TIME_STARTED = System.nanoTime();

    // Elapsed time in seconds (from nanoseconds to seconds)
    public static float getTime() { return (float) ((System.nanoTime() - TIME_STARTED) * 1E-9); }
}
