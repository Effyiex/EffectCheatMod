package cf.effyiex.effect.module.combat;

import cf.effyiex.effect.module.Module;
import cf.effyiex.effect.module.ModuleType;
import cf.effyiex.effect.module.Setting;
import cf.effyiex.effect.module.Setting.SettingType;
import cf.effyiex.effect.utils.Targetting;
import net.minecraft.entity.Entity;
import net.minecraftforge.client.event.MouseEvent;

public class ModReach extends Module {

	private Setting value = new Setting("Value", 3.9F, SettingType.FLOAT, 3.9F, 6.0F);
	
	public ModReach() {
		super("Reach", ModuleType.COMBAT);
		this.listenTo(MouseEvent.class);
		this.add(value);
	}

	@Override
	public void onEvent(Object eventObj) {
		MouseEvent event = ((MouseEvent) eventObj);
		if(event.buttonstate && event.button <= 0 && this.isActive()) {
			CLIENT.pointedEntity = null;
			Entity target = Targetting.getTargetOver(10.0F);
			if(CLIENT.objectMouseOver != null && CLIENT.objectMouseOver.entityHit != null)
				target = CLIENT.objectMouseOver.entityHit;
			if(target != null && target.hurtResistantTime <= 0.0F) {
				float dist = CLIENT.thePlayer.getDistanceToEntity(target);
				if(dist <= value.toFloat()) {
					CLIENT.playerController.attackEntity(CLIENT.thePlayer, target);
					CLIENT.thePlayer.swingItem();
					event.setCanceled(true);
				}
			}
		}
	}

}
