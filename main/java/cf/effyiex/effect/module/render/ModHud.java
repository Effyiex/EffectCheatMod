package cf.effyiex.effect.module.render;

import java.awt.Color;
import java.util.ArrayList;

import cf.effyiex.effect.Effect;
import cf.effyiex.effect.event.CustomEvent.GameRenderEvent;
import cf.effyiex.effect.event.CustomEvent.PostInitializationEvent;
import cf.effyiex.effect.module.Module;
import cf.effyiex.effect.module.ModuleType;
import cf.effyiex.effect.rendering.Animator;
import cf.effyiex.effect.rendering.ColorCode;
import cf.effyiex.effect.utils.Map;

import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraft.client.gui.inventory.GuiBeacon;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.client.gui.inventory.GuiCrafting;
import net.minecraft.client.gui.inventory.GuiFurnace;
import net.minecraft.client.gui.inventory.GuiInventory;

import static net.minecraft.client.gui.Gui.drawRect;

public class ModHud extends Module {

	private static final short FRAME_RATE = 60;
	private static final int ARRAY_BACKGROUND = new Color(0, 0, 0, 144).getRGB();
	private static final int ARRAY_OUTLINE = new Color(0, 0, 0).getRGB();
	
	private ArrayList<Map<Module, Float>> modules;
	
	public ModHud() {
		super("HUD", ModuleType.RENDER);
		this.modules = new ArrayList<Map<Module, Float>>();
		this.hideOnArray = true;
		this.listenTo(GameRenderEvent.class);
		this.listenTo(PostInitializationEvent.class);
		this.toggle();
	}
	
	@Override
	public void onEvent(Object event) {
		if(event instanceof PostInitializationEvent) {
			for(Module module : Effect.getModules()) {
				if(module.renderOnArray())
					modules.add(new Map<Module, Float>(module, 0.0F));
			}
		} else if(this.isActive() && this.shouldRenderHUD() && !Effect.getModule("Ghostmode").isActive()) {
			GameRenderEvent renderer = ((GameRenderEvent)event);
			FONT_RENDERER.drawString(AD, 4, 6, ColorCode.BLACK.getHexCode());
			FONT_RENDERER.drawString(AD, 3, 5, ColorCode.WHITE.getHexCode());
			short yIndex = 0, aboveY = 0;
			int x = 0, y = 0, width = 0, height = 0;
			for(Map<Module, Float> module : modules) {
				width = (FONT_RENDERER.getStringWidth(module.x.getName()) + 3);
				height = (FONT_RENDERER.FONT_HEIGHT + 2);
				if(module.x.isActive()) {
					if(module.y < (width - 0.1F)) {
						module.y += ((width - module.y) / 16)
								* Animator.getFrameMultiplier(FRAME_RATE);
					} else module.y = ((float) width);
				}else if(module.y > 0) module.y -= (1 * Animator.getFrameMultiplier(FRAME_RATE));
				if(module.y > 0) {
					x = Math.round(renderer.width - module.y);
					y = (8 + yIndex * (height + height / 2));
					drawRect(x - 3, y - height / 2, x + width, y + height, ARRAY_BACKGROUND);
					drawRect(x - 3, y - height / 2, x - 2, y + height, ARRAY_OUTLINE);
					if(yIndex <= 0) drawRect(x - 3, y - height / 2, x + width, y - height / 2 + 1, ARRAY_OUTLINE);
					drawRect(x - 3 - Math.round(aboveY - module.y), y - height / 2, x - 3, y - height / 2 + 1, ARRAY_OUTLINE);
					FONT_RENDERER.drawString(module.x.getName(), x + 1, y + 1, Color.black.getRGB());
					FONT_RENDERER.drawString(module.x.getName(), x, y, Color.white.getRGB());
					aboveY = ((short) Math.round(module.y));
					yIndex++;
				}
			}
			if(yIndex > 0) drawRect(x - 3, y + height, renderer.width, y + height + 1, Color.BLACK.getRGB());
		}
	}
	
	private boolean shouldRenderHUD() {
		if(CLIENT.theWorld != null && CLIENT.thePlayer != null) {
			if(CLIENT.currentScreen == null) return true;
			if(CLIENT.currentScreen instanceof GuiChat) return true;
			if(CLIENT.currentScreen instanceof GuiIngameMenu) return true;
			if(CLIENT.currentScreen instanceof GuiChest) return true;
			if(CLIENT.currentScreen instanceof GuiCrafting) return true;
			if(CLIENT.currentScreen instanceof GuiFurnace) return true;
			if(CLIENT.currentScreen instanceof GuiInventory) return true;
			if(CLIENT.currentScreen instanceof GuiBeacon) return true;
		}
		return false;
	}

}
