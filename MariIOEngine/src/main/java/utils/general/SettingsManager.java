package utils.general;

public class SettingsManager {
    private static SettingsManager instance;

    private SettingsManager() { }

    private void tryReadSettings() { }

    public static SettingsManager getInstance() {
        if (SettingsManager.instance == null)
            SettingsManager.instance = new SettingsManager();

        return SettingsManager.instance;
    }
}
