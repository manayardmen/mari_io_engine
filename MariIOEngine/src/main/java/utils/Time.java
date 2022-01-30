package utils;

public class Time {
    // Точное время старта приложения
    public static final float timeStarted = System.nanoTime();

    // Возвращаем пройденное время в секундах (из наносекунд в секунды)
    public static float getTime() { return (float) ((System.nanoTime() - timeStarted) * 1E-9); }
}
