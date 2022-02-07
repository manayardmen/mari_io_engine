package utils.general;

import engine.Camera;
import org.lwjgl.BufferUtils;
import renderer.MIOShader;
import utils.DefaultConstants;
import utils.Logger;
import utils.types.GenGLObjResult;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class TempShaderUtils {
    private static TempShaderUtils instance = null;

    private final Logger logger;

    private TempShaderUtils() {
        this.logger = Logger.getInstance();
    }

    public static TempShaderUtils getInstance() {
        if (TempShaderUtils.instance == null)
            TempShaderUtils.instance = new TempShaderUtils();

        return TempShaderUtils.instance;
    }

    public int loadShader(String shaderSrc, int shaderType) {
        // Load and compile shader
        int shaderId = glCreateShader(shaderType);
        // Next, set shader source code
        glShaderSource(shaderId, shaderSrc);
        glCompileShader(shaderId);

        // Check for compilation errors
        int success = glGetShaderi(shaderId, GL_COMPILE_STATUS);
        if (success == GL_FALSE) {
            int len = glGetShaderi(shaderId, GL_INFO_LOG_LENGTH);
            this.logger.write("Error occurred during shader compilation process:" + shaderType);
            this.logger.write(glGetShaderInfoLog(shaderId, len));
            throw new IllegalStateException("Error occurred during shader compilation process:" + shaderType);
        }

        return shaderId;
    }

    public int linkShaders(int[] shaderIds) {
        // Linking and check for errors
        int shaderProgram = glCreateProgram();

        for (int shadersId : shaderIds)
            glAttachShader(shaderProgram, shadersId);

        glLinkProgram(shaderProgram);

        // Search for linking errors
        int success = glGetProgrami(shaderProgram, GL_LINK_STATUS);
        if (success == GL_FALSE) {
            int len = glGetProgrami(shaderProgram, GL_INFO_LOG_LENGTH);
            this.logger.write("Error occurred during shaders link process");
            this.logger.write(glGetProgramInfoLog(shaderProgram, len));
            throw new IllegalStateException("Error occurred during shaders link process");
        }

        return shaderProgram;
    }

    public GenGLObjResult genGLObjects(
            float[] vertexArray,
            int[] elementArray,
            int positionsSize,
            int colorSize,
            int floatSizeBytes) {

        // Generate VAO, VBO, EBO objects
        int vaoId = glGenVertexArrays();
        glBindVertexArray(vaoId);

        // Create buffer of vertices
        FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(vertexArray.length);
        vertexBuffer.put(vertexArray).flip();

        // Creating VBO and loading vertex buffer
        int vboId = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboId);
        glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW);

        IntBuffer elementBuffer = BufferUtils.createIntBuffer(elementArray.length);
        elementBuffer.put(elementArray).flip();

        int eboId = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboId);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, elementBuffer, GL_STATIC_DRAW);

        int vertexSizeBytes = (positionsSize + colorSize) * floatSizeBytes;
        glVertexAttribPointer(0, positionsSize, GL_FLOAT, false, vertexSizeBytes, 0);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1, colorSize, GL_FLOAT, false, vertexSizeBytes, positionsSize * floatSizeBytes);
        glEnableVertexAttribArray(1);

        return new GenGLObjResult(vaoId, vboId, eboId);
    }

    public void glObjectsUpdate(int shaderProgram, int vaoId, int[] elementArray) {
        glUseProgram(shaderProgram);
        glBindVertexArray(vaoId);

        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        glDrawElements(GL_TRIANGLES, elementArray.length, GL_UNSIGNED_INT, 0);

        // Disable everything
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);

        glBindVertexArray(0);
        glUseProgram(0);
    }

    public void glObjectsUpdate(MIOShader shader, int vaoId, int[] elementArray, Camera camera) {
        shader.use();
        shader.uploadMat4f(DefaultConstants.PROJECTION_VAR_NAME, camera.getProjectionMatrix());
        shader.uploadMat4f(DefaultConstants.VIEW_VAR_NAME, camera.getViewMatrix());
        glBindVertexArray(vaoId);

        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        glDrawElements(GL_TRIANGLES, elementArray.length, GL_UNSIGNED_INT, 0);

        // Disable everything
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);

        glBindVertexArray(0);
        shader.detach();
    }
}
