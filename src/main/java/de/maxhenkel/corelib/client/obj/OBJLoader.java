package de.maxhenkel.corelib.client.obj;

import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.mojang.math.Vector3f;
import com.mojang.math.Vector4f;
import joptsimple.internal.Strings;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.world.phys.Vec2;

import javax.annotation.Nullable;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class OBJLoader {

    private static Map<ResourceLocation, OBJModel.OBJModelData> modelCache = new HashMap<>();

    public static OBJModel.OBJModelData load(ResourceLocation model) {
        try {
            return loadInternal(model);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static OBJModel.OBJModelData loadInternal(ResourceLocation model) throws IOException {
        if (modelCache.containsKey(model)) {
            return modelCache.get(model);
        }

        Optional<Resource> optionalResource = Minecraft.getInstance().getResourceManager().getResource(model);

        if (optionalResource.isEmpty()) {
            throw new IOException("Failed to load model '%s'".formatted(model));
        }

        LineReader reader = new LineReader(optionalResource.get());

        List<Vector3f> positions = Lists.newArrayList();
        List<Vec2> texCoords = Lists.newArrayList();
        List<Vector3f> normals = Lists.newArrayList();
        List<int[][]> faces = Lists.newArrayList();

        String[] line;
        while ((line = reader.readAndSplitLine(true)) != null) {
            switch (line[0]) {
                case "v":
                    positions.add(parseVector4To3(line));
                    break;
                case "vt":
                    Vec2 vec2f = parseVector2(line);
                    texCoords.add(new Vec2(vec2f.x, 1F - vec2f.y));
                    break;
                case "vn":
                    normals.add(parseVector3(line));
                    break;
                case "f":
                    int[][] vertices = new int[line.length - 1][];
                    for (int i = 0; i < vertices.length; i++) {
                        String vertexData = line[i + 1];
                        String[] vertexParts = vertexData.split("/");
                        int[] vertex = Arrays.stream(vertexParts).mapToInt(num -> Strings.isNullOrEmpty(num) ? 0 : Integer.parseInt(num)).toArray();
                        if (vertex[0] < 0) {
                            vertex[0] = positions.size() + vertex[0];
                        } else {
                            vertex[0]--;
                        }
                        if (vertex.length > 1) {
                            if (vertex[1] < 0) {
                                vertex[1] = texCoords.size() + vertex[1];
                            } else {
                                vertex[1]--;
                            }
                            if (vertex.length > 2) {
                                if (vertex[2] < 0) {
                                    vertex[2] = normals.size() + vertex[2];
                                } else {
                                    vertex[2]--;
                                }
                            }
                        }
                        vertices[i] = vertex;
                    }

                    faces.add(vertices);
                    break;
            }
        }

        OBJModel.OBJModelData obj = new OBJModel.OBJModelData(positions, texCoords, normals, faces);
        modelCache.put(model, obj);
        return obj;
    }

    private static class LineReader implements AutoCloseable {
        private InputStreamReader lineStream;
        private BufferedReader lineReader;

        public LineReader(Resource resource) throws IOException {
            this.lineStream = new InputStreamReader(resource.open(), Charsets.UTF_8);
            this.lineReader = new BufferedReader(lineStream);
        }

        @Nullable
        public String[] readAndSplitLine(boolean ignoreEmptyLines) throws IOException {
            do {
                String currentLine = lineReader.readLine();
                if (currentLine == null) {
                    return null;
                }

                List<String> lineParts = new ArrayList<>();

                if (currentLine.startsWith("#")) {
                    currentLine = "";
                }

                if (currentLine.length() > 0) {
                    boolean hasContinuation;
                    do {
                        hasContinuation = currentLine.endsWith("\\");
                        String tmp = hasContinuation ? currentLine.substring(0, currentLine.length() - 1) : currentLine;

                        Arrays.stream(tmp.split("[\t ]+")).filter(s -> !Strings.isNullOrEmpty(s)).forEach(lineParts::add);

                        if (hasContinuation) {
                            currentLine = lineReader.readLine();
                            if (currentLine == null) {
                                break;
                            }

                            if (currentLine.length() == 0 || currentLine.startsWith("#")) {
                                break;
                            }
                        }
                    } while (hasContinuation);
                }

                if (lineParts.size() > 0) {
                    return lineParts.toArray(new String[0]);
                }
            } while (ignoreEmptyLines);

            return new String[0];
        }

        @Override
        public void close() throws Exception {
            lineReader.close();
            lineStream.close();
        }
    }

    private static Vector3f parseVector4To3(String[] line) {
        Vector4f vec4 = parseVector4(line);
        return new Vector3f(
                vec4.x() / vec4.w(),
                vec4.y() / vec4.w(),
                vec4.z() / vec4.w()
        );
    }

    private static Vec2 parseVector2(String[] line) {
        return switch (line.length) {
            case 1 -> new Vec2(0, 0);
            case 2 -> new Vec2(Float.parseFloat(line[1]), 0);
            default -> new Vec2(Float.parseFloat(line[1]), Float.parseFloat(line[2]));
        };
    }

    private static Vector3f parseVector3(String[] line) {
        return switch (line.length) {
            case 1 -> new Vector3f(0, 0, 0);
            case 2 -> new Vector3f(Float.parseFloat(line[1]), 0, 0);
            case 3 -> new Vector3f(Float.parseFloat(line[1]), Float.parseFloat(line[2]), 0);
            default -> new Vector3f(Float.parseFloat(line[1]), Float.parseFloat(line[2]), Float.parseFloat(line[3]));
        };
    }

    static Vector4f parseVector4(String[] line) {
        return switch (line.length) {
            case 1 -> new Vector4f(0, 0, 0, 1);
            case 2 -> new Vector4f(Float.parseFloat(line[1]), 0, 0, 1);
            case 3 -> new Vector4f(Float.parseFloat(line[1]), Float.parseFloat(line[2]), 0, 1);
            case 4 -> new Vector4f(Float.parseFloat(line[1]), Float.parseFloat(line[2]), Float.parseFloat(line[3]), 1);
            default ->
                    new Vector4f(Float.parseFloat(line[1]), Float.parseFloat(line[2]), Float.parseFloat(line[3]), Float.parseFloat(line[4]));
        };
    }
}
