package cf.effyiex.effect.rendering;

import cf.effyiex.effect.Reference;

public class FontRenderer implements Reference {
	
	public static void drawCenteredString(String text, float x, float y, int color) {
		FONT_RENDERER.drawString(text, x - FONT_RENDERER.getStringWidth(text) / 2, y, color, false);
	}
	
	public static void drawXYCenteredString(String text, float x, float y, int color) {
		FontRenderer.drawCenteredString(text, x, y - FONT_RENDERER.FONT_HEIGHT / 2, color);
	}

}
