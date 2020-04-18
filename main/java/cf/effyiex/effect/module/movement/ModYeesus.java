package cf.effyiex.effect.module.movement;

import org.lwjgl.input.Keyboard;

import cf.effyiex.effect.module.Module;
import cf.effyiex.effect.module.ModuleType;
import cf.effyiex.effect.module.Setting;
import cf.effyiex.effect.module.Setting.SettingType;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;

public class ModYeesus extends Module {

	private Setting modes = new Setting("Mode", 0, new String[] {
			"Dolphin", "Bounce", "Waterspeed" 
	});
	
	private Setting speed = new Setting("Speed", 1.0F, SettingType.FLOAT, 1.0F, 5.0F);
	
	public ModYeesus() {
		super("Yeesus", ModuleType.MOVEMENT);
		this.listenTo(PlayerTickEvent.class);
		this.add(modes);
		this.add(speed);
	}

	boolean reset = false;
	
	@Override
	public void onEvent(Object event) {
		if(reset) {
			KeyBinding.setKeyBindState(CLIENT.gameSettings.keyBindJump.getKeyCode(), Keyboard.isKeyDown(CLIENT.gameSettings.keyBindJump.getKeyCode()));
			reset = false;
		}
		if(this.isActive() && !CLIENT.gameSettings.keyBindSneak.isKeyDown()) {
			boolean inLiquid = (CLIENT.thePlayer.isInWater() || CLIENT.thePlayer.isInLava());
			if(modes.toString().equalsIgnoreCase("Dolphin") && inLiquid) {
				KeyBinding.setKeyBindState(CLIENT.gameSettings.keyBindJump.getKeyCode(), true);
				reset = true;
			} else if(modes.toString().equalsIgnoreCase("Bounce") && inLiquid) CLIENT.thePlayer.motionY = 0.1337F;
			if(inLiquid) {
				CLIENT.thePlayer.motionX *= speed.toFloat();
				CLIENT.thePlayer.motionZ *= speed.toFloat();
			}
		}
	}

}
