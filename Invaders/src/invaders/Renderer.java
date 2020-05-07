package invaders;

import static models.Triangle.vertices;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_TRIANGLE_STRIP;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glDeleteBuffers;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glDeleteVertexArrays;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

import java.nio.FloatBuffer;

import org.lwjgl.system.MemoryUtil;

import engine.Shaders;
import engine.Util;
import engine.Window;

public class Renderer {

    private int vboId;

    private int vaoId;

    private Shaders shaders;

    public Renderer() {}

    public void init() throws Exception {
        shaders= new Shaders();
        shaders.createVertexShader(Util.loadResource("/vertex.vs"));
        shaders.createFragmentShader(Util.loadResource("/fragment.fs"));
        shaders.link();

        FloatBuffer verticesBuffer= null;
        try {
            verticesBuffer= MemoryUtil.memAllocFloat(vertices.length);
            verticesBuffer.put(vertices).flip();

            vaoId= glGenVertexArrays();
            glBindVertexArray(vaoId);

            vboId= glGenBuffers();
            glBindBuffer(GL_ARRAY_BUFFER, vboId);
            glBufferData(GL_ARRAY_BUFFER, verticesBuffer, GL_STATIC_DRAW);

            glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);

            glBindBuffer(GL_ARRAY_BUFFER, 0);
            glBindVertexArray(0);

        } finally {
            if (verticesBuffer != null) {
                MemoryUtil.memFree(verticesBuffer);
            }
        }
    }

    public void clear() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }

    public void render(Window window) {
        // clear the window
        clear();

        // bind the shader program
        shaders.bind();

        // bind the VAO
        glBindVertexArray(vaoId);
        glEnableVertexAttribArray(0);

        // draw the vertices in VBO assc to VAO
        glDrawArrays(GL_TRIANGLE_STRIP, 0, 4);

        // restore the state
        glDisableVertexAttribArray(0);
        glBindVertexArray(0);

        shaders.unbind();
    }

    public void cleanup() {
        if (shaders != null) {
            shaders.cleanup();
        }

        glDisableVertexAttribArray(0);

        // Delete the VBO
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glDeleteBuffers(vboId);

        // Delete the VAO
        glBindVertexArray(0);
        glDeleteVertexArrays(vaoId);
    }
}