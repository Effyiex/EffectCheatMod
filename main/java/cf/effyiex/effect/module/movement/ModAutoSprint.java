package cf.effyiex.effect.module.movement;

import cf.effyiex.effect.module.Module;
import cf.effyiex.effect.module.ModuleType;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;

public class ModAutoSprint extends Module {

	public ModAutoSprint() {
		super("AutoSprint", ModuleType.MOVEMENT);
		this.listenTo(PlayerTickEvent.class);
	}

	@Override
	public void onEvent(Object event) {
		if(this.isActive()
				&& !CLIENT.thePlayer.isCollidedHorizontally
				&& CLIENT.thePlayer.moveForward > 0.0F
				&& !CLIENT.thePlayer.isSprinting())
			CLIENT.thePlayer.setSprinting(true);
	}

}
