package cf.effyiex.effect.gui;

import java.awt.Color;
import java.io.IOException;

import cf.effyiex.effect.Reference;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

public class GuiQuit extends GuiScreen implements Reference {
	
	private static final short BUTTON_SPACE = 24;
	private static final short BUTTON_WIDTH = 64;
	private static final short BUTTON_HEIGHT = 24;
	
	@Override public void initGui() {
		this.buttonList.clear();
		this.buttonList.add(new Button(0, width / 2 - BUTTON_SPACE - BUTTON_WIDTH, height / 2, BUTTON_WIDTH, BUTTON_HEIGHT, "No, sorry :C"));
		this.buttonList.add(new Button(1, width / 2 + BUTTON_SPACE, height / 2, BUTTON_WIDTH, BUTTON_HEIGHT, "Yeah, cya!"));
	}
	
	@Override public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		drawRect(0, 0, width, height, new Color(55, 55, 66).getRGB());
		drawRect(0, height / 3, width, height / 3 * 2, new Color(33, 33, 44).getRGB());
		super.drawScreen(mouseX, mouseY, partialTicks);
		drawCenteredString(FONT_RENDERER, "Are you sure about that?",
				width / 2, height / 2 - FONT_RENDERER.FONT_HEIGHT * 2, Color.WHITE.getRGB());
	}
	
	@Override protected void actionPerformed(GuiButton button) throws IOException {
		if(button.id <= 0) CLIENT.displayGuiScreen(new MainMenu()); else CLIENT.shutdown();
	}

}
