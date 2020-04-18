package cf.effyiex.effect.module.player;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;

import org.lwjgl.input.Keyboard;

import cf.effyiex.effect.module.Module;
import cf.effyiex.effect.module.ModuleType;
import cf.effyiex.effect.module.Setting;
import cf.effyiex.effect.module.Setting.SettingType;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.init.Items;
import net.minecraft.item.ItemSoup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;

public class ModSoupHelper extends Module {

	private Setting autoEat = new Setting("AutoEat", true, SettingType.BOOLEAN);
	private Setting autoDrop = new Setting("AutoDrop", true, SettingType.BOOLEAN);
	private Setting autoSelect = new Setting("AutoSelect", true, SettingType.BOOLEAN);
	private Setting health = new Setting("Health", 12, SettingType.INTEGER);
	
	private Robot robot;
	
	public ModSoupHelper() {
		super("SoupHelper", ModuleType.PLAYER);
		this.listenTo(PlayerTickEvent.class);
		this.add(autoSelect);
		this.add(autoEat);
		this.add(autoDrop);
		this.add(health);
		try {
			this.robot = new Robot();
		} catch (AWTException e) { e.printStackTrace(); }
	}

	private boolean reset = false;
	
	@Override
	public void onEvent(Object event) {
		if(this.isActive()) {
			if(CLIENT.thePlayer.getHealth() <= health.toInteger()) {
				if(autoEat.toBoolean() && CLIENT.thePlayer.getHeldItem() != null && CLIENT.thePlayer.getHeldItem().getItem() instanceof ItemSoup)
					CLIENT.thePlayer.setItemInUse(CLIENT.thePlayer.getHeldItem(), 1);
				else if(autoSelect.toBoolean()) for(int slot = 0; slot < 9; slot++) {
					ItemStack stack = CLIENT.thePlayer.getInventory()[slot];
					if(stack != null && stack.getItem() instanceof ItemSoup) {
						robot.keyPress(KeyEvent.VK_0 + slot);
						robot.keyRelease(KeyEvent.VK_0 + slot);
					}
				}
			}
			if(autoDrop.toBoolean() && CLIENT.thePlayer.getHeldItem() != null && CLIENT.thePlayer.getHeldItem().getItem().equals(Items.bowl)) {
				KeyBinding.setKeyBindState(CLIENT.gameSettings.keyBindDrop.getKeyCode(), true);
				reset = true;
			} else if(reset) KeyBinding.setKeyBindState(CLIENT.gameSettings.keyBindDrop.getKeyCode(), Keyboard.isKeyDown(CLIENT.gameSettings.keyBindDrop.getKeyCode()));
		}
	}

}
