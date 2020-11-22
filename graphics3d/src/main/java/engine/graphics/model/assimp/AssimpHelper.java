package engine.graphics.model.assimp;

import org.joml.Matrix4f;
import org.lwjgl.assimp.*;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.lwjgl.assimp.Assimp.*;
import static org.lwjgl.system.MemoryUtil.*;

public class AssimpHelper {
    public static final AIFileIO ASSIMP_JARFILEIO;

    static {
        ASSIMP_JARFILEIO = AIFileIO.create();
        var openproc = new AIFileOpenProc() {
            @Override
            public long invoke(long pFileIO, long fileName, long openMode) {
                AIFile aiFile = AIFile.create();
                final ByteBuffer data;
                String fileNameUtf8 = memUTF8(fileName);
                if (Files.isReadable(Paths.get(fileNameUtf8))) {
                    try (var channel = FileChannel.open(Path.of(fileNameUtf8))) {
                        data = ByteBuffer.allocateDirect((int) channel.size());
                        channel.read(data);
                    } catch (IOException e) {
                        throw new RuntimeException("Could not open file: " + fileNameUtf8, e);
                    }
                } else {
                    var url = AssimpHelper.class.getResource(fileNameUtf8);
                    if (url == null && !fileNameUtf8.startsWith("/")) {
                        url = AssimpHelper.class.getResource("/" + fileNameUtf8);
                    }
                    if (url == null) {
                        throw new RuntimeException("Could not open file: " + fileNameUtf8);
                    }
                    try (var resourceAsStream = url.openStream()) {
                        var bytes = resourceAsStream.readAllBytes();
                        data = ByteBuffer.allocateDirect(bytes.length);
                        data.put(bytes);
                        data.flip();
                    } catch (IOException e) {
                        throw new RuntimeException("Could not open file: " + fileNameUtf8, e);
                    }
                }
                AIFileReadProcI fileReadProc = new AIFileReadProc() {
                    public long invoke(long pFile, long pBuffer, long size, long count) {
                        long max = Math.min(data.remaining(), size * count);
                        memCopy(memAddress(data) + data.position(), pBuffer, max);
                        return max;
                    }
                };
                AIFileSeekI fileSeekProc = new AIFileSeek() {
                    public int invoke(long pFile, long offset, int origin) {
                        if (origin == Assimp.aiOrigin_CUR) {
                            data.position(data.position() + (int) offset);
                        } else if (origin == Assimp.aiOrigin_SET) {
                            data.position((int) offset);
                        } else if (origin == Assimp.aiOrigin_END) {
                            data.position(data.limit() + (int) offset);
                        }
                        return 0;
                    }
                };
                AIFileTellProcI fileTellProc = new AIFileTellProc() {
                    public long invoke(long pFile) {
                        return data.limit();
                    }
                };
                aiFile.ReadProc(fileReadProc);
                aiFile.SeekProc(fileSeekProc);
                aiFile.FileSizeProc(fileTellProc);
                return aiFile.address();
            }
        };
        AIFileCloseProcI closeproc = new AIFileCloseProc() {
            public void invoke(long pFileIO, long pFile) {
                /* Nothing to do */
            }
        };
        ASSIMP_JARFILEIO.set(openproc, closeproc, NULL);
    }

    /**
     * Load a model from jar
     *
     * @param path relative path of the model from root
     * @return a model
     * @todo Use ResourceLocation-styled instead
     */
    public static AssimpModel loadModel(String path) {
        AIScene scene = aiImportFileEx(path,
                /*aiProcess_JoinIdenticalVertices | */aiProcess_Triangulate | aiProcess_CalcTangentSpace | aiProcess_FlipUVs, ASSIMP_JARFILEIO);
        if (scene == null) {
            throw new IllegalStateException(aiGetErrorString());
        }
        return new AssimpModel(scene, path);
    }

    public static Matrix4f generalizeNativeMatrix(AIMatrix4x4 aiMatrix4x4) {
        Matrix4f mat = new Matrix4f().identity();
        if (aiMatrix4x4 != null)
            mat.m00(aiMatrix4x4.a1()).m10(aiMatrix4x4.a2()).m20(aiMatrix4x4.a3()).m30(aiMatrix4x4.a4())
                    .m01(aiMatrix4x4.b1()).m11(aiMatrix4x4.b2()).m21(aiMatrix4x4.b3()).m31(aiMatrix4x4.b4())
                    .m02(aiMatrix4x4.c1()).m12(aiMatrix4x4.c2()).m22(aiMatrix4x4.c3()).m32(aiMatrix4x4.c4())
                    .m03(aiMatrix4x4.d1()).m13(aiMatrix4x4.d2()).m23(aiMatrix4x4.d3()).m33(aiMatrix4x4.d4());
        return mat;
    }
}
