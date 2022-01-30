package utils.general;

public final class DefaultShaders {
    private DefaultShaders() { }

    public static final String VERTEX_SHADER_SRC = "#version 330 core\n" +
                                                   "layout (location=0) in vec3 aPos;\n" +
                                                   "layout (location=1) in vec4 aColor;\n" +
                                                   "out vec4 fColor;\n" +
                                                   "void main()\n" +
                                                   "{" +
                                                       "fColor = aColor;\n" +
                                                       "gl_Position = vec4(aPos, 1.0);\n" +
                                                   "}";

    public static final String FRAGMENT_SHADER_SRC = "#version 330 core\n" +
                                                     "in vec4 fColor;\n" +
                                                     "out vec4 color;\n" +
                                                     "void main()\n" +
                                                     "{\n" +
                                                         "color = fColor;\n" +
                                                     "}";
}
