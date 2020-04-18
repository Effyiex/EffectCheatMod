package cf.effyiex.effect.gui;

import java.awt.Color;
import java.io.IOException;
import java.util.Random;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import cf.effyiex.effect.Reference;
import cf.effyiex.effect.rendering.FontRenderer;
import cf.effyiex.effect.rendering.RenderHelper;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

public abstract class GuiColorchoose extends GuiScreen implements Reference {
	
	private static final String LABEL = ("Choose a color!");
	
	public static Color current = Color.WHITE;
	
	private GuiScreen parent;
	private Random random;
	
	public GuiColorchoose(GuiScreen parent) { this.parent = parent; }
	
	@Override
	public void initGui() {
		this.buttonList.clear();
		this.buttonList.add(new Button(0, 5, 5, 50, 20, "<- Back"));
		this.buttonList.add(new Button(1, 5, 30, 50, 20, "<- Done"));
		this.buttonList.add(new Button(2, 5, 55, 50, 20, "Randomize"));
		this.random = new Random();
	}
	
	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		if(keyCode == Keyboard.KEY_ESCAPE) CLIENT.displayGuiScreen(parent);
	}
	
	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		switch(button.id) {
		
			case 0: CLIENT.displayGuiScreen(parent); break;
			
			case 1:
				this.replace();
				CLIENT.displayGuiScreen(parent);
				break;
				
			case 2: current = new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255)); break;
		
		}
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		super.drawScreen(mouseX, mouseY, partialTicks);
		FontRenderer.drawXYCenteredString(LABEL, width / 2, height / 12 * 5, current.getRGB());
		RenderHelper.drawOutlinedRect(width / 2 - FONT_RENDERER.getStringWidth(LABEL) / 2 - 2,
				height / 12 * 5 - FONT_RENDERER.FONT_HEIGHT - 2, FONT_RENDERER.getStringWidth(LABEL) + 4, FONT_RENDERER.FONT_HEIGHT + 4, 1, current.getRGB());
		drawRect(width / 3, height / 2, width / 3 * 2, height / 2 + 16, Color.black.getRGB());
		drawRect(width / 3, height / 2 + 32, width / 3 * 2, height / 2 + 48, Color.black.getRGB());
		drawRect(width / 3, height / 2 + 64, width / 3 * 2, height / 2 + 80, Color.black.getRGB());
		drawRect(width / 3, height / 2 + 96, width / 3 * 2, height / 2 + 112, Color.black.getRGB());
		drawRect(width / 3 + 1, height / 2 + 1, width / 3 + Math.round(width / 3 / 255.0F * current.getRed()) - 1, height / 2 + 15, Color.red.getRGB());
		drawRect(width / 3 + 1, height / 2 + 33, width / 3 + Math.round(width / 3 / 255.0F * current.getGreen()) - 1, height / 2 + 47, Color.green.getRGB());
		drawRect(width / 3 + 1, height / 2 + 65, width / 3 + Math.round(width / 3 / 255.0F * current.getBlue()) - 1, height / 2 + 79, Color.blue.getRGB());
		drawRect(width / 3 + 1, height / 2 + 97, width / 3 + Math.round(width / 3 / 255.0F * current.getAlpha()) - 1, height / 2 + 111, Color.white.getRGB());
		if(Mouse.isButtonDown(0) && mouseX > width / 3 && mouseX < width / 3 * 2) {
			if(mouseY > height / 2 && mouseY < height / 2 + 16) 
				current = new Color(Math.round(255.0F / (width / 3.0F) * (mouseX - width / 3)), current.getGreen(), current.getBlue(), current.getAlpha());
			else if(mouseY > height / 2 + 32 && mouseY < height / 2 + 48)
				current = new Color(current.getRed(), Math.round(255.0F / (width / 3.0F) * (mouseX - width / 3)), current.getBlue(), current.getAlpha());
			else if(mouseY > height / 2 + 64 && mouseY < height / 2 + 80)
				current = new Color(current.getRed(), current.getGreen(), Math.round(255.0F / (width / 3.0F) * (mouseX - width / 3)), current.getAlpha());
			else if(mouseY > height / 2 + 96 && mouseY < height / 2 + 112)
				current = new Color(current.getRed(), current.getGreen(), current.getBlue(), Math.round(255.0F / (width / 3.0F) * (mouseX - width / 3)));
		}
	}
	
	public abstract void replace();

}
