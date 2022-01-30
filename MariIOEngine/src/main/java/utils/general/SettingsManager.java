package utils.general;

public class SettingsManager {
    private static SettingsManager instance;

    private SettingsManager() {

    }

    private void tryReadSettings() {

    }

    public static SettingsManager getInstance() {
        if (instance == null)
            instance = new SettingsManager();

        return instance;
    }
}
