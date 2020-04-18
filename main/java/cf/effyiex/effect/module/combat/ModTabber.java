package cf.effyiex.effect.module.combat;

import org.lwjgl.input.Keyboard;

import cf.effyiex.effect.module.Module;
import cf.effyiex.effect.module.ModuleType;
import cf.effyiex.effect.module.Setting;
import cf.effyiex.effect.module.Setting.SettingType;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;

public class ModTabber extends Module {

	private Setting reset = new Setting("Reset", 1, SettingType.INTEGER);
	private Setting resTime = new Setting("Resistance", 3, SettingType.INTEGER);
	private Setting mode = new Setting("Mode", 0, new String[] {"Backward", "Forward", "Both"});
	
	private int resetTabbing = -1;
	
	public ModTabber() { 
		super("Tabber", ModuleType.COMBAT);
		this.listenTo(PlayerTickEvent.class);
		this.add(reset);
		this.add(resTime);
		this.add(mode);
	}

	@Override
	public void onEvent(Object eventObj) {
		if(this.isActive())
			if(CLIENT.pointedEntity != null && CLIENT.pointedEntity.hurtResistantTime > resTime.toInteger()) {
				if(mode.toString().equalsIgnoreCase("forward") || mode.toString().equalsIgnoreCase("both"))
					KeyBinding.setKeyBindState(CLIENT.gameSettings.keyBindForward.getKeyCode(), false);
				else if(mode.toString().equalsIgnoreCase("both") || mode.toString().equalsIgnoreCase("backward"))
					KeyBinding.setKeyBindState(CLIENT.gameSettings.keyBindBack.getKeyCode(), true);
				this.resetTabbing = reset.toInteger();
			}else if(resetTabbing == 0) {
				KeyBinding.setKeyBindState(CLIENT.gameSettings.keyBindForward.getKeyCode(), Keyboard.isKeyDown(CLIENT.gameSettings.keyBindForward.getKeyCode()));
				KeyBinding.setKeyBindState(CLIENT.gameSettings.keyBindBack.getKeyCode(), Keyboard.isKeyDown(CLIENT.gameSettings.keyBindBack.getKeyCode()));
				this.resetTabbing = -1;
			}else if(resetTabbing > 0) resetTabbing--;
	}

}
