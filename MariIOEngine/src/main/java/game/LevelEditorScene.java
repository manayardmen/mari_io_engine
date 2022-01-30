package game;

import engine.Scene;
import game.utils.GameUtils;
import renderer.MIOShader;
import utils.DefaultConstants;
import utils.Logger;
import utils.general.TempShaderUtils;
import utils.types.GenGLObjResult;

public class LevelEditorScene extends Scene {
    private final Logger logger;
    private final GameUtils gameUtils;
    private final TempShaderUtils shaderUtils;

    private int vaoId;
    private int vboId;
    private int eboId;

    private MIOShader defaultShader;

    private final float[] vertexArray = {
        // positions                // colors                                   indexes
        0.5f, -0.5f, 0f,            1f, 0f, 0f, 1f, // Bottom right part        0
        -0.5f, 0.5f, 0f,            0f, 1f, 0f, 1f, // Top left part            1
        0.5f, 0.5f, 0f,             1f, 0f, 1f, 1f, // Top right part           2
        -0.5f, -0.5f, 0f,           1f, 1f, 0f, 1f  // Bottom left part         3
    };

    // In counterclockwise order
    private final int[] elementArray = {
        2, 1, 0,    // Top left triangle
        0, 1, 3     // Bottom right triangle
    };

    public LevelEditorScene() {
        logger = Logger.getInstance();
        gameUtils = GameUtils.getInstance();
        shaderUtils = TempShaderUtils.getInstance();
    }

    @Override
    public void init() {
        defaultShader = new MIOShader(DefaultConstants.SHADERS_PATH + "default.glsl");
        defaultShader.compile();

        // Generate VAO, VBO, EBO objects
        int positionsSize = 3;
        int colorSize = 4;
        int floatSizeBytes = 4;

        GenGLObjResult res = shaderUtils.genGLObjects(vertexArray, elementArray, positionsSize, colorSize, floatSizeBytes);
        vaoId = res.vaoId;
        vboId = res.vboId;
        eboId = res.eboId;
    }

    @Override
    public void update(float deltaTime) {
        shaderUtils.glObjectsUpdate(defaultShader, vaoId, elementArray);
    }

    @Override
    public void destroy() {
    }
}
