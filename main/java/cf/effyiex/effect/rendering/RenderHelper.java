package cf.effyiex.effect.rendering;

import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glColor4d;
import static org.lwjgl.opengl.GL11.glDepthMask;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glLineWidth;
import static org.lwjgl.opengl.GL11.glVertex3d;

import java.awt.Color;

import org.lwjgl.opengl.GL11;

import cf.effyiex.effect.Reference;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ResourceLocation;

import static net.minecraft.client.gui.Gui.drawRect;

public class RenderHelper implements Reference {

	public static void renderESPBox(double x, double y, double z, float width, float height, Color color, boolean outline) {
		glBlendFunc(770, 771);
		glEnable(GL_BLEND);
		glLineWidth(2.0F);
		glDisable(GL_TEXTURE_2D);
		glDisable(GL_DEPTH_TEST);
		glDepthMask(false);
		if(outline)	{
			glColor4d(color.getRed() / 255.0F, color.getGreen() / 255.0F, color.getBlue() / 255.0F, 1.0F);
			RenderGlobal.drawSelectionBoundingBox(new AxisAlignedBB(x - width / 2, y, z - width / 2, x + width / 2, y + height, z + width / 2));
		}
		glColor4d(color.getRed() / 255.0F, color.getGreen() / 255.0F, color.getBlue() / 255.0F, color.getAlpha() / 255.0F);
		renderFilledBox(new AxisAlignedBB(x - width / 2, y, z - width / 2, x + width / 2, y + height, z + width / 2));
		glEnable(GL_TEXTURE_2D);
		glEnable(GL_DEPTH_TEST);
		glDepthMask(true);
		glDisable(GL_BLEND);
	}
	
	public static void renderFilledBox(AxisAlignedBB bb) {
		glBegin(GL_QUADS); {
			
			glVertex3d(bb.minX, bb.minY, bb.minZ);
			glVertex3d(bb.maxX, bb.minY, bb.minZ);
			glVertex3d(bb.maxX, bb.minY, bb.maxZ);
			glVertex3d(bb.minX, bb.minY, bb.maxZ);
			
			glVertex3d(bb.minX, bb.maxY, bb.minZ);
			glVertex3d(bb.minX, bb.maxY, bb.maxZ);
			glVertex3d(bb.maxX, bb.maxY, bb.maxZ);
			glVertex3d(bb.maxX, bb.maxY, bb.minZ);
			
			glVertex3d(bb.minX, bb.minY, bb.minZ);
			glVertex3d(bb.minX, bb.maxY, bb.minZ);
			glVertex3d(bb.maxX, bb.maxY, bb.minZ);
			glVertex3d(bb.maxX, bb.minY, bb.minZ);
			
			glVertex3d(bb.maxX, bb.minY, bb.minZ);
			glVertex3d(bb.maxX, bb.maxY, bb.minZ);
			glVertex3d(bb.maxX, bb.maxY, bb.maxZ);
			glVertex3d(bb.maxX, bb.minY, bb.maxZ);
			
			glVertex3d(bb.minX, bb.minY, bb.maxZ);
			glVertex3d(bb.maxX, bb.minY, bb.maxZ);
			glVertex3d(bb.maxX, bb.maxY, bb.maxZ);
			glVertex3d(bb.minX, bb.maxY, bb.maxZ);
			
			glVertex3d(bb.minX, bb.minY, bb.minZ);
			glVertex3d(bb.minX, bb.minY, bb.maxZ);
			glVertex3d(bb.minX, bb.maxY, bb.maxZ);
			glVertex3d(bb.minX, bb.maxY, bb.minZ);
			
		} glEnd();
	}
	
	public static void drawSprite(ResourceLocation resloc, int x, int y, int width, int height) {
		CLIENT.getTextureManager().bindTexture(resloc);
		Tessellator tessellator = Tessellator.getInstance();
		WorldRenderer buffer = tessellator.getWorldRenderer();
		buffer.begin(GL11.GL_TRIANGLES, DefaultVertexFormats.POSITION_TEX);
		buffer.pos(x + width, y, 0F).tex(1, 0).endVertex();
		buffer.pos(x, y, 0F).tex(0, 0).endVertex();
		buffer.pos(x, y + height, 0F).tex(0, 1).endVertex();
		buffer.pos(x, y + height, 0F).tex(0, 1).endVertex();
		buffer.pos(x + width, y + height, 0F).tex(1, 1).endVertex();
		buffer.pos(x + width, y, 0F).tex(1, 0).endVertex();
        tessellator.draw();
	}

	public static void drawVerticalGradient(int left, int top, int right, int bottom, int startColor, int endColor) {
		/*try {
			if(AccessHelper.drawGradientRect != null)
				AccessHelper.drawGradientRect.invoke(null, left, top, right, bottom, startColor, endColor);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}*/
	}
	
	public static void drawHorizontalGradient(int left, int top, int right, int bottom, int startColor, int endColor) {
		GL11.glPushMatrix();
		GL11.glTranslatef(left, top, 0.0F);
		GL11.glRotatef(-90.0F, 1.0F, 1.0F, 0.0F);
		RenderHelper.drawVerticalGradient(0, 0, bottom - top, right - left, startColor, endColor);
		GL11.glPopMatrix();
	}
	
	public static void drawOutlinedRect(int x, int y, int w, int h, int t, int c) {
		drawRect(x, y, x + w, y + t, c);
		drawRect(x, y, x + t, y + h, c);
		drawRect(x, y + h - t, x + w, y + h, c);
		drawRect(x + w - t, y, x + w, y + h, c);
	}

}
