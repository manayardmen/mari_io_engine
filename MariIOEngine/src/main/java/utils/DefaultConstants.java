package utils;

import utils.enums.Scenes;
import utils.enums.TargetPlatforms;

public final class DefaultConstants {
    private DefaultConstants () { }

    public static final boolean IS_DEBUG = true;

    public static final Scenes START_SCENE = Scenes.LEVEL_EDITOR;

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

    public static final String PROJECTION_VAR_NAME = "uProjection";
    public static final String VIEW_VAR_NAME = "uView";

    // Target platform selection
    public static final TargetPlatforms TARGET_PLATFORM = TargetPlatforms.Windows;
}
