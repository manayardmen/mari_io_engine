package engine;

import org.joml.Vector2f;

public abstract class Scene {
    protected Camera camera;

    protected Scene () {
        this.camera = new Camera(new Vector2f());
    }

    public abstract void update(float deltaTime);

    public abstract void init();

    public abstract void destroy();
}
