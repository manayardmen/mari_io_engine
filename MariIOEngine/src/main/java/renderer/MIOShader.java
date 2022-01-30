package renderer;

import utils.DefaultConstants;
import utils.Logger;
import utils.platform.SpecialCharacters;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL20.glGetShaderInfoLog;

public class MIOShader {
    private String vertexSource;
    private String fragmentSource;

    private final String filePath;

    private int vertexId;
    private int fragmentId;

    private int shaderProgramId;

    private final Logger logger = Logger.getInstance();

    private static final String TYPE_LITERAL = "#type";
    private static final String VERTEX_TYPE = "vertex";
    private static final String FRAGMENT_TYPE = "fragment";

    public MIOShader(String filePath) {
        String newLine = System.lineSeparator();
        this.filePath = filePath;

        try {
            String source = new String(Files.readAllBytes(Paths.get(filePath)));
            String[] splitString = source.split("(#type)( )+([a-zA-Z])+");

            // Search for first shader source code block by string: #type
            int firstTypeIndex = source.indexOf(TYPE_LITERAL) + TYPE_LITERAL.length() + 1;
            int firstEndOfLineIndex = source.indexOf(newLine, firstTypeIndex);

            String firstPattern = source.substring(firstTypeIndex, firstEndOfLineIndex).trim();

            // Search for second shader source block by string: #type
            int secondTypeIndex = source.indexOf(TYPE_LITERAL, firstEndOfLineIndex) + TYPE_LITERAL.length() + 1;
            int secondEndOfLineIndex = source.indexOf(newLine, secondTypeIndex);
            String secondPattern = source.substring(secondTypeIndex, secondEndOfLineIndex).trim();

            if (firstPattern.equals(VERTEX_TYPE)) {
                vertexSource = splitString[1].trim();
            } else if (firstPattern.equals(FRAGMENT_TYPE)) {
                fragmentSource = splitString[1].trim();
            } else {
                throw new IllegalStateException("Unexpected shader type:" + firstPattern + " in file:" + filePath);
            }

            if (secondPattern.equals(VERTEX_TYPE)) {
                vertexSource = splitString[2].trim();
            } else if (secondPattern.equals(FRAGMENT_TYPE)) {
                fragmentSource = splitString[2].trim();
            } else {
                throw new IllegalStateException("Unexpected shader type:" + secondPattern + " in file:" + filePath);
            }

            if (firstPattern.equals(secondPattern))
                throw new IllegalStateException("Unexpected shader types (duplicate types):" + firstPattern + " ," + secondPattern);
        } catch (IOException e) {
            logger.write("Shader opening file failed:" + filePath);
            e.printStackTrace();
        } catch (Exception e) {
            logger.write("Shader general exception thrown:" + filePath);
            e.printStackTrace();
        }
    }

    public void compile() {
        try {
            vertexId = loadShader(vertexSource, GL_VERTEX_SHADER);
            fragmentId = loadShader(fragmentSource, GL_FRAGMENT_SHADER);
            shaderProgramId = linkShaders(new int[] { vertexId, fragmentId });
        } catch (IllegalStateException e) {
            logger.write("Compile shader failed:" + this.filePath);
            e.printStackTrace();
        }
    }

    public void use() {
        glUseProgram(shaderProgramId);
    }

    public void detach() {
        glUseProgram(0);
    }

    public int getShaderProgram() { return shaderProgramId; }

    private int loadShader(String shaderSrc, int shaderType) {
        // Load and compile shader
        int shaderId = glCreateShader(shaderType);
        // Next, we set shader source
        glShaderSource(shaderId, shaderSrc);
        glCompileShader(shaderId);

        // Search for compilation errors
        int success = glGetShaderi(shaderId, GL_COMPILE_STATUS);
        if (success == GL_FALSE) {
            int len = glGetShaderi(shaderId, GL_INFO_LOG_LENGTH);
            logger.write("Error occurred during shader compilation process:" + shaderType);
            logger.write(glGetShaderInfoLog(shaderId, len));
            throw new IllegalStateException("Error occurred during shader compilation process:" + shaderType);
        }

        return shaderId;
    }

    private int linkShaders(int[] shaderIds) {
        // Link and check for errors
        int shaderProgram = glCreateProgram();

        for (int shaderId : shaderIds)
            glAttachShader(shaderProgram, shaderId);

        glLinkProgram(shaderProgram);

        // Check for any linking errors
        int success = glGetProgrami(shaderProgram, GL_LINK_STATUS);
        if (success == GL_FALSE) {
            int len = glGetProgrami(shaderProgram, GL_INFO_LOG_LENGTH);
            logger.write("Error occurred during shaders link process");
            logger.write(glGetProgramInfoLog(shaderProgram, len));
            throw new IllegalStateException("Error occurred during shaders link process");
        }

        return shaderProgram;
    }
}
