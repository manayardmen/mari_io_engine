package game.utils;

import engine.Scene;
import game.LevelEditorScene;
import game.LevelScene;
import utils.enums.Scenes;

public class SceneLinking {
    private static SceneLinking instance = null;

    private SceneLinking() { }

    public static SceneLinking getInstance() {
        if (instance == null)
            instance = new SceneLinking();

        return instance;
    }

    public Scene getScene(Scenes sceneIndex) {
        switch (sceneIndex) {
            case LEVEL_EDITOR: return new LevelEditorScene();
            case LEVEL: return new LevelScene();

            default: return null;
        }
    }
}
