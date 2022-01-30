package engine;

import game.utils.GameConstants;
import game.utils.SceneLinking;
import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;
import utils.DefaultConstants;
import utils.Logger;
import utils.Time;
import utils.enums.EScene;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class GLWindow {
    private static GLWindow instance = null;

    private static Scene currentScene = null;

    private final int width;
    private final int height;
    private final String title;

    private final Logger logger;

    private long glfwWindowId;

    private float r;
    private float g;
    private float b;
    private float a;

    private final SceneLinking sceneLinking;

    private GLWindow() {
        width = DefaultConstants.WINDOW_WIDTH;
        height = DefaultConstants.WINDOW_HEIGHT;

        title = DefaultConstants.APP_TITLE + DefaultConstants.TITLE_DIVIDER + GameConstants.GAME_TITLE;

        logger = Logger.getInstance();

        r = 0f;
        g = 0f;
        b = 0f;
        a = 0f;

        sceneLinking = SceneLinking.getInstance();
    }

    public static void changeScene(EScene sceneIndex) {
        Scene targetScene = getInstance().sceneLinking.getScene(sceneIndex);
        if (targetScene == null)
            throw new IllegalStateException("Error occurred while changing scene, scene not found, scene index:" + sceneIndex);

        targetScene.init();

        if (currentScene != null)
            currentScene.destroy();

        currentScene = targetScene;
    }

    public static GLWindow getInstance() {
        if (instance == null)
            instance = new GLWindow();

        return instance;
    }

    public void run() {
        logger.write("LWJGL Version: " + Version.getVersion());

        init();
        loop();

        if (currentScene != null)
            currentScene.destroy();

        currentScene = null;

        // Free all handlers from window
        glfwFreeCallbacks(glfwWindowId);
        // Destroy our window
        glfwDestroyWindow(glfwWindowId);
        glfwTerminate();
        // Remove our errors handler
        GLFWErrorCallback ec = glfwSetErrorCallback(null);
        if (ec !=  null)
            ec.free();
    }

    public void init() {
        // Subscribe to handle any errors and print it
        GLFWErrorCallback.createPrint(System.err).set();

        // Init GLFW and check
        if (!glfwInit())
            throw new IllegalStateException("Failed to initialize GLFW");

        // Set up settings for GLFW
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        glfwWindowHint(GLFW_MAXIMIZED, GLFW_TRUE);

        // Create our window
        glfwWindowId = glfwCreateWindow(this.width, this.height, this.title, NULL, NULL);
        if (glfwWindowId == NULL)
            throw new IllegalStateException("Error occurred while creating GL Window");

        // Setup all handlers
        glfwSetCursorPosCallback(glfwWindowId, MouseListener::mousePosCallback);
        glfwSetMouseButtonCallback(glfwWindowId, MouseListener::mouseButtonCallback);
        glfwSetScrollCallback(glfwWindowId, MouseListener::mouseScrollCallback);
        glfwSetKeyCallback(glfwWindowId, KeyListener::keyCallback);
        // TODO: Create Joystick or Gamepad callback

        // OpenGL context
        glfwMakeContextCurrent(glfwWindowId);

        // Enable V-Sync (lock FPS to screen update frequency)
        glfwSwapInterval(1);

        // Show the window
        glfwShowWindow(glfwWindowId);

        // Important for OpenGL with GLFW context
        // Binds for OpenGL (on C-Language)
        GL.createCapabilities();

        // TODO: Move this logic to game instance
        // Move to main scene
        changeScene(DefaultConstants.START_SCENE);
    }

    public void loop() {
        float beginTime = Time.getTime();
        float endTime;
        float deltaTime = DefaultConstants.INITIAL_FRAME_DELTA_TIME;

        while (!glfwWindowShouldClose(glfwWindowId)) {
            // Throw any events to our handlers
            glfwPollEvents();

            glClearColor(r, g, b, a);
            glClear(GL_COLOR_BUFFER_BIT);

            // Updating current scene
            if (deltaTime >= 0 && currentScene != null)
                currentScene.update(deltaTime);

            glfwSwapBuffers(glfwWindowId);

            endTime = Time.getTime();
            deltaTime = endTime - beginTime;
            beginTime = endTime;
        }
    }
}
