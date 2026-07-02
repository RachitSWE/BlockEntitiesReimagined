package blockentitiesreimagined.client.render;

/* minecraft */
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.core.BlockPos;

/**
 * Zero-allocation frustum culling helper for block entities.
 *
 * <p>Vanilla's {@link Frustum#isVisible} requires constructing an {@link net.minecraft.world.phys.AABB}
 * object per call — that's one heap allocation per block entity per frame, causing GC pressure in
 * scenes with thousands of entities. This class bypasses that by calling
 * {@link org.joml.FrustumIntersection#testAab(float, float, float, float, float, float)} directly
 * via the access-widened {@code Frustum.intersection} field, using only primitive floats.</p>
 *
 * <p>A block entity occupies exactly one block (1×1×1), so its AABB is always
 * {@code (blockPos.x, blockPos.y, blockPos.z) → (blockPos.x+1, blockPos.y+1, blockPos.z+1)}.</p>
 *
 * <p>Usage: call {@link #isVisible(Frustum, BlockPos)} from the render thread only.</p>
 */
public final class BERFrustumCuller {

    private BERFrustumCuller() {}

    /**
     * Tests whether a 1×1×1 block at {@code pos} is visible within {@code frustum}.
     *
     * @param frustum the live camera frustum from {@code CameraRenderState.cullFrustum}
     * @param pos     the block position of the block entity
     * @return {@code true} if the block is (at least partially) inside the view frustum;
     *         {@code false} if it can be safely culled
     */
    public static boolean isVisible(Frustum frustum, BlockPos pos) {
        // Access the widened FrustumIntersection field — zero allocation path.
        // testAab takes min/max as floats and returns INSIDE(1), INTERSECT(0), or OUTSIDE(-1).
        // Any non-OUTSIDE result is considered visible.
        float minX = pos.getX();
        float minY = pos.getY();
        float minZ = pos.getZ();
        return frustum.intersection.testAab(minX, minY, minZ, minX + 1f, minY + 1f, minZ + 1f);
    }
}
