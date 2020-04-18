package cf.effyiex.effect.gui;

import java.awt.Color;

import cf.effyiex.effect.Reference;
import cf.effyiex.effect.rendering.ColorCode;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;

public class Button extends GuiButton implements Reference {

	public Button(int buttonId, int x, int y, int widthIn, int heightIn, String buttonText) {
		super(buttonId, x, y, widthIn, heightIn, buttonText);
	}
	
	@Override
	public void drawButton(Minecraft mc, int mouseX, int mouseY) {
		hovered = (mouseX > xPosition && mouseY > yPosition
				&& mouseX < xPosition + width && mouseY < yPosition + height);
		int color = (hovered ? new Color(0, 0, 0, 120).getRGB() : new Color(0, 0, 0, 100).getRGB());
		drawRect(xPosition, yPosition, xPosition + width, yPosition + height, color);
		drawCenteredString(FONT_RENDERER, displayString, xPosition + width / 2,
				yPosition + (height - FONT_RENDERER.FONT_HEIGHT / 2) / 2, ColorCode.WHITE.getHexCode());
	}

}
