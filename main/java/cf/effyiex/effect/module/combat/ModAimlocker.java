package cf.effyiex.effect.module.combat;

import cf.effyiex.effect.event.CustomEvent.GameRenderEvent;
import cf.effyiex.effect.module.Module;
import cf.effyiex.effect.module.ModuleType;
import cf.effyiex.effect.module.Setting;
import cf.effyiex.effect.module.Setting.SettingType;
import cf.effyiex.effect.utils.MathHelper;
import cf.effyiex.effect.utils.Targetting;

import net.minecraft.entity.Entity;

public class ModAimlocker extends Module {

	private Setting devider = new Setting("Devider", 13.37F, SettingType.FLOAT, 0.0F, 24.0F);
	private Setting range = new Setting("Range", 5.0F, SettingType.FLOAT, 0.0F, 16.0F);
	
	public ModAimlocker() {
		super("Aimlocker", ModuleType.COMBAT);
		this.listenTo(GameRenderEvent.class);
		this.add(devider);
		this.add(range);
	}

	@Override
	public void onEvent(Object event) {
		if(this.isActive() && CLIENT.currentScreen == null) {
			Entity target = Targetting.getNearestTarget(range.toFloat());
			if(target != null) {
				float[] rot = Targetting.getRotations(target);
				float yawDiff = MathHelper.difference(rot[0], CLIENT.thePlayer.rotationYaw);
				float pitchDiff = MathHelper.difference(rot[1], CLIENT.thePlayer.rotationPitch);
				if(pitchDiff < 128 && yawDiff < 96 && Targetting.getTargetOver(5.0F) != target) {
					CLIENT.thePlayer.rotationYaw -= ((CLIENT.thePlayer.rotationYaw - rot[0]) / devider.toFloat());
					CLIENT.thePlayer.rotationYawHead = CLIENT.thePlayer.rotationYaw;
				}
			}
		}
	}

}
