package blockentitiesreimagined.client.render;

import com.mojang.blaze3d.vertex.VertexConsumer;
import java.util.ArrayList;
import java.util.List;

public class GeometryCapturingConsumer implements VertexConsumer {
    public static class CapturedVertex {
        public float x, y, z;
        public int r, g, b, a;
        public float u, v;
        public int uv1; // overlay
        public int uv2; // light
        public float nx, ny, nz;
    }

    private final List<CapturedVertex> vertices = new ArrayList<>();
    private CapturedVertex currentVertex = new CapturedVertex();
    private boolean hasActive = false;

    public List<CapturedVertex> getVertices() {
        return vertices;
    }

    public void commitLast() {
        if (hasActive) {
            vertices.add(currentVertex);
            currentVertex = new CapturedVertex();
            hasActive = false;
        }
    }

    @Override
    public VertexConsumer addVertex(float x, float y, float z) {
        if (hasActive) {
            vertices.add(currentVertex);
            currentVertex = new CapturedVertex();
        }
        currentVertex.x = x;
        currentVertex.y = y;
        currentVertex.z = z;
        hasActive = true;
        return this;
    }

    @Override
    public VertexConsumer setColor(int r, int g, int b, int a) {
        currentVertex.r = r;
        currentVertex.g = g;
        currentVertex.b = b;
        currentVertex.a = a;
        return this;
    }

    @Override
    public VertexConsumer setColor(int color) {
        currentVertex.a = (color >> 24) & 0xFF;
        currentVertex.r = (color >> 16) & 0xFF;
        currentVertex.g = (color >> 8) & 0xFF;
        currentVertex.b = color & 0xFF;
        return this;
    }

    @Override
    public VertexConsumer setUv(float u, float v) {
        currentVertex.u = u;
        currentVertex.v = v;
        return this;
    }

    @Override
    public VertexConsumer setUv1(int u, int v) {
        currentVertex.uv1 = (u & 0xFFFF) | ((v & 0xFFFF) << 16);
        return this;
    }

    @Override
    public VertexConsumer setUv2(int u, int v) {
        currentVertex.uv2 = (u & 0xFFFF) | ((v & 0xFFFF) << 16);
        return this;
    }

    @Override
    public VertexConsumer setNormal(float x, float y, float z) {
        currentVertex.nx = x;
        currentVertex.ny = y;
        currentVertex.nz = z;
        return this;
    }

    @Override
    public VertexConsumer setLineWidth(float width) {
        return this;
    }
}
