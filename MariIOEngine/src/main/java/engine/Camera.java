package engine;

import org.joml.Matrix4f;
import org.joml.Vector2f;

public class Camera {
    private Matrix4f projectionMatrix;
    private Matrix4f viewMatrix;

    private Vector2f position;

    public Camera(Vector2f position) {
        this.position = position;

        this.projectionMatrix = new Matrix4f();
        this.viewMatrix = new Matrix4f();
    }

    public void adjustProjection() {
        // Reset
        projectionMatrix.identity();

        //TODO: projectionMatrix.ortho();
    }
}
