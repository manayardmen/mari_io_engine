package engine;

import game.utils.GameConstants;
import game.utils.SceneLinking;
import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;
import utils.DefaultConstants;
import utils.Logger;
import utils.Time;
import utils.enums.Scenes;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class GLWindow {
    private static GLWindow instance = null;

    private Scene currentScene = null;

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
        this.width = DefaultConstants.WINDOW_WIDTH;
        this.height = DefaultConstants.WINDOW_HEIGHT;

        this.title = DefaultConstants.APP_TITLE + DefaultConstants.TITLE_DIVIDER + GameConstants.GAME_TITLE;

        this.logger = Logger.getInstance();

        this.r = 0f;
        this.g = 0f;
        this.b = 0f;
        this.a = 0f;

        this.sceneLinking = SceneLinking.getInstance();
    }

    public void changeScene(Scenes sceneIndex) {
        Scene targetScene = getInstance().sceneLinking.getScene(sceneIndex);
        if (targetScene == null)
            throw new IllegalStateException("Error occurred while changing scene, scene not found, scene index:" + sceneIndex);

        targetScene.init();

        if (this.currentScene != null)
            this.currentScene.destroy();

        this.currentScene = targetScene;
    }

    public static GLWindow getInstance() {
        if (GLWindow.instance == null)
            GLWindow.instance = new GLWindow();

        return GLWindow.instance;
    }

    public void run() {
        this.logger.write("LWJGL Version: " + Version.getVersion());

        this.init();
        this.loop();

        if (this.currentScene != null)
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

    private void init() {
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
        this.glfwWindowId = glfwCreateWindow(this.width, this.height, this.title, NULL, NULL);
        if (this.glfwWindowId == NULL)
            throw new IllegalStateException("Error occurred while creating GL Window");

        // Setup all handlers
        glfwSetCursorPosCallback(this.glfwWindowId, MouseListener::mousePosCallback);
        glfwSetMouseButtonCallback(this.glfwWindowId, MouseListener::mouseButtonCallback);
        glfwSetScrollCallback(this.glfwWindowId, MouseListener::mouseScrollCallback);
        glfwSetKeyCallback(this.glfwWindowId, KeyListener::keyCallback);
        // TODO: Create Joystick or Gamepad callback

        // OpenGL context
        glfwMakeContextCurrent(this.glfwWindowId);

        // Enable V-Sync (lock FPS to screen update frequency)
        glfwSwapInterval(1);

        // Show the window
        glfwShowWindow(this.glfwWindowId);

        // Important for OpenGL with GLFW context
        // Binds for OpenGL (on C-Language)
        GL.createCapabilities();

        // TODO: Move this logic to game instance <- get start scene
        // Move to main scene
        this.changeScene(DefaultConstants.START_SCENE);
    }

    private void loop() {
        float beginTime = Time.getTime();
        float endTime;
        float deltaTime = DefaultConstants.INITIAL_FRAME_DELTA_TIME;

        while (!glfwWindowShouldClose(glfwWindowId)) {
            // Throw any events to our handlers
            glfwPollEvents();

            glClearColor(this.r, this.g, this.b, this.a);
            glClear(GL_COLOR_BUFFER_BIT);

            // Updating current scene
            if (deltaTime >= 0 && this.currentScene != null)
                this.currentScene.update(deltaTime);

            glfwSwapBuffers(glfwWindowId);

            endTime = Time.getTime();
            deltaTime = endTime - beginTime;
            beginTime = endTime;
        }
    }
}
