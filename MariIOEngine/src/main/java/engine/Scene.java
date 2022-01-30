package engine;

public abstract class Scene {
    protected Scene () { }

    public abstract void update(float deltaTime);

    public abstract void init();

    public abstract void destroy();
}
