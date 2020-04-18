package cf.effyiex.effect.gui;

import static cf.effyiex.effect.rendering.Animator.getFrameMultiplier;

import java.awt.Color;
import java.awt.Rectangle;
import java.io.IOException;

import org.lwjgl.opengl.GL11;

import cf.effyiex.effect.Effect;
import cf.effyiex.effect.Reference;
import cf.effyiex.effect.rendering.RenderHelper;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiOptions;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiSelectWorld;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.fml.client.GuiModList;

public class MainMenu extends GuiScreen implements Reference {

	private static final short SIDEBAR_WIDTH = 96;
	private static final short BUTTON_HEIGHT = 16;
	private static final short BUTTON_WIDTH = 64;
	private static final short BUTTON_ANCHOR = 112;
	private static final short FRAME_RATE = 60;
	private static final short PARTICLES = 64;
	
	private float entryAnimation;
	private Particles particles;
	private Rectangle particleArea;
	
	public MainMenu() {
		this.particles = new Particles(FRAME_RATE, PARTICLES);
	}
	
	@Override public void initGui() {
		entryAnimation = 1.0F;
		int buttonSpaceX = Math.round((SIDEBAR_WIDTH - BUTTON_WIDTH) / 2.0F);
		int buttonSpaceY = Math.round(BUTTON_HEIGHT * 1.2F);
		this.buttonList.clear();
		this.buttonList.add(new Button(0, buttonSpaceX, BUTTON_ANCHOR, BUTTON_WIDTH, BUTTON_HEIGHT, "Singeplayer"));
		this.buttonList.add(new Button(1, buttonSpaceX, BUTTON_ANCHOR + buttonSpaceY, BUTTON_WIDTH, BUTTON_HEIGHT, "Multiplayer"));
		this.buttonList.add(new Button(2, buttonSpaceX, BUTTON_ANCHOR + buttonSpaceY * 2, BUTTON_WIDTH, BUTTON_HEIGHT, "Mod Options"));
		this.buttonList.add(new Button(3, buttonSpaceX, BUTTON_ANCHOR + buttonSpaceY * 3, BUTTON_WIDTH, BUTTON_HEIGHT, "MC Options"));
		this.buttonList.add(new Button(4, buttonSpaceX, BUTTON_ANCHOR + buttonSpaceY * 4, BUTTON_WIDTH, BUTTON_HEIGHT, "Account"));
		this.buttonList.add(new Button(5, buttonSpaceX, BUTTON_ANCHOR + buttonSpaceY * 5, BUTTON_WIDTH, BUTTON_HEIGHT, "Exit Game"));
		this.particleArea = new Rectangle(SIDEBAR_WIDTH, 0, width, height);
	}
	
	@Override public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		drawRect(0, 0, width, height, new Color(55, 55, 66).getRGB());
		particles.render(particleArea);
		try {
			int alpha = Math.round(155 * (1-entryAnimation));
			if(alpha > 0 && alpha <= 255)
				drawGradientRect(0, 0, width, height, 0, new Color(255, 55, 255, alpha).getRGB());
		} catch(IllegalArgumentException exception) {
			exception.printStackTrace();
		}
		drawRect(0, 0, SIDEBAR_WIDTH + 1, height, new Color(11, 11, 22).getRGB());
		GlStateManager.pushMatrix();
		GlStateManager.translate(-SIDEBAR_WIDTH * entryAnimation, 0.0F, 0.0F);
		drawRect(0, 0, SIDEBAR_WIDTH, height, new Color(44, 44, 55).getRGB());
		drawRect(SIDEBAR_WIDTH, 0, SIDEBAR_WIDTH + 1, height, new Color(11, 11, 22).getRGB());
		super.drawScreen(mouseX, mouseY, partialTicks);
		GL11.glColor3f(1.0F, 1.0F, 1.0F);
		RenderHelper.drawSprite(Effect.mod.getLogo(), 0, SIDEBAR_WIDTH / 6, SIDEBAR_WIDTH, SIDEBAR_WIDTH);
		GlStateManager.popMatrix();
		for(short s = 0; s < width; s++)
			drawRect(SIDEBAR_WIDTH + s, 0, SIDEBAR_WIDTH + s + 1, height, new Color(0, 0, 0, 30 + Math.round(124.0F / width * s)).getRGB());
		if(entryAnimation > 0.01F) entryAnimation -= entryAnimation * 0.025 * getFrameMultiplier(FRAME_RATE);
		else entryAnimation = 0.0F;
		String userInfo = ("User: " + CLIENT.getSession().getUsername());
		GlStateManager.pushMatrix();
		GlStateManager.translate(0.0F, (FONT_RENDERER.FONT_HEIGHT * 2 + 10) * entryAnimation, 0.0F);
		int textboxWidth = FONT_RENDERER.getStringWidth(AD);
		if(FONT_RENDERER.getStringWidth(userInfo) > textboxWidth)
			textboxWidth = FONT_RENDERER.getStringWidth(userInfo);
		drawRect(width - textboxWidth - 11, height - FONT_RENDERER.FONT_HEIGHT * 2 - 11, width, height, new Color(11, 11, 22).getRGB());
		drawRect(width - textboxWidth - 10, height - FONT_RENDERER.FONT_HEIGHT * 2 - 10,
				width, height, new Color(44, 44, 55).getRGB());
		drawString(FONT_RENDERER, userInfo, width - textboxWidth - 5,
				height - FONT_RENDERER.FONT_HEIGHT * 2 - 5, new Color(255, 55, 255).getRGB());
		drawString(FONT_RENDERER, AD, width - textboxWidth - 5,
				height - FONT_RENDERER.FONT_HEIGHT - 5, new Color(255, 55, 255).getRGB());
		GlStateManager.popMatrix();
	}
	
	@Override protected void actionPerformed(GuiButton button) throws IOException {
		switch(button.id) {
		
			case 5:
				CLIENT.displayGuiScreen(new GuiQuit());
				break;
				
			case 4:
				CLIENT.displayGuiScreen(new GuiLogin());
				break;
				
			case 3:
				CLIENT.displayGuiScreen(new GuiOptions(this, CLIENT.gameSettings));
				break;
				
			case 2:
				CLIENT.displayGuiScreen(new GuiModList(this));
				break;
				
			case 1:
				CLIENT.displayGuiScreen(new GuiMultiplayer(this));
				break;
				
			case 0:
				CLIENT.displayGuiScreen(new GuiSelectWorld(this));
				break;
		
		}
	}

}
