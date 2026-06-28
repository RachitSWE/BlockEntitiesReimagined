package blockentitiesreimagined.client.render.culling;

/* joml */
import org.joml.FrustumIntersection;
import org.joml.Matrix4f;
import org.joml.Vector3f;

/* testing */
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BlockVisibilityCheckerTest {
    
    @Test
    public void testDistanceCulling() {
        Vector3f camera = new Vector3f(0f, 0f, 0f);
        Vector3f closePos = new Vector3f(10f, 0f, 0f);
        Vector3f farPos = new Vector3f(100f, 0f, 0f);
        Vector3f boundaryPos = new Vector3f(64f, 0f, 0f); // Exactly on boundary
        
        assertTrue(BlockVisibilityChecker.isWithinDistance(camera, closePos, 4096f));
        assertFalse(BlockVisibilityChecker.isWithinDistance(camera, farPos, 4096f));
        assertTrue(BlockVisibilityChecker.isWithinDistance(camera, boundaryPos, 4096f));
        
        // Primitive overload test
        assertTrue(BlockVisibilityChecker.isWithinDistance(0f, 0f, 0f, 64f, 0f, 0f, 4096f));
        assertFalse(BlockVisibilityChecker.isWithinDistance(0f, 0f, 0f, 64.001f, 0f, 0f, 4096f));
    }

    @Test
    public void testNormalFaceCulling() {
        Vector3f toCamera = new Vector3f(0f, 0f, -1f);
        Vector3f normal = new Vector3f(0f, 0f, 1f);
        
        assertFalse(BlockVisibilityChecker.isFrontFaceVisible(toCamera, normal));
        
        toCamera.set(0f, 0f, 1f);
        assertTrue(BlockVisibilityChecker.isFrontFaceVisible(toCamera, normal));
        
        // Boundary case: exactly 90 degrees (dot product 0)
        toCamera.set(1f, 0f, 0f);
        assertTrue(BlockVisibilityChecker.isFrontFaceVisible(toCamera, normal));
        
        // Primitive overload test
        assertFalse(BlockVisibilityChecker.isFrontFaceVisible(0f, 0f, -1f, 0f, 0f, 1f));
        assertTrue(BlockVisibilityChecker.isFrontFaceVisible(0f, 0f, 1f, 0f, 0f, 1f));
        assertTrue(BlockVisibilityChecker.isFrontFaceVisible(1f, 0f, 0f, 0f, 0f, 1f)); // Boundary
    }
    
    @Test
    public void testFrustumCulling() {
        Matrix4f projView = new Matrix4f().perspective((float) Math.toRadians(90.0), 1.0f, 0.1f, 100.0f)
                                          .lookAt(0f, 0f, 0f, 0f, 0f, -1f, 0f, 1f, 0f);
        FrustumIntersection frustum = new FrustumIntersection(projView);
        
        // Inside
        assertTrue(BlockVisibilityChecker.isWithinFrustum(frustum, -1f, -1f, -5f, 1f, 1f, -3f));
        
        // Outside (behind camera)
        assertFalse(BlockVisibilityChecker.isWithinFrustum(frustum, -1f, -1f, 3f, 1f, 1f, 5f));
    }
}
