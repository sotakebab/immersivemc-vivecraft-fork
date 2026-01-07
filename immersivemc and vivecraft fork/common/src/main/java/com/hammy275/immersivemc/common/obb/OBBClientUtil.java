package com.hammy275.immersivemc.common.obb;

import com.hammy275.immersivemc.api.common.hitbox.OBB;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.debug.DebugScreenEntries;
import net.minecraft.client.renderer.RenderType;

public class OBBClientUtil {

    public static void renderOBB(PoseStack stack, com.hammy275.immersivemc.api.common.hitbox.OBB obb, boolean forceRender,
                                 float red, float green, float blue, float alpha) {
        if ((Minecraft.getInstance().debugEntries.isCurrentlyEnabled(DebugScreenEntries.ENTITY_HITBOXES) || forceRender) &&
                obb != null) {
            Camera renderInfo = Minecraft.getInstance().gameRenderer.getMainCamera();
            // Use a new stack here, so we don't conflict with the stack.scale() for the item itself
            stack.pushPose();
            stack.translate(-renderInfo.position().x + obb.getCenter().x,
                    -renderInfo.position().y + obb.getCenter().y,
                    -renderInfo.position().z + obb.getCenter().z);
            rotateStackForOBB(stack, obb);
            // Use ClientUtil.renderLineBox for 1.21.10 compatibility
            net.minecraft.world.phys.AABB aabb = obb.getUnderlyingAABB().move(obb.getCenter().scale(-1));
            renderLineBoxInternal(stack, Minecraft.getInstance().renderBuffers().bufferSource().getBuffer(RenderType.lines()),
                    aabb.minX, aabb.minY, aabb.minZ, aabb.maxX, aabb.maxY, aabb.maxZ, red, green, blue, alpha);
            stack.popPose();
        }
    }

    /**
     * Internal method to render a line box. Adapted for 1.21.10 compatibility.
     */
    private static void renderLineBoxInternal(PoseStack poseStack, VertexConsumer consumer,
                                              double minX, double minY, double minZ,
                                              double maxX, double maxY, double maxZ,
                                              float red, float green, float blue, float alpha) {
        PoseStack.Pose pose = poseStack.last();
        float x1 = (float) minX, y1 = (float) minY, z1 = (float) minZ;
        float x2 = (float) maxX, y2 = (float) maxY, z2 = (float) maxZ;
        
        // Bottom face
        consumer.addVertex(pose, x1, y1, z1).setColor(red, green, blue, alpha).setNormal(pose, 1, 0, 0);
        consumer.addVertex(pose, x2, y1, z1).setColor(red, green, blue, alpha).setNormal(pose, 1, 0, 0);
        consumer.addVertex(pose, x2, y1, z1).setColor(red, green, blue, alpha).setNormal(pose, 0, 0, 1);
        consumer.addVertex(pose, x2, y1, z2).setColor(red, green, blue, alpha).setNormal(pose, 0, 0, 1);
        consumer.addVertex(pose, x2, y1, z2).setColor(red, green, blue, alpha).setNormal(pose, -1, 0, 0);
        consumer.addVertex(pose, x1, y1, z2).setColor(red, green, blue, alpha).setNormal(pose, -1, 0, 0);
        consumer.addVertex(pose, x1, y1, z2).setColor(red, green, blue, alpha).setNormal(pose, 0, 0, -1);
        consumer.addVertex(pose, x1, y1, z1).setColor(red, green, blue, alpha).setNormal(pose, 0, 0, -1);
        
        // Top face
        consumer.addVertex(pose, x1, y2, z1).setColor(red, green, blue, alpha).setNormal(pose, 1, 0, 0);
        consumer.addVertex(pose, x2, y2, z1).setColor(red, green, blue, alpha).setNormal(pose, 1, 0, 0);
        consumer.addVertex(pose, x2, y2, z1).setColor(red, green, blue, alpha).setNormal(pose, 0, 0, 1);
        consumer.addVertex(pose, x2, y2, z2).setColor(red, green, blue, alpha).setNormal(pose, 0, 0, 1);
        consumer.addVertex(pose, x2, y2, z2).setColor(red, green, blue, alpha).setNormal(pose, -1, 0, 0);
        consumer.addVertex(pose, x1, y2, z2).setColor(red, green, blue, alpha).setNormal(pose, -1, 0, 0);
        consumer.addVertex(pose, x1, y2, z2).setColor(red, green, blue, alpha).setNormal(pose, 0, 0, -1);
        consumer.addVertex(pose, x1, y2, z1).setColor(red, green, blue, alpha).setNormal(pose, 0, 0, -1);
        
        // Vertical edges
        consumer.addVertex(pose, x1, y1, z1).setColor(red, green, blue, alpha).setNormal(pose, 0, 1, 0);
        consumer.addVertex(pose, x1, y2, z1).setColor(red, green, blue, alpha).setNormal(pose, 0, 1, 0);
        consumer.addVertex(pose, x2, y1, z1).setColor(red, green, blue, alpha).setNormal(pose, 0, 1, 0);
        consumer.addVertex(pose, x2, y2, z1).setColor(red, green, blue, alpha).setNormal(pose, 0, 1, 0);
        consumer.addVertex(pose, x2, y1, z2).setColor(red, green, blue, alpha).setNormal(pose, 0, 1, 0);
        consumer.addVertex(pose, x2, y2, z2).setColor(red, green, blue, alpha).setNormal(pose, 0, 1, 0);
        consumer.addVertex(pose, x1, y1, z2).setColor(red, green, blue, alpha).setNormal(pose, 0, 1, 0);
        consumer.addVertex(pose, x1, y2, z2).setColor(red, green, blue, alpha).setNormal(pose, 0, 1, 0);
    }

    public static void rotateStackForOBB(PoseStack stack, OBB obb) {
        stack.mulPose(obb.getRotation());
    }
}
