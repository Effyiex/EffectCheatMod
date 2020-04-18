package cf.effyiex.effect.gui;

import java.awt.Color;
import java.io.IOException;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import cf.effyiex.effect.Effect;
import cf.effyiex.effect.Reference;
import cf.effyiex.effect.event.EventCatcher;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiChat extends net.minecraft.client.gui.GuiChat implements Reference {

	private static final String STANDARD_MESSAGE = ("Use '" + COMMAND_PREFIX + "' to use Effect-Commands!");
	private static final int STANDARD_COLOR = new Color(200, 200, 200).getRGB();
	private static final int OUTLINES = new Color(225, 55, 255).getRGB();
	
	@Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		if(Effect.getModule("ghostmode").isActive()) {
			super.drawScreen(mouseX, mouseY, partialTicks);
			return;
		}
		String text = inputField.getText();
		if(text.isEmpty()) text = STANDARD_MESSAGE;
		int maxWidth = (this.width - 2);
		int width = (fontRendererObj.getStringWidth(text) + fontRendererObj.getCharWidth('_') * 2 + 2);
		if(width > maxWidth) width = maxWidth;
        drawRect(2, height - 14, width, height - 2, Integer.MIN_VALUE);
        if(inputField.getText().isEmpty())
        	mc.fontRendererObj.drawString(text, 4, height - 12, STANDARD_COLOR);
        this.inputField.drawTextBox();
        IChatComponent ichatcomponent = this.mc.ingameGUI.getChatGUI().getChatComponent(Mouse.getX(), Mouse.getY());
        if (ichatcomponent != null && ichatcomponent.getChatStyle().getChatHoverEvent() != null) {
            this.handleComponentHover(ichatcomponent, mouseX, mouseY);
        }
        drawRect(1, height - 15, width, height - 14, OUTLINES);
        drawRect(1, height - 15, 2, height - 1, OUTLINES);
        drawRect(1, height - 2, width, height - 1, OUTLINES);
        drawRect(width, height - 15, width + 1, height - 1, OUTLINES);
    }
	
	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		if(keyCode != Keyboard.KEY_RETURN || !EventCatcher.onChatEvent(inputField.getText()))
			super.keyTyped(typedChar, keyCode);
		else CLIENT.setIngameFocus();
	}
    
}