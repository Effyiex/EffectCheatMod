package cf.effyiex.effect.module.render;

import cf.effyiex.effect.module.Module;
import cf.effyiex.effect.module.ModuleType;

import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.WorldTickEvent;

public class ModNoHurtCam extends Module {
	
	public ModNoHurtCam() {
		super("NoHurtCam", ModuleType.RENDER);
		this.listenTo(LivingHurtEvent.class);
		this.listenTo(WorldTickEvent.class);
	}

	private float lastYaw = 0.0F, lastPitch = 0.0F;
	
	@Override
	public void onEvent(Object event) {
		if(this.isActive()) {
			if(event instanceof LivingHurtEvent) {
				LivingHurtEvent hurtEvent = ((LivingHurtEvent)event);
				if(hurtEvent.entity.equals(CLIENT.thePlayer)) {
					CLIENT.thePlayer.cameraYaw = lastYaw;
					CLIENT.thePlayer.cameraPitch = lastPitch;
				}
			}else if(event instanceof WorldTickEvent) {
				lastYaw = CLIENT.thePlayer.cameraYaw;
				lastPitch = CLIENT.thePlayer.cameraPitch;
			}
		}
	}

}
