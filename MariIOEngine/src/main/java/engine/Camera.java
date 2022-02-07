package engine;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class Camera {
    private Matrix4f projectionMatrix;
    private Matrix4f viewMatrix;

    private Vector2f position;

    public Camera(Vector2f position) {
        this.position = position;

        this.projectionMatrix = new Matrix4f();
        this.viewMatrix = new Matrix4f();

        adjustProjection();
    }

    public void adjustProjection() {
        // Reset
        this.projectionMatrix.identity();

        final float blocksAmountInCamVert = 40f;
        final float blocksAmountInCamHorz = 20f;
        final float blockSizeInCam = 32f;

        final float farDistance = 100f;

        // Set all sizes and distance
        // From left to right, from 0f to size*amount
        // From bottom to top, from 0f to size*amount

        projectionMatrix.ortho(
            0f,
            blockSizeInCam * blocksAmountInCamVert,
            0f,
            blockSizeInCam * blocksAmountInCamHorz,
            0f,
            farDistance);
    }

    public Matrix4f getViewMatrix() {
        // Camera looking at -1f on Z direction
        Vector3f cameraFront = new Vector3f(0f, 0f, -1f);

        Vector3f cameraUp = new Vector3f(0f, 1f, 0f);

        // Up to 20 items in Z-Coords
        final float magicZCamLocation = 20f;

        // Reset
        this.viewMatrix.identity();

        this.viewMatrix.lookAt(
            new Vector3f(this.position.x, this.position.y, magicZCamLocation),    // Current camera position
            cameraFront.add(this.position.x, this.position.y, 0f),             // Center of this here (center in front of camera)
            cameraUp);

        return this.viewMatrix;
    }

    public Matrix4f getProjectionMatrix() {
        return this.projectionMatrix;
    }

    public Vector2f getPosition() {
        return new Vector2f(this.position.x, this.position.y);
    }

    public void updatePositionX(float x) {
        this.position.x = x;
    }

    public void updatePositionY(float y) {
        this.position.y = y;
    }
}
