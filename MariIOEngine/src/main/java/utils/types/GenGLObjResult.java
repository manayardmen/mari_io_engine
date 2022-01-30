package utils.types;

public class GenGLObjResult {
    public int vaoId;
    public int vboId;
    public int eboId;

    public GenGLObjResult() { }

    public GenGLObjResult (int vaoId, int vboId, int eboId) {
        this.vaoId = vaoId;
        this.vboId = vboId;
        this.eboId = eboId;
    }
}
