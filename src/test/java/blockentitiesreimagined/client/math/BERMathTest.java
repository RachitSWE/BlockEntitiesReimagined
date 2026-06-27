package blockentitiesreimagined.client.math;

/* joml */
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.joml.Vector4f;

/* junit */
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

public class BERMathTest {

    @Test
    public void testVector3fPoolDoesNotAllocateNewInstancesSequentially() {
        Vector3f v1 = BERMath.getVec3();
        Vector3f v2 = BERMath.getVec3();
        
        Assertions.assertNotSame(v1, v2, "Pool should provide different instances for consecutive calls");
        
        // After pooling a certain amount (256), it should wrap around and reuse the first instance.
        // Let's exhaust the pool up to size 256.
        for (int i = 0; i < 254; i++) {
            BERMath.getVec3();
        }
        
        Vector3f v257 = BERMath.getVec3();
        Assertions.assertSame(v1, v257, "Pool should wrap around and reuse instances");
    }

    @Test
    public void testVector3fInitialization() {
        Vector3f v = BERMath.getVec3(1.0f, 2.0f, 3.0f);
        Assertions.assertEquals(1.0f, v.x(), 1e-6);
        Assertions.assertEquals(2.0f, v.y(), 1e-6);
        Assertions.assertEquals(3.0f, v.z(), 1e-6);
    }
    
    @Test
    public void testVector4fInitialization() {
        Vector4f v = BERMath.getVec4(1.0f, 2.0f, 3.0f, 4.0f);
        Assertions.assertEquals(1.0f, v.x(), 1e-6);
        Assertions.assertEquals(2.0f, v.y(), 1e-6);
        Assertions.assertEquals(3.0f, v.z(), 1e-6);
        Assertions.assertEquals(4.0f, v.w(), 1e-6);
    }

    @Test
    public void testMatrixStackPushPop() {
        BERMath.MatrixStack stack = BERMath.getStack();
        Matrix4f m1 = stack.peek();
        
        m1.translate(1.0f, 2.0f, 3.0f);
        
        stack.push();
        Matrix4f m2 = stack.peek();
        
        // m2 should have the same values as m1 after push
        Assertions.assertEquals(m1, m2);
        Assertions.assertNotSame(m1, m2, "Pushed matrix should be a different instance");
        
        m2.scale(2.0f);
        Assertions.assertNotEquals(m1, m2, "Modifying m2 should not modify m1");
        
        stack.pop();
        Matrix4f m3 = stack.peek();
        
        Assertions.assertSame(m1, m3, "Popping should return the original instance");
    }
    
    @Test
    public void testMatrixStackOverflow() {
        BERMath.MatrixStack stack = BERMath.getStack();
        
        // MAX_DEPTH is 32. We can push 31 times.
        for (int i = 0; i < 31; i++) {
            stack.push();
        }
        
        Assertions.assertThrows(IllegalStateException.class, () -> {
            stack.push();
        }, "Should throw on overflow");
    }

    @Test
    public void testMatrixStackUnderflow() {
        BERMath.MatrixStack stack = BERMath.getStack();
        stack.clear(); // Reset before testing
        
        Assertions.assertThrows(IllegalStateException.class, () -> {
            stack.pop();
        }, "Should throw on underflow");
    }

    @Test
    public void testGetStackDoesNotClearState() {
        BERMath.MatrixStack stack1 = BERMath.getStack();
        stack1.clear();
        
        stack1.peek().translate(5.0f, 5.0f, 5.0f);
        stack1.push();
        
        BERMath.MatrixStack stack2 = BERMath.getStack();
        Assertions.assertSame(stack1, stack2, "getStack() should return the same instance for the same thread");
        
        // Assert that the state was not cleared
        Assertions.assertEquals(5.0f, stack2.peek().m30(), 1e-6, "Translation X should persist");
        
        // Pop the state added earlier to clean up
        stack2.pop();
    }
}
