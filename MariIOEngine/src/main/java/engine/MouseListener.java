package engine;

import utils.DefaultConstants;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

public class MouseListener {
    private static MouseListener instance = null;

    private double scrollX;
    private double scrollY;

    private double xPos;
    private double yPos;

    private double lastX;
    private double lastY;

    private final boolean[] mouseButtonPressed = new boolean[DefaultConstants.MOUSE_BUTTONS_COUNT];
    private boolean isDragging;

    private MouseListener() {
        this.scrollX = 0.0;
        this.scrollY = 0.0;

        this.lastX = 0.0;
        this.lastY = 0.0;

        this.xPos = 0.0;
        this.yPos = 0.0;

        this.isDragging = false;
    }

    private static boolean isAnyButtonPressed() {
        for (boolean pressedMouseButton : getInstance().mouseButtonPressed)
            if (pressedMouseButton)
                return true;

        return false;
    }

    public static MouseListener getInstance() {
        if (MouseListener.instance == null)
            MouseListener.instance = new MouseListener();

        return MouseListener.instance;
    }

    public static void mousePosCallback(long window, double xPos, double yPos) {
        getInstance().lastX = getInstance().xPos;
        getInstance().lastY = getInstance().yPos;

        getInstance().xPos = xPos;
        getInstance().yPos = yPos;

        getInstance().isDragging = isAnyButtonPressed();
    }

    public static void mouseButtonCallback(long window, int button, int action, int mods) {
        boolean[] pressedMouseButtons = getInstance().mouseButtonPressed;
        if (action == GLFW_PRESS) {
            if (button < pressedMouseButtons.length) {
                pressedMouseButtons[button] = true;
            }
        } else if (action == GLFW_RELEASE && button < pressedMouseButtons.length) {
            pressedMouseButtons[button] = false;
            getInstance().isDragging = false;
        }
    }

    public static void mouseScrollCallback(long window, double xOffset, double yOffset) {
        getInstance().scrollX = xOffset;
        getInstance().scrollY = yOffset;
    }

    public static void endFrame() {
        getInstance().scrollX = 0.0;
        getInstance().scrollY = 0.0;

        getInstance().lastX = getInstance().xPos;
        getInstance().lastY = getInstance().yPos;
    }

    public static float getX() {
        return (float) getInstance().xPos;
    }

    public static float getY() {
        return (float) getInstance().yPos;
    }

    public static float getDx() {
        return (float) (getInstance().lastX - getInstance().xPos);
    }

    public static float getDy() {
        return (float) (getInstance().lastY - getInstance().yPos);
    }

    public static float getScrollX() {
        return (float) getInstance().scrollX;
    }

    public static float getScrollY() {
        return (float) getInstance().scrollY;
    }

    public static boolean isDragging() {
        return getInstance().isDragging;
    }

    public static boolean isMouseButtonDown(int button) {
        if (button < 0)
            return false;

        boolean[] pressedMouseButtons = getInstance().mouseButtonPressed;
        if (button < pressedMouseButtons.length)
            return pressedMouseButtons[button];

        return false;
    }
}
