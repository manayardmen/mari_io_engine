package game.utils;

public class GameUtils {
    private static GameUtils instance = null;

    private GameUtils() { }

    public static GameUtils getInstance() {
        if (instance == null)
            instance = new GameUtils();

        return instance;
    }

    public String getFPSString(float deltaTime) {
        return "FPS:" + (1f / deltaTime);
    }
}
