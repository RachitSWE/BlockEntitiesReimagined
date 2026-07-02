package blockentitiesreimagined.client.api;

/* minecraft */
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.SubmitNodeCollector;
import com.mojang.blaze3d.vertex.PoseStack;
import org.jetbrains.annotations.NotNull;

/**
 * Implemented by each BER renderer.
 *
 * <p>In Minecraft 26.2 the block-entity rendering pipeline is two-phase:
 * <ol>
 *   <li>{@link net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher#tryExtractRenderState}
 *       snapshots live data into an immutable {@link BlockEntityRenderState}.</li>
 *   <li>{@link net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher#submit}
 *       draws that snapshot on the render thread.</li>
 * </ol>
 * BER intercepts phase 2 ({@code submit}) and delegates here.</p>
 *
 * @param <S> the concrete {@link BlockEntityRenderState} subtype for this renderer
 */
public interface IInstancedRenderer<S extends BlockEntityRenderState> {

    /**
     * Render using the already-extracted render state (phase 2).
     * Called from {@code BlockEntityRenderDispatcherMixin} instead of vanilla {@code submit}.
     *
     * @return {@code true} if this renderer fully handled rendering and vanilla should be
     *         suppressed; {@code false} to fall through to vanilla (use during stub / WIP
     *         implementation to prevent invisible blocks).
     */
    boolean renderState(
            @NotNull S state,
            @NotNull PoseStack matrices,
            @NotNull SubmitNodeCollector collector,
            @NotNull CameraRenderState cameraRenderState);

    /** @return {@code true} if this renderer produces fully static geometry suitable for instancing. */
    boolean isStatic();

    default boolean renderStateGeneric(
            @NotNull S state,
            @NotNull PoseStack matrices,
            @NotNull SubmitNodeCollector collector,
            @NotNull CameraRenderState cameraRenderState) {
        
        if (state.blockEntityType == null) return false;
        
        blockentitiesreimagined.client.render.instanced.BERInstancedBuffer buffer = 
            blockentitiesreimagined.client.BlockEntitiesReimaginedClient.getInstancer()
                .getBuffer(state.blockEntityType);

        if (buffer.getGeometryVertexCount() == 0) {
            net.minecraft.client.renderer.blockentity.BlockEntityRenderer<net.minecraft.world.level.block.entity.BlockEntity, S> vanillaRenderer =
                    (net.minecraft.client.renderer.blockentity.BlockEntityRenderer) 
                    blockentitiesreimagined.client.render.immediate.BERRendererRegistry.getVanillaRenderer(state.blockEntityType);
            
            if (vanillaRenderer != null) {
                blockentitiesreimagined.client.render.GeometryCapturingConsumer capturingConsumer = 
                        new blockentitiesreimagined.client.render.GeometryCapturingConsumer();
                
                net.minecraft.client.renderer.SubmitNodeCollector capturingCollector = 
                        new net.minecraft.client.renderer.SubmitNodeCollector() {
                            
                            @Override
                            public net.minecraft.client.renderer.OrderedSubmitNodeCollector order(int order) {
                                return this;
                            }
                            
                            @Override
                            public void submitShadow(PoseStack pose, float f, java.util.List<net.minecraft.client.renderer.entity.state.EntityRenderState.ShadowPiece> list) {}
                            
                            @Override
                            public void submitNameTag(PoseStack pose, net.minecraft.world.phys.Vec3 vec, int i, net.minecraft.network.chat.Component comp, boolean b, int j, CameraRenderState cam) {}
                            
                            @Override
                            public void submitText(PoseStack pose, float f1, float f2, net.minecraft.util.FormattedCharSequence seq, boolean b, net.minecraft.client.gui.Font.DisplayMode mode, int i, int j, int k, int l) {}
                            
                            @Override
                            public void submitFlame(PoseStack pose, net.minecraft.client.renderer.entity.state.EntityRenderState state, org.joml.Quaternionf q) {}
                            
                            @Override
                            public void submitLeash(PoseStack pose, net.minecraft.client.renderer.entity.state.EntityRenderState.LeashState leash) {}
                            
                            @Override
                            public <M> void submitModel(
                                    net.minecraft.client.model.Model<? super M> model,
                                    M sState,
                                    PoseStack pose,
                                    net.minecraft.client.renderer.rendertype.RenderType renderType,
                                    int light,
                                    int overlay,
                                    int color,
                                    net.minecraft.client.renderer.texture.TextureAtlasSprite sprite,
                                    int l,
                                    net.minecraft.client.renderer.feature.ModelFeatureRenderer.CrumblingOverlay overlayVal) {
                                model.setupAnim(sState);
                                model.renderToBuffer(pose, capturingConsumer, light, overlay, color);
                            }
                            
                            @Override
                            public void submitMovingBlock(PoseStack pose, net.minecraft.client.renderer.block.MovingBlockRenderState state, int i) {}
                            
                            @Override
                            public void submitBlockModel(PoseStack pose, net.minecraft.client.renderer.rendertype.RenderType type, java.util.List<net.minecraft.client.renderer.block.dispatch.BlockStateModelPart> list, int[] arr, int i, int j, int k) {}
                            
                            @Override
                            public void submitBreakingBlockModel(PoseStack pose, java.util.List<net.minecraft.client.renderer.block.dispatch.BlockStateModelPart> list, int i) {}
                            
                            @Override
                            public void submitShapeOutline(PoseStack pose, net.minecraft.world.phys.shapes.VoxelShape shape, net.minecraft.client.renderer.rendertype.RenderType type, int i, float f, boolean b) {}
                            
                            @Override
                            public void submitItem(PoseStack pose, net.minecraft.world.item.ItemDisplayContext context, int i, int j, int k, int[] arr, java.util.List<net.minecraft.client.resources.model.geometry.BakedQuad> list, net.minecraft.client.renderer.item.ItemStackRenderState.FoilType foil) {}
                            
                            @Override
                            public void submitCustomGeometry(PoseStack pose, net.minecraft.client.renderer.rendertype.RenderType type, net.minecraft.client.renderer.SubmitNodeCollector.CustomGeometryRenderer renderer) {}
                            
                            @Override
                            public void submitQuadParticleGroup(net.minecraft.client.renderer.state.level.QuadParticleRenderState state) {}
                            
                            @Override
                            public void submitGizmoPrimitives(net.minecraft.client.renderer.gizmos.DrawableGizmoPrimitives.Group group, CameraRenderState cam, boolean b) {}
                            
                            @Override
                            public void submitModelPart(net.minecraft.client.model.geom.ModelPart part, PoseStack pose, net.minecraft.client.renderer.rendertype.RenderType type, int light, int overlay, net.minecraft.client.renderer.texture.TextureAtlasSprite sprite) {
                                part.render(pose, capturingConsumer, light, overlay);
                            }
                            
                            @Override
                            public void submitModelPart(net.minecraft.client.model.geom.ModelPart part, PoseStack pose, net.minecraft.client.renderer.rendertype.RenderType type, int light, int overlay, net.minecraft.client.renderer.texture.TextureAtlasSprite sprite, int color, net.minecraft.client.renderer.feature.ModelFeatureRenderer.CrumblingOverlay overlayVal) {
                                part.render(pose, capturingConsumer, light, overlay, color);
                            }
                            
                            @Override
                            public void submitModelPart(net.minecraft.client.model.geom.ModelPart part, PoseStack pose, net.minecraft.client.renderer.rendertype.RenderType type, int light, int overlay, net.minecraft.client.renderer.texture.TextureAtlasSprite sprite, int color, net.minecraft.client.renderer.feature.ModelFeatureRenderer.CrumblingOverlay overlayVal, int j) {
                                part.render(pose, capturingConsumer, light, overlay, color);
                            }
                        };
                
                vanillaRenderer.submit(state, new PoseStack(), capturingCollector, cameraRenderState);
                capturingConsumer.commitLast();
                
                java.util.List<blockentitiesreimagined.client.render.GeometryCapturingConsumer.CapturedVertex> vertices = 
                        capturingConsumer.getVertices();
                if (!vertices.isEmpty()) {
                    java.nio.FloatBuffer floatBuffer = java.nio.ByteBuffer.allocateDirect(vertices.size() * 48)
                            .order(java.nio.ByteOrder.nativeOrder()).asFloatBuffer();
                    
                    for (blockentitiesreimagined.client.render.GeometryCapturingConsumer.CapturedVertex v : vertices) {
                        floatBuffer.put(v.x).put(v.y).put(v.z);
                        floatBuffer.put(v.r / 255.0f).put(v.g / 255.0f).put(v.b / 255.0f).put(v.a / 255.0f);
                        floatBuffer.put(v.u).put(v.v);
                        floatBuffer.put(v.nx).put(v.ny).put(v.nz);
                    }
                    floatBuffer.flip();
                    buffer.uploadGeometry(floatBuffer, vertices.size());
                }
            }
            return false;
        }
        
        return true;
    }
}
