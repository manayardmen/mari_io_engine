package utils;

import utils.enums.EScene;
import utils.enums.ETargetPlatform;

public final class DefaultConstants {
    private DefaultConstants () { }

    public static final boolean IS_DEBUG = true;

    public static final EScene START_SCENE = EScene.LEVEL_EDITOR;

    public static final int WINDOW_WIDTH = 1920;
    public static final int WINDOW_HEIGHT = 1080;

    public static final int KEYS_COUNT = 350;

    public static final int MOUSE_BUTTONS_COUNT = 3;

    public static final float INITIAL_FRAME_DELTA_TIME = -1f;

    public static final String APP_TITLE = "MariIOEngine";

    public static final String TITLE_DIVIDER = " -> ";

    public static final float TIME_TO_CHANGE_SCENE = 2f;

    public static final String SHADERS_PATH = "assets/shaders/";

    public static final String SETTINGS_PATH = "";

    // Target platform selection
    public static final ETargetPlatform TARGET_PLATFORM = ETargetPlatform.Linux;
}
