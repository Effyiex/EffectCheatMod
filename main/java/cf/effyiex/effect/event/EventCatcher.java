package cf.effyiex.effect.event;

import static cf.effyiex.effect.event.EventFactory.triggerModules;

import org.lwjgl.input.Keyboard;

import cf.effyiex.effect.Effect;
import cf.effyiex.effect.Reference;
import cf.effyiex.effect.command.Command;
import cf.effyiex.effect.event.CustomEvent.GameRenderEvent;
import cf.effyiex.effect.event.CustomEvent.LivingRenderEvent;
import cf.effyiex.effect.event.CustomEvent.ScreenRenderEvent;
import cf.effyiex.effect.event.CustomEvent.WorldChangeEvent;
import cf.effyiex.effect.gui.GuiChat;
import cf.effyiex.effect.gui.MainMenu;
import cf.effyiex.effect.module.Module;
import cf.effyiex.effect.utils.FileHelper;

import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.client.event.GuiScreenEvent.DrawScreenEvent;
import net.minecraftforge.client.event.GuiScreenEvent.InitGuiEvent;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.client.event.RenderBlockOverlayEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.RenderTickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.WorldTickEvent;

public class EventCatcher implements Reference {
	
	@SubscribeEvent
	public void onBlockHUDRender(RenderBlockOverlayEvent event) {
		triggerModules(RenderBlockOverlayEvent.class, event);
	}
	
	@SubscribeEvent
	public void onScreenInit(InitGuiEvent event) {
		if(CLIENT.currentScreen instanceof GuiMainMenu)
			CLIENT.displayGuiScreen(new MainMenu());
		else if(CLIENT.currentScreen instanceof GuiIngameMenu)
			FileHelper.saveConfig("auto");
		else if(CLIENT.currentScreen.getClass().equals(net.minecraft.client.gui.GuiChat.class))
			CLIENT.displayGuiScreen(new GuiChat());
		triggerModules(InitGuiEvent.class, event);
	}
	
	private WorldClient world;
	
	@SubscribeEvent
	public void onPlayerTick(PlayerTickEvent event) {
		if(event.player.equals(CLIENT.thePlayer)) {
			for(Module module : Effect.getModules()) {
				if(module.isListeningTo(PlayerTickEvent.class)
						|| module.isListeningTo(WorldTickEvent.class))
					module.onEvent(event);
				if(module.getBind() != Keyboard.KEY_NONE && CLIENT.currentScreen == null)
					if(Keyboard.isKeyDown(module.getBind())) {
						if(!module.holdingBind) module.toggle();
						module.holdingBind = true;
					}else module.holdingBind = false;
			}
		}
		if(world != CLIENT.theWorld) {
			triggerModules(WorldChangeEvent.class, new WorldChangeEvent(world, CLIENT.theWorld));
			world = CLIENT.theWorld;
		}
	}
	
	@SubscribeEvent
	public void onScreenRender(DrawScreenEvent event) {
		triggerModules(ScreenRenderEvent.class, new ScreenRenderEvent(
			event.renderPartialTicks, event.mouseX, event.mouseY
		));
	}
	
	@SubscribeEvent
	public void onLivingRender(RenderLivingEvent.Post<EntityLivingBase> event) {
		triggerModules(LivingRenderEvent.class,
				new LivingRenderEvent(event.entity, event.x, event.y, event.z));
	}
	
	@SubscribeEvent
	public void onRenderTick(RenderTickEvent event) {
		triggerModules(GameRenderEvent.class, new GameRenderEvent(1.0F));
	}
	
	@SubscribeEvent
	public void onLivingHurt(LivingHurtEvent event) {
		triggerModules(LivingHurtEvent.class, event);
	}
	
	@SubscribeEvent
	public void onMouseTrigger(MouseEvent event) {
		triggerModules(MouseEvent.class, event);
	}
	
	
	public static boolean onChatEvent(String message) {
		if(message.startsWith(String.valueOf(COMMAND_PREFIX))) {
			String[] args = message.substring(1).split(String.valueOf(' '));
			for(Command command : Effect.getCommands())  {
				if(command.getLabel().equalsIgnoreCase(args[0])) {
					command.onUsage(args);
					return true;
				}
			}
		}
		return false;
	}
	
}
