package cf.effyiex.effect.module.movement;

import cf.effyiex.effect.module.Module;
import cf.effyiex.effect.module.ModuleType;

import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;

public class ModFlight extends Module {

	public ModFlight() {
		super("Flight", ModuleType.MOVEMENT);
		this.listenTo(PlayerTickEvent.class);
	}

	@Override
	public void onEvent(Object event) {
		if(this.isActive()) {
			CLIENT.thePlayer.jumpMovementFactor = 0.1F;
			CLIENT.thePlayer.motionY = 0.0F;
			if(CLIENT.gameSettings.keyBindJump.isKeyDown())
				CLIENT.thePlayer.motionY += 0.4F;
			if(CLIENT.gameSettings.keyBindSneak.isKeyDown())
				CLIENT.thePlayer.motionY -= 0.4F;
		}
	}

}
