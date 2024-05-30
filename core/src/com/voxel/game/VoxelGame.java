package com.voxel.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import static com.badlogic.gdx.graphics.VertexAttributes.*;

public class VoxelGame extends ApplicationAdapter {
    public PerspectiveCamera cam;
    private CameraInputController camController;
    private Viewport viewport;

    private Mesh quadMesh;
    private Texture texture;
    private ShaderProgram shader;

    private int[] chunk = new int[32768];
    private Vector3 chunkPosition = new Vector3(0f, 0f, 0f);

    @Override
    public void create() {
        super.create();

        //camera settings
        cam = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        cam.position.set(3f, 5f, 5f);
        cam.lookAt(0f,0f,0f);
        cam.near = 0.1f;
        cam.far = 300f;
        cam.update();

        //camera controller
        camController = new CameraInputController(cam);
        Gdx.input.setInputProcessor(camController);

        viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), cam);

        //drawing dirt
        texture = new Texture(Gdx.files.internal("dirtDebug.png"));

        //createWorld();
        setupQuadMeshCompressed();
        setupShaderCompressed();
    }

    @Override
    public void render() {
        super.render();
        camController.update();

        Gdx.gl20.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        Gdx.gl.glEnable(GL20.GL_CULL_FACE);
        Gdx.gl.glCullFace(GL20.GL_BACK);

        shader.bind();
        texture.bind();
        shader.setUniformi("u_texture", 0);
        shader.setUniformf("chunkPosition", new Vector3(0f, 0f, 0f));
        shader.setUniformMatrix("projectionViewMatrix", cam.combined);

        quadMesh.bind(shader);
        quadMesh.render(shader, GL20.GL_TRIANGLES);



    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public void dispose() {
        quadMesh.dispose();
        texture.dispose();
        shader.dispose();
    }

    private void setupQuadMeshExample() {
        quadMesh = new Mesh(true, 4, 6,
                new VertexAttribute(Usage.Position, 3, "a_position"),
                new VertexAttribute(Usage.TextureCoordinates, 2, "a_texCoord0")
        );

        quadMesh.setVertices(new float[] {
                -0.5f,  0.5f,  0.5f, 0, 0,
                -0.5f, -0.5f,  0.5f, 0, 1,
                0.5f, -0.5f,  0.5f, 1, 1,
                0.5f,  0.5f,  0.5f, 1, 0
        });

        quadMesh.setIndices(new short[]{0, 1, 2, 2, 3, 0});
    }

    private void setupMesh() {
        quadMesh = new Mesh(true, 4, 6,
                new VertexAttribute(Usage.Position, 3, "a_position"),
                new VertexAttribute(Usage.TextureCoordinates, 2, "a_texCoord0"));

        quadMesh.setVertices(new float[]{
                -0.5f,  0.5f,  0.5f, 0, 0,
                -0.5f, -0.5f,  0.5f, 0, 1,
                 0.5f, -0.5f,  0.5f, 1, 1,
                 0.5f,  0.5f,  0.5f, 1, 0,
        });

        quadMesh.setIndices(new short[]{0, 1, 2, 2, 3, 0});
    }

    private void setupShaderInstanced() {
        String vertexShader = Gdx.files.internal("shaders/instanced_vertex.glsl").readString();
        String fragmentShader = Gdx.files.internal("shaders/instanced_fragment.glsl").readString();
        shader = new ShaderProgram(vertexShader, fragmentShader);

        if (!shader.isCompiled()) {
            throw new GdxRuntimeException("Shader compilation failed:\n" + shader.getLog());
        }
    }

    private void setupShaderRegular() {
        String vertexShader = Gdx.files.internal("shaders/vertex.glsl").readString();
        String fragmentShader = Gdx.files.internal("shaders/fragment.glsl").readString();
        shader = new ShaderProgram(vertexShader, fragmentShader);

        if (!shader.isCompiled()) {
            throw new GdxRuntimeException("Shader compilation failed:\n" + shader.getLog());
        }
    }

    private void setupQuadMeshCompressed() {
        quadMesh = new Mesh(true, 8, 12,
                new VertexAttribute(Usage.Generic, 1, "inVertexData")
        );

        quadMesh.setVertices(new float[] {
                Float.intBitsToFloat(0b00000000000111000001000001000000),
                Float.intBitsToFloat(0b00000000001111000001000000000000),
                Float.intBitsToFloat(0b00000000010111000001000000000001),
                Float.intBitsToFloat(0b00000000011111000001000001000001),

                Float.intBitsToFloat(0b00000000000111000001000010000000),
                Float.intBitsToFloat(0b00000000001111000001000001000000),
                Float.intBitsToFloat(0b00000000010111000001000001000001),
                Float.intBitsToFloat(0b00000000011111000001000010000001),
        });


        quadMesh.setIndices(new short[]{
                0, 1, 2, 2, 3, 0,
                4, 5, 6, 6, 7, 4});
    }

    private void setupShaderCompressed() {
        String vertexShader = Gdx.files.internal("shaders/vertex_chunked.glsl").readString();
        String fragmentShader = Gdx.files.internal("shaders/fragment_chunked.glsl").readString();
        shader = new ShaderProgram(vertexShader, fragmentShader);

        if (!shader.isCompiled()) {
            throw new GdxRuntimeException("Shader compilation failed:\n" + shader.getLog());
        }
    }

    private void createWorld() {
        for (int y = 0; y < 32; y++) {
            for (int z = 0; z < 32; z++) {
                for (int x = 0; x < 32; x++) {
                    if (y == 15)
                        chunk[x + z * 32 + y * 1024] = 0;
                    else
                        chunk[x + z * 32 + y * 1024] = 1;
                }
            }
        }
    }
}

