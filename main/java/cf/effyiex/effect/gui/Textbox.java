package cf.effyiex.effect.gui;

import java.awt.Color;

import cf.effyiex.effect.Reference;
import net.minecraft.client.gui.GuiTextField;

public class Textbox extends GuiTextField implements Reference {

	private static final short MAX_BLINK_HEAP = 16;
	
	private short blinker = 0;
	
	private Character passchar;
	private String title;
	
	public Textbox(int x, int y, int width, int height, String title) {
		this(x, y, width, height, title, null);
	}
	
	public Textbox(int x, int y, int width, int height, String title, Character passchar) {
		super(0, FONT_RENDERER, x, y, width, height);
		this.passchar = passchar;
		this.title = title;
	}
	
	public String getDisplayedString() {
		String display = getText();
		if(display.isEmpty() && !isFocused()) display = title;
		else {
			if(passchar != null) {
				display = "";
				for(int i = 0; i < getText().length(); i++)
					display += passchar;
			}
			if(this.isFocused())
				display += (this.blinker <= MAX_BLINK_HEAP / 2 ? '_' : ' ');
		}
	    return display;
	}
	
	@Override public void drawTextBox() {
		drawRect(xPosition, yPosition, xPosition + width, yPosition + height, new Color(33, 33, 44).getRGB());
		int color = Color.WHITE.getRGB();
		if(getText().isEmpty()) color = Color.GRAY.getRGB();
		drawString(FONT_RENDERER, this.getDisplayedString(), xPosition + FONT_RENDERER.FONT_HEIGHT,
				yPosition + height / 4 * 3 - FONT_RENDERER.FONT_HEIGHT, color);
	}

	@Override public void updateCursorCounter() {
		this.blinker++;
		if(!(this.blinker <= MAX_BLINK_HEAP))
			this.blinker = 0;
	}
	
}
