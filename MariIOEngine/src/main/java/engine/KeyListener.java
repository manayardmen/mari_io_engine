package engine;

import utils.DefaultConstants;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

public class KeyListener {
    private static KeyListener instance = null;

    private final boolean[] keyPressed = new boolean[DefaultConstants.KEYS_COUNT];

    private KeyListener() { }

    public static KeyListener getInstance() {
        if (instance == null)
            instance = new KeyListener();

        return instance;
    }

    public static void keyCallback(long window, int key, int scanCode, int action, int mods) {
        boolean[] pressedKeys = getInstance().keyPressed;
        if (action == GLFW_PRESS) {
            if (key < pressedKeys.length) {
                pressedKeys[key] = true;
            }
        } else if (action == GLFW_RELEASE && key < pressedKeys.length) {
            pressedKeys[key] = false;
        }
    }

    public static boolean isKeyPressed(int keyCode) {
        if (keyCode < 0)
            return false;

        boolean[] pressedKeys = getInstance().keyPressed;
        if (keyCode < pressedKeys.length)
            return pressedKeys[keyCode];

        return false;
    }
}
