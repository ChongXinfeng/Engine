package nullengine.client.rendering.gl.shader;

import org.joml.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.lwjgl.opengl.GL11.GL_TRUE;
import static org.lwjgl.opengl.GL20.*;

public class ShaderProgram {

    private static final Logger LOGGER = LoggerFactory.getLogger(ShaderProgram.class);

    private int id;

    public ShaderProgram(Shader... shaders) {
        this.id = glCreateProgram();

        for (Shader shader : shaders) {
            glAttachShader(this.id, shader.getId());
        }

        glLinkProgram(id);
        if (glGetProgrami(id, GL_LINK_STATUS) != GL_TRUE) {
            LOGGER.warn("Error initializing shader program (id: {}), log: {}", id, glGetProgramInfoLog(id));
        }

        use();
        glValidateProgram(id);
        if (glGetProgrami(id, GL_VALIDATE_STATUS) != GL_TRUE) {
            LOGGER.warn("Error initializing shader program (id: {}), log: {}", id, glGetProgramInfoLog(id));
        }
        glUseProgram(0);

        for (Shader shader : shaders) {
            shader.dispose();
        }
    }

    public void use() {
        glUseProgram(id);
    }

    public void dispose() {
        if (isDisposed()) {
            return;
        }

        glUseProgram(0);
        glDeleteProgram(id);
        id = 0;
    }

    public boolean isDisposed() {
        return id == 0;
    }

    public int getUniformLocation(String name) {
        return glGetUniformLocation(id, name);
    }

    public void setUniform(String location, int value) {
        Uniform.setUniform(getUniformLocation(location), value);
    }

    public void setUniform(String location, float value) {
        Uniform.setUniform(getUniformLocation(location), value);
    }

    public void setUniform(String location, boolean value) {
        Uniform.setUniform(getUniformLocation(location), value);
    }

    public void setUniform(String location, Vector2fc value) {
        Uniform.setUniform(getUniformLocation(location), value);
    }

    public void setUniform(String location, Vector3fc value) {
        Uniform.setUniform(getUniformLocation(location), value);
    }

    public void setUniform(String location, Vector4fc value) {
        Uniform.setUniform(getUniformLocation(location), value);
    }

    public void setUniform(String location, Matrix3fc value) {
        Uniform.setUniform(getUniformLocation(location), value);
    }

    public void setUniform(String location, Matrix4fc value) {
        Uniform.setUniform(getUniformLocation(location), value);
    }

    public void setUniform(String location, Matrix4fc[] values) {
        Uniform.setUniform(getUniformLocation(location), values);
    }
}
