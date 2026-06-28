package blockentitiesreimagined.client.render.culling;

/* joml */
import org.joml.FrustumIntersection;
import org.joml.Vector3f;

public final class BlockVisibilityChecker {
    private BlockVisibilityChecker() {}

    public static boolean isWithinDistance(float cx, float cy, float cz, float px, float py, float pz, float maxDistanceSq) {
        float dx = cx - px;
        float dy = cy - py;
        float dz = cz - pz;
        float distSq = dx * dx + dy * dy + dz * dz;
        return distSq <= maxDistanceSq;
    }

    public static boolean isWithinDistance(Vector3f camera, Vector3f blockPos, float maxDistanceSq) {
        return isWithinDistance(camera.x, camera.y, camera.z, blockPos.x, blockPos.y, blockPos.z, maxDistanceSq);
    }

    public static boolean isFrontFaceVisible(float toCameraX, float toCameraY, float toCameraZ, float nx, float ny, float nz) {
        return (toCameraX * nx + toCameraY * ny + toCameraZ * nz) >= 0.0f;
    }

    public static boolean isFrontFaceVisible(Vector3f toCamera, Vector3f normal) {
        return isFrontFaceVisible(toCamera.x, toCamera.y, toCamera.z, normal.x, normal.y, normal.z);
    }

    public static boolean isWithinFrustum(FrustumIntersection frustum, float minX, float minY, float minZ, float maxX, float maxY, float maxZ) {
        return frustum.testAab(minX, minY, minZ, maxX, maxY, maxZ);
    }
}
